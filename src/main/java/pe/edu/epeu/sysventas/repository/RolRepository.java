package pe.edu.epeu.sysventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.epeu.sysventas.entity.Rol;

public interface RolRepository extends JpaRepository<Rol, Long> {
}