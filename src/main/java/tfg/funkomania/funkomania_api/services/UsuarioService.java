package tfg.funkomania.funkomania_api.services;

import tfg.funkomania.funkomania_api.dtos.usuario_dtos.UsuarioUpdateRequestDTO;
import tfg.funkomania.funkomania_api.dtos.usuario_dtos.VistaUsuarioPerfilClienteDTOId;

/**
 * Interfaz de servicio para la gestión de usuarios en la aplicación Funkomania.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.1
 * @since 0.4.0
 */
public interface UsuarioService {
    /**
     * Obtiene los datos del perfil del cliente autenticado.
     * @return El perfil del cliente autenticado, o null si no hay un cliente autenticado.
     */
    VistaUsuarioPerfilClienteDTOId obtenerPerfilClienteAutenticado();

    /**
     * Actualiza los datos del usuario autenticado con la información proporcionada en el DTO de actualización.
     * @param usuarioUpdateRequestDTO El DTO que contiene la información actualizada del usuario. No debe ser nulo y debe contener datos válidos.
     */
    void actualizarUsuarioAutenticado(UsuarioUpdateRequestDTO usuarioUpdateRequestDTO);
}
