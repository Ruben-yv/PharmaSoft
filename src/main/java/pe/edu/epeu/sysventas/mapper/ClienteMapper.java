package pe.edu.epeu.sysventas.mapper;

import org.springframework.stereotype.Component;
import pe.edu.epeu.sysventas.dto.ClienteResponseDTO;
import pe.edu.epeu.sysventas.entity.Cliente;

@Component
public class ClienteMapper {

    public ClienteResponseDTO toResponse(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getDni(),
                cliente.getNombres(),
                cliente.getApellidos(),
                cliente.getEmail(),
                cliente.getTelefono(),
                cliente.getDireccion(),
                cliente.getEstado(),
                cliente.getFechaCreacion(),
                cliente.getFechaModificacion()
        );
    }
}