package tfg.funkomania.funkomania_api.services;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>Servicio de lista negra de tokens en memoria.</p>
 *
 * <p>Almacena en una estructura de datos SET los tokens JWT que han sido invalidados (por ejemplo, al cerrar sesión)
 * para evitar su uso posterior.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.1.0
 */
@Service
public class InMemoryTokenBlackListService {
    /** Set que almacena los tokens JWT que han sido invalidados. */
    private final Set<String> tokenBlackList = new HashSet<>();

    /**
     * Agrega un token JWT a la lista negra, marcándolo como inválido para futuras solicitudes.
     * @param token El token JWT que se desea invalidar y agregar a la lista negra.
     */
    public void addToken(String token) {
        tokenBlackList.add(token);
    }

    /**
     * Verifica si un token JWT es válido, es decir, que no se encuentra en la lista negra.
     * @param token El token JWT que se desea verificar.
     * @return true si el token es válido (no está en la lista negra), false si el token es inválido (está en la lista negra).
     */
    public boolean isTokenValid(String token) {
        return !tokenBlackList.contains(token);
    }
}
