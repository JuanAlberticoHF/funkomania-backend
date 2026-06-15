package tfg.funkomania.funkomania_api.controllers.admin_controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import tfg.funkomania.funkomania_api.persistence.entities.Usuario;
import tfg.funkomania.funkomania_api.persistence.entities.Direccion;
import tfg.funkomania.funkomania_api.persistence.repositories.IUsuarioRepository;
import tfg.funkomania.funkomania_api.persistence.repositories.IDireccionRepository;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UsuarioAdminControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private IDireccionRepository direccionRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void limpiarBaseDeDatos() {
        direccionRepository.deleteAll();
        usuarioRepository.deleteAll();
        entityManager.clear();
    }

    @Test
    void obtenerTodosLosUsuarios_ComoAdmin_DeberiaRetornar200() throws Exception {
        Usuario user = Usuario.builder()
                .nombre("Juan")
                .apellido1("Perez")
                .email("juan@example.com")
                .password("pass")
                .activo(true)
                .rol(tfg.funkomania.funkomania_api.persistence.enums.RoleEnum.CLIENTE)
                .fechaRegistro(LocalDateTime.now())
                .build();
        usuarioRepository.saveAndFlush(user);
        entityManager.clear();

        mockMvc.perform(get("/admin/usuarios/")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].email").value("juan@example.com"));
    }

    @Test
    void obtenerTodosLosUsuarios_ConBusqueda_DeberiaFiltrar() throws Exception {
        Usuario user1 = Usuario.builder()
                .nombre("Juan")
                .apellido1("Perez")
                .email("juan@example.com")
                .password("pass")
                .activo(true)
                .rol(tfg.funkomania.funkomania_api.persistence.enums.RoleEnum.CLIENTE)
                .fechaRegistro(LocalDateTime.now())
                .build();
        Usuario user2 = Usuario.builder()
                .nombre("Maria")
                .apellido1("Lopez")
                .email("maria@example.com")
                .password("pass")
                .activo(true)
                .rol(tfg.funkomania.funkomania_api.persistence.enums.RoleEnum.CLIENTE)
                .fechaRegistro(LocalDateTime.now())
                .build();
        usuarioRepository.saveAll(java.util.List.of(user1, user2));
        entityManager.clear();

        mockMvc.perform(get("/admin/usuarios/")
                        .param("search", "juan")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].email").value("juan@example.com"));
    }

    @Test
    void obtenerDireccionesDeUsuario_ComoAdmin_DeberiaRetornar200() throws Exception {
        Usuario user = Usuario.builder()
                .nombre("Juan")
                .apellido1("Perez")
                .email("juan@example.com")
                .password("pass")
                .activo(true)
                .rol(tfg.funkomania.funkomania_api.persistence.enums.RoleEnum.CLIENTE)
                .fechaRegistro(LocalDateTime.now())
                .build();
        user = usuarioRepository.saveAndFlush(user);
        
        Direccion dir = Direccion.builder()
                .calle("Calle 1")
                .numero("10")
                .ciudad("City")
                .municipio("Mun")
                .provincia("Prov")
                .codigoPostal("12345")
                .activo(true)
                .usuario(user)
                .build();
        direccionRepository.saveAndFlush(dir);
        entityManager.clear();
        
        Long idUser = user.getIdUsuario();

        mockMvc.perform(get("/admin/usuarios/" + idUser + "/direcciones")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].calle").value("Calle 1"));
    }

    @Test
    void obtenerDireccionesDeUsuario_Inexistente_DeberiaRetornar404() throws Exception {
        mockMvc.perform(get("/admin/usuarios/999/direcciones")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN")))
                .andExpect(status().isNotFound());
    }

    @Test
    void accederSinToken_DeberiaRetornar401() throws Exception {
        mockMvc.perform(get("/admin/usuarios/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void accederComoUser_DeberiaRetornar403() throws Exception {
        mockMvc.perform(get("/admin/usuarios/")
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().isForbidden());
    }
}
