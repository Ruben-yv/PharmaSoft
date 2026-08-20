package pe.edu.epeu.sysventas.service.service;

import org.springframework.stereotype.Service;
import pe.edu.epeu.sysventas.dto.CategoriaRequestDTO;
import pe.edu.epeu.sysventas.dto.CategoriaResponseDTO;
import pe.edu.epeu.sysventas.service.generic.CrudService;

@Service
public interface CategoriaService extends CrudService<CategoriaRequestDTO, CategoriaResponseDTO, Long> {
}
