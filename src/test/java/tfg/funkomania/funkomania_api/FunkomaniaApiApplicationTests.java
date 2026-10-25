package tfg.funkomania.funkomania_api;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Prueba básica de arranque del contexto de Spring Boot.
 *
 * <p>Sirve como comprobación rápida de que la aplicación puede levantar el contexto
 * de pruebas sin errores de configuración.</p>
 */
@SpringBootTest
@Disabled("Deshabilitado temporalmente para evitar la ejecución de pruebas de contexto durante el desarrollo activo.")
class FunkomaniaApiApplicationTests {

	/**
	 * Debe cargar correctamente el contexto de la aplicación.
	 */
	@Test
	void contextLoads() {
	}

}
