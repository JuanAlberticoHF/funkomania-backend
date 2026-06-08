package tfg.funkomania.funkomania_api.services;

import tfg.funkomania.funkomania_api.dtos.direccion_dtos.DireccionDTO;
import tfg.funkomania.funkomania_api.dtos.direccion_dtos.DireccionDTOId;
import tfg.funkomania.funkomania_api.persistence.entities.Direccion;

import java.util.List;

/**
 * Interfaz de servicio de la entidad {@link Direccion}.
 * Define los métodos para realizar operaciones relacionadas con las categorías de productos.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.5.0
 */
public interface DireccionService {
    /**
     * Obtiene todas las direcciones del usuario autenticado.
     * @return Lista de direcciones del usuario.
     */
    List<DireccionDTOId> getDirecciones();

    /**
     * Crea una nueva dirección para el usuario autenticado.
     * @param direccion DTO de la dirección a crear.
     */
    void addDireccion(DireccionDTO direccion);

    /**
     * Actualiza una dirección existente del usuario autenticado.
     * @param idDireccion ID de la dirección a actualizar.
     * @param direccion DTO con los datos actualizados de la dirección.
     */
    void updateDireccion(Long idDireccion, DireccionDTO direccion);
    /**
     * Activa una dirección específica para el usuario autenticado, desactivando las demás.
     * @param idDireccion ID de la dirección a activar.
     */
    void activarDireccion(Long idDireccion);
}
