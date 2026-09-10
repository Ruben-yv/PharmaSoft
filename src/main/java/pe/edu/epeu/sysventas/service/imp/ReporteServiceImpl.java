package pe.edu.epeu.sysventas.service.imp;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.epeu.sysventas.dto.reporte.ProductoMasVendidoDTO;
import pe.edu.epeu.sysventas.dto.reporte.VentaPorCategoriaDTO;
import pe.edu.epeu.sysventas.exception.ReglaNegocioException;
import pe.edu.epeu.sysventas.repository.VentaRepository;
import pe.edu.epeu.sysventas.service.service.ReporteService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReporteServiceImpl implements ReporteService {

    private static final Logger log =
            LoggerFactory.getLogger(ReporteServiceImpl.class);

    private final VentaRepository ventaRepository;

    public ReporteServiceImpl(
            VentaRepository ventaRepository) {

        this.ventaRepository = ventaRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<VentaPorCategoriaDTO> ventasPorCategoria(
            LocalDate desde,
            LocalDate hasta) {

        long inicio = System.currentTimeMillis();

        log.info("Inicio reporte ventas por categoria | desde={} | hasta={}",
                desde, hasta);

        validarRango(desde, hasta);

        List<VentaPorCategoriaDTO> resultado =
                ventaRepository.reporteVentasPorCategoria(
                        inicioDelDia(desde),
                        finDelDia(hasta)
                );

        log.info("Fin reporte ventas por categoria | desde={} | hasta={} | "
                        + "filas={} | duracionMs={}",
                desde, hasta,
                resultado.size(),
                System.currentTimeMillis() - inicio);

        return resultado;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductoMasVendidoDTO> productosMasVendidos(
            LocalDate desde,
            LocalDate hasta) {

        long inicio = System.currentTimeMillis();

        log.info("Inicio reporte productos mas vendidos | desde={} | hasta={}",
                desde, hasta);

        validarRango(desde, hasta);

        List<ProductoMasVendidoDTO> resultado =
                ventaRepository.reporteProductosMasVendidos(
                        inicioDelDia(desde),
                        finDelDia(hasta)
                );

        log.info("Fin reporte productos mas vendidos | desde={} | hasta={} | "
                        + "filas={} | duracionMs={}",
                desde, hasta,
                resultado.size(),
                System.currentTimeMillis() - inicio);

        return resultado;
    }

    private void validarRango(LocalDate desde, LocalDate hasta) {

        if (desde != null
                && hasta != null
                && desde.isAfter(hasta)) {

            throw new ReglaNegocioException(
                    "El rango de fechas es inválido: 'desde' ("
                            + desde
                            + ") es posterior a 'hasta' ("
                            + hasta + ")");
        }
    }

    private LocalDateTime inicioDelDia(LocalDate fecha) {

        return (fecha == null)
                ? null
                : fecha.atStartOfDay();
    }

    private LocalDateTime finDelDia(LocalDate fecha) {

        return (fecha == null)
                ? null
                : fecha.atTime(LocalTime.MAX);
    }
}