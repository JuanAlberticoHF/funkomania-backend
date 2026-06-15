package tfg.funkomania.funkomania_api.persistence.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class INotificacionRepositoryTest {

    @Autowired
    private INotificacionRepository notificacionRepository;

    @Test
    void repositorioDeberiaEstarInstanciado() {
        assertThat(notificacionRepository).isNotNull();
    }
}
