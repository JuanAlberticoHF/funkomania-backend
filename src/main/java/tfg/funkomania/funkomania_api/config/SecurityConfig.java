package tfg.funkomania.funkomania_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad para la API de Funkomania.
 * <p>Esta clase define la configuración de seguridad utilizando Spring Security y el password encoder</p>
 *
 * @author JuanAlbeticoHF
 * @version 0.1.1
 * @since 0.1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

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

                // Configura la gestión de sesiones para que no se creen sesiones (STATELESS)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Restricciones endpoints
                .authorizeHttpRequests(auth -> auth
                        // Permite el acceso a los endpoints de autenticación sin necesidad de autenticación previa.
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // El acceso a los endpoints para cualquier otra solicitud necesita autenticación.
                        .anyRequest().authenticated()
                )
                .build();
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
}
