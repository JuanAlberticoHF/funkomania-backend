package tfg.funkomania.funkomania_api.dtos.direccion_dtos;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class DireccionDTOTest {
    @Test
    void testGettersAndSetters() {
        DireccionDTO dto = new DireccionDTO("Calle", "1", "1A", "B", "Ciudad", "Mun", "Prov", "28001", true);
        assertThat(dto.getCalle()).isEqualTo("Calle");
        dto.setCalle("Nueva Calle");
        assertThat(dto.getCalle()).isEqualTo("Nueva Calle");
    }
}
