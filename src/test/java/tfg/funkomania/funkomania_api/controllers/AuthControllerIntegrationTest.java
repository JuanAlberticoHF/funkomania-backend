package tfg.funkomania.funkomania_api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import tfg.funkomania.funkomania_api.dtos.security_dtos.LoginRequest;
import tfg.funkomania.funkomania_api.dtos.security_dtos.TokenResponse;
import tfg.funkomania.funkomania_api.dtos.usuario_dtos.UsuarioRegistroDTO;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import tfg.funkomania.funkomania_api.persistence.entities.Usuario;
import tfg.funkomania.funkomania_api.persistence.repositories.IUsuarioRepository;
import tfg.funkomania.funkomania_api.testutils.UsuarioTestFactory;
import tfg.funkomania.funkomania_api.utils.JwtUtils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas de integración para el endpoint de registro del controlador de autenticación.
 * Pruebas de integración para el endpoint de registro del controlador de autenticación.
 *
 * <p>Ejecuta peticiones HTTP simuladas con MockMvc y valida respuestas JSON y códigos HTTP
 * contra el contexto real de Spring Boot.</p>
 *
 * @version 1.0.0
 * @since 0.1.0
 */
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @BeforeEach
    void limpiarBaseDeDatos() {
        usuarioRepository.deleteAll();
    }

    /**
     * Debe crear un usuario cuando el email no existe y el cuerpo es válido.
     *
     * @throws Exception sí falla la ejecución de la petición MockMvc
     */
    @Test
    void registrar_deberiaCrearUsuario() throws Exception {
        UsuarioRegistroDTO dto = UsuarioTestFactory.registroDto("newuser@example.com");

        mockMvc.perform(post("/auth/register")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    /**
     * Debe responder conflicto cuando el email ya existe en el sistema.
     *
     * @throws Exception sí falla la ejecución de la petición MockMvc
     */
    @Test
    void registrar_deberiaResponderConflictoCuandoEmailExiste() throws Exception {
        Usuario existing = UsuarioTestFactory.usuarioPersistible("dup@example.com", passwordEncoder.encode("pass"));
        usuarioRepository.save(existing);

        UsuarioRegistroDTO dto = UsuarioTestFactory.registroDto("dup@example.com");

        mockMvc.perform(post("/auth/register")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").isNotEmpty());
    }

    /**
     * Debe responder bad request cuando el cuerpo de la petición es inválido.
     *
     * @throws Exception sí falla la ejecución de la petición MockMvc
     */
    @Test
    void registrar_deberiaResponderBadRequestCuandoCuerpoInvalido() throws Exception {
        UsuarioRegistroDTO dto = new UsuarioRegistroDTO("", "", "");

        mockMvc.perform(post("/auth/register")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").isNotEmpty());
    }

    /**
     * Debe iniciar sesión y devolver un token JWT válido.
     *
     * @throws Exception sí falla la ejecución de la petición MockMvc
     */
    @Test
    void login_deberiaRetornarTokenValido() throws Exception {
        Usuario existing = UsuarioTestFactory.usuarioPersistible(
                "login@example.com",
                passwordEncoder.encode("Password123")
        );
        usuarioRepository.save(existing);

        LoginRequest loginRequest = new LoginRequest("login@example.com", "Password123");

        String response = mockMvc.perform(post("/auth/login")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.email").value("login@example.com"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String token = objectMapper.readValue(response, TokenResponse.class).token();
        assertThat(jwtUtils.isTokenValid(token)).isTrue();
    }

    /**
     * Debe responder bad request cuando el cuerpo del login es inválido.
     *
     * @throws Exception sí falla la ejecución de la petición MockMvc
     */
    @Test
    void login_deberiaResponderBadRequestCuandoCuerpoInvalido() throws Exception {
        LoginRequest loginRequest = new LoginRequest("", "");

        mockMvc.perform(post("/auth/login")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").isNotEmpty());
    }
}
