package tfg.funkomania.funkomania_api.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tfg.funkomania.funkomania_api.persistence.entities.VistaAdminAlertasStock;
import tfg.funkomania.funkomania_api.persistence.entities.VistaAdminAlertasStockDTOId;
import tfg.funkomania.funkomania_api.persistence.repositories.IVistaAdminAlertasStockRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class AlertasStockServiceImplTest {

    @Mock
    private IVistaAdminAlertasStockRepository vistaAdminAlertasStockRepository;

    @InjectMocks
    private AlertasStockServiceImpl alertasStockService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerAlertasStock() {
        VistaAdminAlertasStock alerta = VistaAdminAlertasStock.builder()
                .idProducto(1L)
                .nombre("Producto1")
                .stock(5)
                .idCategoria(1L)
                .prioridad("ALTA")
                .build();
        when(vistaAdminAlertasStockRepository.findAll()).thenReturn(List.of(alerta));

        List<VistaAdminAlertasStockDTOId> result = alertasStockService.obtenerAlertasStock();
        assertEquals(1, result.size());
    }
}
