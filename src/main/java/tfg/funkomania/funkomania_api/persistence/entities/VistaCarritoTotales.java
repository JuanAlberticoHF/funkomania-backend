package tfg.funkomania.funkomania_api.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;

/**
 * <p>Entidad que representa una vista de totales del carrito de compras en Funkomania.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.1
 * @since 0.7.0
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Immutable
@Table(name = "VCarrito_Totales")
public class VistaCarritoTotales {
    @NotNull(message = "El ID del carrito no puede ser nulo.")
    @Positive(message = "El ID del carrito debe ser un número positivo.")
    @Id
    @Column(name = "idCarrito", nullable = false)
    private Long idCarrito;

    @NotNull(message = "El ID del usuario no puede ser nulo.")
    @Positive(message = "El ID del usuario debe ser un número positivo.")
    @Column(name = "idUsuario", nullable = false)
    private Long idUsuario;

    @NotNull(message = "El total de artículos diferentes en el carrito no puede ser nulo.")
    @Positive(message = "El total de artículos diferentes en el carrito debe ser un número positivo.")
    @Column(name = "Total_Articulos_Diferentes", nullable = false)
    private Integer totalArticulosDiferentes;

    @NotNull(message = "El total de unidades físicas en el carrito no puede ser nulo.")
    @Positive(message = "El total de unidades físicas en el carrito debe ser un número positivo.")
    @Column(name = "Total_Unidades_Fisicas", nullable = false)
    private Integer totalUnidadesFisicas;

    @NotNull(message = "La base imponible no puede ser nulo.")
    @Digits(integer = 10, fraction = 2, message = "La base imponible debe ser un número con hasta 10 dígitos enteros y 2 decimales.")
    @PositiveOrZero(message = "La base imponible debe ser un número positivo o cero.")
    @Column(name = "Base_Imponible", nullable = false)
    private BigDecimal baseImponible;

    @NotNull(message = "El total a pagar no puede ser nulo.")
    @Digits(integer = 10, fraction = 2, message = "El total a pagar debe ser un número con hasta 10 dígitos enteros y 2 decimales.")
    @PositiveOrZero(message = "El total a pagar debe ser un número positivo o cero.")
    @Column(name = "Total_A_Pagar", nullable = false)
    private BigDecimal totalAPagar;
}
