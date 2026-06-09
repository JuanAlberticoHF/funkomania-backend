package tfg.funkomania.funkomania_api.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/**
 * <p>Clase de utilidad para operaciones relacionadas con productos en el sistema de Funkomania.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.6.0
 */
public class ProductoUtils {
    /**
     * Calcula el precio final de un producto aplicando un descuento y redondeando a dos decimales.
     * @param precio Original El precio original del producto.
     * @param iva El porcentaje de IVA a aplicar.
     * @return El precio final del producto con IVA incluido, redondeado a dos decimales.
     */
    public static BigDecimal calcularPrecioConIva(BigDecimal precio, BigDecimal iva) {
        if (precio == null) return BigDecimal.ZERO;
        if (iva == null) return precio;

        // precio * (1 + iva / 100)
        BigDecimal porcentajeIva = iva.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        BigDecimal factor = BigDecimal.ONE.add(porcentajeIva);

        return precio.multiply(factor);
    }

    /**
     * Calcula el precio final de un producto aplicando un descuento y redondeando a dos decimales.
     * @param precio Original El precio original del producto.
     * @param enOferta Indica si el producto está en oferta.
     * @param descuento El porcentaje de descuento a aplicar.
     * @param fechaFinOferta La fecha de finalización de la oferta.
     * @return El precio final del producto con el descuento aplicado, redondeado a dos
     */
    public static BigDecimal calcularPrecioConDescuento(
            BigDecimal precio,
            boolean enOferta,
            BigDecimal descuento,
            LocalDateTime fechaFinOferta
    ) {
        if (precio == null) return BigDecimal.ZERO;

        // Validación de condiciones: enOferta == true, descuento > 0 y (fechaFin es nula o no ha expirado)
        boolean ofertaActiva = enOferta
                && descuento != null
                && descuento.compareTo(BigDecimal.ZERO) > 0
                && (fechaFinOferta == null || !fechaFinOferta.isBefore(LocalDateTime.now()));

        if (ofertaActiva) {
            // precio - (precio * descuento / 100)
            BigDecimal porcentajeDescuento = descuento.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
            BigDecimal ahorro = precio.multiply(porcentajeDescuento);
            BigDecimal precioFinal = precio.subtract(ahorro);

            // Redondear a 2 decimales como hace el ROUND(..., 2) de tu función MySQL
            return precioFinal.setScale(2, RoundingMode.HALF_UP);
        }

        return precio.setScale(2, RoundingMode.HALF_UP);
    }
}
