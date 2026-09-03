package pe.edu.epeu.sysventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.epeu.sysventas.entity.Venta;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    }

