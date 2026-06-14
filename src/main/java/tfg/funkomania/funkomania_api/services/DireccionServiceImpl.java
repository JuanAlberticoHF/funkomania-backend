package tfg.funkomania.funkomania_api.services;

import jakarta.persistence.EntityManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import tfg.funkomania.funkomania_api.dtos.direccion_dtos.DireccionDTO;
import tfg.funkomania.funkomania_api.dtos.direccion_dtos.DireccionDTOId;
import tfg.funkomania.funkomania_api.exceptions.custom_exceptions.DireccionNotFoundException;
import tfg.funkomania.funkomania_api.exceptions.custom_exceptions.NullEmailAutenticationException;
import tfg.funkomania.funkomania_api.exceptions.custom_exceptions.UsuarioNotFoundException;
import tfg.funkomania.funkomania_api.persistence.entities.Direccion;
import tfg.funkomania.funkomania_api.persistence.entities.Usuario;
import tfg.funkomania.funkomania_api.persistence.repositories.IDireccionRepository;
import tfg.funkomania.funkomania_api.persistence.repositories.IUsuarioRepository;

import java.util.List;

/**
 * <p>Servicio para gestionar las direcciones de un usuario en la aplicación.</p>
 * <p>Esta clase implementa la interfaz {@link DireccionService} y proporciona la lógica de negocio para las operaciones
 * relacionadas con las direcciones.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.1.0
 * @since 0.5.0
 */
@Service
@Slf4j
public class DireccionServiceImpl implements DireccionService, DireccionAdminService {

    /** Repositorio de direcciones. */
    private final IDireccionRepository direccionRepository;

    /** Repositorio de usuarios. */
    private final IUsuarioRepository usuarioRepository;

    /** EntityManager para forzar operaciones y vaciar el contexto */
    private final EntityManager entityManager;

    public DireccionServiceImpl(IDireccionRepository direccionRepository,
                                IUsuarioRepository usuarioRepository,
                                EntityManager entityManager) {
        this.direccionRepository = direccionRepository;
        this.usuarioRepository = usuarioRepository;
        this.entityManager = entityManager;
    }

    @Override
    public List<DireccionDTOId> getDirecciones() {
        log.info("Obteniendo direcciones del usuario.");
        // Obtenemos el email del usuario autenticado desde el contexto de seguridad de Spring Security
        final String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // Validamos que el email no sea nulo antes de continuar con la obtención de las direcciones.
        if (email == null) throw new NullEmailAutenticationException(
                "El email obtenido es nulo por problemas de autenticación, no se puede obtener las direcciones");

        // Obtenemos el identificador del usuario
        final Long idUsuario = usuarioRepository.findUsuarioByEmail(email)
                .orElseThrow(() -> new UsuarioNotFoundException(
                        "No se encontró un usuario con el email del usuario autenticado: " + email)).getIdUsuario();

        // Obtenemos las direcciones del usuario autenticado utilizando el identificador del usuario
        return direccionRepository.findDireccionsByUsuario_IdUsuario(idUsuario).stream().map(DireccionDTOId::new).toList();
    }

    @Transactional
    @Override
    public void addDireccion(DireccionDTO direccionDTO) {
        log.info("Añadiendo nueva dirección para el usuario.");
        // Obtenemos el email del usuario autenticado desde el contexto de seguridad de Spring Security
        final String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // Validamos que el email no sea nulo antes de continuar con la adición de la dirección
        if (email == null) throw new NullEmailAutenticationException(
                "El email obtenido es nulo por problemas de autenticación, no se puede añadir la dirección");

        // Obtenemos el identificador del usuario
        final Usuario usuario = usuarioRepository.findUsuarioByEmail(email)
                .orElseThrow(() -> new UsuarioNotFoundException(
                        "No se encontró un usuario con el email del usuario autenticado: " + email));

        // Parsear el DTO al objeto entidad y añadimos el usuario
        final Direccion direccion = new Direccion(direccionDTO);
        direccion.setUsuario(usuario);

        // Añadimos la dirección a la base de datos
        Direccion direccionNueva = direccionRepository.save(direccion);

        // Sincronizamos y limpiamos para asegurar coherencia al leer desde vistas
        entityManager.flush();
        entityManager.clear();

        // Activamos la dirección del usuario autenticado utilizando el identificador del usuario y el identificador de la dirección
        direccionRepository.activarDireccion(direccionNueva.getId(), usuario.getIdUsuario());
    }

    @Override
    public void updateDireccion(Long idDireccion, DireccionDTO direccionDTO) {
        log.info("Actualizando dirección con ID: {}.", idDireccion);
        // Obtenemos la dirección desde la base de datos
        Direccion direccion = direccionRepository.findById(idDireccion)
                .orElseThrow(() -> new DireccionNotFoundException(
                        "No se encontró una dirección con el id: " + idDireccion));

        // Actualizamos los campos de la dirección con los datos del DTO
        direccion.setCalle(direccionDTO.getCalle());
        direccion.setNumero(direccionDTO.getNumero());
        direccion.setPiso(direccionDTO.getPiso());
        direccion.setPuerta(direccionDTO.getPuerta());
        direccion.setCiudad(direccionDTO.getCiudad());
        direccion.setMunicipio(direccionDTO.getMunicipio());
        direccion.setProvincia(direccionDTO.getProvincia());
        direccion.setCodigoPostal(direccionDTO.getCodigoPostal());
        direccion.setActivo(direccionDTO.getActivo());

        // Guardamos la dirección actualizada en la base de datos
        direccionRepository.save(direccion);
    }

    @Transactional()
    @Override
    public void activarDireccion(Long idDireccion) {
        log.info("Activando dirección con ID: {}.", idDireccion);
        // Obtenemos la dirección desde la base de datos para validar que existe
        if (!direccionRepository.existsDireccionById(idDireccion))
            throw new DireccionNotFoundException("No se encontró una dirección con el id: " + idDireccion);

        // Obtenemos el email del usuario autenticado desde el contexto de seguridad de Spring Security
        final String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // Validamos que el email no sea nulo antes de continuar con la activación de la dirección del usuario.
        if (email == null) throw new NullEmailAutenticationException(
                "El email obtenido es nulo por problemas de autenticación, no se puede activar la dirección");

        // Obtenemos el identificador del usuario
        final Long idUsuario = usuarioRepository.findUsuarioByEmail(email)
                .orElseThrow(() -> new UsuarioNotFoundException(
                        "No se encontró un usuario con el email del usuario autenticado: " + email)).getIdUsuario();

        // Activamos la dirección del usuario autenticado utilizando el identificador del usuario y el identificador de la dirección
        direccionRepository.activarDireccion(idDireccion, idUsuario);
    }

    @Override
    public List<DireccionDTOId> getDireccionesByUsuarioId(Long idUsuario) {
        log.info("Obteniendo direcciones del usuario con ID: {}.", idUsuario);

        // Validamos que el usuario exista antes de obtener sus direcciones
        if (!usuarioRepository.existsById(idUsuario))
            throw new UsuarioNotFoundException("No se encontró un usuario con el id: " + idUsuario);

        // Obtenemos las direcciones del usuario autenticado utilizando el identificador del usuario
        return direccionRepository.findDireccionsByUsuario_IdUsuario(idUsuario).stream().map(DireccionDTOId::new).toList();
    }
}
