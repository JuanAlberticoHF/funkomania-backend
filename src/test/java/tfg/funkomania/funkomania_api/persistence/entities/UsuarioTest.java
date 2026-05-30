package tfg.funkomania.funkomania_api.persistence.entities;

import org.junit.jupiter.api.Test;
import tfg.funkomania.funkomania_api.dtos.usuario_dtos.UsuarioRegistroDTO;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas unitarias para la entidad Usuario.
 *
 * <p>Verifica que el constructor basado en DTO copia correctamente los campos
 * esperados y deja sin inicializar los valores opcionales.</p>
 */
class UsuarioTest {

    /**
     * Debe poblar los campos de la entidad a partir de un DTO de registro.
     */
    @Test
    void constructor_deberiaPoblarCamposDesdeRegistroDto() {
        UsuarioRegistroDTO dto = new UsuarioRegistroDTO("user@example.com", "pass", "Nombre");

        Usuario usuario = new Usuario(dto);

        assertThat(usuario.getId()).isNull();
        assertThat(usuario.getEmail()).isEqualTo("user@example.com");
        assertThat(usuario.getPassword()).isEqualTo("pass");
        assertThat(usuario.getNombre()).isEqualTo("Nombre");
        assertThat(usuario.getApellido1()).isNull();
        assertThat(usuario.getApellido2()).isNull();
        assertThat(usuario.getTelefono()).isNull();
        assertThat(usuario.getFechaRegistro()).isNull();
        assertThat(usuario.getUltimoLogin()).isNull();
        assertThat(usuario.getRol()).isNull();
        assertThat(usuario.isActivo()).isFalse();
    }
}
