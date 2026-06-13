package tfg.funkomania.funkomania_api.dtos.pedido_dtos;

/**
 * DTO de respuesta para la creación de un pedido.
 * @param idPedido El ID del pedido creado.
 * @param codigoPedido El código único del pedido creado.
 * @param mensaje Un mensaje informativo sobre el resultado de la creación del pedido.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
public record CrearPedidoResponseDTO (
        Long idPedido,
        String codigoPedido,
        String mensaje) {}
