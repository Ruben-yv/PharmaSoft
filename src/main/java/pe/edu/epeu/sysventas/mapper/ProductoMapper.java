package pe.edu.epeu.sysventas.mapper;

import org.springframework.stereotype.Component;
import pe.edu.epeu.sysventas.dto.ProductoResponseDTO;
import pe.edu.epeu.sysventas.entity.Producto;

/*
 * Requiere que producto.categoria ya esté cargada (entity graph);
 * no dispara consultas por su cuenta.
 */
@Component
public class ProductoMapper {

    public ProductoResponseDTO toResponse(Producto producto) {
        return new ProductoResponseDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getEstado(),
                producto.getCategoria().getId(),
                producto.getCategoria().getNombre(),
                producto.getFechaCreacion(),
                producto.getFechaModificacion()
        );
    }
}