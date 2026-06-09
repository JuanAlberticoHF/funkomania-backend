package tfg.funkomania.funkomania_api.dtos.categoria_dtos;

import lombok.*;
import tfg.funkomania.funkomania_api.dtos.producto_dtos.VistaProductosCatalogoDTOId;
import tfg.funkomania.funkomania_api.persistence.entities.Categoria;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>DTO que representa un categoria con su id y los productos asociados en el sistema de Funkomania.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.6.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CategoriaDTOIdProductosAsociados {
    private Long id;
    private String nombre;
    private CategoriaDTOId CategoriaPadre;
    private Set<VistaProductosCatalogoDTOId> productosAsociados;

    public CategoriaDTOIdProductosAsociados(Categoria categoria) {
        this.id = categoria.getId();
        this.nombre = categoria.getNombre();
        this.CategoriaPadre = categoria.getCategoriaPadre() != null ? new CategoriaDTOId(categoria.getCategoriaPadre()) : null;
        this.productosAsociados = new HashSet<>(
                categoria.getProductosAsociados().stream().map(VistaProductosCatalogoDTOId::new).toList());
    }
}
