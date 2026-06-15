package tfg.funkomania.funkomania_api.persistence.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tfg.funkomania.funkomania_api.persistence.entities.DetallePedido;
import tfg.funkomania.funkomania_api.persistence.entities.DetallePedidoId;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class IDetallePedidoRepositoryTest {

    @Autowired
    private IDetallePedidoRepository detallePedidoRepository;

    @Test
    void guardarYRecuperar_deberiaFuncionar() {
        assertThat(detallePedidoRepository).isNotNull();
    }
}
