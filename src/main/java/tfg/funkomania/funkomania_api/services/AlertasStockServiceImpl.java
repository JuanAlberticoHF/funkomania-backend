package tfg.funkomania.funkomania_api.services;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import tfg.funkomania.funkomania_api.persistence.entities.VistaAdminAlertasStockDTOId;
import tfg.funkomania.funkomania_api.persistence.repositories.IVistaAdminAlertasStockRepository;

import java.util.List;

/**
 * <p>Servicio que implementa la lógica de negocio relacionada con las alertas de stock.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
@Service
@Slf4j
public class AlertasStockServiceImpl implements AlertasStockService {

    /** Repositorio para acceder a los datos de las alertas de stock. */
    private final IVistaAdminAlertasStockRepository vistaAdminAlertasStockRepository;

    public AlertasStockServiceImpl(IVistaAdminAlertasStockRepository vistaAdminAlertasStockRepository) {
        this.vistaAdminAlertasStockRepository = vistaAdminAlertasStockRepository;
    }

    @Override
    public List<VistaAdminAlertasStockDTOId> obtenerAlertasStock() {
        log.info("Obteniendo alertas de stock en servicio.");
        // Obtener todas las alertas de stock desde el repositorio y mapearlas a DTOs
        return vistaAdminAlertasStockRepository.findAll().stream()
                .map(VistaAdminAlertasStockDTOId::new).toList();
    }
}
