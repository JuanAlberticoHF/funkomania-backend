package tfg.funkomania.funkomania_api.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;

/**
 * <p>Entidad que representa una vista de totales del pedido en Funkomania.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Immutable
@Table(name = "VPedido_Totales")
@IdClass(VistaPedidoTotalesId.class)
public class VistaPedidoTotales {
    @Id
    @NotNull(message = "El ID del pedido no puede ser nulo.")
    @Positive(message = "El ID del pedido debe ser un número positivo.")
    @Column(name = "idPedido", nullable = false)
    private Long idPedido;

    @Id
    @NotNull(message = "El ID del usuario no puede ser nulo.")
    @Positive(message = "El ID del usuario debe ser un número positivo.")
    @Column(name = "idUsuario", nullable = false)
    private Long idUsuario;

    @NotNull(message = "La cantidad de artículos diferentes en el carrito no puede ser nulo.")
    @Positive(message = "La cantidad de artículos diferentes en el carrito debe ser un número positivo.")
    @Column(name = "Cantidad_Articulos_Diferentes", nullable = false)
    private Integer cantidadArticulosDiferentes;

    @NotNull(message = "El total de unidades físicas en el carrito no puede ser nulo.")
    @Positive(message = "El total de unidades físicas en el carrito debe ser un número positivo.")
    @Column(name = "Total_Unidades_Fisicas", nullable = false)
    private Integer totalUnidadesFisicas;

    @NotNull(message = "La base imponible no puede ser nulo.")
    @Digits(integer = 42, fraction = 2, message = "La base imponible debe ser un número con hasta 42 dígitos enteros y 2 decimales.")
    @PositiveOrZero(message = "La base imponible debe ser un número positivo o cero.")
    @Column(name = "Base_Imponible", nullable = false)
    private BigDecimal baseImponible;

    @NotNull(message = "El total con IVA no puede ser nulo.")
    @Digits(integer = 10, fraction = 2, message = "El total con IVA debe ser un número con hasta 10 dígitos enteros y 2 decimales.")
    @PositiveOrZero(message = "El total con IVA debe ser un número positivo o cero.")
    @Column(name = "Total_Con_IVA", nullable = false)
    private BigDecimal totalConIVA;
}
