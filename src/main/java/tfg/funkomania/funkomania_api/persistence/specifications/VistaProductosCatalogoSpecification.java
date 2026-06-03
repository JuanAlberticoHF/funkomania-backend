package tfg.funkomania.funkomania_api.persistence.specifications;

import org.springframework.data.jpa.domain.Specification;
import tfg.funkomania.funkomania_api.persistence.entities.VistaProductosCatalogo;


/**
 * Clase de especificación para construir consultas dinámicas sobre la entidad Producto.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.2.0
 */
public class VistaProductosCatalogoSpecification {
    /**
     * Crea una especificación para filtrar productos por nombre o descripción
     * @param search El texto a buscar en el nombre del producto
     * @return Una especificación que filtra productos por nombre o descripción
     */
    public static Specification<VistaProductosCatalogo> busquedaContiene(String search) {
        return (root, query, criteriaBuilder) -> {
            if (search == null || search.isEmpty()) return null; // Si no hay filtro, lo ignora
            // SQL: WHERE LOWER(nombre) LIKE '%texto%' OR LOWER(descripción) LIKE '%texto%'
            return criteriaBuilder.or(
                    criteriaBuilder.like(root.get("nombre"), "%" + search.toLowerCase() + "%"),
                    criteriaBuilder.like(root.get("descripcion"), "%" + search.toLowerCase() + "%")
            );
        };
    }

    /**
     * Crea una especificación para filtrar productos por categoría
     * @param idCategoria El id de la categoria a filtrar
     * @return Una especificación que filtra productos por categoria
     */
    public static Specification<VistaProductosCatalogo> idCategoriaIgual(Long idCategoria) {
        return (root, query, criteriaBuilder) -> {
            if (idCategoria == null) return null;
            // SQL: WHERE categoria_id = X
            return criteriaBuilder.equal(root.get("idCategoria"), idCategoria);
        };
    }

    /**
     * Crea una especificación para filtrar productos por precio mínimo
     * @param precioMin El precio mínimo a filtrar
     * @return Una especificación que filtra productos por precio mínimo
     */
    public static Specification<VistaProductosCatalogo> precioMayorOIgualQue(Double precioMin) {
        return (root, query, criteriaBuilder) -> {
            if (precioMin == null) return null;
            // SQL: WHERE precioFinal_ConIVA >= X
            return criteriaBuilder.greaterThanOrEqualTo(root.get("precioFinalConIVA"), precioMin);
        };
    }

    /**
     * Crea una especificación para filtrar productos por precio mínimo
     * @param precioMax El precio mínimo a filtrar
     * @return Una especificación que filtra productos por precio mínimo
     */
    public static Specification<VistaProductosCatalogo> precioMenorOIgualQue(Double precioMax) {
        return (root, query, criteriaBuilder) -> {
            if (precioMax == null) return null;
            // SQL: WHERE precioFinal_ConIVA <= X
            return criteriaBuilder.lessThanOrEqualTo(root.get("precioFinalConIVA"), precioMax);
        };
    }

    /**
     * Crea una especificación para filtrar productos por su oferta
     * @param oferta La condición de la oferta
     * @return Una especificación que filtra productos por su oferta
     */
    public static Specification<VistaProductosCatalogo> estaEnOferta(Boolean oferta) {
        return (root, query, criteriaBuilder) -> {
            if (oferta == null) return null;
            // SQL: WHERE oferta = X
            return criteriaBuilder.equal(root.get("enOferta"), oferta);
        };
    }
}
