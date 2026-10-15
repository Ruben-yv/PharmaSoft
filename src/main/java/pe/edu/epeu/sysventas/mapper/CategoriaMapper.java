package pe.edu.epeu.sysventas.mapper;

import org.springframework.stereotype.Component;
import pe.edu.epeu.sysventas.dto.CategoriaResponseDTO;
import pe.edu.epeu.sysventas.entity.Categoria;

/*
 * Conversión entidad -> DTO de respuesta. El servicio se queda solo
 * con la orquestación y las reglas de negocio.
 */
@Component
public class CategoriaMapper {

    public CategoriaResponseDTO toResponse(Categoria categoria) {
        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getDescripcion(),
                categoria.getEstado(),
                categoria.getFechaCreacion(),
                categoria.getFechaModificacion()
        );
    }
}