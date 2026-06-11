package tfg.funkomania.funkomania_api.services;

import jakarta.persistence.EntityManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tfg.funkomania.funkomania_api.dtos.carrito_dtos.VistaCarritoContenidoDTOId;
import tfg.funkomania.funkomania_api.dtos.carrito_dtos.VistaCarritoTotalesContenidoDTOId;
import tfg.funkomania.funkomania_api.exceptions.custom_exceptions.*;
import tfg.funkomania.funkomania_api.persistence.entities.*;
import tfg.funkomania.funkomania_api.persistence.enums.EstadoCarritoEnum;
import tfg.funkomania.funkomania_api.persistence.repositories.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>Servicio para gestionar el carrito de compras de un usuario en la aplicación.</p>
 * <p>Esta clase implementa la interfaz {@link CarritoService}.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.1
 * @since 0.7.0
 */
@Service
public class CarritoServiceImpl implements CarritoService {

    /** Repositorio de operaciones para la entidad Carrito */
    private final ICarritoRepository carritoRepository;

    /** Repositorio de operaciones para la entidad DetalleCarrito */
    private final IDetalleCarritoRepository detalleCarritoRepository;

    /** Repositorio de operaciones para la vista VistaCarritoTotales */
    private final IVistaCarritoContenidoRepository vistaCarritoContenidoRepository;

    /** Repositorio de operaciones para la vista VistaCarritoTotales */
    private final IVistaCarritoTotalesRepository vistaCarritoTotalesRepository;

    /** Repositorio para obtener al usuario */
    private final IUsuarioRepository usuarioRepository;

    /** Repositorio para obtener al producto */
    private final IProductoRepository productoRepository;

    /** EntityManager para forzar operaciones y vaciar el contexto */
    private final EntityManager entityManager;

