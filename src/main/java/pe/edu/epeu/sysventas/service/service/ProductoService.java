package pe.edu.epeu.sysventas.service.service;

import org.springframework.stereotype.Service;
import pe.edu.epeu.sysventas.dto.ProductoRequestDTO;
import pe.edu.epeu.sysventas.dto.ProductoResponseDTO;
import pe.edu.epeu.sysventas.service.generic.CrudService;
@Service
public interface ProductoService extends CrudService<ProductoRequestDTO, ProductoResponseDTO, Long> {
}
