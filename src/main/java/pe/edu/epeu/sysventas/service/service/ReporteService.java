package pe.edu.epeu.sysventas.service.service;

import org.springframework.transaction.annotation.Transactional;
import pe.edu.epeu.sysventas.dto.reporte.ProductoMasVendidoDTO;
import pe.edu.epeu.sysventas.dto.reporte.VentaPorCategoriaDTO;

import java.time.LocalDate;
import java.util.List;

public interface ReporteService {
    @Transactional(readOnly = true)
    List<VentaPorCategoriaDTO> ventasPorCategoria(
            LocalDate desde,
            LocalDate hasta);

    @Transactional(readOnly = true)
    List<ProductoMasVendidoDTO> productosMasVendidos(
            LocalDate desde,
            LocalDate hasta);
}
