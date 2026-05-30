# Tests con MySQL en Docker

Guia para ejecutar tests usando un MySQL levantado con Docker Compose y un script SQL de inicializacion.

## Requisitos

- Docker Desktop en ejecucion.
- El script SQL existe en `src/main/resources/init.sql`.

## Tests creados

- `AuthControllerIntegrationTest.java`: pruebas de integracion del endpoint de registro con MockMvc (alta correcta, conflicto por email duplicado y validacion de cuerpo invalido).
- `AuthServiceImplTest.java`: pruebas unitarias del servicio de autenticacion (registro, excepcion por email duplicado y verificacion de existencia).
- `IUsuarioRepositoryTest.java`: pruebas de integracion del repositorio para `existsByEmail`.
- `UsuarioTest.java`: prueba unitaria del constructor de `Usuario` desde `UsuarioRegistroDTO`.
- `UsuarioDTOIdTest.java`: prueba unitaria del mapeo de entidad a `UsuarioDTOId`.
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
