package tfg.funkomania.funkomania_api.controllers.admin_controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.constraints.Size;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tfg.funkomania.funkomania_api.dtos.direccion_dtos.DireccionDTOId;
import tfg.funkomania.funkomania_api.dtos.usuario_dtos.UsuarioDTOId;
import tfg.funkomania.funkomania_api.services.DireccionServiceImpl;
import tfg.funkomania.funkomania_api.services.UsuarioServiceImpl;

import java.util.List;

/**
 * <p>Controlador REST para gestión de operaciones de usuario por parte del administrador</p>
 *
 * <p>Utiliza el servicio {@link UsuarioServiceImpl} y {@link DireccionServiceImpl} para interactuar con la capa de negocio y la base de datos.</p>
 * <p>Proporciona endpoints obtener todos los usuarios registrados y sus direcciones.
 *
 * @author JuanAlbeticoHF
 * @version 1.1.1
 * @since 0.6.0
 */
@RestController
@RequestMapping("/admin/usuarios")
@Validated
@Tag(name = "[ADMIN] Gestor de Usuarios", description = "Endpoints para obtener todos los datos de los usuarios registrados en el sistema. Solo accesible para administradores.")
@Slf4j
public class UsuarioAdminController {
    /** Servicio de usuario que contiene la lógica de negocio de todas las operaciones */
    private final UsuarioServiceImpl usuarioService;

    /** Servicio de dirección que contiene la lógica de negocio de todas las operaciones */
    private final DireccionServiceImpl direccionService;

    public UsuarioAdminController(UsuarioServiceImpl usuarioService,
                                  DireccionServiceImpl direccionService) {
        this.usuarioService = usuarioService;
        this.direccionService = direccionService;
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
        log.info("Obteniendo listado de usuarios por administrador.");
        return ResponseEntity.ok(usuarioService.obtenerTodosLosUsuarios(search));
    }

    @Operation(summary = "Obtener listados de direcciones de un usuario en especifico", description = "Obtiene los datos de las direcciones de un usuario en especifico. Requiere que el usuario esté autenticado y tenga el rol de administrador para acceder a esta información.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de direcciones del usuario obtenido exitosamente", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DireccionDTOId.class),
                    examples = @ExampleObject(
                            value = """
                                    [
                                        {
                                            "idDireccion": 1,
                                            "calle": "Calle Funkomania",
                                            "numero": "2",
                                            "piso": "",
                                            "puerta": "",
                                            "ciudad": "Funkopolis",
                                            "municipio": "Funkotown",
                                            "provincia": "Funkovincia",
                                            "codigoPostal": "54321",
                                            "activo": true
                                        }
                                    ]
                                    """
                    )
            )),
            @ApiResponse(responseCode = "401", description = "No autorizado - el usuario no está autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: el usuario no tiene permiso para acceder al recurso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{idUsuario}/direcciones")
    public ResponseEntity<List<DireccionDTOId>> getAllDireccionByUsuarioId(
            @Parameter(description = "ID del usuario para obtener sus direcciones", example = "2")
            @NotNull @Positive
            @PathVariable Long idUsuario) {
        log.info("Obteniendo listado de direcciones del usuario {} por administrador.", idUsuario);
        return ResponseEntity.ok(direccionService.getDireccionesByUsuarioId(idUsuario));
    }
}
