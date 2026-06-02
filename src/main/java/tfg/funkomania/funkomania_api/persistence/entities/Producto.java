package tfg.funkomania.funkomania_api.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>Entidad que representa un producto en el sistema de Funkomania.</p>
 * <p>La entidad mapea tabla {@code producto} de la base de datos</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.1
 * @since 0.2.0
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Table(name = "Producto")
public class Producto {
    /**
     * Identificador único del producto.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProducto", nullable = false)
    private Long id;

    /**
     * Nombre del producto.
     */
    @NotNull(message = "El nombre del producto no puede ser nulo.")
    @Max(value = 150, message = "El nombre del producto no puede exceder los 150 caracteres.")
    @Column(name = "Nombre", nullable = false)
    private String nombre;

    /**
     * Precio del producto.
     */
    @NotNull(message = "El precio del producto no puede ser nulo.")
    @Digits(integer = 10, fraction = 2, message = "El precio debe ser un número con hasta 10 dígitos enteros y 2 decimales.")
    @PositiveOrZero(message = "El precio del producto debe ser un número positivo o cero.")
    @Column (name = "Precio", nullable = false)
    private BigDecimal precio;

    /**
     * Stock disponible del producto.
     */
    @PositiveOrZero(message = "El stock del producto debe ser un número positivo o cero.")
    @Column(name = "Stock", nullable = false)
    private Integer stock;

    /**
     * URL de la imagen del producto.
     */
    @Max(value = 255, message = "La URL de la imagen no puede exceder los 255 caracteres.")
    @Column(name = "Image")
    private String imagen;

    /**
     * Descripción detallada del producto.
     */
    @Lob
    @Column(name = "Descripcion", columnDefinition = "TEXT")
    private String descripcion;

    /**
     * Porcentaje IVA del producto
     */
    @NotNull
    @Digits(integer = 5, fraction = 2, message = "El iva debe ser un número con hasta 5 dígitos enteros y 2 decimales.")
    @DecimalMax(value = "100.00", message = "El iva del producto no puede exceder el 100%.")
    @PositiveOrZero(message = "El iva del producto debe ser un número positivo o cero.")
    @Column(name = "iva", nullable = false, precision = 7, scale = 2)
    private BigDecimal iva;

    /**
     * Producto activado o inactivo
     */
    @NotNull
    @Column(name = "Activo", nullable = false)
    private boolean activo;

    /**
     * Producto en oferta o sin oferta
     */
    @NotNull
    @Column(name = "EnOferta", nullable = false)
    private boolean enOferta;

    /**
     * Descuento aplicado al producto si está en oferta.
     */
    @NotNull
    @Digits(integer = 5, fraction = 2, message = "El descuento debe ser un número con hasta 5 dígitos enteros y 2 decimales.")
    @DecimalMax(value = "90.00", message = "El descuento del producto no puede exceder el 90%.")
    @PositiveOrZero(message = "El descuento del producto debe ser un número positivo o cero.")
    @Column(name = "Descuento", nullable = false, precision = 7, scale = 2)
    private BigDecimal descuento;

    /**
     * Fecha de creación del producto.
     */
    @Column(name = "FechaFinOferta")
    private LocalDateTime fechaFinOferta;

    /**
     * Categoría a la que pertenece el producto.
     */
    @NotNull(message = "La categoría del producto no puede ser nula.")
    @ManyToOne
    @JoinColumn(name="idCategoria", nullable = false, unique = true)
    private Categoria categoria;
}
