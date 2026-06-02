package tfg.funkomania.funkomania_api.dtos.producto_dtos;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import tfg.funkomania.funkomania_api.persistence.entities.VistaProductosCatalogo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>DTO que representa una vista de productos en el catálogo de Funkomania con ID.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.2.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class VistaProductosCatalogoDTOId {
    /**
     * Identificador único del producto.
     */
    private Long id;

    /**
     * Nombre del producto.
     */
    @NotNull(message = "El nombre del producto no puede ser nulo.")
    @Max(value = 150, message = "El nombre del producto no puede exceder los 150 caracteres.")
    private String nombre;

    /**
     * Precio original sin iva del producto.
     */
    @NotNull(message = "El precio original sin iva del producto no puede ser nulo.")
    @Digits(integer = 10, fraction = 2, message = "El precio original sin iva debe ser un número con hasta 10 dígitos enteros y 2 decimales.")
    @PositiveOrZero(message = "El precio original sin iva del producto debe ser un número positivo o cero.")
    private BigDecimal precioOriginalSinIVA;

    /**
     * Precio original sin iva del producto.
     */
    @NotNull(message = "El precio original con iva del producto no puede ser nulo.")
    @Digits(integer = 13, fraction = 2, message = "El precio original con iva debe ser un número con hasta 13 dígitos enteros y 2 decimales.")
    @PositiveOrZero(message = "El precio original con iva del producto debe ser un número positivo o cero.")
    private BigDecimal precioOriginalConIVA;

    /**
     * Producto en oferta o sin oferta
     */
    @NotNull
    private boolean enOferta;

    /**
     * Descuento aplicado al producto si está en oferta.
     */
    @NotNull
    @Digits(integer = 5, fraction = 2, message = "El descuento debe ser un número con hasta 5 dígitos enteros y 2 decimales.")
    @DecimalMax(value = "90.00", message = "El descuento del producto no puede exceder el 90%.")
    @PositiveOrZero(message = "El descuento del producto debe ser un número positivo o cero.")
    private BigDecimal descuento;

    /**
     * Fecha de creación del producto.
     */
    private LocalDateTime fechaFinOferta;

    /**
     * Precio final sin iva del producto.
     */
    @NotNull(message = "El precio final sin iva del producto no puede ser nulo.")
    @Digits(integer = 10, fraction = 2, message = "El precio final sin iva debe ser un número con hasta 10 dígitos enteros y 2 decimales.")
    @PositiveOrZero(message = "El precio final sin iva del producto debe ser un número positivo o cero.")
    private BigDecimal precioFinalSinIVA;

    /**
     * Precio final sin iva del producto.
     */
    @NotNull(message = "El precio final con iva del producto no puede ser nulo.")
    @Digits(integer = 13, fraction = 2, message = "El precio final con iva debe ser un número con hasta 13 dígitos enteros y 2 decimales.")
    @PositiveOrZero(message = "El precio final con iva del producto debe ser un número positivo o cero.")
    private BigDecimal precioFinalConIVA;

    /**
     * Porcentaje IVA del producto
     */
    @NotNull
    @Digits(integer = 5, fraction = 2, message = "El iva debe ser un número con hasta 5 dígitos enteros y 2 decimales.")
    @DecimalMax(value = "100.00", message = "El iva del producto no puede exceder el 100%.")
    @PositiveOrZero(message = "El iva del producto debe ser un número positivo o cero.")
    private BigDecimal iva;

    /**
     * Stock disponible del producto.
     */
    @PositiveOrZero(message = "El stock del producto debe ser un número positivo o cero.")
    private Integer stock;

    /**
     * URL de la imagen del producto.
     */
    @Max(value = 255, message = "La URL de la imagen no puede exceder los 255 caracteres.")
    private String imagen;

    /**
     * Descripción detallada del producto.
     */
    @Lob
    private String descripcion;

    /**
     * Producto activado o inactivo
     */
    @NotNull
    private boolean activo;

    /**
     * Identificador de la categoria del producto.
     */
    @NotNull(message = "El id de la categoria del producto no puede ser nulo.")
    private Long idCategoria;

    /**
     * Nombre de la categoría a la que pertenece el producto.
     */
    @NotNull(message = "El nombre de la categoria del producto no puede ser nula.")
    private String nombreCategoria;

    /**
     * Nombre de la categoría padre a la que pertenece el producto.
     */
    @NotNull(message = "El nombre de la categoria padre del producto no puede ser nula.")
    private String nombreCategoriaPadre;

    public VistaProductosCatalogoDTOId (VistaProductosCatalogo vistaProductosCatalogo) {
        this.id = vistaProductosCatalogo.getId();
        this.nombre = vistaProductosCatalogo.getNombre();
        this.precioOriginalSinIVA = vistaProductosCatalogo.getPrecioOriginalSinIVA();
        this.precioOriginalConIVA = vistaProductosCatalogo.getPrecioOriginalConIVA();
        this.enOferta = vistaProductosCatalogo.isEnOferta();
        this.descuento = vistaProductosCatalogo.getDescuento();
        this.fechaFinOferta = vistaProductosCatalogo.getFechaFinOferta();
        this.precioFinalSinIVA = vistaProductosCatalogo.getPrecioFinalSinIVA();
        this.precioFinalConIVA = vistaProductosCatalogo.getPrecioFinalConIVA();
        this.iva = vistaProductosCatalogo.getIva();
        this.stock = vistaProductosCatalogo.getStock();
        this.imagen = vistaProductosCatalogo.getImagen();
        this.descripcion = vistaProductosCatalogo.getDescripcion();
        this.activo = vistaProductosCatalogo.isActivo();
        this.idCategoria = vistaProductosCatalogo.getIdCategoria();
        this.nombreCategoria = vistaProductosCatalogo.getNombreCategoria();
        this.nombreCategoriaPadre = vistaProductosCatalogo.getNombreCategoriaPadre();
    }
}

