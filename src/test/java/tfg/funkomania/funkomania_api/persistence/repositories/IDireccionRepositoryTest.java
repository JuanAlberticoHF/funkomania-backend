package tfg.funkomania.funkomania_api.persistence.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tfg.funkomania.funkomania_api.persistence.entities.Direccion;
import tfg.funkomania.funkomania_api.persistence.entities.Usuario;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class IDireccionRepositoryTest {

    @Autowired
    private IDireccionRepository direccionRepository;
    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Test
    void findDireccionsByUsuario_IdUsuario_deberiaRetornarDireccionesDelUsuario() {
        Usuario usuario = usuarioRepository.save(new Usuario());
        
        Direccion direccion = new Direccion();
        direccion.setUsuario(usuario);
        direccionRepository.save(direccion);

        List<Direccion> direcciones = direccionRepository.findDireccionsByUsuario_IdUsuario(usuario.getIdUsuario());

        assertThat(direcciones).hasSize(1);
    }
}
