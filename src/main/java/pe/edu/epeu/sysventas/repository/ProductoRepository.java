package pe.edu.epeu.sysventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.epeu.sysventas.entity.Producto;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    boolean existsByNombreIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, long id);

    List<Producto> findByCategoriaId(Long categoriaId);
    boolean existsByCategoriaId(Long categoriaId);
    boolean existsByCategoriaIdAndEstadoTrue(Long categoriaId);

    @Modifying(flushAutomatically = true)
    @Query("update Producto p set p.stock = p.stock - :cantidad where p.id = :id and p.estado = true and p.stock >= :cantidad")
    int descontarStockSiDisponible(@Param("id") Long id, @Param("cantidad") int cantidad);

    @Query("select p.stock from Producto p where p.id = :id")
    Integer consultarStockActual(@Param("id") Long id);

    @Query("select p.estado from Producto p where p.id = :id")
    Boolean consultarEstadoActual(@Param("id") Long id);

}
