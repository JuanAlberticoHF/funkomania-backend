package tfg.funkomania.funkomania_api.services;

import tfg.funkomania.funkomania_api.persistence.entities.VistaAdminAlertasStockDTOId;

import java.util.List;

/**
 * Interfaz de servicio para la gestión de alertas de stock para administrador
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
public interface AlertasStockService {
    /**
     * Metodo para obtener las alertas de stock para el administrador.
     * @return Lista de objetos {@link VistaAdminAlertasStockDTOId} que representan las alertas de stock.
     */
    List<VistaAdminAlertasStockDTOId> obtenerAlertasStock();
}
