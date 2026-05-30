package tfg.funkomania.funkomania_api.persistence.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tfg.funkomania.funkomania_api.persistence.entities.Usuario;
import tfg.funkomania.funkomania_api.testutils.UsuarioTestFactory;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas de integración para el repositorio de usuarios.
 *
 * <p>Comprueba el comportamiento del metodo de consulta por email contra la base de datos
 * de pruebas configurada para el proyecto.</p>
 */
@SpringBootTest
@Transactional
class IUsuarioRepositoryTest {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    /**
     * Debe devolver verdadero cuando existe un usuario con el email indicado.
     */
    @Test
    void existePorEmail_deberiaRetornarTrueCuandoUsuarioExiste() {
        Usuario usuario = UsuarioTestFactory.usuarioPersistible("exists@example.com", "hash");
        usuarioRepository.save(usuario);

        boolean exists = usuarioRepository.existsByEmail("exists@example.com");

        assertThat(exists).isTrue();
    }

    /**
     * Debe devolver falso cuando no existe ningún usuario con el email indicado.
     */
    @Test
    void existePorEmail_deberiaRetornarFalseCuandoUsuarioNoExiste() {
        boolean exists = usuarioRepository.existsByEmail("missing@example.com");

        assertThat(exists).isFalse();
    }
}
