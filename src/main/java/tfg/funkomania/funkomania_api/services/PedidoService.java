package tfg.funkomania.funkomania_api.services;

import tfg.funkomania.funkomania_api.dtos.pedido_dtos.CrearPedidoRequestDTO;
import tfg.funkomania.funkomania_api.dtos.pedido_dtos.CrearPedidoResponseDTO;
import tfg.funkomania.funkomania_api.dtos.pedido_dtos.PedidoCompletoDTOId;
import tfg.funkomania.funkomania_api.dtos.pedido_dtos.VistaHistorialPedidosUsuarioDTOId;

import java.util.List;

/**
 * Interfaz de servicio de la entidad Pedido.
 *
 * @author JuanAlbeticoHF
 * @version 0.3.0
 * @since 0.7.0
 */
public interface PedidoService {
    /**
     * Crea un nuevo pedido a partir del carrito de compras del usuario autenticado.
     * @param datosCrearPedido DTO que contiene la información necesaria para crear el pedido.
     * @return Un DTO que contiene la información del pedido recién creado, incluyendo su ID y detalles relevantes.
     */
    CrearPedidoResponseDTO crearPedidoDesdeCarrito(CrearPedidoRequestDTO datosCrearPedido);

    /**
     * Obtiene todos los pedidos realizados por el usuario autenticado.
     * @return Lista de DTOs que representan el historial de pedidos del usuario.
     */
    List<VistaHistorialPedidosUsuarioDTOId> obtenerPedidosUsuario();

    /**
     * Obtiene los detalles de un pedido específico realizado por el usuario autenticado, utilizando el ID del pedido para identificar cuál pedido se desea obtener.
     * @param idPedido El ID del pedido que se desea obtener.
     * @return Un DTO que contiene los detalles completos del pedido, incluyendo los totales y las líneas de pedido.
     */
    PedidoCompletoDTOId obtenerPedidoUsuarioPorId(Long idPedido);
}
