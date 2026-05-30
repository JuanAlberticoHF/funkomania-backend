package tfg.funkomania.funkomania_api.testutils;

import tfg.funkomania.funkomania_api.dtos.usuario_dtos.UsuarioRegistroDTO;
import tfg.funkomania.funkomania_api.enums.RoleEnum;
import tfg.funkomania.funkomania_api.persistence.entities.Usuario;

import java.time.LocalDateTime;

public final class UsuarioTestFactory {

    private UsuarioTestFactory() {
    }

    public static UsuarioRegistroDTO registroDto(String email) {
        return new UsuarioRegistroDTO(email, "Password123", "Nombre");
    }

    public static Usuario usuarioPersistible(String email, String passwordHash) {
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setPassword(passwordHash);
        usuario.setNombre("Nombre");
        usuario.setApellido1("Apellido1");
        usuario.setApellido2("Apellido2");
        usuario.setTelefono("123456789");
        usuario.setFechaRegistro(LocalDateTime.now());
        usuario.setUltimoLogin(LocalDateTime.now());
        usuario.setRol(RoleEnum.CLIENTE);
        usuario.setActivo(true);
        return usuario;
    }
}

