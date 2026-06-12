package tfg.funkomania.funkomania_api.services;

import tfg.funkomania.funkomania_api.dtos.pedido_dtos.*;
import tfg.funkomania.funkomania_api.persistence.enums.EstadoPagoEnum;
import tfg.funkomania.funkomania_api.persistence.enums.EstadoPedidoEnum;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interfaz de servicio para administrador de la entidad Pedido.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
public interface PedidoAdminService {
    /**
     * Obtiene todos los pedidos de administrador que coinciden con los criterios de búsqueda proporcionados.
     *
     * @param idPedido     El ID del pedido que se desea buscar (opcional).
     * @param codigoPedido El código del pedido que se desea buscar (opcional).
     * @param usuario      El nombre o email del usuario que realizó el pedido (opcional).
     * @param fechaPedido  La fecha del pedido que se desea buscar (opcional).
     * @param estadoPedido El estado del pedido que se desea buscar (opcional).
     * @param estadoPago   El estado del pago que se desea buscar (opcional).
     * @param metodoPago   El metodo de pago del pedido que se desea buscar (opcional).
     * @return Una lista de DTOs que representan los pedidos de administrador que coinciden con los criterios de búsqueda proporcionados.
     */
    List<VistaPedidosAdminDTOId> getAllPedidosAdmin(
            Long idPedido, String codigoPedido, String usuario, LocalDateTime fechaPedido, EstadoPedidoEnum estadoPedido,
            EstadoPagoEnum estadoPago, String metodoPago);

    /**
     * Obtiene los detalles de un pedido específico, utilizando el ID del pedido para identificar cuál pedido se desea obtener.
     * @param idPedido El ID del pedido que se desea obtener.
     * @return Un DTO que contiene los detalles completos del pedido, incluyendo los totales y las líneas de pedido.
     */
    PedidoCompletoDTOId obtenerPedidoUsuarioPorId(Long idPedido);


    /**
     * Actualiza el estado de un pedido específico, utilizando el ID del pedido para identificar cuál pedido se desea actualizar.
     * @param datosCrearPedido DTO que contiene la información necesaria para actualizar el pedido, incluyendo el nuevo estado del pedido y del pago, así como cualquier comentario adicional.
     */
    void crearPedidoParaUsuario(CrearPedidoAdminRequestDTO datosCrearPedido);

    /**
     * Agrega una nueva línea de pedido a un pedido existente.
     * @param idPedido                El ID del pedido al que se desea agregar la línea de pedido.
     * @param datosAgregarLineaPedido DTO que contiene la información necesaria para agregar una nueva línea de pedido.
     * @return Un DTO que contiene la información de la línea de pedido recién agregada, incluyendo su ID y detalles relevantes.
     */
    PedidoCompletoDTOId agregarUnNuevoProductoAlPedido(Long idPedido, AdminAgregarLineaPedidoRequestDTO datosAgregarLineaPedido);

    /**
     * Actualiza los datos de un pedido específico.
     * @param idPedido              El ID del pedido que se desea actualizar.
     * @param datosActualizarPedido DTO que contiene la información necesaria para actualizar el pedido.
     * @return Un DTO que contiene los detalles completos del pedido actualizado, incluyendo los totales y las líneas de pedido.
     */
    PedidoCompletoDTOId actualizarDatosPedido(Long idPedido, AdminUpdatePedidoRequestDTO datosActualizarPedido);

    /**
     * Actualiza una línea de pedido específica de un pedido existente.
     *
     * @param idPedido                     El ID del pedido del cual se desea actualizar la línea de pedido.
     * @param idProducto                   El ID del producto que se desea actualizar en el pedido.
     * @param datosActualizarDetallePedido DTO que contiene la información necesaria para actualizar el detalle del pedido.
     * @return Un DTO que contiene los detalles completos del pedido actualizado, incluyendo los totales y las líneas de pedido.
     */
    PedidoCompletoDTOId actualizarDatosDetallePedido(Long idPedido, Long idProducto, AdminUpdateProductoPedidoRequestDTO datosActualizarDetallePedido);

    /**
     * Elimina una línea de pedido específica de un pedido existente.
     * @param idPedido   El ID del pedido del cual se desea eliminar la línea de pedido
     * @param idProducto El ID del producto que se desea eliminar del pedido.
     */
    void eliminarDetallePedido(Long idPedido, Long idProducto);
}
