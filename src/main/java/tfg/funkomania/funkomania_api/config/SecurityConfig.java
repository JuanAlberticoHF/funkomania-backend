package tfg.funkomania.funkomania_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import tfg.funkomania.funkomania_api.exceptions.custom_exceptions.TokenInvalidoException;
import tfg.funkomania.funkomania_api.exceptions.security.CustomAccessDeniedHandler;
import tfg.funkomania.funkomania_api.exceptions.security.CustomAuthenticationEntryPoint;
import tfg.funkomania.funkomania_api.security.filters.JwtAuthorizationFilter;
import tfg.funkomania.funkomania_api.services.InMemoryTokenBlackListService;
import tfg.funkomania.funkomania_api.services.UserDetailServiceImpl;
import tfg.funkomania.funkomania_api.utils.JwtUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Configuración de seguridad para la API de Funkomania.
 * <p>Esta clase define la configuración de seguridad utilizando Spring Security y el password encoder</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.6.0
 * @since 0.1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /** Filtro de autorización JWT. */
    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    /** Bean de servicio para añadir tokens a la lista negra. */
    private final InMemoryTokenBlackListService tokenBlackListService;
    private final JwtUtils jwtUtils;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(JwtAuthorizationFilter jwtAuthorizationFilter,
                          InMemoryTokenBlackListService tokenBlackListService, JwtUtils jwtUtils,
                          CustomAuthenticationEntryPoint authenticationEntryPoint,
                          CustomAccessDeniedHandler accessDeniedHandler) {
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
        this.tokenBlackListService = tokenBlackListService;
        this.jwtUtils = jwtUtils;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    /**
     * Configuración de la cadena de filtros ejecutada en cada petición HTTP.
     * @param httpSecurity Objeto que permite configurar la seguridad HTTP.
     * @return Un bean de tipo SecurityFilterChain que define la configuración de seguridad para las solicitudes HTTP.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        return httpSecurity
                // Desactiva la protección contra ataques CSRF.
                .csrf(AbstractHttpConfigurer::disable)

                // Activa el CORS usando el Bean de cors.
                .cors(Customizer.withDefaults())

                // Restricciones endpoints
                .authorizeHttpRequests(auth -> auth
                        // Permite el acceso a los endpoints de autenticación sin necesidad de autenticación previa.
                        // - Endpoints de documentación
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // - Endpoints de acceso libre
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/productos/**").permitAll()
                        .requestMatchers("/categorias/**").permitAll()
                        .requestMatchers("/metodos-pago/**").permitAll()
                        // - Endpoints para usuarios autenticados
                        .requestMatchers("/usuario/**").authenticated()
                        .requestMatchers("/pedidos/crear-desde-pedido").authenticated()
                        .requestMatchers("/carrito/**").authenticated()
                        // - Endpoints para admin
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // El acceso a los endpoints para cualquier otra solicitud necesita autenticación.
                        .anyRequest().authenticated()
                )

                // Configura la gestión de sesiones para que no se creen sesiones (STATELESS)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Agrega el filtro de autorización JWT antes del filtro de autenticación de Spring Security.
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)

                // Configura los manejadores de excepciones personalizados para devolver ProblemDetails.
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )

                // Configura el cierre de sesión para invalidar el token JWT y limpiar el contexto de seguridad.
                .logout(logout ->
                        logout.logoutUrl("/auth/logout")
                                .addLogoutHandler((request, response, authentication) -> {
                                    final var authHeader = request.getHeader("Authorization");
                                    logout(authHeader);
                                })
                                .logoutSuccessHandler((request, response, authentication) ->
                                        SecurityContextHolder.clearContext())
                )
                .build();
    }

    /**
     * <p>Configura el AuthenticationManager, encargado de gestionar la autenticación de los usuarios.</p>
     *
     * @param authenticationConfiguration Objeto AuthenticationConfiguration que permite configurar el
     *                                    AuthenticationManager, definiendo los proveedores de autenticación y
     *                                    otros aspectos relacionados con la autenticación.
     * @return El objeto AuthenticationManager configurado a partir del AuthenticationConfiguration, que gestiona la
     * autenticación de los usuarios.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration){
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * <p>Configura el AuthenticationProvider, que es el componente encargado de realizar la autenticación de una
     * solicitud validando las credenciales, distanciando un objeto DaoAuthenticationProvider para autenticar usuario y
     * contraseña de una base de datos</p>
     * @return El objeto AuthenticationProvider configurado.
     */
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailServiceImpl userDetailsService){
        // Creamos el proveedor de autenticación DaoAuthenticationProvider que utiliza un UserDetailsService para
        // cargar los detalles del usuario.
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        // Establece el PasswordEncoder para el proveedor de autenticación.
        provider.setPasswordEncoder(passwordEncoder());
        // Devuelve el proveedor de autenticación configurado.
        return provider;
    }

    /**
     * Configura el PasswordEncoder, que es el componente encargado de encriptar las contraseñas y verificar las
     * contraseñas en texto plano contra las contraseñas encriptadas almacenadas en la base de datos.
     * @return Un bean de tipo PasswordEncoder que utiliza el algoritmo BCrypt para encriptar las contraseñas.
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * Agrega un token JWT a la lista negra para invalidarlo, impidiendo su uso en futuras solicitudes.
     * @param token El token JWT que se desea invalidar.
     */
    private void logout (final String token){
        if (token == null || !token.startsWith("Bearer ")){
            throw new TokenInvalidoException("El token de autenticación es inválido o no se proporcionó.");
        }

        // Extrae el token JWT del encabezado de autorización.
        final String jwt = token.substring(7);

        // Verifica si el token JWT es válido antes de agregarlo a la lista negra.
        if (!jwtUtils.isTokenValid(jwt)){
            throw new TokenInvalidoException("El token proporcionado no es válido.");
        }

        // Agrega el token JWT a la lista negra para invalidarlo.
        tokenBlackListService.addToken(jwt);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 1. ¿A quién le permitimos el paso?
        configuration.setAllowedOrigins(List.of(
                "http://localhost:4200",
                "http://localhost:3000",
                "http://localhost:5173",
                "http://funkomania-web:5173",
                "http://funkomania-web:4200",
                "http://funkomania-web:3000"
        ));

        // 2. ¿Qué métodos HTTP permitimos?
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 3. ¿Qué cabeceras permitimos que nos envíen?
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        // 4. ¿Permitimos el envío de cookies/credenciales?
        configuration.setAllowCredentials(true);

        // Aplicamos esta configuración a TODAS las rutas de nuestra API
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
