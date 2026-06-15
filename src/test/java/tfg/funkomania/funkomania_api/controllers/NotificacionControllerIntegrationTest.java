package tfg.funkomania.funkomania_api.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import tfg.funkomania.funkomania_api.persistence.entities.Usuario;
import tfg.funkomania.funkomania_api.persistence.repositories.INotificacionRepository;
import tfg.funkomania.funkomania_api.persistence.repositories.IUsuarioRepository;
import tfg.funkomania.funkomania_api.testutils.UsuarioTestFactory;
import jakarta.persistence.EntityManager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class NotificacionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private INotificacionRepository notificacionRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void limpiarBaseDeDatos() {
        notificacionRepository.deleteAll();
        usuarioRepository.deleteAll();
        entityManager.clear();
    }

    @Test
    void obtenerTodasLasNotificacionesDelCliente_Exitoso() throws Exception {
        Usuario usuario = UsuarioTestFactory.usuarioPersistible("test@example.com", "password");
        usuarioRepository.save(usuario);

        mockMvc.perform(get("/usuario/notificaciones/")
                        .with(SecurityMockMvcRequestPostProcessors.user(usuario.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void leerNotificacion_NoEncontrada() throws Exception {
        Usuario usuario = UsuarioTestFactory.usuarioPersistible("test@example.com", "password");
        usuarioRepository.save(usuario);

        mockMvc.perform(put("/usuario/notificaciones/{idNotificacion}/leer", 999L)
                        .with(SecurityMockMvcRequestPostProcessors.user(usuario.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
