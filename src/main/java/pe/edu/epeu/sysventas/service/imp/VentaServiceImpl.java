package pe.edu.epeu.sysventas.service.imp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.epeu.sysventas.dto.DetalleVentaRequestDTO;
import pe.edu.epeu.sysventas.auditoria.EventoAuditoria;
import pe.edu.epeu.sysventas.dto.DetalleVentaResponseDTO;
import pe.edu.epeu.sysventas.dto.VentaRequestDTO;
import pe.edu.epeu.sysventas.dto.VentaResponseDTO;
import pe.edu.epeu.sysventas.dto.PaginaResponseDTO;
import pe.edu.epeu.sysventas.entity.Cliente;
import pe.edu.epeu.sysventas.entity.DetalleVenta;
import pe.edu.epeu.sysventas.entity.Producto;
import pe.edu.epeu.sysventas.entity.Venta;
import pe.edu.epeu.sysventas.enums.EstadoVenta;
import pe.edu.epeu.sysventas.exception.RecursosNoEncontradosException;
import pe.edu.epeu.sysventas.exception.ReglaNegocioException;
import pe.edu.epeu.sysventas.repository.ClienteRepository;
import pe.edu.epeu.sysventas.repository.ProductoRepository;
import pe.edu.epeu.sysventas.repository.VentaRepository;
import pe.edu.epeu.sysventas.service.service.VentaService;
import pe.edu.epeu.sysventas.util.PaginacionUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Service
public class VentaServiceImpl implements VentaService {
    private static final Logger log = LoggerFactory.getLogger(VentaServiceImpl.class);

    private final VentaRepository ventaRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final ApplicationEventPublisher eventos;

    private static final Set<String> CAMPOS_ORDENABLES = Set.of("id", "fecha", "total", "estado");
    private static final String ORDEN_POR_DEFECTO = "fecha";

    public VentaServiceImpl(
            VentaRepository ventaRepository,
            ClienteRepository clienteRepository,
            ProductoRepository productoRepository,
            ApplicationEventPublisher eventos) {

        this.ventaRepository = ventaRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
        this.eventos = eventos;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "productos", allEntries = true)
    public VentaResponseDTO registrar(VentaRequestDTO request) {
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new RecursosNoEncontradosException("Cliente no encontrado con id: " + request.getClienteId()));

        if (!Boolean.TRUE.equals(cliente.getEstado())) {
            throw new ReglaNegocioException("No se puede registrar una venta para un cliente inactivo");
        }
        Venta venta = new Venta();

        venta.setCliente(cliente);
        venta.setFecha(LocalDateTime.now());
        venta.setEstado(EstadoVenta.REGISTRADA);

        BigDecimal total = BigDecimal.ZERO;

        for (DetalleVentaRequestDTO item : request.getDetalles()) {
            Producto producto = productoRepository.findById(item.getProductoId()).orElseThrow(() ->
                    new RecursosNoEncontradosException("Producto no encontrado con id: " + item.getProductoId()));

            if (!Boolean.TRUE.equals(producto.getEstado())) {
                throw new ReglaNegocioException("El producto " + producto.getNombre() + " se encuentra inactivo");
            }

            int stockDisponible = producto.getStock();
            if (stockDisponible < item.getCantidad()) {
                throw stockInsuficiente(producto.getNombre(), stockDisponible, item.getCantidad());
            }

            if (productoRepository.descontarStockSiDisponible(producto.getId(), item.getCantidad()) == 0) {
                if (!Boolean.TRUE.equals(productoRepository.consultarEstadoActual(producto.getId()))) {
                    throw new ReglaNegocioException("El producto " + producto.getNombre() + " se encuentra inactivo");
                }
                int stockActual = productoRepository.consultarStockActual(producto.getId());
                throw stockInsuficiente(producto.getNombre(), stockActual, item.getCantidad());
            }

            BigDecimal subtotal = producto.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad()));

            DetalleVenta detalle = new DetalleVenta();

            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecio(producto.getPrecio());
            detalle.setSubtotal(subtotal);

            venta.agregarDetalle(detalle);

            total = total.add(subtotal);

        }

        venta.setTotal(total);

        Venta guardada = ventaRepository.save(venta);
        eventos.publishEvent(EventoAuditoria.de("VENTA_REGISTRADA", "Venta", guardada.getId(),
                "total=" + guardada.getTotal() + " | items=" + guardada.getDetalles().size()));

        return convertirResponse(guardada);
    }

    private ReglaNegocioException stockInsuficiente(String nombre, int disponible, int solicitado) {
        return new ReglaNegocioException("Stock insuficiente para " + nombre + ". Disponible: " + disponible
                + ", solicitado: " + solicitado);
    }

    @Override
    @Transactional(readOnly = true)
    public VentaResponseDTO buscar(Long id) {

        Venta venta = ventaRepository.findById(id).orElseThrow(() ->
                new RecursosNoEncontradosException("Venta no encontrada con id: " + id));
        return convertirResponse(venta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponseDTO> listar() {
        return ventaRepository.findAll().stream().map(this::convertirResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PaginaResponseDTO<VentaResponseDTO> buscarVentas(
            Long clienteId,
            EstadoVenta estado,
            LocalDate desde,
            LocalDate hasta,
            String ordenarPor,
            String direccion,
            int pagina,
            int tamanio) {

        long inicio = System.currentTimeMillis();

        log.info("Inicio buscar ventas | clienteId={} | estado={} | "
                        + "desde={} | hasta={} | ordenarPor={} | direccion={}",
                clienteId, estado, desde, hasta, ordenarPor, direccion);

        if (desde != null
                && hasta != null
                && desde.isAfter(hasta)) {

            throw new ReglaNegocioException(
                    "El rango de fechas es inválido: 'desde' ("
                            + desde
                            + ") es posterior a 'hasta' ("
                            + hasta + ")");
        }

        Pageable pageable = PaginacionUtil.construir(
                pagina, tamanio, ordenarPor, direccion, CAMPOS_ORDENABLES, ORDEN_POR_DEFECTO);

        LocalDateTime desdeHora = (desde == null)
                ? null
                : desde.atStartOfDay();

        LocalDateTime hastaHora = (hasta == null)
                ? null
                : hasta.atTime(LocalTime.MAX);

        Page<VentaResponseDTO> resultado = ventaRepository
                .buscarPaginado(clienteId, estado, desdeHora, hastaHora, pageable)
                .map(this::convertirResponse);

        log.info("Fin buscar ventas | clienteId={} | estado={} | "
                        + "desde={} | hasta={} | orden={} {} | "
                        + "filas={} | duracionMs={}",
                clienteId, estado, desde, hasta, ordenarPor, direccion,
                resultado.getNumberOfElements(),
                System.currentTimeMillis() - inicio);

        return PaginaResponseDTO.de(resultado);
    }

    private VentaResponseDTO convertirResponse(Venta venta) {

        List<DetalleVentaResponseDTO> detalles =
                venta.getDetalles()
                        .stream()
                        .map(detalle ->
                                new DetalleVentaResponseDTO(
                                        detalle.getProducto().getId(),
                                        detalle.getProducto().getNombre(),
                                        detalle.getCantidad(),
                                        detalle.getPrecio(),
                                        detalle.getSubtotal()
                                )
                        ).toList();

        String clienteNombre = venta.getCliente().getNombres() + " " + venta.getCliente().getApellidos();

        return new VentaResponseDTO(
                venta.getId(),
                venta.getFecha(),
                venta.getCliente().getId(),
                clienteNombre,
                venta.getEstado().name(),
                venta.getTotal(),
                venta.getCreadoPor(),
                detalles
        );
    }
}
