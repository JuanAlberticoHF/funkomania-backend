package tfg.funkomania.funkomania_api.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import tfg.funkomania.funkomania_api.dtos.producto_dtos.ProductoDTOIdCategoria;
import tfg.funkomania.funkomania_api.utils.ProductoUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/**
 * <p>Entidad que representa un producto en el sistema de Funkomania.</p>
 * <p>La entidad mapea tabla {@code producto} de la base de datos</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.2.1
 * @since 0.2.0
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
    @Size(max = 150, message = "El nombre del producto no puede exceder los 150 caracteres.")
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
    @Size(max = 255, message = "La URL de la imagen no puede exceder los 255 caracteres.")
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="idCategoria", nullable = false)
    @JsonIgnoreProperties("productosAsociados") // Evita la serialización recursiva de los productos dentro de la categoría
    @EqualsAndHashCode.Exclude
    private Categoria categoria;

    /**
     * Constructor que crea un producto a partir de un DTO de producto con ID de categoría.
     * @param productoDTOIdCategoria DTO que contiene los datos del producto a crear, incluyendo el ID de la categoría a la que pertenece.
     */
    public Producto(ProductoDTOIdCategoria productoDTOIdCategoria) {
        this.id = null;
        this.nombre = productoDTOIdCategoria.getNombre();
        this.precio = productoDTOIdCategoria.getPrecio();
        this.stock = productoDTOIdCategoria.getStock();
        this.imagen = productoDTOIdCategoria.getImagen();
        this.descripcion = productoDTOIdCategoria.getDescripcion();
        this.iva = productoDTOIdCategoria.getIva();
        this.activo = productoDTOIdCategoria.isActivo();
        this.enOferta = productoDTOIdCategoria.isEnOferta();
        this.descuento = productoDTOIdCategoria.getDescuento();
        this.fechaFinOferta = productoDTOIdCategoria.getFechaFinOferta();
    }

    /**
     * Devuelve el precio final sin IVA del producto, aplicando el descuento si el producto está en oferta.
     * @return Precio final sin IVA del producto.
     */
    public BigDecimal getPrecioOriginalConIVA() {
        BigDecimal precioConIva = ProductoUtils.calcularPrecioConIva(this.precio, this.iva);
        return precioConIva.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Devuelve el precio final sin IVA del producto, aplicando el descuento si el producto está en oferta.
     * @return Precio final sin IVA del producto.
     */
    public BigDecimal getPrecioFinalSinIVA() {
        return ProductoUtils.calcularPrecioConDescuento(
                this.precio,
                this.enOferta,
                this.descuento,
                this.fechaFinOferta
        );
    }

    /**
     * Devuelve el precio final sin IVA del producto, aplicando el descuento si el producto está en oferta.
     * @return Precio final sin IVA del producto.
     */
    public BigDecimal getPrecioFinalConIVA() {
        // En SQL: fn_precio_con_iva(fn_precio_con_descuento(...))
        BigDecimal precioFinalSinIva = getPrecioFinalSinIVA();
        BigDecimal precioFinalConIva = ProductoUtils.calcularPrecioConIva(precioFinalSinIva, this.iva);
        return precioFinalConIva.setScale(2, RoundingMode.HALF_UP);
    }

}
