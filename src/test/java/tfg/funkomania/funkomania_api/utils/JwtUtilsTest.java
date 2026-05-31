package tfg.funkomania.funkomania_api.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas unitarias para JwtUtils.
 *
 * @version 1.0.0
 * @since 0.1.0
 */
class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void configurarJwtUtils() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "secretKey", "dGVzdF9zZWNyZXRfZm9yX2p3dF9wYXlsb2FkXzEyMzQ1Njc4OTA=");
        ReflectionTestUtils.setField(jwtUtils, "timeExpiration", "3600000");
    }

    /**
     * Debe generar un token valido y recuperar el username desde el token.
     */
    @Test
    void generarToken_deberiaSerValidoYContenerUsername() {
        String email = "user@example.com";

        String token = jwtUtils.generateAccessToken(email);

        assertThat(token).isNotBlank();
        assertThat(jwtUtils.isTokenValid(token)).isTrue();
        assertThat(jwtUtils.getUsernameFromToken(token)).isEqualTo(email);
    }

    /**
     * Debe marcar como invalido un token mal formado.
     */
    @Test
    void validarToken_deberiaRetornarFalseParaTokenInvalido() {
        assertThat(jwtUtils.isTokenValid("token.invalido")).isFalse();
    }
}

