package tfg.funkomania.funkomania_api.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;


/**
 * Utilidad para la generación y validación de tokens JWT.
 *
 * <p>Proporciona métodos para crear tokens con información de usuario y validar su autenticidad
 * y vigencia.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.1.0
 */
@Component
public class JwtUtils {

    /** Clave secreta para firmar los tokens. */
    @Value("${jwt.secret}")
    private String secretKey;
    /** Tiempo de expiración del token. */
    @Value("${jwt.expiration}")
    private String timeExpiration;

    /**
     * Genera un token JWT para el usuario autenticado.
     * @param email El correo electrónico del usuario autenticado para el cual se desea generar el token JWT. Este
     *              correo se establece como el sujeto (subject) del token, lo que permite identificar al usuario
     *              al que pertenece el token.
     * @return Devuelve el token JWT generado como una cadena de texto.
     */
    public String generateAccessToken(String email){
        // Jwts.builder() -> Permite construir un token JWT utilizando la biblioteca JJWT.
        return Jwts.builder()
                // Establece el sujeto del token, siendo el email del usuario autenticado al que pertenece el token
                .subject(email)
                // Establece la fecha de emisión del token. (fecha actual)
                .issuedAt(new Date(System.currentTimeMillis()))
                // Establece la fecha de expiración del token. (fecha actual + tiempo de expiración)
                .expiration(new Date(System.currentTimeMillis() + Long.parseLong(timeExpiration)))
                // Establece la firma del token
                .signWith(getSignatureKey())
                // Construye el token y lo devuelve como una cadena.
                .compact();
    }

    /**
     * Obtiene la clave de firma para un token JWT.
     * @return Devuelve la clave de firma secreta encriptada utilizando el algoritmo HMAC-SHA, de la biblioteca JJWT.
     */
    public Key getSignatureKey(){
        // Decodifica la clave secreta que se encuentra en formato Base64 a un arreglo de bytes.
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        // Crea una instancia de Key utilizando la clave decodificada y el algoritmo HMAC-SHA.
        return Keys.hmacShaKeyFor(decodedKey);
    }

    /**
     * Válida un token JWT verificando su firma y su fecha de expiración.
     * @param token El token JWT que se desea validar.
     * @return Devuelve {@code true} si el token es válido (es decir, si la firma es correcta y el token no ha expirado),
     * o {@code false} si el token es inválido o ha expirado.
     */
    public boolean isTokenValid(String token){
        try {
            Jwts.parser()
                    // Establece la clave de firma para verificar el token.
                    .setSigningKey(getSignatureKey())
                    // Construye el parser de JWT con la clave de firma establecida.
                    .build()
                    // Analiza el token JWT y verifica su firma utilizando la clave de firma establecida. Si el token es válido, no se lanza ninguna excepción.
                    .parseClaimsJws(token)
                    // Si el token es válido, se devuelve el cuerpo del token (claims) que contiene la información del token.
                    .getBody();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Obtener todos los claims (información) de un token JWT.
     * <li>Un claim es un dato específico que se incluye en el token JWT.</li>
     * @param token El token JWT del cual se desea extraer los claims.
     * @return Devuelve el cuerpo del token (claims) que contiene la información del token
     */
    public Claims extractAllClaimsFromToken(String token){
        return Jwts.parser()
                // Establece la clave de firma para verificar el token.
                .setSigningKey(getSignatureKey())
                // Construye el parser de JWT con la clave de firma establecida.
                .build()
                // Analiza el token JWT y verifica su firma utilizando la clave de firma establecida.
                .parseClaimsJws(token)
                // Si el token es válido, se devuelve el cuerpo del token (claims) que contiene la información del token.
                .getBody();
    }

    /**
     * <h1>Obtener un claim de un token base a una función de mapeo</h1>
     * Metodo para obtener un claim específico del token JWT utilizando una función de mapeo (claimsTFunction) que se
     * aplica al cuerpo del token (claims) para extraer el claim deseado.
     * @param token El token JWT del cual se desea extraer el claim específico.
     * @param claimsTFunction Una función de mapeo que toma el cuerpo del token (claims) como entrada y devuelve el
     *                        claim específico que se desea extraer. Esta función se aplica al cuerpo del token para
     *                        obtener el claim deseado.
     * @return Devuelve el claim específico extraído del token JWT utilizando la función de mapeo proporcionada.
     */

    /**
     * Obtener un claim específico de un token JWT utilizando una función de mapeo.
     * @param token El token JWT del cual se desea extraer el claim específico.
     * @param claimsTFunction Una función de mapeo que toma el cuerpo del token (claims) como entrada y devuelve el
     *                        claim específico que se desea extraer. Esta función se aplica al cuerpo del token para
     *                        obtener el claim deseado.
     * @return Devuelve el claim específico extraído del token JWT utilizando la función de mapeo proporcionada.
     * @param <T> El tipo de dato del claim específico que se desea extraer del token JWT. Este tipo se determina por
     *           la función de mapeo proporcionada.
     */
    public <T> T getClaim(String token, @NonNull Function<Claims, T> claimsTFunction) {
        Claims claims = extractAllClaimsFromToken(token);
        return claimsTFunction.apply(claims);
    }

    /**
     * Obtener el nombre de usuario (subject) de un token JWT.
     * @param token El token JWT del cual se desea obtener el nombre de usuario (subject).
     * @return Devuelve el nombre de usuario (subject) extraído del token JWT utilizando el metodo getClaim.
     */
    public String getUsernameFromToken(String token){
        return getClaim(token, Claims::getSubject);
    }
}

