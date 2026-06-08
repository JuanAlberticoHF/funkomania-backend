package tfg.funkomania.funkomania_api.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tfg.funkomania.funkomania_api.dtos.producto_dtos.ProductoDTOId;
import tfg.funkomania.funkomania_api.exceptions.custom_exceptions.NullEmailAutenticationException;
import tfg.funkomania.funkomania_api.exceptions.custom_exceptions.ProductoNotFoundException;
import tfg.funkomania.funkomania_api.exceptions.custom_exceptions.ProductoYaEnListaDeseadosException;
import tfg.funkomania.funkomania_api.exceptions.custom_exceptions.UsuarioNotFoundException;
import tfg.funkomania.funkomania_api.persistence.entities.Producto;
import tfg.funkomania.funkomania_api.persistence.entities.Usuario;
import tfg.funkomania.funkomania_api.persistence.repositories.IProductoRepository;
import tfg.funkomania.funkomania_api.persistence.repositories.IUsuarioRepository;

import java.util.List;

/**
 * <p>Servicio para gestionar la lista de deseados de un usuario de Funkomania.</p>
 * <p>Esta clase implementa la interfaz {@link ListaDeseosService} y proporciona la lógica de negocio para las operaciones
 * relacionadas con la lista de productos deseados del usuario.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.5.0
 */
@Service
public class ListaDeseosServiceImpl implements ListaDeseosService {

    /** Repositorio de usuarios. */
    private final IUsuarioRepository usuarioRepository;

    /** Repositorio de productos. */
    private final IProductoRepository productoRepository;

    public ListaDeseosServiceImpl(IUsuarioRepository usuarioRepository,
                                  IProductoRepository productoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public List<ProductoDTOId> obtenerListaDeseosDelUsuario() {
        // Obtenemos usuario autenticado desde el contexto de seguridad de Spring Security con la lista de deseos
        final Usuario usuario = obtenerUsuarioAutenticadoConListaDeseados();

        // Devolvemos la lista de productos deseados del usuario autenticado
        return usuario.getProductosDeseados().stream().map(ProductoDTOId::new).toList();
    }

    @Override
    public void agregarProductoListaDeseosDelUsuario(Long idProducto) {
        // Obtenemos usuario autenticado desde el contexto de seguridad de Spring Security con la lista de deseos
        Usuario usuario = obtenerUsuarioAutenticadoConListaDeseados();

        // Obtenemos el producto a añadir a la lista de deseados del usuario
        final Producto producto = productoRepository.findById(idProducto).orElseThrow(
                () -> new ProductoNotFoundException("No se encontró un producto con el id: " + idProducto));

        // Comprobamos que el producto no esté ya en la lista de deseados del usuario antes de añadirlo
        if (usuario.getProductosDeseados().contains(producto)){
            throw new ProductoYaEnListaDeseadosException(
                    "El producto con id: " + idProducto + " ya está en la lista de deseados del usuario con email: " + usuario.getEmail());
        }

        // Agregamos el producto a la lista de deseados del usuario
        usuario.getProductosDeseados().add(producto);

        // Guardamos el usuario con la nueva lista de deseados
        usuarioRepository.save(usuario);
    }

    @Override
    public void eliminarProductoListaDeseosDelUsuario(Long idProducto) {
        // Obtenemos usuario autenticado desde el contexto de seguridad de Spring Security con la lista de deseos
        Usuario usuario = obtenerUsuarioAutenticadoConListaDeseados();

        // Obtenemos el producto a añadir a la lista de deseados del usuario
        final Producto producto = productoRepository.findById(idProducto).orElseThrow(
                () -> new ProductoNotFoundException("No se encontró un producto con el id: " + idProducto));

        // Comprobamos que el producto no esté ya en la lista de deseados del usuario antes de añadirlo
        if (!usuario.getProductosDeseados().contains(producto)){
            throw new ProductoNotFoundException(
                    "El producto con id: " + idProducto + " no existe en la lista de deseados del usuario con email: " + usuario.getEmail());
        }

        // Agregamos el producto a la lista de deseados del usuario
        usuario.getProductosDeseados().remove(producto);

        // Guardamos el usuario con la nueva lista de deseados
        usuarioRepository.save(usuario);
    }

    /**
     * Metodo auxiliar para obtener el usuario autenticado con su lista de deseados.
     * @return El usuario autenticado con su lista de deseados.
     * @throws NullEmailAutenticationException Si el email obtenido del contexto de seguridad es nulo.
     * @throws UsuarioNotFoundException Si no se encuentra un usuario con el email obtenido
     */
    private Usuario obtenerUsuarioAutenticadoConListaDeseados(){
        // Obtenemos el email del usuario autenticado desde el contexto de seguridad de Spring Security
        final String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // Validamos que el email no sea nulo antes de continuar
        if (email == null) throw new NullEmailAutenticationException(
                "El email obtenido es nulo por problemas de autenticación, no se puede obtener las direcciones");

        // Obtenemos el identificador del usuario
        final Long idUsuario = usuarioRepository.findUsuarioByEmail(email)
                .orElseThrow(() -> new UsuarioNotFoundException(
                        "No se encontró un usuario con el email del usuario autenticado: " + email)).getIdUsuario();

        // Obtenemos de nuevo el usuario y obtenemos su información junto a los productos deseados
        return usuarioRepository.findUsuarioByIdConListaDeseos(idUsuario).orElseThrow(
                () -> new UsuarioNotFoundException("No se encontró un usuario con el email del usuario autenticado: " + email));
    }
}
