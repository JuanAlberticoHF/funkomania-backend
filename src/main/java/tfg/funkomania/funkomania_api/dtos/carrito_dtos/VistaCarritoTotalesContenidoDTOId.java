package tfg.funkomania.funkomania_api.dtos.carrito_dtos;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import tfg.funkomania.funkomania_api.persistence.entities.VistaCarritoContenido;
import tfg.funkomania.funkomania_api.persistence.entities.VistaCarritoTotales;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>DTO que representa los totales del carrito de un usuario y un listado de items productos del carrito.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class VistaCarritoTotalesContenidoDTOId {
    @NotNull(message = "El ID del carrito no puede ser nulo.")
    @Positive(message = "El ID del carrito debe ser un número positivo.")
    private Long idCarrito;

    @NotNull(message = "El ID del usuario no puede ser nulo.")
    @Positive(message = "El ID del usuario debe ser un número positivo.")
    private Long idUsuario;

    @NotNull(message = "El total de artículos diferentes en el carrito no puede ser nulo.")
    @Positive(message = "El total de artículos diferentes en el carrito debe ser un número positivo.")
    private Integer totalArticulosDiferentes;

    @NotNull(message = "El total de unidades físicas en el carrito no puede ser nulo.")
    @Positive(message = "El total de unidades físicas en el carrito debe ser un número positivo.")
    private Integer totalUnidadesFisicas;

    @NotNull(message = "La base imponible no puede ser nulo.")
    @Digits(integer = 10, fraction = 2, message = "La base imponible debe ser un número con hasta 10 dígitos enteros y 2 decimales.")
    @PositiveOrZero(message = "La base imponible debe ser un número positivo o cero.")
    private BigDecimal baseImponible;

    @NotNull(message = "El total a pagar no puede ser nulo.")
    @Digits(integer = 10, fraction = 2, message = "El total a pagar debe ser un número con hasta 10 dígitos enteros y 2 decimales.")
    @PositiveOrZero(message = "El total a pagar debe ser un número positivo o cero.")
    private BigDecimal totalAPagar;

    /**
     * DTO que representa el listado de productos en el carrito de compra utilizando la vista {@link VistaCarritoContenido}
     */
    @NotNull(message = "La lista de productos en el carrito no puede ser nula.")
    List<VistaCarritoContenidoDTOId> items;

    public VistaCarritoTotalesContenidoDTOId(List<VistaCarritoContenidoDTOId> items, VistaCarritoTotales totales) {
        this.idCarrito = totales.getIdCarrito();
        this.idUsuario = totales.getIdUsuario();
        this.totalArticulosDiferentes = totales.getTotalArticulosDiferentes();
        this.totalUnidadesFisicas = totales.getTotalUnidadesFisicas();
        this.baseImponible = totales.getBaseImponible();
        this.totalAPagar = totales.getTotalAPagar();
        this.items = items;
    }
}
