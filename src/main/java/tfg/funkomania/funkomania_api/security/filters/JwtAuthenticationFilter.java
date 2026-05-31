package tfg.funkomania.funkomania_api.security.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tfg.funkomania.funkomania_api.dtos.security_dtos.LoginRequest;
import tfg.funkomania.funkomania_api.utils.JwtUtils;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Filtro de autenticación JWT.</p>
 *
 * <p>Extiende del filtro de autenticación de Spring Security, UsernamePasswordAuthenticationFilter,
 * para manejar la autenticación de usuarios utilizando JSON Web Tokens (JWT).</p>
 *
 * <p>Incluye el metodo de autenticación, attemptAuthentication, que se encarga de extraer los parámetros de la petición
 * HTTP, realizar la autenticación y devolver un objeto Authentication si la autenticación es exitosa, y el metodo de
 * éxito de autenticación, successfulAuthentication, que se encarga de generar el token JWT y enviarlo en la respuesta
 * HTTP.</p>
 *
 * @author JuanAlberticoHF
 * @version 1.0.0
 * @since 0.1.0
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    /** Utilidad para generar y validar tokens JWT. */
    private final JwtUtils jwtUtils;

    public JwtAuthenticationFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    /**
     * <p>Metodo de intento de autenticación.</p>
     *
     * <p>Se ejecuta cuando se intenta autenticar un usuario, encargandose de extraer los parametros de la petición
     * HTTP, realizar la autenticación y devolver un objeto Authenticatión si la autenticación es exitosa.</p>
     *
     * <p>Se utiliza {@code @NullMarked} para indicar que los parámetros de entrada no pueden ser nulos.</p>
     *
     * @param request la petición HTTP, que contiene los parámetros de autenticación (como el email del usuario y la
     *                contraseña) que se deben extraer para realizar la autenticación.
     * @param response la respuesta HTTP, que se puede utilizar para enviar una respuesta personalizada en caso de
     *                 autenticación exitosa o fallida.
     * @return Devuelve un objeto Authentication que representa el resultado de la autenticación. Si la autenticación es
     *         exitosa, este objeto contendrá la información del usuario autenticado, como su email y autoridades.
     * @throws AuthenticationException Si la autenticación falla.
     */
    @Override
    @NullMarked
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        LoginRequest userLoginRequest = null; // Objeto que obtiene el usuario de la petición HTTP.
        String username; // Variable email del usuario
        String password; // Variable contraseña

        // Obtenemos los datos de autenticación de la petición
        try {
            // Se utiliza ObjectMapper para convertir el JSON de la petición HTTP a un objeto de tipo LoginRequest.
            userLoginRequest = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);
            username = userLoginRequest.username();
            password = userLoginRequest.password();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Crea un objeto UsernamePasswordAuthenticationToken con el nombre de usuario y la contraseña obtenidos de la petición HTTP.
        // Este objeto se utiliza para realizar la autenticación del usuario.
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        // Llama al metodo authenticate del AuthenticationManager para realizar la autenticación del usuario utilizando el token de autenticación creado.
        // Devuelve el resultado de la autenticación, que puede ser un objeto Authentication si la autenticación es exitosa o una excepción si falla.
        return getAuthenticationManager().authenticate(authenticationToken);
    }

    /**
     * <p>Metodo de autenticación exitosa.</p>
     *
     * <p>Se ejecuta cuando la autenticación de un usuario es exitosa, encargandose de generar el token JWT y enviarlo
     * en la respuesta HTTP.</p>
     *
     * @param request la petición HTTP, que se puede utilizar para obtener información adicional sobre la autenticación
     *                exitosa.
     * @param response la respuesta HTTP, que se utiliza para enviar el token JWT generado al cliente.
     * @param chain el filtro de la cadena de filtros de Spring Security, que se puede utilizar para continuar con el
     *              procesamiento de la solicitud después de enviar la respuesta.
     * @param authResult el resultado de la autenticación, que contiene la información del usuario autenticado, como su
     *                   email y autoridades.
     * @throws IOException Si ocurre un error al escribir la respuesta HTTP.
     * @throws ServletException Si ocurre un error en el procesamiento de la solicitud.
     */
    @Override
    protected void successfulAuthentication(@NonNull HttpServletRequest request,
                                            HttpServletResponse response,
                                            @NonNull FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        // Extraemos de la autentificación el usuario autenticado, que es el objeto (principal) de la autentificación.
        User user = (User) authResult.getPrincipal();

        // Generamos el Token de acceso base al username del usuario (email)
        assert user != null;
        String token = jwtUtils.generateAccessToken(user.getUsername());

        // Enviamos el token en la cabecera de la respuesta HTTP. Da autorización para los endpoints.
        response.setHeader("Authorization", token);

        // Construimos el cuerpo de la respuesta con la información del token, el mensaje y el nombre de usuario.
        Map<String, Object> httpResponse = new HashMap<>();
        httpResponse.put("token", token);
        httpResponse.put("message", "Autenticación exitosa");
        httpResponse.put("email", user.getUsername());

        // Enviamos la respuesta HTTP con el cuerpo construido en formato JSON utilizando ObjectMapper para convertir el mapa a JSON.
        response.getWriter().write(new ObjectMapper().writeValueAsString(httpResponse));

        // Establece el código de estado HTTP de la respuesta como 200 OK.
        response.setStatus(HttpStatus.OK.value());
        // Establece el tipo de contenido de la respuesta HTTP como JSON.
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // Asegura que el cuerpo de la respuesta se envíe al cliente.
        response.getWriter().flush();

        // Llama al metodo de la clase padre para realizar cualquier procesamiento adicional necesario después de una autenticación exitosa.
        super.successfulAuthentication(request, response, chain, authResult);
    }
}
