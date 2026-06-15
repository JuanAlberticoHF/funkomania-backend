package tfg.funkomania.funkomania_api.persistence.entities;

import org.junit.jupiter.api.Test;
import tfg.funkomania.funkomania_api.dtos.direccion_dtos.DireccionDTO;
import static org.assertj.core.api.Assertions.assertThat;

class DireccionTest {

    @Test
    void constructor_deberiaPoblarCamposDesdeDto() {
        DireccionDTO dto = new DireccionDTO("Calle", "1", "1A", "A", "Ciudad", "Mun", "Prov", "28001", true);

        Direccion direccion = new Direccion(dto);

        assertThat(direccion.getCalle()).isEqualTo("Calle");
        assertThat(direccion.getCodigoPostal()).isEqualTo("28001");
        assertThat(direccion.getActivo()).isTrue();
    }
}
