package tfg.funkomania.funkomania_api.services;

import tfg.funkomania.funkomania_api.dtos.metodoPago_dtos.MetodoPagoDTOId;

import java.util.List;

/**
 * Interfaz de servicio de la entidad MetodoPago
 * Define el metodo para obtener los métodos de pago activos.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.5.0
 */
public interface MetodoPagoService {
    /**
     * Obtiene los métodos de pago activos.
     * @return Lista de métodos de pago activos.
     */
    List<MetodoPagoDTOId> obtenerMetodosPagoActivos();
}
