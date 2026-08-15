package pe.edu.epeu.sysventas.service.service;

import org.springframework.stereotype.Service;
import pe.edu.epeu.sysventas.entity.Categoria;
import pe.edu.epeu.sysventas.service.generic.CrudService;

@Service
public interface CategoriaService extends CrudService<Categoria, Long> {
}
