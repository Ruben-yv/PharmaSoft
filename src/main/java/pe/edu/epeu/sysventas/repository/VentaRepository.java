package pe.edu.epeu.sysventas.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.epeu.sysventas.dto.VentaResponseDTO;
import pe.edu.epeu.sysventas.dto.reporte.ProductoMasVendidoDTO;
import pe.edu.epeu.sysventas.dto.reporte.VentaPorCategoriaDTO;
import pe.edu.epeu.sysventas.entity.Venta;
import pe.edu.epeu.sysventas.enums.EstadoVenta;

import java.time.LocalDateTime;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    @Query(
            """ 
            SELECT DISTINCT v FROM venta v 
            LEFT JOIN FETCH v.cliente c
            LEFT JOIN FETCH v.detalle d
            LEFT JOIN FETCH v.producto p
            WHERE (:clienteId IS NULL OR c.id = :clienteId) 
             AND (:estado IS NULL OR v.estado = :estado)
             AND (:desde IS NULL OR v.fecha >= :desde)
             AND (:hasta IS NULL OR v.fecha <= :hasta)
            """
    )
    List<Venta> buscar(
            @Param("clienteId") Long clienteId,
            @Param("estado")EstadoVenta estado,
            @Param("desde")LocalDateTime desde,
            @Param("hasta")LocalDateTime hasta,
            Sort sort
            );
    @Query("""
            select new pe.edu.epeu.sysventas.dto.reporte.VentaPorCategoriaDTO(
                       cat.id,
                       cat.nombre,
                       sum(d.cantidad),
                       sum(d.subtotal))
            from DetalleVenta d
            join d.venta v
            join d.producto p
            join p.categoria cat
            where v.estado = pe.edu.epeu.sysventas.enums.EstadoVenta.REGISTRADA
              and (:desde is null or v.fecha >= :desde)
              and (:hasta is null or v.fecha <= :hasta)
            group by cat.id, cat.nombre
            order by sum(d.subtotal) desc
            """)
    List<VentaPorCategoriaDTO> reporteVentasPorCategoria(
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta);

    @Query("""
            select new pe.edu.epeu.sysventas.dto.reporte.ProductoMasVendidoDTO(
                       p.id,
                       p.nombre,
                       cat.nombre,
                       sum(d.cantidad),
                       sum(d.subtotal))
            from DetalleVenta d
            join d.venta v
            join d.producto p
            join p.categoria cat
            where v.estado = pe.edu.epeu.sysventas.enums.EstadoVenta.REGISTRADA
              and (:desde is null or v.fecha >= :desde)
              and (:hasta is null or v.fecha <= :hasta)
            group by p.id, p.nombre, cat.nombre
            order by sum(d.cantidad) desc
            """)
    List<ProductoMasVendidoDTO> reporteProductosMasVendidos(
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta);

    List<VentaResponseDTO> buscar(
            Long clienteId,
            EstadoVenta estado,
            LocalDateTime desde,
            LocalDateTime hasta,
            String ordenarPor,
            String direccion);
}


