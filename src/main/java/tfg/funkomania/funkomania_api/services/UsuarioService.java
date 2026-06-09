package tfg.funkomania.funkomania_api.services;

import tfg.funkomania.funkomania_api.dtos.usuario_dtos.UsuarioDTOId;
import tfg.funkomania.funkomania_api.dtos.usuario_dtos.UsuarioUpdateRequestDTO;
import tfg.funkomania.funkomania_api.dtos.usuario_dtos.VistaUsuarioPerfilClienteDTOId;

import java.util.List;

/**
 * Interfaz de servicio para la gestión de usuarios en la aplicación Funkomania.
 *
 * @author JuanAlbeticoHF
 * @version 1.1.0
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

    /**
     * Obtiene una lista de todos los usuarios registrados en el sistema, con la opción de filtrar por un término de búsqueda.
     * @param search Un término de búsqueda opcional para filtrar los usuarios por nombre,
     * @return Una lista de objetos UsuarioDTOId que representan a los usuarios registrados en el sistema,
     * filtrados por el término de búsqueda si se proporciona.
     */
    List<UsuarioDTOId> obtenerTodosLosUsuarios(String search);
}
