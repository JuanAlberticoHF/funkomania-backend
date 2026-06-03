package tfg.funkomania.funkomania_api.persistence.specifications;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Pruebas unitarias para la clase de especificaciones de {@link VistaProductosCatalogoSpecification}.
 *
 * @version 1.0.0
 * @since 0.2.0
 */
class VistaProductosCatalogoSpecificationTest {

	@Test
	@DisplayName("Los metodos factoria publicos y estaticos devuelven Specification no nula")
	void shouldReturnNonNullSpecificationForPublicStaticFactoryMethods() {
		Method[] declaredMethods = VistaProductosCatalogoSpecification.class.getDeclaredMethods();
		List<Method> specificationFactoryMethods = new ArrayList<>();

		for (Method method : declaredMethods) {
			boolean isPublicStatic = Modifier.isPublic(method.getModifiers()) && Modifier.isStatic(method.getModifiers());
			boolean returnsSpecification = Specification.class.isAssignableFrom(method.getReturnType());

			if (isPublicStatic && returnsSpecification) {
				specificationFactoryMethods.add(method);
			}
		}

		assertFalse(specificationFactoryMethods.isEmpty(),
				"Debe existir al menos un metodo factoria publico y estatico que devuelva Specification");

		for (Method method : specificationFactoryMethods) {
			Object[] args = buildArguments(method.getParameterTypes());

			try {
				Object result = method.invoke(null, args);
				assertNotNull(result, "El metodo " + method.getName() + " no debe devolver null");
			} catch (InvocationTargetException ex) {
				Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
				fail("El metodo " + method.getName() + " lanzo una excepcion: " + cause.getMessage());
			} catch (IllegalAccessException ex) {
				fail("No se pudo acceder al metodo " + method.getName() + ": " + ex.getMessage());
			}
		}
	}

	private Object[] buildArguments(Class<?>[] parameterTypes) {
		Object[] args = new Object[parameterTypes.length];
		for (int i = 0; i < parameterTypes.length; i++) {
			args[i] = defaultValueFor(parameterTypes[i]);
		}
		return args;
	}

	private Object defaultValueFor(Class<?> type) {
		if (type == String.class) {
			return "test";
		}
		if (type == Long.class || type == long.class) {
			return 1L;
		}
		if (type == Integer.class || type == int.class) {
			return 1;
		}
		if (type == Double.class || type == double.class) {
			return 1.0d;
		}
		if (type == Float.class || type == float.class) {
			return 1.0f;
		}
		if (type == BigDecimal.class) {
			return BigDecimal.ONE;
		}
		if (type == Boolean.class || type == boolean.class) {
			return true;
		}
		if (type == LocalDate.class) {
			return LocalDate.now();
		}
		if (type == LocalDateTime.class) {
			return LocalDateTime.now();
		}
		if (type == LocalTime.class) {
			return LocalTime.NOON;
		}
		if (type == UUID.class) {
			return UUID.randomUUID();
		}
		if (type == Locale.class) {
			return Locale.ROOT;
		}
		if (type == Optional.class) {
			return Optional.empty();
		}
		if (type == List.class) {
			return Collections.emptyList();
		}
		if (type == Set.class) {
			return Collections.emptySet();
		}
		if (type == Map.class) {
			return Collections.emptyMap();
		}
		if (type == Pageable.class) {
			return Pageable.unpaged();
		}
		if (type == Sort.class) {
			return Sort.unsorted();
		}
		if (type == Specification.class) {
			return (Specification<Object>) (root, query, cb) -> cb.conjunction();
		}
		if (type.isEnum()) {
			Object[] constants = type.getEnumConstants();
			if (constants != null && constants.length > 0) {
				return constants[0];
			}
		}

		return null;
	}
}
