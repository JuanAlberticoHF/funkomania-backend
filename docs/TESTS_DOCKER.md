# Tests con MySQL en Docker

Guia para ejecutar tests usando un MySQL levantado con Docker Compose y un script SQL de inicializacion.

## Requisitos

- Docker Desktop en ejecucion.
- El script SQL existe en `src/main/resources/init.sql`.

## Tests creados

- `AuthControllerIntegrationTest.java`: pruebas de integracion de los endpoints de autenticacion con MockMvc (registro, login, validaciones de cuerpo y conflicto por email duplicado).
- `AuthServiceImplTest.java`: pruebas unitarias del servicio de autenticacion (registro, excepciones por email duplicado, verificacion de existencia y login).
- `IUsuarioRepositoryTest.java`: pruebas de integracion del repositorio para `existsByEmail`.
- `UsuarioTest.java`: pruebas unitarias de la entidad `Usuario` y del constructor desde `UsuarioRegistroDTO`.
- `UsuarioDTOIdTest.java`: pruebas unitarias del mapeo de entidad a `UsuarioDTOId`.
- `UsuarioRegistroDTOTest.java`: pruebas unitarias de getters/setters y validaciones del DTO de registro.
- `LoginRequestTest.java`: pruebas unitarias de validacion y constructor del DTO de login.
- `TokenResponseTest.java`: pruebas unitarias de validacion y constructor del DTO de respuesta de login.
- `JwtUtilsTest.java`: pruebas unitarias de generacion y validacion de tokens JWT.
- `GlobalExceptionHandlerTest.java`: pruebas unitarias del manejador global de excepciones (respuestas estandarizadas para errores).
- `CommonExceptionHandlerTest.java`: pruebas unitarias de manejadores de validacion y errores comunes.
- `FunkomaniaApiApplicationTests.java`: prueba de carga del contexto de Spring Boot.
- `ProductoControllerIntegrationTest.java`: pruebas de integración del controlador de productos (endpoints para catálogo y ofertas) ejecutadas con MockMvc. Estas pruebas insertan filas de prueba directamente mediante `JdbcTemplate` (evitando validaciones y lógica JPA indeseada) y llaman a `entityManager.clear()` antes de cada `MockMvc.perform(...)` para forzar lecturas desde la base de datos real.
- `CategoriaControllerIntegrationTest.java`: pruebas de integración del controlador de categorías (listado de categorías y relación padre/hija) usando MockMvc.
- `ProductoServiceImplTest.java`: pruebas unitarias del servicio de productos (lógica de negocio, filtrado y paginación).
- `CategoriaServiceImplTest.java`: pruebas unitarias del servicio de categorías.
- `VistaProductosCatalogoDTOIdTest.java`: pruebas unitarias del DTO usado para representar productos en el catálogo.
- `VistaProductosCatalogoSpecificationTest.java`: pruebas unitarias de la especificación JPA/criteria utilizada para filtrar la vista/productos en consultas.
- `ProductoTest.java`: pruebas unitarias de la entidad `Producto` (constructores, getters/setters y comportamiento básico).
- `CategoriaTest.java`: pruebas unitarias de la entidad `Categoria`.
- `VistaProductosCatalogoTest.java`: pruebas unitarias de la entidad/vista `VistaProductosCatalogo` (mapeo y valores esperados).

## 1) Levantar MySQL para tests (con inicializacion)

```bash
docker compose -f docker-compose.test.yml up -d
```

El script `init.sql` solo se ejecuta la primera vez que se crea el volumen de la base de datos.

## 2) Ejecutar tests

```bash
.\mvnw -q test
```

## 3) Detener Docker sin borrar datos

```bash
docker compose -f docker-compose.test.yml down
```

## 4) Detener Docker y borrar volumenes (reinicializa la base de datos)

```bash
docker compose -f docker-compose.test.yml down -v
```

## Nota sobre reinicializacion

Si necesitas volver a ejecutar el script `init.sql`, debes borrar el volumen con el comando del paso 4.


