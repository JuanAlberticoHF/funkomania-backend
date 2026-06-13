package tfg.funkomania.funkomania_api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tfg.funkomania.funkomania_api.dtos.usuario_dtos.UsuarioUpdateRequestDTO;
import tfg.funkomania.funkomania_api.dtos.usuario_dtos.VistaUsuarioPerfilClienteDTOId;
import tfg.funkomania.funkomania_api.services.UsuarioServiceImpl;

/**
 * <p>Controlador REST para gestión de operaciones de usuario</p>
 *
 * <p>Utiliza el servicio {@link UsuarioServiceImpl} para interactuar con la capa de negocio y la base de datos.</p>
 * <p>Proporciona endpoints para el registro de un usuario.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.1
 * @since 0.4.0
 */
@RestController
@RequestMapping("/usuario")
@Validated
@Tag(name = "Gestion Perfil de Usuario", description = "Endpoints para gestionar el perfil del usuario autenticado, incluyendo la obtención de su perfil y la actualización de sus datos.")
public class UsuarioController {
    /** Servicio de usuario que contiene la lógica de negocio de todas las operaciones */
    private final UsuarioServiceImpl usuarioService;

    public UsuarioController(UsuarioServiceImpl usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Obtener perfil del cliente autenticado", description = "Obtiene los datos del perfil del cliente autenticado utilizando su email de autenticación. Requiere que el usuario esté autenticado para acceder a esta información.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil del cliente autenticado obtenido exitosamente", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = VistaUsuarioPerfilClienteDTOId.class)
            )),
            @ApiResponse(responseCode = "401", description = "No autorizado - el usuario no está autenticado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al obtener el perfil del cliente autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/perfil")
    public ResponseEntity<VistaUsuarioPerfilClienteDTOId> obtenerPerfilClienteAutenticado() {
        return ResponseEntity.ok(usuarioService.obtenerPerfilClienteAutenticado());
    }

    @Operation(summary = "Actualizar perfil del cliente autenticado", description = "Actualiza los datos del cliente autenticado utilizando la información proporcionada en el DTO de actualización. Requiere que el usuario esté autenticado para realizar esta operación. Todos los campos del DTO de actualización no pueden ser nulos o el nombre no puede estar vacío.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Actualización exitosa"),
            @ApiResponse(responseCode = "400", description = "El cuerpo de la petición no es valido o no cumple con las validaciones o los valores son nulos", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "401", description = "No autorizado - el usuario no está autenticado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al obtener el perfil del cliente autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/perfil")
    public ResponseEntity<Void> actualizarDatosUsuarioAutenticado(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "DTO con los datos actualizados del cliente autenticado. Todos los campos no pueden ser nulos o el nombre no puede estar vacío.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioUpdateRequestDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "nombre": "Funkomania2",
                                                "apellido1": "Backend",
                                                "apellido2": "Frontend",
                                                "telefono": ""
                                            }
                                            """
                            )
                    )
            )
            @Valid @RequestBody UsuarioUpdateRequestDTO usuarioUpdateRequestDTO) {
        usuarioService.actualizarUsuarioAutenticado(usuarioUpdateRequestDTO);
        return ResponseEntity.ok().build();
    }
}
