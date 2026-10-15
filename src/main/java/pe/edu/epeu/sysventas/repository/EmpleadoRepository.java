package pe.edu.epeu.sysventas.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.epeu.sysventas.entity.Empleado;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
}