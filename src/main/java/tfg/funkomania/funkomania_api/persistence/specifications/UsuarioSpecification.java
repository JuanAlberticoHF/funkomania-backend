package tfg.funkomania.funkomania_api.persistence.specifications;

import org.springframework.data.jpa.domain.Specification;
import tfg.funkomania.funkomania_api.persistence.entities.Usuario;

/**
 * Clase de especificación para construir consultas dinámicas sobre la entidad Usuario.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.6.0
 */
public class UsuarioSpecification {
    /**
     * Crea una especificación para filtrar usuarios por email
     * @param search El texto a buscar en el email del usuario
     * @return Una especificación que filtra usuarios por email
     */
    public static Specification<Usuario> busquedaContiene(String search) {
        return (root, query, criteriaBuilder) -> {
            if (search == null || search.isEmpty()) return null; // Si no hay filtro, lo ignora
            // SQL: WHERE LOWER(email) LIKE '%texto%'
            return criteriaBuilder.like(root.get("email"), "%" + search.toLowerCase() + "%");
        };
    }
}
