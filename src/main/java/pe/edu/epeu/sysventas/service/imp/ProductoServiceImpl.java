package pe.edu.epeu.sysventas.service.imp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.epeu.sysventas.dto.ProductoRequestDTO;
import pe.edu.epeu.sysventas.dto.ProductoResponseDTO;
import pe.edu.epeu.sysventas.dto.PaginaResponseDTO;
import pe.edu.epeu.sysventas.entity.Categoria;
import pe.edu.epeu.sysventas.entity.Producto;
import pe.edu.epeu.sysventas.exception.RecursosNoEncontradosException;
import pe.edu.epeu.sysventas.exception.ReglaNegocioException;
import pe.edu.epeu.sysventas.repository.CategoriaRepository;
import pe.edu.epeu.sysventas.repository.ProductoRepository;
import pe.edu.epeu.sysventas.service.service.ProductoService;
@Service
public class ProductoServiceImpl implements ProductoService {
    private static final Logger LOG = LoggerFactory.getLogger(ProductoServiceImpl.class);
    private static final java.util.Set<String> CAMPOS_ORDENABLES = java.util.Set.of("id", "nombre", "precio", "stock");

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    @Transactional
    public ProductoResponseDTO create(ProductoRequestDTO t) {
        String nombre = t.getNombre().trim();
        if (productoRepository.existsByNombreIgnoreCase(nombre)){
            throw new ReglaNegocioException("Ya existe un producto con el nombre " + nombre);
        }
        Categoria categoria = categoriaRepository.findById(t.getCategoriaId())
                .orElseThrow(() -> new RecursosNoEncontradosException(
                        "Categoría no encontrada con id: " + t.getCategoriaId()
                ));
        validarCategoriaActiva(categoria);
        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setDescripcion(t.getDescripcion());
        producto.setEstado(t.getEstado());
        producto.setPrecio(t.getPrecio());
        producto.setStock(t.getStock());
        producto.setCategoria(categoria);

        Producto ProdCreada = productoRepository.save(producto);
        return convertirResponse(ProdCreada);
    }

    @Override
    @Transactional
    public ProductoResponseDTO update(Long aLong, ProductoRequestDTO t) {
            Producto producto = productoRepository.findById(aLong).orElseThrow(() ->
                    new RecursosNoEncontradosException(
                            "Producto no encontrado con id: " + aLong
                    )
            );
        Categoria categoria = categoriaRepository.findById(t.getCategoriaId())
                .orElseThrow(() -> new RecursosNoEncontradosException(
                        "Categoría no encontrada con id: " + t.getCategoriaId()
                ));
        validarCategoriaActiva(categoria);
        String nombre = t.getNombre().trim();
        if (productoRepository.existsByNombreIgnoreCaseAndIdNot(nombre, aLong)) {
            throw new ReglaNegocioException("Ya existe un producto con el nombre " + nombre);
        }
            producto.setNombre(nombre);
            if (t.getDescripcion() != null) {
                producto.setDescripcion(t.getDescripcion());
            }
            producto.setEstado(t.getEstado());
            producto.setPrecio(t.getPrecio());
            producto.setStock(t.getStock());
            producto.setCategoria(categoria);
            Producto prodActualizada = productoRepository.save(producto);
            return convertirResponse(prodActualizada);

    }
    @Override
    @Transactional
    public ProductoResponseDTO read (Long aLong){
        Producto producto = productoRepository.findById(aLong)
                .orElseThrow(()->
                        new RecursosNoEncontradosException(
                                "Producto no encontrado con id: " + aLong
                        )
                );
        return convertirResponse(producto);
    }

    @Override
    @Transactional
    public void delete (Long aLong){
        Producto producto = productoRepository.findById(aLong).orElseThrow(()->
                new RecursosNoEncontradosException(
                        "Producto no encontrado con id: " + aLong
                )
        );
        if (!Boolean.TRUE.equals(producto.getEstado())) {
            throw new ReglaNegocioException("El producto ya se encuentra inactivo");
        }
        producto.setEstado(false);
        productoRepository.save(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<ProductoResponseDTO> readAll () {
        return productoRepository.findAll()
                .stream()
                .map(this::convertirResponse)
                .toList();
        }

    @Override
    @Transactional(readOnly = true)
    public PaginaResponseDTO<ProductoResponseDTO> listar(int pagina, int tamanio, String ordenarPor, String direccion) {
        if (!CAMPOS_ORDENABLES.contains(ordenarPor)) {
            throw new ReglaNegocioException("Campo de orden no válido. Use id, nombre, precio o stock");
        }
        Sort.Direction sentido;
        try {
            sentido = Sort.Direction.fromString(direccion);
        } catch (IllegalArgumentException ex) {
            throw new ReglaNegocioException("Dirección de orden no válida. Use asc o desc");
        }
        Pageable pageable = PageRequest.of(pagina, tamanio, Sort.by(sentido, ordenarPor));
        Page<ProductoResponseDTO> resultado = productoRepository.findAll(pageable).map(this::convertirResponse);
        return new PaginaResponseDTO<>(
                resultado.getContent(),
                resultado.getNumber(),
                resultado.getSize(),
                resultado.getTotalElements(),
                resultado.getTotalPages(),
                resultado.isLast());
    }

    private void validarCategoriaActiva(Categoria categoria) {
        if (!Boolean.TRUE.equals(categoria.getEstado())) {
            throw new ReglaNegocioException("La categoría seleccionada está inactiva");
        }
    }
    private ProductoResponseDTO convertirResponse(Producto producto){
        return new ProductoResponseDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getEstado(),
                producto.getFechaCreacion(),
                producto.getFechaModificacion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getCategoria().getId(),
                producto.getCategoria().getNombre()
        );
    }
}
