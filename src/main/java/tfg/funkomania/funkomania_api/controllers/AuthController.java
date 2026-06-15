package tfg.funkomania.funkomania_api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tfg.funkomania.funkomania_api.dtos.security_dtos.LoginRequest;
import tfg.funkomania.funkomania_api.dtos.security_dtos.TokenResponse;
import tfg.funkomania.funkomania_api.dtos.usuario_dtos.UsuarioRegistroDTO;
import tfg.funkomania.funkomania_api.persistence.entities.Usuario;
import tfg.funkomania.funkomania_api.persistence.enums.TipoNotificacionEnum;
import tfg.funkomania.funkomania_api.services.AuthServiceImpl;
import tfg.funkomania.funkomania_api.services.NotificacionServiceImpl;

/**
 * <p>Controlador REST para la autenticación de usuarios.</p>
 *
 * <p>Utiliza el servicio AuthServiceImpl para interactuar con la capa de negocio y la base de datos.</p>
 * <p>Proporciona endpoints para el registro de un usuario.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.1.0
 */
@RestController
@RequestMapping("/auth")
@Validated
@Tag(name = "Gestor de Autenticación", description = "Endpoints para gestionar la autenticación de usuarios, incluyendo el registro.")
@Slf4j
public class AuthController {

    /** Servicio de autenticación */
    private final AuthServiceImpl authService;

    /** Servicio de notificaciones para enviar correos electrónicos de bienvenida a los nuevos usuarios. */
    private final NotificacionServiceImpl notificacionService;

    public AuthController(AuthServiceImpl authServiceimpl,
                          NotificacionServiceImpl notificacionService) {
        this.authService = authServiceimpl;
        this.notificacionService = notificacionService;
    }

    @Operation(summary = "Registrar un nuevo usuario", description = "Registra un nuevo usuario en la base de datos. Retorna el objeto creado con su ID generado automáticamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "El usuario ha sido registrado satisfactoriamente"),
            @ApiResponse(responseCode = "400", description = "El cuerpo de la petición no es valido o no cumple con las validaciones", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "404", description = "El usuario registrado no ha sido encontrado en la base de datos.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "409", description = "Conflicto: El email del usuario ya existe en la base de datos", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
    })
    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Objeto JSON con los datos necesarios para registrar un nuevo usuario. El campo 'email' debe ser único en la base de datos.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioRegistroDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                         {
                                            "nombre": "Funkomania",
                                            "email": "Funkomania@gmail.com",
                                            "password": "Funkomania123"
                                         }
                                         """
                            )
                    )
            )
            @Valid @RequestBody UsuarioRegistroDTO usuarioRegistroDTO) {
        log.info("Iniciando registro de usuario: {}", usuarioRegistroDTO.getEmail());
        // Registrar el nuevo usuario utilizando el servicio de autenticación
        Usuario usuario = authService.register(new Usuario(usuarioRegistroDTO));
        // Obtengo el identificador generado automáticamente para el nuevo usuario registrado y registro la notificación de registro.
        notificacionService.generarNotificacion(usuario.getIdUsuario(), TipoNotificacionEnum.REGISTRO);
        // Retornar una respuesta HTTP 201 Created sin cuerpo, indicando que el usuario ha sido registrado satisfactoriamente.
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Inicio de sesión de un usuario", description = "Autentica a un usuario utilizando su correo electrónico y contraseña. Retorna un token JWT si las credenciales son válidas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TokenResponse.class),
                    examples = @ExampleObject(
                            value = """
                                    {
                                        "token": "abcdef1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef1234567890",
                                        "username": "Funkomania@gmail.com",
                                        "name": "Funkomania",
                                        "role": "CLIENTE"
                                    }
                                    """
                    )
            )),
            @ApiResponse(responseCode = "400", description = "El cuerpo de la petición no es válido o no cumple con las validaciones" , content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "403", description = "Credenciales incorrectas o cuenta de usuario no registrada: El correo electrónico o la contraseña proporcionados son incorrectos. (BadCredentialsException o UsernameNotFoundException o AutenticacionFallidaException)", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "404", description = "El usuario no ha sido encontrado en la base de datos.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
    })
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> authenticate(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Objeto JSON con las credenciales de inicio de sesión del usuario. El campo 'username' debe existir en la base de datos y la 'password' debe ser correcta para generar un token JWT.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginRequest.class),
                            examples = @ExampleObject(
                                    value = """
                                         {
                                            "username": "Funkomania@gmail.com",
                                            "password": "Funkomania123"
                                         }
                                         """
                            )
                    )
            )
            @Valid @RequestBody LoginRequest loginRequest) {
        log.info("Iniciando sesión para el usuario: {}", loginRequest.username());
        final TokenResponse tokenResponse = authService.login(loginRequest);
        return ResponseEntity.ok(tokenResponse);
    }
}
