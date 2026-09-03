package pe.edu.epeu.sysventas.service.service;

import pe.edu.epeu.sysventas.dto.ClienteRequestDTO;
import pe.edu.epeu.sysventas.dto.ClienteResponseDTO;
import pe.edu.epeu.sysventas.service.generic.CrudService;

public interface ClienteService extends CrudService<ClienteRequestDTO, ClienteResponseDTO, Long> {
}
