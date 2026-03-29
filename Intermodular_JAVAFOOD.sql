-- phpMyAdmin SQL Dump
-- version 5.2.1deb3
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost:3306
-- Tiempo de generación: 27-03-2026 a las 09:05:49
-- Versión del servidor: 8.0.45-0ubuntu0.24.04.1
-- Versión de PHP: 8.3.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `Intermodular_JAVAFOOD`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categorias`
--

CREATE TABLE `categorias` (
  `id_categoria` int NOT NULL,
  `nombre` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `categorias`
--

INSERT INTO `categorias` (`id_categoria`, `nombre`) VALUES
(1, 'Productos'),
(2, 'Bebidas'),
(3, 'Complementos'),
(4, 'Postres');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detalle_categoria`
--

CREATE TABLE `detalle_categoria` (
  `id_detalle` int NOT NULL,
  `descripcion` text,
  `id_categoria` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `detalle_categoria`
--

INSERT INTO `detalle_categoria` (`id_detalle`, `descripcion`, `id_categoria`) VALUES
(1, 'Refrescos y Aguas', 2),
(2, 'Cervezas y Alcohol', 2),
(3, 'Cafés e Infusiones', 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productos`
--

CREATE TABLE `productos` (
  `id_producto` int NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `precio` decimal(10,2) NOT NULL,
  `id_detalle` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `productos`
--

INSERT INTO `productos` (`id_producto`, `nombre`, `precio`, `id_detalle`) VALUES
(1, 'Agua Con Gas [0.75 cl]', 2.00, 1),
(2, 'Agua Natural [0.75 cl]', 1.80, 1),
(3, 'CocaCola', 2.50, 1),
(4, 'CocaCola Zero', 2.50, 1),
(5, 'CocaCola Zero Zero', 2.50, 1),
(6, 'CocaCola Light', 2.50, 1),
(7, 'Fanta de Naranja', 2.50, 1),
(8, 'Fanta de Limon', 2.50, 1),
(9, 'Aquarius de Limon', 3.00, 1),
(10, 'Aquarius de Naranja', 3.00, 1),
(11, 'Nestea', 3.00, 1),
(12, 'Sprite', 2.50, 1),
(13, 'Trina', 3.00, 1),
(14, 'Zumo de Melocoton', 2.00, 1),
(15, 'Zumo de Piña', 2.00, 1),
(16, 'Cerveza', 2.20, 2),
(17, 'Cerveza Sin Alcohol', 2.20, 2),
(18, 'Anis Tenis [Chupito]', 1.50, 2),
(19, 'Tinto de Verano', 3.20, 2),
(20, 'Tinto de Verano Sin Alcohol', 3.20, 2),
(21, 'Aigua de Valencia', 3.50, 2),
(22, 'Café Solo', 1.20, 3),
(23, 'Café Cortado', 1.40, 3),
(24, 'Café con Leche', 1.60, 3),
(25, 'Café Solo Descafeinado', 1.20, 3),
(26, 'Café Cortado Descafeinado', 1.40, 3),
(27, 'Café con Leche Descafeinado', 1.60, 3),
(28, 'Te Verde', 1.50, 3),
(29, 'Te Negro', 1.50, 3),
(30, 'Manzanilla', 1.50, 3);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `categorias`
--
ALTER TABLE `categorias`
  ADD PRIMARY KEY (`id_categoria`);

--
-- Indices de la tabla `detalle_categoria`
--
ALTER TABLE `detalle_categoria`
  ADD PRIMARY KEY (`id_detalle`),
  ADD KEY `fk_categoria_detalle` (`id_categoria`);

--
-- Indices de la tabla `productos`
--
ALTER TABLE `productos`
  ADD PRIMARY KEY (`id_producto`),
  ADD KEY `fk_detalle_productos` (`id_detalle`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `categorias`
--
ALTER TABLE `categorias`
  MODIFY `id_categoria` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `detalle_categoria`
--
ALTER TABLE `detalle_categoria`
  MODIFY `id_detalle` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `productos`
--
ALTER TABLE `productos`
  MODIFY `id_producto` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `detalle_categoria`
--
ALTER TABLE `detalle_categoria`
  ADD CONSTRAINT `fk_categoria_detalle` FOREIGN KEY (`id_categoria`) REFERENCES `categorias` (`id_categoria`) ON DELETE CASCADE;

--
-- Filtros para la tabla `productos`
--
ALTER TABLE `productos`
  ADD CONSTRAINT `fk_detalle_productos` FOREIGN KEY (`id_detalle`) REFERENCES `detalle_categoria` (`id_detalle`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
