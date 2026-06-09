package tfg.funkomania.funkomania_api.services;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tfg.funkomania.funkomania_api.dtos.usuario_dtos.UsuarioDTOId;
import tfg.funkomania.funkomania_api.dtos.usuario_dtos.UsuarioUpdateRequestDTO;
import tfg.funkomania.funkomania_api.dtos.usuario_dtos.VistaUsuarioPerfilClienteDTOId;
import tfg.funkomania.funkomania_api.exceptions.custom_exceptions.NullEmailAutenticationException;
import tfg.funkomania.funkomania_api.exceptions.custom_exceptions.UsuarioNotFoundException;
import tfg.funkomania.funkomania_api.persistence.entities.Usuario;
import tfg.funkomania.funkomania_api.persistence.entities.VistaUsuarioPerfilCliente;
import tfg.funkomania.funkomania_api.persistence.repositories.IUsuarioRepository;
import tfg.funkomania.funkomania_api.persistence.repositories.IVistaUsuarioPerfilClienteRepository;
import tfg.funkomania.funkomania_api.persistence.specifications.UsuarioSpecification;

import java.util.List;

/**
 * <p>Servicio para gestionar las operaciones de usuarios</p>
 * <p>Esta clase implementa la interfaz {@link UsuarioService} y proporciona la lógica de negocio para las operaciones
 * relacionadas con los datos del usuario y la vistas de usuarios.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.2.0
 * @since 0.4.0
 */
@Service
public class UsuarioServiceImpl implements UsuarioService {

    /** Repositorio para obtener la vista con los datos del perfil del cliente autenticado. */
    private final IVistaUsuarioPerfilClienteRepository vistaUsuarioPerfilClienteRepository;

    /** Repositorio para la entidad Usuario */
    private final IUsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(IVistaUsuarioPerfilClienteRepository vistaUsuarioPerfilClienteRepository,
                              IUsuarioRepository usuarioRepository) {
        this.vistaUsuarioPerfilClienteRepository = vistaUsuarioPerfilClienteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public VistaUsuarioPerfilClienteDTOId obtenerPerfilClienteAutenticado() {
        // Obtenemos el email del usuario autenticado desde el contexto de seguridad de Spring Security
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // Validamos que el email no sea nulo antes de continuar con la actualización del usuario
        if (email == null) throw new NullEmailAutenticationException(
                "El email obtenido es nulo por problemas de autenticación, no se puede obtener el perfil del cliente autenticado");

        // Buscamos el perfil del cliente en la base de datos utilizando el email obtenido del contexto de seguridad
        VistaUsuarioPerfilCliente vistaUsuarioPerfilCliente = vistaUsuarioPerfilClienteRepository.findVistaUsuarioPerfilClienteByEmail(email)
                .orElseThrow(() -> new UsuarioNotFoundException(
                "No se encontró un perfil de cliente con el email del usuario autenticado: " + email));

        // Convertimos la entidad VistaUsuarioPerfilCliente a un DTO VistaUsuarioPerfilClienteDTOId y lo retornamos
        return new VistaUsuarioPerfilClienteDTOId(vistaUsuarioPerfilCliente);
    }

    @Override
    public void actualizarUsuarioAutenticado(UsuarioUpdateRequestDTO usuarioUpdateRequestDTO) {
        // Comprobamos que todas los campos del DTO de actualización sean nulos.
        if (UsuarioUpdateRequestDTO.isNullOrEmpty(usuarioUpdateRequestDTO)) {
            throw new IllegalArgumentException("Todos los parámetros de actualización no pueden ser nulos o el nombre no puede estar vacío");
        }

        // Obtenemos el email del usuario autenticado desde el contexto de seguridad de Spring Security
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // Validamos que el email no sea nulo antes de continuar con la actualización del usuario
        if (email == null) throw new NullEmailAutenticationException(
                "El email obtenido es nulo por problemas de autenticación, no se puede actualizar el usuario autenticado");

        // Buscamos el usuario en la base de datos utilizando el email obtenido del contexto de seguridad
        Usuario usuario = usuarioRepository.findUsuarioByEmail(email)
                .orElseThrow(() -> new UsuarioNotFoundException(
                        "No se encontró un usuario con el email del usuario autenticado: " + email));

        // Solo actualizamos los campos que no sean nulos en el DTO de actualización y los vacíos los ponemos a null en la base de datos
        if (usuarioUpdateRequestDTO.getNombre() != null) {
            usuario.setNombre(usuarioUpdateRequestDTO.getNombre());
        }

        if(usuarioUpdateRequestDTO.getApellido1() != null) {
            if (usuarioUpdateRequestDTO.getApellido1().isEmpty()) {
                usuario.setApellido1(null);
            } else {
                usuario.setApellido1(usuarioUpdateRequestDTO.getApellido1());
            }
        }

        if(usuarioUpdateRequestDTO.getApellido2() != null) {
            if (usuarioUpdateRequestDTO.getApellido2().isEmpty()) {
                usuario.setApellido2(null);
            } else {
                usuario.setApellido2(usuarioUpdateRequestDTO.getApellido2());
            }
        }

        if(usuarioUpdateRequestDTO.getTelefono() != null) {
            if (usuarioUpdateRequestDTO.getTelefono().isEmpty()) {
                usuario.setTelefono(null);
            } else {
                usuario.setTelefono(usuarioUpdateRequestDTO.getTelefono());
            }
        }

        // Guardamos el usuario actualizado en la base de datos
        usuarioRepository.save(usuario);
    }

    public List<UsuarioDTOId> obtenerTodosLosUsuarios(String search) {
        // Creamos una especificación vacía para construir la consulta dinámica
        Specification<Usuario> spec = null;

        // Agregar filtros a la especificación según los parámetros de búsqueda
        // - Si se proporciona un término de búsqueda, filtrar por email.
        if (search != null && !search.isEmpty()) {
            spec = UsuarioSpecification.busquedaContiene(search);
        }

        // Creamos una lista de usuarios que recibirá la busqueda con o sin especificaciones.
        List<Usuario> usuarioList;

        // Si no se ha creado ninguna especificación, obtenemos todos los usuarios sin filtrar.
        if (spec == null) usuarioList = usuarioRepository.findAll();
        else usuarioList = usuarioRepository.findAll(spec);

        // Devolvemos el listado de usuarios en formado UsuarioDTOId
        return usuarioList.stream().map(UsuarioDTOId::new).toList();
    }
}
