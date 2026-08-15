package pe.edu.epeu.sysventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.epeu.sysventas.entity.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
