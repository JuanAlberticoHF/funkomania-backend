package tfg.funkomania.funkomania_api.persistence.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tfg.funkomania.funkomania_api.persistence.entities.MetodoPago;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class IMetodoPagoRepositoryTest {

    @Autowired
    private IMetodoPagoRepository metodoPagoRepository;

    @Test
    void findMetodoPagosByActivoIsTrue_deberiaRetornarSoloActivos() {
        MetodoPago activo = new MetodoPago();
        activo.setNombre("Tarjeta");
        activo.setActivo(true);
        metodoPagoRepository.save(activo);

        MetodoPago inactivo = new MetodoPago();
        inactivo.setNombre("Transferencia");
        inactivo.setActivo(false);
        metodoPagoRepository.save(inactivo);

        List<MetodoPago> activos = metodoPagoRepository.findMetodoPagosByActivoIsTrue();

        assertThat(activos).hasSize(1);
        assertThat(activos.get(0).getActivo()).isTrue();
    }
}
