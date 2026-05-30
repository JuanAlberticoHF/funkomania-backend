package tfg.funkomania.funkomania_api.dtos;

import org.junit.jupiter.api.Test;
import tfg.funkomania.funkomania_api.dtos.usuario_dtos.UsuarioDTOId;
import tfg.funkomania.funkomania_api.enums.RoleEnum;
import tfg.funkomania.funkomania_api.persistence.entities.Usuario;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas unitarias para el DTO UsuarioDTOId.
 *
 * <p>Comprueba que el constructor que recibe una entidad mapea todos los campos
 * al objeto de transferencia correctamente.</p>
 */
class UsuarioDTOIdTest {

    /**
     * Debe copiar todos los valores relevantes desde la entidad Usuario.
     */
    @Test
    void constructor_deberiaMapearCamposDeEntidad() {
        LocalDateTime now = LocalDateTime.now();
        Usuario usuario = new Usuario();
        usuario.setId(10L);
        usuario.setEmail("dto@example.com");
        usuario.setPassword("hash");
        usuario.setNombre("Nombre");
        usuario.setApellido1("Apellido1");
        usuario.setApellido2("Apellido2");
        usuario.setTelefono("123456789");
        usuario.setFechaRegistro(now);
        usuario.setUltimoLogin(now);
        usuario.setRol(RoleEnum.CLIENTE);
        usuario.setActivo(true);

        UsuarioDTOId dto = new UsuarioDTOId(usuario);

        assertThat(dto.getId()).isEqualTo(10L);
        assertThat(dto.getEmail()).isEqualTo("dto@example.com");
        assertThat(dto.getPassword()).isEqualTo("hash");
        assertThat(dto.getNombre()).isEqualTo("Nombre");
        assertThat(dto.getApellido1()).isEqualTo("Apellido1");
        assertThat(dto.getApellido2()).isEqualTo("Apellido2");
        assertThat(dto.getTelefono()).isEqualTo("123456789");
        assertThat(dto.getFechaRegistro()).isEqualTo(now);
        assertThat(dto.getUltimoLogin()).isEqualTo(now);
        assertThat(dto.getRol()).isEqualTo(RoleEnum.CLIENTE);
        assertThat(dto.isActivo()).isTrue();
    }
}
