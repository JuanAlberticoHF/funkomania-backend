package tfg.funkomania.funkomania_api.services;

import tfg.funkomania.funkomania_api.dtos.direccion_dtos.DireccionDTOId;
import tfg.funkomania.funkomania_api.persistence.entities.Direccion;

import java.util.List;

/**
 * Interfaz de servicio de la entidad {@link Direccion}.
 * Define los métodos para realizar operaciones relacionadas con las direcciones de los usuarios para administrador.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.8.3
 */
public interface DireccionAdminService {
    /**
     * Obtiene todas las direcciones de un usuario específico por su ID.
     * @return Lista de direcciones del usuario solicitado.
     */
    List<DireccionDTOId> getDireccionesByUsuarioId(Long idUsuario);
}
