package pe.edu.epeu.sysventas.service.service;

import pe.edu.epeu.sysventas.dto.auth.EmpleadoResponseDTO;
import pe.edu.epeu.sysventas.dto.auth.AsignarRolesRequestDTO;
import pe.edu.epeu.sysventas.dto.auth.CambiarEstadoUsuarioRequestDTO;
import pe.edu.epeu.sysventas.dto.auth.RolResponseDTO;
import pe.edu.epeu.sysventas.dto.auth.UsuarioRequestDTO;
import pe.edu.epeu.sysventas.dto.auth.UsuarioResponseDTO;

import java.util.List;

public interface UsuarioService {

    UsuarioResponseDTO crear(UsuarioRequestDTO request);

    List<UsuarioResponseDTO> listar();

    List<EmpleadoResponseDTO> listarEmpleados();

    List<RolResponseDTO> listarRoles();

    UsuarioResponseDTO asignarRoles(Long id, AsignarRolesRequestDTO request, String usernameAutenticado);

    UsuarioResponseDTO cambiarEstado(Long id, CambiarEstadoUsuarioRequestDTO request, String usernameAutenticado);
}
