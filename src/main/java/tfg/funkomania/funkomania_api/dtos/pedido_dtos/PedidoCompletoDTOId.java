package tfg.funkomania.funkomania_api.dtos.pedido_dtos;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tfg.funkomania.funkomania_api.persistence.entities.VistaPedidoTotales;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>DTO que representa la información completa de un pedido realizado por un usuario en el sistema de Funkomania.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class PedidoCompletoDTOId {
    @NotNull(message = "El ID del pedido no puede ser nulo.")
    @Positive(message = "El ID del pedido debe ser un número positivo.")
    private Long idPedido;

    @NotNull(message = "El ID del usuario no puede ser nulo.")
    @Positive(message = "El ID del usuario debe ser un número positivo.")
    private Long idUsuario;

    @NotNull(message = "La cantidad de artículos diferentes en el carrito no puede ser nulo.")
    @Positive(message = "La cantidad de artículos diferentes en el carrito debe ser un número positivo.")
    private Integer cantidadArticulosDiferentes;

    @NotNull(message = "El total de unidades físicas en el carrito no puede ser nulo.")
    @Positive(message = "El total de unidades físicas en el carrito debe ser un número positivo.")
    private Integer totalUnidadesFisicas;

    @NotNull(message = "La base imponible no puede ser nulo.")
    @Digits(integer = 42, fraction = 2, message = "La base imponible debe ser un número con hasta 42 dígitos enteros y 2 decimales.")
    @PositiveOrZero(message = "La base imponible debe ser un número positivo o cero.")
    private BigDecimal baseImponible;

    @NotNull(message = "El total con IVA no puede ser nulo.")
    @Digits(integer = 10, fraction = 2, message = "El total con IVA debe ser un número con hasta 10 dígitos enteros y 2 decimales.")
    @PositiveOrZero(message = "El total con IVA debe ser un número positivo o cero.")
    private BigDecimal totalConIVA;

    private List<VistaDetallePedidoDTOId> lineas;

    /**
     * Constructor que inicializa un PedidoCompletoDTO a partir de una instancia de VistaPedidoTotales y una lista de VistaDetallePedidoDTOId.
     *
     * @param pedidoTotales La vista que contiene los totales del pedido.
     * @param lineas La lista de detalles del pedido.
     */
    public PedidoCompletoDTOId(VistaPedidoTotales pedidoTotales, List<VistaDetallePedidoDTOId> lineas) {
        this.idPedido = pedidoTotales.getIdPedido();
        this.idUsuario = pedidoTotales.getIdUsuario();
        this.cantidadArticulosDiferentes = pedidoTotales.getCantidadArticulosDiferentes();
        this.totalUnidadesFisicas = pedidoTotales.getTotalUnidadesFisicas();
        this.baseImponible = pedidoTotales.getBaseImponible();
        this.totalConIVA = pedidoTotales.getTotalConIVA();
        this.lineas = lineas;
    }
}
