package pe.edu.epeu.sysventas.service.imp;

import org.springframework.stereotype.Service;
import pe.edu.epeu.sysventas.entity.Categoria;
import pe.edu.epeu.sysventas.repository.CategoriaRepository;
import pe.edu.epeu.sysventas.service.service.CategoriaService;

import java.util.Optional;
@Service
public class CategoriaServiceImpl implements CategoriaService {
    private final CategoriaRepository categoriaRepository;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public Categoria create(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @Override
    public Categoria update(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @Override
    public Optional<Categoria> read(Long aLong) {
        return categoriaRepository.findById(aLong);
    }

    @Override
    public void delete(Long aLong) {
        categoriaRepository.deleteById(aLong);
    }

    @Override
    public Iterable<Categoria> readAll() {
        return categoriaRepository.findAll();
    }
}
