package pe.edu.epeu.sysventas.service.service;

import pe.edu.epeu.sysventas.dto.auth.LoginRequestDTO;
import pe.edu.epeu.sysventas.dto.auth.LoginResponseDTO;
import pe.edu.epeu.sysventas.dto.auth.UsuarioResponseDTO;

public interface AuthService {

    LoginResponseDTO login(LoginRequestDTO request);

    UsuarioResponseDTO usuarioActual(String username);
}