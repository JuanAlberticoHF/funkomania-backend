package tfg.funkomania.funkomania_api.controllers;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import tfg.funkomania.funkomania_api.dtos.usuario_dtos.UsuarioRegistroDTO;
import tfg.funkomania.funkomania_api.persistence.entities.Usuario;
import tfg.funkomania.funkomania_api.persistence.repositories.IUsuarioRepository;
import tfg.funkomania.funkomania_api.testutils.UsuarioTestFactory;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas de integración para el endpoint de registro del controlador de autenticación.
 *
 * <p>Ejecuta peticiones HTTP simuladas con MockMvc y valida respuestas JSON y códigos HTTP
 * contra el contexto real de Spring Boot.</p>
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

    @BeforeEach
    void limpiarBaseDeDatos() {
        usuarioRepository.deleteAll();
    }

    /**
     * Debe crear un usuario cuando el email no existe y el cuerpo es válido.
     *
     * @throws Exception si falla la ejecución de la petición MockMvc
     */
    @Test
    void registrar_deberiaCrearUsuario() throws Exception {
        UsuarioRegistroDTO dto = UsuarioTestFactory.registroDto("newuser@example.com");

        String response = mockMvc.perform(post("/auth/register")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.email").value("newuser@example.com"))
                .andExpect(jsonPath("$.password").isNotEmpty())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String returnedPassword = objectMapper.readTree(response).get("password").asString();
        assertThat(returnedPassword).isNotEqualTo("Password123");
    }

    /**
     * Debe responder conflicto cuando el email ya existe en el sistema.
     *
     * @throws Exception si falla la ejecución de la petición MockMvc
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
     * @throws Exception si falla la ejecución de la petición MockMvc
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
}
