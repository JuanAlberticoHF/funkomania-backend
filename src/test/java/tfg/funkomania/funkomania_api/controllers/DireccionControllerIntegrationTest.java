package tfg.funkomania.funkomania_api.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tfg.funkomania.funkomania_api.dtos.direccion_dtos.DireccionDTO;
import tfg.funkomania.funkomania_api.persistence.entities.*;
import tfg.funkomania.funkomania_api.persistence.repositories.*;
import tfg.funkomania.funkomania_api.testutils.UsuarioTestFactory;
import tools.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class DireccionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IDireccionRepository direccionRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void limpiarBaseDeDatos() {
        direccionRepository.deleteAllInBatch();
        usuarioRepository.deleteAllInBatch();
        entityManager.clear();
    }

    @Test
    void getAllDirecciones_Exitoso() throws Exception {
        Usuario usuario = UsuarioTestFactory.usuarioPersistible("test@example.com", "password");
        usuarioRepository.save(usuario);

        mockMvc.perform(get("/usuario/direcciones/")
                        .with(SecurityMockMvcRequestPostProcessors.user(usuario.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void addDireccion_Exitoso() throws Exception {
        Usuario usuario = UsuarioTestFactory.usuarioPersistible("test@example.com", "password");
        usuarioRepository.save(usuario);

        DireccionDTO dto = new DireccionDTO("Calle", "1", "1", "A", "Ciudad", "Municipio", "Provincia", "12345", true);

        mockMvc.perform(post("/usuario/direcciones/")
                        .with(SecurityMockMvcRequestPostProcessors.user(usuario.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void updateDireccion_Exitoso() throws Exception {
        Usuario usuario = UsuarioTestFactory.usuarioPersistible("test@example.com", "password");
        usuario = usuarioRepository.saveAndFlush(usuario);
        
        Direccion dir = Direccion.builder()
                .calle("Calle").numero("1").ciudad("Ciudad").municipio("Municipio").provincia("Provincia").codigoPostal("12345")
                .usuario(usuario)
                .activo(true)
                .build();
        dir = direccionRepository.saveAndFlush(dir);

        DireccionDTO dto = new DireccionDTO("CalleNueva", "2", "2", "B", "Ciudad", "Municipio", "Provincia", "54321", true);

        mockMvc.perform(put("/usuario/direcciones/{idDireccion}", dir.getId())
                        .with(SecurityMockMvcRequestPostProcessors.user(usuario.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void activarDireccion_Exitoso() throws Exception {
        Usuario usuario = UsuarioTestFactory.usuarioPersistible("test@example.com", "password");
        usuario = usuarioRepository.saveAndFlush(usuario);
        
        Direccion dir = Direccion.builder()
                .calle("Calle").numero("1").ciudad("Ciudad").municipio("Municipio").provincia("Provincia").codigoPostal("12345")
                .usuario(usuario)
                .activo(true)
                .build();
        dir = direccionRepository.saveAndFlush(dir);

        mockMvc.perform(put("/usuario/direcciones/{idDireccion}/activar", dir.getId())
                        .with(SecurityMockMvcRequestPostProcessors.user(usuario.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
