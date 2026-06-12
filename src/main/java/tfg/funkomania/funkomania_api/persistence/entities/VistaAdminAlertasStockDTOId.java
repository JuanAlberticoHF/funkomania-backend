package tfg.funkomania.funkomania_api.persistence.entities;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

/**
 * <p>DTO que representa una vista de alertas de stock para el administrador en Funkomania.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Immutable
public class VistaAdminAlertasStockDTOId {
    @NotNull(message = "El ID del producto no puede ser nulo.")
    @Positive(message = "El ID del producto debe ser un número positivo.")
    private Long idProducto;

    @NotNull(message = "El nombre del producto no puede ser nulo.")
    @Size(max = 150, message = "El nombre del producto no puede exceder los 150 caracteres.")
    private String nombre;

    @NotNull(message = "El stock del producto no puede ser nulo.")
    @Positive(message = "El stock del producto debe ser un número positivo.")
    private Integer stock;

    @NotNull(message = "El ID de la categoria no puede ser nulo.")
    @Positive(message = "El ID de la categoria debe ser un número positivo.")
    private Long idCategoria;

    @Size(max = 7, message = "La prioridad no puede exceder los 7 caracteres.")
    private String prioridad;

    /**
     * <p>Constructor que inicializa un objeto {@link VistaAdminAlertasStockDTOId} a partir de una instancia de {@link VistaAdminAlertasStock}.</p>
     * @param vistaAdminAlertasStock Objeto de tipo {@link VistaAdminAlertasStock} del cual se extraen los datos para inicializar el DTO.
     */
    public VistaAdminAlertasStockDTOId(VistaAdminAlertasStock vistaAdminAlertasStock) {
        this.idProducto = vistaAdminAlertasStock.getIdProducto();
        this.nombre = vistaAdminAlertasStock.getNombre();
        this.stock = vistaAdminAlertasStock.getStock();
        this.idCategoria = vistaAdminAlertasStock.getIdCategoria();
        this.prioridad = vistaAdminAlertasStock.getPrioridad();
    }
}
