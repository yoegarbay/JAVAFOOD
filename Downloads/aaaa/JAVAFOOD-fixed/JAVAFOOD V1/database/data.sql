-- Estructura de tabla para la tabla `categorias`
CREATE TABLE `categorias` (
  `id_categoria` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  PRIMARY KEY (`id_categoria`)
);

-- Estructura de tabla para la tabla `detalle_categoria`
CREATE TABLE `detalle_categoria` (
  `id_detalle` int NOT NULL AUTO_INCREMENT,
  `descripcion` text,
  `id_categoria` int NOT NULL,
  PRIMARY KEY (`id_detalle`),
  KEY `fk_categoria_detalle` (`id_categoria`),
  CONSTRAINT `fk_categoria_detalle` FOREIGN KEY (`id_categoria`) REFERENCES `categorias` (`id_categoria`) ON DELETE CASCADE
);

-- Estructura de tabla para la tabla `productos`
CREATE TABLE `productos` (
  `id_producto` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `precio` decimal(10,2) NOT NULL,
  `id_detalle` int DEFAULT NULL,
  PRIMARY KEY (`id_producto`),
  KEY `fk_detalle_productos` (`id_detalle`),
  CONSTRAINT `fk_detalle_productos` FOREIGN KEY (`id_detalle`) REFERENCES `detalle_categoria` (`id_detalle`)
);

-- Estructura de tabla para la tabla `pedidos`
CREATE TABLE `pedidos` (
  `id_pedido` int NOT NULL AUTO_INCREMENT,
  `fecha` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `total` decimal(10,2) NOT NULL,
  `estado` varchar(50) NOT NULL DEFAULT 'PAGADO',
  PRIMARY KEY (`id_pedido`)
);

-- Estructura de tabla para la tabla `pedido_detalle`
CREATE TABLE `pedido_detalle` (
  `id_linea` int NOT NULL AUTO_INCREMENT,
  `id_pedido` int NOT NULL,
  `nombre_producto` varchar(200) NOT NULL,
  `cantidad` int NOT NULL,
  `precio_unitario` decimal(10,2) NOT NULL,
  `subtotal` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id_linea`),
  KEY `fk_pedido_detalle_cabecera` (`id_pedido`),
  CONSTRAINT `fk_pedido_detalle_cabecera` FOREIGN KEY (`id_pedido`) REFERENCES `pedidos` (`id_pedido`) ON DELETE CASCADE
);