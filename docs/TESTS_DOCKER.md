# Tests con MySQL en Docker

Guia para ejecutar tests usando un MySQL levantado con Docker Compose y un script SQL de inicializacion.

## Requisitos

- Docker Desktop en ejecucion.
- El script SQL existe en `src/main/resources/init.sql`.

## Tests creados

- `AuthControllerIntegrationTest.java`: pruebas de integracion de los endpoints de autenticacion con MockMvc (registro, login con token valido, validacion de cuerpo invalido y conflicto por email duplicado).
- `AuthServiceImplTest.java`: pruebas unitarias del servicio de autenticacion (registro, excepcion por email duplicado, verificacion de existencia y login con token).
- `IUsuarioRepositoryTest.java`: pruebas de integracion del repositorio para `existsByEmail`.
- `UsuarioTest.java`: prueba unitaria del constructor de `Usuario` desde `UsuarioRegistroDTO`.
- `UsuarioDTOIdTest.java`: prueba unitaria del mapeo de entidad a `UsuarioDTOId`.
- `UsuarioRegistroDTOTest.java`: pruebas unitarias de getters/setters y validaciones del DTO de registro.
- `LoginRequestTest.java`: pruebas unitarias de validacion y constructor del DTO de login.
- `TokenResponseTest.java`: pruebas unitarias de validacion y constructor del DTO de respuesta de login.
- `JwtUtilsTest.java`: pruebas unitarias de generacion y validacion de tokens JWT.
- `GlobalExceptionHandlerTest.java`: pruebas unitarias del manejador de excepciones de conflicto.
- `CommonExceptionHandlerTest.java`: pruebas unitarias de manejadores de validacion y errores comunes.
- `FunkomaniaApiApplicationTests.java`: prueba de carga del contexto de Spring Boot.

## 1) Levantar MySQL para tests (con inicializacion)

```powershell
docker compose -f docker-compose.test.yml up -d
```

El script `init.sql` solo se ejecuta la primera vez que se crea el volumen de la base de datos.

## 2) Ejecutar tests

```powershell
.\mvnw -q test
```

## 3) Detener Docker sin borrar datos

```powershell
docker compose -f docker-compose.test.yml down
```

## 4) Detener Docker y borrar volumenes (reinicializa la base de datos)

```powershell
docker compose -f docker-compose.test.yml down -v
```

## Nota sobre reinicializacion

Si necesitas volver a ejecutar el script `init.sql`, debes borrar el volumen con el comando del paso 4.
