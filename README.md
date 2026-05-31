# Funkomania Backend

Funkomania es proyecto de e-commerce de figuras Funko Pop! desarrollado con Spring Boot y MySQL. El proyecto en general
incluye tanto el backend como el frontend, pero este repositorio se centra exclusivamente en el desarrollo del backend.

## ¿Qué incluye Funkomania Backend?

Actualmente, Funkomania Backend `v0.1.0-SNAPSHOT-2` incluye las siguientes funcionalidades:

- API REST para registrar usuarios.
- Integración con MySQL para almacenamiento de datos.
- Configuración de seguridad con Spring Security.
- Documentación de la API con SpringDoc OpenAPI.
- Dockerización para facilitar el despliegue.

## Documentación de la API
La API REST de Funkomania Backend está documentada utilizando SpringDoc OpenAPI, lo que permite a los desarrolladores 
entender y utilizar fácilmente los endpoints disponibles. La documentación se puede acceder a través de la URL 
`/swagger-ui.html` una vez que el backend esté en funcionamiento y se puede realizar pruebas desde la misma página.

- Acceso a la documentación: http://localhost:8080/swagger-ui/index.html

## Instalación y Ejecución
Para descargar y ejecutar el proyecto, sigue estos pasos:
1. Descargar el proyecto abriendo un terminal y clonar el repositorio usando Git en un directorio:
   ```bash
   git clone https://github.com/JuanAlberticoHF/funkomania-backend.git --branch feature/auth-core
   ```
   - Esto descargará el proyecto en tu máquina local de la rama `feature/auth-core`, que es la rama de desarrollo actual.
2. Acceder al directorio del proyecto:
   ```bash
   cd funkomania-backend
   ```
3. Crear un archivo `.env` en la raíz del proyecto con las siguientes variables de entorno:
   ```
   SPRING_DATASOURCE_URL_DOCKER=jdbc:mysql://funkomania-db:3306/funkomania_db?createDatabaseIfNotExist=true
   SPRING_DATASOURCE_URL_IDE=jdbc:mysql://localhost:3306/funkomania_db?createDatabaseIfNotExist=true
   SPRING_DATASOURCE_USERNAME=root
   SPRING_DATASOURCE_PASSWORD=123456
   
   MYSQL_ROOT_PASSWORD=123456
   
   JWT_SECRET=7bf01f0db25d13bfe0aaae08631a75e97d44bb3f1b82e7b5a1cee71d3b4826a9
   ```
   - Este archivo es necesario para configurar la conexión a la base de datos MySQL y la clave secreta para JWT, tanto
   en el IDE como en Docker.
4. Generar el archivo JAR del proyecto usando Maven:
   ```bash
   ./mvnw package -D maven.test.skip
   ```
   - Esto compilará el proyecto y generará un archivo JAR en el directorio `target/`.
   - El flag `-D maven.test.skip` omitirá la ejecución de pruebas durante el proceso de empaquetado.
5. Ejecutar el proyecto usando Docker Compose:
   ```bash
   docker-compose up --build
   ```
   - Esto levantará tanto el contenedor de MySQL como el contenedor del backend de Funkomania.
   - El backend estará disponible en `http://localhost:8080` y la base de datos MySQL en el puerto 3307 para evitar conflictos con la instalación local.
   - La base de datos MySQL en Docker estará disponible en `localhost:3306` con el nombre `funkomania_db`.