    public CarritoServiceImpl(ICarritoRepository carritoRepository,
                              IDetalleCarritoRepository detalleCarritoRepository,
                              IVistaCarritoContenidoRepository vistaCarritoContenidoRepository,
                              IVistaCarritoTotalesRepository vistaCarritoTotalesRepository,
                              IUsuarioRepository usuarioRepository,
                              IProductoRepository productoRepository,
                              EntityManager entityManager) {
        this.carritoRepository = carritoRepository;
        this.detalleCarritoRepository = detalleCarritoRepository;
        this.vistaCarritoContenidoRepository = vistaCarritoContenidoRepository;
        this.vistaCarritoTotalesRepository = vistaCarritoTotalesRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public VistaCarritoTotalesContenidoDTOId obtenerCarritoCompletoUsuario() {
        // Obtenemos usuario autenticado desde el contexto de seguridad de Spring Security
        Usuario usuario = obtenerUsuarioAutenticado();

        // Comprobamos que el usuario tenga un carrito creado
        Carrito carritoUsuario = usuario.getCarrito();

        if (carritoUsuario == null) {
            usuario = crearCarritoParaUsuario(usuario);
            carritoUsuario = usuario.getCarrito();
        }
        // Devolvemos el carrito completo del usuario
        return obtenerCarritoCompletoDelUsuario(usuario.getIdUsuario(), carritoUsuario.getIdCarrito());
    }

    @Transactional
    @Override
    public VistaCarritoTotalesContenidoDTOId agregarProductoAlCarrito(Long idProducto, Integer cantidad) {
        // Comprobamos primero que el producto exista
        Producto producto = productoRepository.findById(idProducto).orElseThrow(() -> new ProductoNotFoundException(
                "Producto deseado no encontrado"));

        // Si la cantidad es nula se añade uno por defecto
        if (cantidad == null) cantidad = 1;

        // Obtenemos usuario autenticado desde el contexto de seguridad de Spring Security
        Usuario usuario = obtenerUsuarioAutenticado();

        // Comprobamos que el usuario tenga un carrito creado
        Carrito carritoUsuario = usuario.getCarrito();

        if (carritoUsuario == null) {
            usuario = crearCarritoParaUsuario(usuario);
            carritoUsuario = usuario.getCarrito();
        }

        // Comprobamos si el producto ya existe en el carrito del usuario y lo obtenemos, si no existe, lo creamos
        DetalleCarrito detalleCarrito = detalleCarritoRepository.findById(
                new DetalleCarritoId(carritoUsuario.getIdCarrito(), idProducto)).orElse(null);

        // Si el detalle del carrito no existe, lo creamos
        if (detalleCarrito == null) {
            // Creamos nuevo detalle del carrito
            detalleCarritoRepository.agregarProductoAlCarrito(usuario.getIdUsuario(), producto.getId(), cantidad);
        } else {
            // Si el detalle del carrito ya existe, actualizamos la cantidad sumando la nueva cantidad a la existente
            Integer nuevaCantidad = detalleCarrito.getCantidad() + cantidad;

            // Establecemos la nueva cantidad al producto.
            detalleCarrito.setCantidad(nuevaCantidad);

            // Guardamos el detalle del carrito actualizado
            detalleCarritoRepository.save(detalleCarrito);
        }

        // Actualizamos la última fecha de modificación
        carritoRepository.updateFechaActualizacionByIdCarrito(carritoUsuario.getIdCarrito());

        // Forzamos la ejecución de las operaciones pendientes en la base de datos para asegurar que los datos estén actualizados antes de obtener el carrito completo
        entityManager.flush();
        // Limpiamos el contexto de persistencia para evitar que se devuelvan datos obsoletos en la consulta del carrito completo.
        entityManager.clear();

        // Devolvemos el carrito completo del usuario con el producto actualizado
        return obtenerCarritoCompletoDelUsuario(usuario.getIdUsuario(), carritoUsuario.getIdCarrito());
    }

    @Transactional
    @Override
    public VistaCarritoTotalesContenidoDTOId actualizarCantidadProducto(Long idProducto, Integer cantidad) {
        // Comprobamos primero que el producto exista
        productoRepository.findById(idProducto).orElseThrow(() -> new ProductoNotFoundException(
                "Producto deseado no encontrado"));

        // Obtenemos usuario autenticado desde el contexto de seguridad de Spring Security
        Usuario usuario = obtenerUsuarioAutenticado();

        // Comprobamos que el usuario tenga un carrito creado
        Carrito carritoUsuario = usuario.getCarrito();

        if (carritoUsuario == null) throw new CarritoNotFoundException("Carrito no encontrado");

        // Comprobamos si el producto ya existe en el carrito del usuario y lo obtenemos
        DetalleCarrito detalleCarrito = detalleCarritoRepository.findById(
                new DetalleCarritoId(carritoUsuario.getIdCarrito(), idProducto)
                ).orElseThrow(() -> new ProductoNotFoundInCarritoException(
                        "El producto con id " + idProducto + " no se encuentra en el carrito del usuario"));

        // Establecemos la nueva cantidad al producto.
        detalleCarrito.setCantidad(cantidad);

        // Guardamos el detalle del carrito actualizado
        detalleCarritoRepository.save(detalleCarrito);

        // Actualizamos la última fecha de modificación
        carritoRepository.updateFechaActualizacionByIdCarrito(carritoUsuario.getIdCarrito());

        // Forzamos la ejecución de las operaciones pendientes en la base de datos para asegurar que los datos estén actualizados antes de obtener el carrito completo
        entityManager.flush();
        // Limpiamos el contexto de persistencia para evitar que se devuelvan datos obsoletos en la consulta del carrito completo.
        entityManager.clear();

        // Devolvemos el carrito completo del usuario con el producto actualizado
        return obtenerCarritoCompletoDelUsuario(usuario.getIdUsuario(), carritoUsuario.getIdCarrito());
    }

    @Transactional
    @Override
    public VistaCarritoTotalesContenidoDTOId eliminarProductoDelCarrito(Long idProducto) {
        // Comprobamos primero que el producto exista
        productoRepository.findById(idProducto)
                .orElseThrow(() -> new ProductoNotFoundException("Producto deseado no encontrado"));

        // Obtenemos usuario autenticado desde el contexto de seguridad de Spring Security
        Usuario usuario = obtenerUsuarioAutenticado();

        // Comprobamos que el usuario tenga un carrito creado
        Carrito carritoUsuario = usuario.getCarrito();

        if (carritoUsuario == null) throw new CarritoNotFoundException("Carrito no encontrado");

        // Comprobamos si el producto ya existe en el carrito del usuario y lo obtenemos
        // Construimos el identificador del detalle del carrito.
        DetalleCarritoId detalleCarritoId = new DetalleCarritoId(carritoUsuario.getIdCarrito(), idProducto);

        // Si el detalle del carrito no existe, lanzamos excepción
        DetalleCarrito detalleCarrito = detalleCarritoRepository.findById(detalleCarritoId)
                .orElseThrow(() -> new ProductoNotFoundInCarritoException(
                "El producto con id " + idProducto + " no se encuentra en el carrito del usuario"));

        // Eliminamos el detalle del carrito
        detalleCarritoRepository.delete(detalleCarrito);

        // Actualizamos la última fecha de modificación
        carritoRepository.updateFechaActualizacionByIdCarrito(carritoUsuario.getIdCarrito());

        // Forzamos la ejecución de las operaciones pendientes en la base de datos para asegurar que los datos estén actualizados antes de obtener el carrito completo
        entityManager.flush();
        // Limpiamos el contexto de persistencia para evitar que se devuelvan datos obsoletos en la consulta del carrito completo.
        entityManager.clear();

        // Devolvemos la información actualizada del carrito del usuario sin el producto eliminado
        return obtenerCarritoCompletoDelUsuario(usuario.getIdUsuario(), carritoUsuario.getIdCarrito());
    }

    @Transactional
    @Override
    public VistaCarritoTotalesContenidoDTOId vaciarCarrito() {

        // Obtenemos usuario autenticado desde el contexto de seguridad de Spring Security
        Usuario usuario = obtenerUsuarioAutenticado();

        // Comprobamos que el usuario tenga un carrito creado
        Carrito carritoUsuario = usuario.getCarrito();

        if (carritoUsuario == null) throw new CarritoNotFoundException("Carrito no encontrado");

        // Eliminamos todos los elementos del carrito
        detalleCarritoRepository.deleteDetalleCarritosByCarrito(carritoUsuario);

        // Actualizamos la última fecha de modificación
        carritoRepository.updateFechaActualizacionByIdCarrito(carritoUsuario.getIdCarrito());

        // Forzamos la ejecución de las operaciones pendientes en la base de datos para asegurar que los datos estén actualizados antes de obtener el carrito completo
        entityManager.flush();
        // Limpiamos el contexto de persistencia para evitar que se devuelvan datos obsoletos en la consulta del carrito completo.
        entityManager.clear();

        // Devolvemos la información actualizada del carrito del usuario sin productos
        return obtenerCarritoCompletoDelUsuario(usuario.getIdUsuario(), carritoUsuario.getIdCarrito());
    }

    /**
     * Metodo para obtener el carrito completo del usuario.
     * @param idUsuario Id del usuario del que se desea obtener el carrito completo.
     * @param idCarrito Id del carrito del que se desea obtener el carrito completo.
     * @return VistaCarritoTotalesContenidoDTOId con el carrito completo del usuario.
     */
    private VistaCarritoTotalesContenidoDTOId obtenerCarritoCompletoDelUsuario(Long idUsuario, Long idCarrito) {
        // Obtenemos el contenido del carrito del usuario
        List<VistaCarritoContenidoDTOId> listaContenidoCarrito =
                vistaCarritoContenidoRepository.findVistaCarritoContenidosByIdUsuario(idUsuario)
                        .stream().map(VistaCarritoContenidoDTOId::new).toList();

        // Obtenemos el total del carrito del usuario o si esta vacío creamos un VistaCarritoTotales con totales a 0
        VistaCarritoTotales carritoTotales = vistaCarritoTotalesRepository.findByIdCarrito(idCarrito)
                .orElse(VistaCarritoTotales.builder()
                        .idCarrito(idCarrito)
                        .idUsuario(idUsuario)
                        .totalArticulosDiferentes(0)
                        .totalUnidadesFisicas(0)
                        .baseImponible(BigDecimal.ZERO)
                        .totalAPagar(BigDecimal.ZERO)
                        .build());

        // Construimos y devolvemos el DTO con el contenido y el total del carrito del usuario
        return new VistaCarritoTotalesContenidoDTOId(listaContenidoCarrito, carritoTotales);
    }

    /**
     * Metodo auxiliar para crear un nuevo carrito para un usuario dado.
     * @param usuario El usuario para el cual se creará el carrito.
     * @return El carrito creado para el usuario.
     */
    private Usuario crearCarritoParaUsuario(Usuario usuario) {
        if (usuario.getCarrito() == null) {
            Carrito nuevoCarrito = Carrito.builder()
                    .idCarrito(null) // El ID se generará automáticamente al guardar el carrito
                    .usuario(usuario)
                    .fechaCreacion(LocalDateTime.now())
                    .fechaActualizacion(LocalDateTime.now())
                    .estado(EstadoCarritoEnum.ACTIVO).build();

            // Asociamos el nuevo carrito al usuario
            usuario.setCarrito(nuevoCarrito);

            // Guardamos el usuario con el nuevo carrito asociado
            // Devolvemos el carrito del usuario guardado, que ahora incluye el nuevo carrito creado
            return usuarioRepository.save(usuario);
        }
        return usuario;
    }

    /**
     * Metodo auxiliar para obtener el usuario autenticado.
     * @return El usuario autenticado.
     * @throws NullEmailAutenticationException Si el email obtenido del contexto de seguridad es nulo.
     * @throws UsuarioNotFoundException Si no se encuentra un usuario con el email obtenido
     */
    private Usuario obtenerUsuarioAutenticado(){
        // Obtenemos el email del usuario autenticado desde el contexto de seguridad de Spring Security
        final String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // Validamos que el email no sea nulo antes de continuar
        if (email == null) throw new NullEmailAutenticationException(
                "El email obtenido es nulo por problemas de autenticación, no se puede obtener las direcciones");

        // Devolvemos al usuario autenticado
        return usuarioRepository.findUsuarioByEmail(email)
                .orElseThrow(() -> new UsuarioNotFoundException(
                        "No se encontró un usuario con el email del usuario autenticado: " + email));
    }

}
