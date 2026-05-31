package tfg.funkomania.funkomania_api.security.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tfg.funkomania.funkomania_api.services.InMemoryTokenBlackListService;
import tfg.funkomania.funkomania_api.services.UserDetailServiceImpl;
import tfg.funkomania.funkomania_api.utils.JwtUtils;

import java.io.IOException;

/**
 * Filtro de autorización JWT para validar tokens en las solicitudes HTTP.
 *
 * <p>Este filtro se ejecuta una vez por cada solicitud HTTP y se encarga de extraer el token JWT de la cabecera de
 * autorización, validar su autenticidad y establecer el contexto de seguridad para el usuario autenticado.</p>
 *
 * Utiliza @Component para que Spring lo detecte como un bean y lo incluya en la cadena de filtros de seguridad.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.1.0
 */
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    /** Bean de utilidad para tokens JWT */
    private final JwtUtils jwtUtils;

    /** Bean de servicio para cargar los detalles del usuario a partir del token JWT. */
    private final UserDetailServiceImpl userDetailService;

    /** Bean de servicio para añadir tokens a la lista negra. */
    private final InMemoryTokenBlackListService tokenBlackListService;

    public JwtAuthorizationFilter(JwtUtils jwtUtils,
                                  UserDetailServiceImpl userDetailService,
                                  InMemoryTokenBlackListService tokenBlackListService) {
        this.jwtUtils = jwtUtils;
        this.userDetailService = userDetailService;
        this.tokenBlackListService = tokenBlackListService;
    }

    /**
     * <p>Procesa cada solicitud HTTP para validar el token JWT y establecer el contexto de seguridad.</p>
     * @param request El objeto HttpServletRequest que representa la solicitud HTTP entrante.
     * @param response El objeto HttpServletResponse que representa la respuesta HTTP saliente.
     * @param filterChain El objeto FilterChain que permite continuar con el procesamiento de la solicitud a través de
     *                    la cadena de filtros de Spring Security.
     * @throws ServletException Si ocurre un error durante el procesamiento de la solicitud.
     * @throws IOException Si ocurre un error de entrada/salida durante el procesamiento de la solicitud
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Obtener el token JWT de la cabecera Authorization de la petición HTTP.
        String tokenHeader = request.getHeader("Authorization");

        // Comprobamos la presencia del token y que empire por "Bearer ".
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            // Se extrae el token JWT de la cabecera Authorization eliminando el prefijo "Bearer ".
            String token = tokenHeader.substring(7);

            // Si el token es válido, obtenemos el usuario y sus permisos.
            if (jwtUtils.isTokenValid(token) || tokenBlackListService.isTokenValid(token)) {
                // Obtenemos el sujeto del token (email)
                String username = jwtUtils.getUsernameFromToken(token);
                // Obtenemos el usuario y sus permisos
                UserDetails userDetails = userDetailService.loadUserByUsername(username);

                // Creamos un objeto de tipo UsernamePasswordAuthenticationToken con la información del usuario y sus permisos.
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                // Establecemos el objeto de autenticación en el contexto de seguridad de Spring Security, lo que
                // permite que la aplicación reconozca al usuario autenticado y sus permisos en las siguientes etapas
                // del procesamiento de la petición HTTP.
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // Permite que la petición HTTP continúe su procesamiento normal a través de la cadena de filtros de Spring Security,
        // lo que incluye la autorización y el acceso a los recursos protegidos de la aplicación.
        filterChain.doFilter(request, response);
    }
}
