package pe.edu.epeu.sysventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.epeu.sysventas.entity.DetalleVenta;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
}