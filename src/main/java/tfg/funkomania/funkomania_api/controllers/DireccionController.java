package tfg.funkomania.funkomania_api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.web.bind.annotation.*;
import tfg.funkomania.funkomania_api.dtos.direccion_dtos.DireccionDTO;
import tfg.funkomania.funkomania_api.dtos.direccion_dtos.DireccionDTOId;
import tfg.funkomania.funkomania_api.services.DireccionServiceImpl;

import java.util.List;

/**
 * <p>Controlador REST para manejar las solicitudes relacionadas con las direcciones de un usuario.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.1
 * @since 0.5.0
 */
@RestController
@RequestMapping("/usuario/direcciones")
@Tag(name = "Gestor de Direcciones", description = "Endpoints para gestionar las direcciones del usuario autenticado, incluyendo la creación, actualización y activación de direcciones.")
public class DireccionController {
    /** Servicio de direcciones que contiene la lógica de negocio de todas las operaciones */
    private final DireccionServiceImpl direccionService;

    public DireccionController(DireccionServiceImpl direccionService) {
        this.direccionService = direccionService;
    }

    @Operation(summary = "Obtener todas las direcciones del usuario autenticado", description = "Retorna una lista de todas las direcciones asociadas al usuario autenticado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de objetos JSON con la lista de direcciones del usuario", content = @Content(
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
            @ApiResponse(responseCode = "404", description = "No se encontro el usuario autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al obtener el perfil del cliente autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/")
    public ResponseEntity<List<DireccionDTOId>> getAllDirecciones() {
        return ResponseEntity.ok(direccionService.getDirecciones());
    }

    @Operation(summary = "Registrar una nueva dirección para el usuario autenticado", description = "Registra una nueva dirección para el usuario autenticado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La dirección se ha creado exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado - el usuario no está autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "404", description = "No se encontro el usuario autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al obtener el perfil del cliente autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/")
    public ResponseEntity<Void> addDireccion(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "DTO con los datos de la dirección a crear. El campo 'idUsuario' se ignora y se asigna automáticamente al usuario autenticado.",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DireccionDTO.class),
                    examples = @ExampleObject(
                            value = """
                                    {
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
                                    """
                    )
            ))
            @Valid @RequestBody DireccionDTO direccionDTO) {
        direccionService.addDireccion(direccionDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Actualizar una dirección existente del usuario autenticado", description = "Actualiza una dirección existente del usuario autenticado. El campo 'idUsuario' se ignora porque la dirección se asocia automáticamente al usuario autenticado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dirección actualizada exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado - el usuario no está autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "404", description = "El la direccion con el identificador proporcionado no existe", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/{idDireccion}")
    public ResponseEntity<Void> updateDireccion(
            @Parameter(
                    description = "ID de la dirección a actualizar",
                    required = true,
                    schema = @Schema(type = "integer", format = "int64", minimum = "1"),
                    examples = @ExampleObject(value = "1"))
            @PathVariable Long idDireccion,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "DTO con los datos de la dirección a actualizar. El campo 'idUsuario' se ignora y se asigna automáticamente al usuario autenticado.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DireccionDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "calle": "Calle Funkomania",
                                        "numero": "3",
                                        "piso": "",
                                        "puerta": "",
                                        "ciudad": "Funkopolis",
                                        "municipio": "Funkotown",
                                        "provincia": "Funkovincia",
                                        "codigoPostal": "98765",
                                        "activo": false
                                    }
                                    """
                            )
                    ))
            @Valid @RequestBody DireccionDTO direccionDTO) {
        direccionService.updateDireccion(idDireccion, direccionDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Activar una dirección existente del usuario autenticado", description = "Activa una dirección existente del usuario autenticado. Solo se puede activar una dirección a la vez, por lo que si se activa una nueva dirección, las demás se desactivan automáticamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La dirección se ha activado exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado - el usuario no está autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "404", description = "No se encontro el usuario autenticado o no se ha encontrado la direccion con el identificador proporcionado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al obtener el perfil del cliente autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/{idDireccion}/activar")
    public ResponseEntity<Void> activarDireccion(
            @Parameter(
                    description = "ID de la dirección a activar",
                    required = true,
                    schema = @Schema(type = "integer", format = "int64", minimum = "1"),
                    examples = @ExampleObject(value = "1"))
            @PathVariable Long idDireccion) {
        direccionService.activarDireccion(idDireccion);
        return ResponseEntity.ok().build();
    }
}
