# Funkomania Backend

> Versión: 1.0.1

Funkomania es proyecto de e-commerce de figuras Funko Pop! desarrollado con Spring Boot y MySQL. El proyecto en general
incluye tanto el backend como el frontend, pero este repositorio se centra exclusivamente en el desarrollo del backend.

Enlaces a otros repositorios:
- [Repositorio Frontend](https://github.com/ViciBh/funkomania-frontend)
- [Repositorio Global](https://github.com/JuanAlberticoHF/funkomania)

## Funcionalidades & Características

- Autenticación de usuarios:
    - Registro de usuarios, login y logout.
- Catálogo de productos:
    - Listado de productos con paginación y filtrado por categoría, precio y nombre.
    - Listado de categorías y subcategorías.
    - Listado de productos en oferta.
- Gestión del perfil de usuario:
    - Visualización y edición de datos del perfil (nombre, apellidos y telefono).
    - Visualizar, añadir nuevas y editar direcciones existentes. 
    - Visualización y lectura de notificaciones.
    - Gestión lista de deseos (añadir, eliminar y listar productos).
- Carrito & Checkout:
    - CRUD productos en el carrito.
    - Proceso de checkout que crea un pedido con los productos del carrito, calcula el total y lo marca como pendiente de pago (simulado).
    - Métodos de pago permitidos (simulados)
    - Activación automática de direcciones de envío.
- Gestión de pedidos:
    - Listado de pedidos del usuario.
    - Visualización de detalles de un pedido (productos, total, estado y dirección de envío).
    - Cancelación de pedidos.
- Administrador:
    - Gestión de productos (CRUD).
    - Gestión de categorías (CRUD).
    - Gestión de pedidos (crear pedido y sus líneas, listado, detalles, actualización y eliminación detalles).
    - Listado de usuarios y sus direcciones.
    - Alertas de stock bajo y productos agotados.
- Seguridad:
    - Autenticación y autorización basada en JWT.
    - Roles de usuario (USER, ADMIN).
    - Protección de endpoints según rol.
    - Manejo global de excepciones y validaciones.
- Despliegue contenedores:
    - Docker Compose para levantar MySQL y la aplicación backend.
- Documentación:
    - Documentación de la API con Swagger UI (autogenerado por Springdoc OpenAPI).

## Documentación de la API
La API REST de Funkomania Backend está documentada utilizando SpringDoc OpenAPI, lo que permite a los desarrolladores 
entender y utilizar fácilmente los endpoints disponibles. La documentación se puede acceder a través de la URL 
`/swagger-ui.html` una vez que el backend esté en funcionamiento y se puede realizar pruebas desde la misma página.

- Acceso a la documentación: http://localhost:8080/swagger-ui/index.html

## Requisitos previos
- Instalar [Git](https://git-scm.com/install/windows) para clonar el repositorio.
- Instalar [Docker](https://docs.docker.com/get-started/get-docker/) para el despliegue de contenedores.

## Instalación
Para descargar y ejecutar el proyecto, sigue estos pasos:
### 1. Clonar el repositorio
   ```bash
   git clone https://github.com/JuanAlberticoHF/funkomania-backend.git
   cd funkomania-backend
   ```
   - Esto descargará el proyecto en tu máquina local de la rama `main`, que es la rama de desarrollo actual.
### 2. Configurar variables de entorno
   Crear un archivo `.env` en la raíz del proyecto con las siguientes variables de entorno:
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
### 3. Generar el JAR del proyecto
   ```bash
   ./mvnw package -D maven.test.skip
   ```
   - Esto compilará el proyecto y generará un archivo JAR en el directorio `target/`.
   - El flag `-D maven.test.skip` omitirá la ejecución de pruebas durante el proceso de empaquetado.
### 4. Levantar los contenedores con Docker Compose
   ```bash
   docker-compose up --build
   ```
   - Esto levantará tanto el contenedor de MySQL como el contenedor del backend de Funkomania.
   - El backend estará disponible en `http://localhost:8080`.
   - La base de datos MySQL en Docker estará disponible en `localhost:3307` con el nombre `funkomania_db`.
   - Para eliminar los contenedores y volúmenes utilizar el comando: `docker-compose down -v`.