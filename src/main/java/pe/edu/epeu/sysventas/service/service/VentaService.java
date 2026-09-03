package pe.edu.epeu.sysventas.service.service;

import pe.edu.epeu.sysventas.dto.VentaRequestDTO;
import pe.edu.epeu.sysventas.dto.VentaResponseDTO;

import java.util.List;

public interface VentaService {
    VentaResponseDTO registrar(VentaRequestDTO request);
    VentaResponseDTO buscar(Long id);
    List<VentaResponseDTO> listar();
}