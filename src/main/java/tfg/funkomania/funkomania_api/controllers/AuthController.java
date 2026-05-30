package tfg.funkomania.funkomania_api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tfg.funkomania.funkomania_api.dtos.usuario_dtos.UsuarioDTOId;
import tfg.funkomania.funkomania_api.dtos.usuario_dtos.UsuarioRegistroDTO;
import tfg.funkomania.funkomania_api.persistence.entities.Usuario;
import tfg.funkomania.funkomania_api.services.AuthServiceImpl;

/**
 * <p>Controlador REST para la autenticación de usuarios.</p>
 *
 * <p>Utiliza el servicio AuthServiceImpl para interactuar con la capa de negocio y la base de datos.</p>
 * <p>Proporciona endpoints para el registro de un usuario.</p>
 *
 * @author JuanAlbeticoHF
 * @version 0.1.0
 * @since 0.1.0
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Gestor de Autenticación", description = "Endpoints para gestionar la autenticación de usuarios, incluyendo el registro.")
public class AuthController {

    /** Servicio de autenticación */
    private final AuthServiceImpl authService;

    public AuthController(AuthServiceImpl authServiceimpl) {
        this.authService = authServiceimpl;
    }

    @Operation(summary = "Registrar un nuevo usuario", description = "Registra un nuevo usuario en la base de datos. Retorna el objeto creado con su ID generado automáticamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "El cuerpo de la petición no es valido o no cumple con las validaciones"),
            @ApiResponse(responseCode = "409", description = "Conflicto: El email del usuario ya existe en la base de datos")
    })
    @PostMapping("/register")
    public ResponseEntity<UsuarioDTOId> register(@RequestBody UsuarioRegistroDTO usuarioRegistroDTO) {
        Usuario usuarioRes = authService.registerUsuario(new Usuario(usuarioRegistroDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(new UsuarioDTOId(usuarioRes));
    }
}
