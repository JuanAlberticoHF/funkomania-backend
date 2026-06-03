CREATE DATABASE IF NOT EXISTS funkomania_db;

USE funkomania_db;

CREATE TABLE IF NOT EXISTS Usuario (
    idUsuario BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    passwordHash VARCHAR(255) NOT NULL,
    Nombre VARCHAR(50) NOT NULL,
    Apellido1 VARCHAR(50) NULL,
    Apellido2 VARCHAR(50) NULL,
    Telefono VARCHAR(20) NULL,
    FechaRegistro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UltimoLogin DATETIME NULL,
    Rol ENUM('CLIENTE', 'ADMIN') NOT NULL DEFAULT 'CLIENTE',
    Activo TINYINT(1) NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS Carrito (
    idCarrito BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    idUsuario BIGINT UNSIGNED NOT NULL UNIQUE,
    FechaCreacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FechaActualizacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    Estado ENUM('activo', 'abandonado') NOT NULL DEFAULT 'activo',

    CONSTRAINT fk_carrito_usuario FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario)  ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Direccion (
    idDireccion BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    idUsuario BIGINT UNSIGNED NOT NULL,
    Calle VARCHAR(120) NOT NULL,
    Numero VARCHAR(10) NOT NULL,
    Piso VARCHAR(10) NULL,
    Puerta VARCHAR(10) NULL,
    Ciudad VARCHAR(100) NOT NULL,
    Municipio VARCHAR(100) NOT NULL,
    Provincia VARCHAR(100) NOT NULL,
    CP VARCHAR(10) NOT NULL,
    Activo TINYINT(1) NOT NULL DEFAULT 1,

    CONSTRAINT fk_direccion_usuario FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Metodo_Pago (
    idMetodoPago BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(50) UNIQUE NOT NULL,
    Activo TINYINT(1) NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS Pedido (
    idPedido BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    CodigoPedido VARCHAR(30) NOT NULL UNIQUE,
    idUsuario BIGINT UNSIGNED NOT NULL,
    FechaPedido DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    EstadoPedido ENUM('pendiente', 'procesando', 'enviado', 'entregado', 'cancelado') NOT NULL DEFAULT 'pendiente',
    EstadoPago ENUM('pendiente', 'pagado', 'rechazado') NOT NULL DEFAULT 'pendiente',
    idDireccion BIGINT UNSIGNED NOT NULL,
    idMetodoPago BIGINT UNSIGNED NOT NULL,
    Comentarios TEXT NULL,
    UltimaModif DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_pedido_usuario FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario) ON DELETE RESTRICT,
    CONSTRAINT fk_pedido_direccion FOREIGN KEY (idDireccion) REFERENCES Direccion(idDireccion) ON DELETE RESTRICT,
    CONSTRAINT fk_pedido_metodo_pago FOREIGN KEY (idMetodoPago) REFERENCES Metodo_Pago(idMetodoPago) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS Categoria (
    idCategoria BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(50) NOT NULL,
    CategoriaPadre BIGINT UNSIGNED NULL,

    CONSTRAINT fk_categoria_padre FOREIGN KEY (CategoriaPadre) REFERENCES Categoria(idCategoria) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS Producto (
    idProducto BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(150) NOT NULL,
    Precio DECIMAL(10,2) NOT NULL,
    Stock INT NOT NULL DEFAULT 0,
    Image VARCHAR(255) NULL,
    Descripcion TEXT NULL,
    idCategoria BIGINT UNSIGNED NOT NULL,
    iva DECIMAL(5,2) NOT NULL,
    Activo TINYINT(1) NOT NULL DEFAULT 1,
    EnOferta TINYINT(1) NOT NULL DEFAULT 0,
    Descuento DECIMAL(5,2) NOT NULL DEFAULT 0,
    FechaFinOferta DATETIME NULL,

    CONSTRAINT fk_producto_categoria FOREIGN KEY (idCategoria) REFERENCES Categoria(idCategoria) ON DELETE RESTRICT,
    CONSTRAINT productoStock CHECK (Stock >= 0),
    CONSTRAINT productoPrecio CHECK (Precio >= 0),
    CONSTRAINT producto_iva CHECK (iva >= 0 AND iva < 100),
    CONSTRAINT producto_descuento CHECK (Descuento >= 0 AND Descuento <= 90)
);

CREATE TABLE IF NOT EXISTS Lista_Deseos (
    idUsuario BIGINT UNSIGNED NOT NULL,
    idProducto BIGINT UNSIGNED NOT NULL,

    PRIMARY KEY (idUsuario, idProducto),
    CONSTRAINT fk_listaDeseo_usuario  FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario) ON DELETE CASCADE,
    CONSTRAINT fk_listaDeseo_producto FOREIGN KEY (idProducto) REFERENCES Producto(idProducto) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Detalle_Pedido (
    idPedido BIGINT UNSIGNED NOT NULL,
    idProducto BIGINT UNSIGNED NOT NULL,
    precioUnitario DECIMAL(10,2) NOT NULL,
    cantidad INT NOT NULL,
    iva DECIMAL(5,2) NOT NULL,

    PRIMARY KEY (idPedido, idProducto),
    CONSTRAINT fk_detalle_pedido_pedido FOREIGN KEY (idPedido) REFERENCES Pedido(idPedido) ON DELETE CASCADE,
    CONSTRAINT fk_detalle_pedido_producto FOREIGN KEY (idProducto) REFERENCES Producto(idProducto) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS Detalle_Carrito (
    idCarrito BIGINT UNSIGNED NOT NULL,
    idProducto BIGINT UNSIGNED NOT NULL,
    cantidad INT NOT NULL DEFAULT 1,

    PRIMARY KEY (idCarrito, idProducto),
    CONSTRAINT fk_detalle_carrito_carrito FOREIGN KEY (idCarrito) REFERENCES Carrito(idCarrito) ON DELETE CASCADE,
    CONSTRAINT fk_detalle_carrito_producto FOREIGN KEY (idProducto) REFERENCES Producto(idProducto) ON DELETE RESTRICT,
    CONSTRAINT detalle_carrito_cantidad CHECK (cantidad >= 1)
);

CREATE TABLE IF NOT EXISTS Notificacion (
    idNotificacion BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    idUsuario BIGINT UNSIGNED NOT NULL,
    tipo ENUM('registro', 'compra', 'estado_pedido', 'carrito_abandonado', 'pago_error', 'wishlist_stock', 'bienvenida') NOT NULL,
    estado ENUM('pendiente', 'enviada', 'leida') NOT NULL DEFAULT 'pendiente',

    CONSTRAINT fk_notificacion_usuario FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario) ON DELETE CASCADE
    );

-- Formulas
DROP FUNCTION IF EXISTS fn_precio_con_iva;
DROP FUNCTION IF EXISTS fn_subtotal_linea;
DROP FUNCTION IF EXISTS fn_precio_con_descuento;

DELIMITER //

CREATE FUNCTION fn_precio_con_iva(
    p_precio DECIMAL(10,2),
    p_iva DECIMAL(5,2)
)
    RETURNS DECIMAL(14,4)
    DETERMINISTIC
BEGIN
RETURN p_precio * (1 + p_iva / 100);
END //

CREATE FUNCTION fn_subtotal_linea(
    p_precio DECIMAL(10,2),
    p_cantidad INT,
    p_iva DECIMAL(5,2)
)
    RETURNS DECIMAL(20,4)
    DETERMINISTIC
BEGIN
    RETURN (p_precio * p_cantidad) * (1 + p_iva / 100);
END //

CREATE FUNCTION fn_precio_con_descuento(
    p_precio DECIMAL(10,2),
    p_enOferta TINYINT,
    p_descuento DECIMAL(5,2),
    p_fechaFinOferta DATETIME
)
    RETURNS DECIMAL(10,2)
    DETERMINISTIC
BEGIN
    IF p_enOferta = 1 AND p_descuento > 0 AND (p_fechaFinOferta IS NULL OR p_fechaFinOferta >= NOW())
    THEN RETURN ROUND(p_precio - (p_precio * p_descuento / 100), 2);
    END IF;
    RETURN p_precio;
END //

DELIMITER ;



-- View

-- ----------------------Usuario---------------------------

CREATE OR REPLACE VIEW VUsuario_Perfil_Cliente AS
SELECT
    u.idUsuario,
    u.email,
    u.Nombre,
    CONCAT(u.Apellido1, IFNULL(CONCAT(' ', u.Apellido2), '')) AS Apellidos,
    u.Telefono,
    u.FechaRegistro,
    (SELECT CONCAT( d.Calle, ' ', d.Numero, IFNULL(CONCAT(', Piso ', d.Piso), ''), IFNULL(CONCAT(', Puerta ', d.Puerta), ''), ' - ', d.Ciudad) FROM Direccion d
     WHERE d.idUsuario = u.idUsuario AND d.Activo = 1
     ORDER BY d.idDireccion DESC
                                                                 LIMIT 1) AS DireccionPrincipal,
    (SELECT COUNT(*) FROM Pedido p WHERE p.idUsuario = u.idUsuario) AS CantidadPedidos,
    (SELECT ROUND(IFNULL(SUM(fn_subtotal_linea(dp.precioUnitario, dp.cantidad, dp.iva)), 0), 2)
     FROM Detalle_Pedido dp
     JOIN Pedido p ON dp.idPedido = p.idPedido
     WHERE p.idUsuario = u.idUsuario AND p.EstadoPago = 'pagado') AS TotalGastado
FROM Usuario u
WHERE u.Activo = 1 AND u.Rol = 'cliente';

-- ---------------Carrito------------------


CREATE OR REPLACE VIEW VCarrito_Contenido AS
SELECT
    c.idUsuario,
    dc.idCarrito,
    p.idProducto,
    p.Nombre AS Producto,
    p.Image,
    dc.cantidad,
    p.Precio AS PrecioOriginal_SinIVA,
    p.EnOferta,
    p.FechaFinOferta,
    p.Descuento,

    fn_precio_con_descuento(p.Precio, p.EnOferta,  p.Descuento,  p.FechaFinOferta) AS PrecioUnitario_SinIVA,
    p.iva AS IVA_Porcentaje,
    ROUND( fn_precio_con_iva( fn_precio_con_descuento(p.Precio, p.EnOferta, p.Descuento, p.FechaFinOferta), p.iva), 2) AS PrecioUnitario_ConIVA,
    ROUND( fn_subtotal_linea( fn_precio_con_descuento(p.Precio, p.EnOferta, p.Descuento, p.FechaFinOferta), dc.cantidad, p.iva), 2) AS Subtotal_Posicion
FROM Detalle_Carrito dc
         JOIN Producto p ON dc.idProducto = p.idProducto
         JOIN Carrito c ON dc.idCarrito = c.idCarrito
WHERE c.Estado = 'activo';


CREATE OR REPLACE VIEW VCarrito_Totales AS
SELECT
    idCarrito,
    idUsuario,
    COUNT(idProducto) AS Total_Articulos_Diferentes,
    SUM(cantidad) AS Total_Unidades_Fisicas,
    ROUND(SUM(PrecioUnitario_SinIVA * cantidad), 2) AS Base_Imponible,
    ROUND(SUM(Subtotal_Posicion), 2) AS Total_A_Pagar
FROM VCarrito_Contenido
GROUP BY idCarrito, idUsuario;





-- ----------------------Pedidos---------------------------

CREATE OR REPLACE VIEW VHistorial_Pedidos_Usuario AS
SELECT
    p.idPedido,
    p.CodigoPedido,
    p.idUsuario,
    p.FechaPedido,
    p.EstadoPedido,
    p.EstadoPago,
    mp.Nombre AS MetodoPago,
    CONCAT(
            d.Calle, ' ', d.Numero,
            IFNULL(CONCAT(', Piso ', d.Piso), ''),
            IFNULL(CONCAT(', Puerta ', d.Puerta), ''),
            ', ', d.CP,
            ', ', d.Ciudad,
            ' (', d.Provincia, ')'
    ) AS DireccionEnvio,
    (SELECT ROUND(IFNULL(SUM(fn_subtotal_linea(dp.precioUnitario, dp.cantidad, dp.iva)), 0), 2)
     FROM Detalle_Pedido dp WHERE dp.idPedido = p.idPedido) AS TotalPedido
FROM Pedido p
         JOIN Metodo_Pago mp ON p.idMetodoPago = mp.idMetodoPago
         JOIN Direccion d ON p.idDireccion = d.idDireccion;

CREATE OR REPLACE VIEW VDetalle_Pedido AS
SELECT
    dp.idPedido,
    pe.CodigoPedido,
    dp.idProducto,
    p.Nombre AS NombreProducto,
    dp.cantidad,
    dp.precioUnitario,
    dp.iva AS IVA_Porcentaje,
    ROUND(fn_subtotal_linea(dp.precioUnitario, dp.cantidad, dp.iva), 2) AS Subtotal_Linea
FROM Detalle_Pedido dp
         JOIN Producto p ON dp.idProducto = p.idProducto
         JOIN Pedido pe ON dp.idPedido = pe.idPedido;

CREATE OR REPLACE VIEW VPedido_Totales AS
SELECT
    p.idPedido,
    p.idUsuario,
    COUNT(dp.idProducto) AS Cantidad_Articulos_Diferentes,
    SUM(dp.cantidad) AS Total_Unidades_Fisicas,
    ROUND(SUM(dp.precioUnitario * dp.cantidad), 2) AS Base_Imponible,
    ROUND(SUM(fn_subtotal_linea(dp.precioUnitario, dp.cantidad, dp.iva)), 2) AS Total_Con_IVA
FROM Pedido p
         JOIN Detalle_Pedido dp ON p.idPedido = dp.idPedido
GROUP BY p.idPedido, p.idUsuario;

CREATE OR REPLACE VIEW VPedidos_Admin AS
SELECT
    p.idPedido,
    p.CodigoPedido,
    u.email AS Email_Usuario,
    CONCAT(u.Nombre, ' ', u.Apellido1, IFNULL(CONCAT(' ', u.Apellido2), '')) AS Nombre_Usuario,
    p.FechaPedido,
    p.EstadoPedido,
    p.EstadoPago,
    mp.Nombre AS Metodo_Pago
FROM Pedido p
         JOIN Usuario u ON p.idUsuario = u.idUsuario
         JOIN Metodo_Pago mp ON p.idMetodoPago = mp.idMetodoPago;

-- ---------------------- Producto ---------------------------

CREATE OR REPLACE VIEW VProductos_Catalogo AS
SELECT
    p.idProducto,
    p.Nombre,
    p.Precio AS PrecioOriginal_SinIVA,
    ROUND(fn_precio_con_iva(p.Precio, p.iva), 2) AS PrecioOriginal_ConIVA,
    p.EnOferta,
    p.Descuento,
    p.FechaFinOferta,
    fn_precio_con_descuento(p.Precio, p.EnOferta, p.Descuento, p.FechaFinOferta) AS PrecioFinal_SinIVA,
    ROUND(fn_precio_con_iva(fn_precio_con_descuento(p.Precio, p.EnOferta, p.Descuento, p.FechaFinOferta), p.iva), 2) AS PrecioFinal_ConIVA,
    p.iva,
    p.Stock,
    p.Image,
    p.Descripcion,
    p.Activo,
    p.idCategoria,
    c1.Nombre AS NombreCategoria,
    c2.Nombre AS NombreCategoriaPadre
FROM Producto p
         JOIN Categoria c1 ON p.idCategoria = c1.idCategoria
         LEFT JOIN Categoria c2 ON c1.CategoriaPadre = c2.idCategoria;

CREATE OR REPLACE VIEW VProductos_Ofertas AS
SELECT * FROM VProductos_Catalogo
WHERE EnOferta = 1 AND Descuento > 0 AND (FechaFinOferta IS NULL OR FechaFinOferta >= NOW()) AND Activo = 1;

CREATE OR REPLACE VIEW VAdmin_Alertas_Stock AS
SELECT
    idProducto,
    Nombre,
    Stock,
    idCategoria,
    CASE
        WHEN Stock = 0 THEN 'Agotado'
        WHEN Stock <= 5 THEN 'Critico'
        ELSE 'Bajo'
        END AS Prioridad
FROM Producto
WHERE Stock <= 10 AND Activo = 1;

-- ---------------------- Notificaciones ---------------------------

CREATE OR REPLACE VIEW VNotificaciones_Usuarios AS
SELECT
    n.idNotificacion,
    n.idUsuario,
    n.tipo,
    n.estado,
    CASE
        WHEN n.tipo = 'registro' THEN CONCAT('Hola, ', u.Nombre, '. Tu cuenta ha sido creada correctamente. Ya puedes iniciar sesión y empezar a explorar nuestros productos.')

        WHEN n.tipo = 'bienvenida' THEN  CONCAT('¡Bienvenido/a a Funkomanía, ', u.Nombre, '! Descubre nuestro catálogo, guarda tus favoritos y no te pierdas las novedades.')

        WHEN n.tipo = 'compra' THEN  CONCAT('Hola, ', u.Nombre, '. Hemos recibido tu pedido correctamente y ya está siendo procesado. Te avisaremos cuando haya cambios en su estado.')

        WHEN n.tipo = 'estado_pedido' THEN CONCAT('Hola, ', u.Nombre, '. El estado de tu pedido ha sido actualizado. Puedes consultar los detalles en tu área de usuario.')

        WHEN n.tipo = 'carrito_abandonado' THEN CONCAT('Hola, ', u.Nombre, '. Tienes productos en tu carrito pendientes de compra. ¡No te quedes sin ellos!')

        WHEN n.tipo = 'pago_error' THEN CONCAT('Hola, ', u.Nombre, '. Se ha producido un error al procesar tu pago. Revisa los datos e inténtalo de nuevo.')

        WHEN n.tipo = 'wishlist_stock' THEN CONCAT('Hola, ', u.Nombre, '. Uno de los productos de tu lista de deseos vuelve a estar disponible. ¡Aprovecha antes de que se agote!')

        ELSE
            CONCAT('Hola, ', u.Nombre, '. Tienes una nueva notificación en tu cuenta.')
        END AS Mensaje
FROM Notificacion n
         JOIN Usuario u ON n.idUsuario = u.idUsuario;


-- --------------------------PROCEDURE---------------------------

DELIMITER //

DROP PROCEDURE IF EXISTS sp_crear_pedido_desde_carrito//

CREATE PROCEDURE sp_crear_pedido_desde_carrito(
    IN p_idUsuario INT UNSIGNED,
    IN p_idDireccion INT UNSIGNED,
    IN p_idMetodoPago INT UNSIGNED,
    IN p_comentarios TEXT
)
BEGIN
    DECLARE v_idCarrito INT UNSIGNED;
    DECLARE v_idPedido INT UNSIGNED;
    DECLARE v_codigoPedido VARCHAR(30);
    DECLARE v_itemCount INT DEFAULT 0;
    DECLARE v_existe INT DEFAULT 0;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN
ROLLBACK;
RESIGNAL;
END;

START TRANSACTION;

-- Validar usuario activo
SELECT COUNT(*) INTO v_existe FROM Usuario WHERE idUsuario = p_idUsuario AND Activo = 1;

IF v_existe = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El usuario no existe o esta inactivo.';
END IF;

    -- Validar direccion activa del usuario
SELECT COUNT(*) INTO v_existe FROM Direccion WHERE idDireccion = p_idDireccion
                                               AND idUsuario = p_idUsuario AND Activo = 1;

IF v_existe = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La direccion no es valida para este usuario.';
END IF;

    -- Validar metodo de pago activo
SELECT COUNT(*) INTO v_existe FROM Metodo_Pago
WHERE idMetodoPago = p_idMetodoPago AND Activo = 1;

IF v_existe = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El metodo de pago no existe o esta inactivo.';
END IF;

    -- Buscar carrito activo
SELECT idCarrito INTO v_idCarrito FROM Carrito WHERE idUsuario = p_idUsuario AND Estado = 'activo'
    LIMIT 1;

IF v_idCarrito IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se encontro un carrito activo.';
END IF;

    -- Comprobar que el carrito no este vacio
SELECT COUNT(*) INTO v_itemCount FROM Detalle_Carrito WHERE idCarrito = v_idCarrito;

IF v_itemCount = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El carrito esta vacio.';
END IF;

    -- Comprobar productos inactivos o stock insuficiente
    IF EXISTS (
        SELECT 1 FROM Detalle_Carrito dc
        JOIN Producto p ON dc.idProducto = p.idProducto
        WHERE dc.idCarrito = v_idCarrito AND (p.Activo = 0 OR p.Stock < dc.cantidad)
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Hay productos inactivos o sin stock suficiente.';
END IF;

    -- Generar codigo de pedido
    SET v_codigoPedido = CONCAT('FM', UUID_SHORT());

    -- Crear pedido
INSERT INTO Pedido ( CodigoPedido, idUsuario, idDireccion, idMetodoPago, Comentarios, EstadoPedido, EstadoPago)
VALUES ( v_codigoPedido, p_idUsuario, p_idDireccion, p_idMetodoPago, p_comentarios, 'pendiente', 'pendiente');

SET v_idPedido = LAST_INSERT_ID();

    -- Copiar productos del carrito al detalle del pedido
INSERT INTO Detalle_Pedido (idPedido, idProducto, precioUnitario, cantidad, iva)
SELECT v_idPedido, vc.idProducto, vc.PrecioUnitario_SinIVA, vc.cantidad, vc.IVA_Porcentaje FROM VCarrito_Contenido vc
WHERE vc.idCarrito = v_idCarrito;

-- Descontar stock
UPDATE Producto p
    JOIN Detalle_Carrito dc ON p.idProducto = dc.idProducto
    SET p.Stock = p.Stock - dc.cantidad
WHERE dc.idCarrito = v_idCarrito;

-- Vaciar carrito
DELETE FROM Detalle_Carrito
WHERE idCarrito = v_idCarrito;

-- Crear notificacion
INSERT INTO Notificacion ( idUsuario, tipo, estado)
VALUES ( p_idUsuario, 'compra', 'pendiente');

COMMIT;

SELECT v_idPedido AS idPedidoCreado, v_codigoPedido AS Codigo;
END //




DROP PROCEDURE IF EXISTS sp_cancelar_pedido//

CREATE PROCEDURE sp_cancelar_pedido(
    IN p_idPedido INT UNSIGNED
)
BEGIN
    DECLARE v_idUsuario INT UNSIGNED;
    DECLARE v_estadoActual VARCHAR(20);
    DECLARE v_existe INT DEFAULT 0;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN
ROLLBACK;
RESIGNAL;
END;

START TRANSACTION;

SELECT COUNT(*) INTO v_existe FROM Pedido WHERE idPedido = p_idPedido;

IF v_existe = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Pedido no encontrado.';
END IF;

SELECT idUsuario, EstadoPedido INTO v_idUsuario, v_estadoActual FROM Pedido
WHERE idPedido = p_idPedido
    LIMIT 1;

IF v_estadoActual = 'cancelado' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El pedido ya fue cancelado.';
END IF;

    IF v_estadoActual = 'entregado' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'No se puede cancelar un pedido ya entregado.';
END IF;

    IF v_estadoActual = 'enviado' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'No se puede cancelar un pedido ya enviado.';
END IF;

UPDATE Pedido
SET EstadoPedido = 'cancelado', UltimaModif = CURRENT_TIMESTAMP
WHERE idPedido = p_idPedido;

UPDATE Producto p
    JOIN Detalle_Pedido dp ON p.idProducto = dp.idProducto
    SET p.Stock = p.Stock + dp.cantidad
WHERE dp.idPedido = p_idPedido;

INSERT INTO Notificacion ( idUsuario, tipo, estado)
VALUES (v_idUsuario, 'estado_pedido', 'pendiente');

COMMIT;

SELECT
    p_idPedido AS idPedidoCancelado,
    'cancelado' AS NuevoEstado,
    'Stock devuelto correctamente' AS Mensaje;
END //




DROP PROCEDURE IF EXISTS sp_activar_direccion_usuario//

CREATE PROCEDURE sp_activar_direccion_usuario(
    IN p_idUsuario INT UNSIGNED,
    IN p_idDireccion INT UNSIGNED
)
BEGIN
    DECLARE v_existe INT DEFAULT 0;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN
ROLLBACK;
RESIGNAL;
END;

START TRANSACTION;

-- Validar usuario activo
SELECT COUNT(*) INTO v_existe FROM Usuario
WHERE idUsuario = p_idUsuario AND Activo = 1;

IF v_existe = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El usuario no existe o esta inactivo.';
END IF;

    -- Validar que la direccion pertenece al usuario
SELECT COUNT(*) INTO v_existe FROM Direccion
WHERE idDireccion = p_idDireccion AND idUsuario = p_idUsuario;

IF v_existe = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La direccion no pertenece a este usuario.';
END IF;

    -- Desactivar todas las direcciones del usuario
UPDATE Direccion
SET Activo = 0
WHERE idUsuario = p_idUsuario;

-- Activar la direccion seleccionada
UPDATE Direccion
SET Activo = 1
WHERE idDireccion = p_idDireccion AND idUsuario = p_idUsuario;

COMMIT;

SELECT
    p_idUsuario AS idUsuario,
    p_idDireccion AS idDireccionActiva;
END //




DROP PROCEDURE IF EXISTS sp_agregar_producto_carrito//

CREATE PROCEDURE sp_agregar_producto_carrito(
    IN p_idUsuario INT UNSIGNED,
    IN p_idProducto INT UNSIGNED,
    IN p_cantidad INT
)
BEGIN
    DECLARE v_idCarrito INT UNSIGNED;
    DECLARE v_existe INT DEFAULT 0;
    DECLARE v_stock INT DEFAULT 0;
    DECLARE v_activoProducto TINYINT DEFAULT 0;
    DECLARE v_cantidadActual INT DEFAULT 0;
    DECLARE v_nuevaCantidad INT DEFAULT 0;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN
ROLLBACK;
RESIGNAL;
END;

START TRANSACTION;

-- Validar usuario activo
SELECT COUNT(*) INTO v_existe FROM Usuario WHERE idUsuario = p_idUsuario AND Activo = 1;

IF v_existe = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El usuario no existe o esta inactivo.';
END IF;

    -- Validar cantidad
    IF p_cantidad IS NULL OR p_cantidad < 1 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La cantidad debe ser mayor o igual a 1.';
END IF;

    -- Validar producto
SELECT COUNT(*) INTO v_existe FROM Producto
WHERE idProducto = p_idProducto;

IF v_existe = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El producto no existe.';
END IF;

SELECT Stock, Activo INTO v_stock, v_activoProducto FROM Producto
WHERE idProducto = p_idProducto
    LIMIT 1;

IF v_activoProducto = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El producto esta inactivo.';
END IF;

    IF v_stock < p_cantidad THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No hay stock suficiente.';
END IF;

    -- Buscar carrito del usuario
SELECT idCarrito INTO v_idCarrito FROM Carrito
WHERE idUsuario = p_idUsuario
    LIMIT 1;

-- Si no existe, crearlo
IF v_idCarrito IS NULL THEN
        INSERT INTO Carrito ( idUsuario, Estado)
        VALUES ( p_idUsuario, 'activo');

        SET v_idCarrito = LAST_INSERT_ID();
ELSE
        -- Si existe con estado abandonado - activarlo
UPDATE Carrito
SET Estado = 'activo', FechaActualizacion = CURRENT_TIMESTAMP
WHERE idCarrito = v_idCarrito;
END IF;

    -- Comprobar si el producto ya esta en el carrito
SELECT COUNT(*) INTO v_existe FROM Detalle_Carrito
WHERE idCarrito = v_idCarrito AND idProducto = p_idProducto;

IF v_existe > 0 THEN
SELECT cantidad INTO v_cantidadActual FROM Detalle_Carrito
WHERE idCarrito = v_idCarrito AND idProducto = p_idProducto
    LIMIT 1;

SET v_nuevaCantidad = v_cantidadActual + p_cantidad;

        IF v_nuevaCantidad > v_stock THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La cantidad total en carrito supera el stock disponible.';
END IF;

UPDATE Detalle_Carrito
SET cantidad = v_nuevaCantidad
WHERE idCarrito = v_idCarrito AND idProducto = p_idProducto;
ELSE
        INSERT INTO Detalle_Carrito (idCarrito, idProducto, cantidad)
        VALUES (v_idCarrito, p_idProducto, p_cantidad);

        SET v_nuevaCantidad = p_cantidad;
END IF;

    -- Actualizar fecha del carrito
UPDATE Carrito
SET FechaActualizacion = CURRENT_TIMESTAMP
WHERE idCarrito = v_idCarrito;

COMMIT;

-- Resultado para backend
SELECT
    v_idCarrito AS idCarrito,
    p_idProducto AS idProducto,
    v_nuevaCantidad AS CantidadFinal;
END //

DELIMITER ;


-- --------------------------INDEXES---------------------------

-- Producto: catálogo y disponibilidad
CREATE INDEX idx_producto_activo_stock ON Producto(Activo, Stock);
CREATE INDEX idx_producto_nombre ON Producto(Nombre);

-- Pedido: historial de usuario y panel admin
CREATE INDEX idx_pedido_usuario_fecha ON Pedido(idUsuario, FechaPedido);
CREATE INDEX idx_pedido_estado_fecha ON Pedido(EstadoPedido, FechaPedido);

-- Direccion: dirección activa del usuario
CREATE INDEX idx_direccion_usuario_activo ON Direccion(idUsuario, Activo);

-- Notificacion: notificaciones por usuario y estado
CREATE INDEX idx_notificacion_usuario_estado ON Notificacion(idUsuario, estado);

-- --------------------------REGISTROS---------------------------
-- Insertar Categorías
INSERT INTO Categoria (Nombre) VALUES ('Marvel');
INSERT INTO Categoria (Nombre, CategoriaPadre) VALUES ('DC Comics', 1);
INSERT INTO Categoria (Nombre, CategoriaPadre) VALUES ('Star Wars', 1);
INSERT INTO Categoria (Nombre) VALUES ('Anime');
INSERT INTO Categoria (Nombre, CategoriaPadre) VALUES ('Shonen', 4);
INSERT INTO Categoria (Nombre, CategoriaPadre) VALUES ('Seinen', 4);
INSERT INTO Categoria (Nombre) VALUES ('Videojuegos');
INSERT INTO Categoria (Nombre, CategoriaPadre) VALUES ('Nintendo', 7);
INSERT INTO Categoria (Nombre, CategoriaPadre) VALUES ('PlayStation', 7);
INSERT INTO Categoria (Nombre, CategoriaPadre) VALUES ('Xbox', 7);

-- Insertar Productos
INSERT INTO Producto (Nombre, Precio, Stock, Image, Descripcion, idCategoria, iva, Activo, EnOferta, Descuento, FechaFinOferta) VALUES
('Spider-Man Classic', 14.99, 120, 'funko_spiderman_classic.jpg', 'Figura Funko Pop de Spider-Man con traje clasico.', 1, 21.00, 1, 1, 10.00, '2026-12-31'),
('Iron Man Mark 85', 16.99, 80, 'funko_ironman_mk85.jpg', 'Funko Pop de Iron Man Mark 85 con detalles metalicos.', 1, 21.00, 1, 0, 0.00, NULL),
('Captain America Shield', 15.49, 70, 'funko_captain_america.jpg', 'Capitan America con escudo vibranium.', 1, 21.00, 1, 1, 12.50, '2026-10-31'),
('Doctor Strange', 14.49, 65, 'funko_doctor_strange.jpg', 'Doctor Strange con capa levitacion.', 1, 21.00, 1, 0, 0.00, NULL),
('Batman Detective', 14.99, 90, 'funko_batman_detective.jpg', 'Batman estilo detective con capa.', 2, 21.00, 1, 0, 0.00, NULL),
('Joker Classic', 13.99, 75, 'funko_joker_classic.jpg', 'Joker clasico con sonrisa iconica.', 2, 21.00, 1, 1, 15.00, '2026-11-30'),
('Wonder Woman Shield', 15.99, 60, 'funko_wonder_woman.jpg', 'Wonder Woman con escudo y espada.', 2, 21.00, 1, 0, 0.00, NULL),
('Darth Vader', 16.49, 110, 'funko_darth_vader.jpg', 'Darth Vader con sable rojo.', 3, 21.00, 1, 1, 8.00, '2026-09-30'),
('Grogu', 15.29, 150, 'funko_grogu.jpg', 'Grogu con taza y orejas grandes.', 3, 21.00, 1, 0, 0.00, NULL),
('Mandalorian', 16.29, 95, 'funko_mandalorian.jpg', 'Mandalorian con armadura beskar.', 3, 21.00, 1, 1, 9.50, '2026-08-31'),
('Goku Super Saiyan', 15.99, 140, 'funko_goku_ss.jpg', 'Goku en modo Super Saiyan.', 4, 21.00, 1, 0, 0.00, NULL),
('Naruto Sage', 15.59, 130, 'funko_naruto_sage.jpg', 'Naruto en modo sabio.', 4, 21.00, 1, 1, 11.00, '2026-12-15'),
('Luffy Gear 4', 16.79, 85, 'funko_luffy_gear4.jpg', 'Luffy Gear 4 con pose dinamica.', 5, 21.00, 1, 0, 0.00, NULL),
('Ichigo Hollow', 15.89, 60, 'funko_ichigo_hollow.jpg', 'Ichigo con mascara hollow.', 5, 21.00, 1, 1, 13.00, '2026-10-15'),
('Guts Berserker', 17.99, 50, 'funko_guts_berserker.jpg', 'Guts con armadura berserker.', 6, 21.00, 1, 0, 0.00, NULL),
('Levi Ackerman', 15.99, 70, 'funko_levi_ackerman.jpg', 'Levi con equipo de maniobras.', 6, 21.00, 1, 1, 10.00, '2026-11-15'),
('Mario Classic', 14.49, 160, 'funko_mario_classic.jpg', 'Mario con gorra roja clasica.', 7, 21.00, 1, 0, 0.00, NULL),
('Link BOTW', 16.49, 90, 'funko_link_botw.jpg', 'Link con arco de Breath of the Wild.', 8, 21.00, 1, 1, 12.00, '2026-10-01'),
('Samus Aran', 16.99, 55, 'funko_samus_aran.jpg', 'Samus con armadura power suit.', 8, 21.00, 1, 0, 0.00, NULL),
('Kratos', 17.49, 75, 'funko_kratos.jpg', 'Kratos con hacha leviathan.', 9, 21.00, 1, 1, 9.00, '2026-09-15'),
('Ellie', 15.49, 65, 'funko_ellie.jpg', 'Ellie de The Last of Us.', 9, 21.00, 1, 0, 0.00, NULL),
('Master Chief', 16.79, 100, 'funko_master_chief.jpg', 'Master Chief con armadura Mjolnir.', 10, 21.00, 1, 1, 8.50, '2026-12-01'),
('Marcus Fenix', 15.99, 40, 'funko_marcus_fenix.jpg', 'Marcus Fenix con armadura COG.', 10, 21.00, 1, 0, 0.00, NULL),
('Black Panther', 15.29, 85, 'funko_black_panther.jpg', 'Black Panther con traje vibranium.', 1, 21.00, 1, 1, 7.50, '2026-11-01'),
('Scarlet Witch', 15.69, 70, 'funko_scarlet_witch.jpg', 'Scarlet Witch con efectos magicos.', 1, 21.00, 1, 0, 0.00, NULL),
('Superman Classic', 14.89, 95, 'funko_superman_classic.jpg', 'Superman con capa roja.', 2, 21.00, 1, 1, 10.00, '2026-12-20'),
('Harley Quinn', 14.59, 85, 'funko_harley_quinn.jpg', 'Harley Quinn con mazo.', 2, 21.00, 1, 0, 0.00, NULL),
('Luke Skywalker', 15.79, 105, 'funko_luke_skywalker.jpg', 'Luke con sable verde.', 3, 21.00, 1, 1, 9.99, '2026-10-20'),
('R2-D2', 13.99, 130, 'funko_r2d2.jpg', 'R2-D2 con detalles clasicos.', 3, 21.00, 1, 0, 0.00, NULL),
('Tanjiro Kamado', 15.39, 120, 'funko_tanjiro.jpg', 'Tanjiro con katana y haori.', 4, 21.00, 1, 1, 11.50, '2026-09-10'),
('Nezuko', 15.19, 110, 'funko_nezuko.jpg', 'Nezuko con bozal de bambu.', 4, 21.00, 1, 0, 0.00, NULL);
