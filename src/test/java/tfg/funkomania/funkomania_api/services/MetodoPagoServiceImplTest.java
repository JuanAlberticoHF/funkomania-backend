package tfg.funkomania.funkomania_api.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tfg.funkomania.funkomania_api.dtos.metodoPago_dtos.MetodoPagoDTOId;
import tfg.funkomania.funkomania_api.persistence.entities.MetodoPago;
import tfg.funkomania.funkomania_api.persistence.repositories.IMetodoPagoRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class MetodoPagoServiceImplTest {

    @Mock
    private IMetodoPagoRepository metodoPagoRepository;

    @InjectMocks
    private MetodoPagoServiceImpl metodoPagoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerMetodosPagoActivos() {
        MetodoPago mp = new MetodoPago(1L, "Tarjeta", true);
        when(metodoPagoRepository.findMetodoPagosByActivoIsTrue()).thenReturn(List.of(mp));

        List<MetodoPagoDTOId> result = metodoPagoService.obtenerMetodosPagoActivos();
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }
}
