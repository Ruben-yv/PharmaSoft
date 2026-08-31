package pe.edu.epeu.sysventas.service.imp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.epeu.sysventas.dto.ProductoRequestDTO;
import pe.edu.epeu.sysventas.dto.ProductoResponseDTO;
import pe.edu.epeu.sysventas.entity.Producto;
import pe.edu.epeu.sysventas.exception.RecursosNoEncontradosException;
import pe.edu.epeu.sysventas.exception.ReglaNegocioException;
import pe.edu.epeu.sysventas.repository.ProductoRepository;
import pe.edu.epeu.sysventas.service.service.ProductoService;
@Service
public class ProductoServiceImpl implements ProductoService {
    private static final Logger LOG = LoggerFactory.getLogger(ProductoServiceImpl.class);

    private final ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    @Transactional
    public ProductoResponseDTO create(ProductoRequestDTO t) {
        String nombre = t.getNombre().trim();
        if (productoRepository.existsByNombreIgnoreCase(nombre)){
            throw new ReglaNegocioException("Ya existe un producto con el nombre " + nombre);
        }
        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setDescripcion(t.getDescripcion());
        producto.setEstado(t.getEstado());
        producto.setPrecio(t.getPrecio());
        producto.setStock(t.getStock());

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
            producto.setNombre(t.getNombre());
            producto.setDescripcion(t.getDescripcion());
            producto.setEstado(t.getEstado());
            producto.setPrecio(t.getPrecio());
            producto.setStock(t.getStock());
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
        productoRepository.delete(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<ProductoResponseDTO> readAll () {
        return productoRepository.findAll()
                .stream()
                .map(this::convertirResponse)
                .toList();
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
                producto.getStock()
        );
    }
}