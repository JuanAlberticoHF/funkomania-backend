package tfg.funkomania.funkomania_api.controllers.admin_controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Size;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tfg.funkomania.funkomania_api.dtos.usuario_dtos.UsuarioDTOId;
import tfg.funkomania.funkomania_api.services.UsuarioServiceImpl;

import java.util.List;

/**
 * <p>Controlador REST para gestión de operaciones de usuario por parte del administrador</p>
 *
 * <p>Utiliza el servicio {@link UsuarioServiceImpl} para interactuar con la capa de negocio y la base de datos.</p>
 * <p>Proporciona endpoints para el registro de un usuario.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.2
 * @since 0.6.0
 */
@RestController
@RequestMapping("/admin/usuarios")
@Validated
@Tag(name = "[ADMIN] Gestor de Usuarios", description = "Endpoints para obtener todos los datos de los usuarios registrados en el sistema. Solo accesible para administradores.")
public class UsuarioAdminController {
    /** Servicio de usuario que contiene la lógica de negocio de todas las operaciones */
    private final UsuarioServiceImpl usuarioService;

    public UsuarioAdminController(UsuarioServiceImpl usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Obtener listados de usuarios del sistema", description = "Obtiene los datos de los perfiles de los usuarios del sistema. Requiere que el usuario esté autenticado y tenga el rol de administrador para acceder a esta información.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de usuarios obtenido exitosamente", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UsuarioDTOId.class)
            )),
            @ApiResponse(responseCode = "401", description = "No autorizado - el usuario no está autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: el usuario no tiene permiso para acceder al recurso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/")
    public ResponseEntity<List<UsuarioDTOId>> obtenerTodosLosUsuariosFiltrados(
            @Parameter(description = "Email del usuario para filtrar por coincidencia parcial (opcional)", example = "Juan")
            @Size(max = 255)
            @RequestParam(required = false) String search) {
        return ResponseEntity.ok(usuarioService.obtenerTodosLosUsuarios(search));
    }
}
