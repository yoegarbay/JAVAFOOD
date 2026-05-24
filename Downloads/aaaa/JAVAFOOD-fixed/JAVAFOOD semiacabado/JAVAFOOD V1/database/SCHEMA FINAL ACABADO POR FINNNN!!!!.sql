-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 24-05-2026 a las 21:01:12
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `javafoodfinalv2`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categorias`
--

CREATE TABLE `categorias` (
  `id_categoria` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `categorias`
--

INSERT INTO `categorias` (`id_categoria`, `nombre`) VALUES
(1, 'Productos'),
(2, 'Bebidas'),
(3, 'Complementos'),
(4, 'Postres'),
(5, 'Salsas'),
(6, 'Promociones');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cliente`
--

CREATE TABLE `cliente` (
  `id` int(11) NOT NULL,
  `puntos` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Volcado de datos para la tabla `cliente`
--

INSERT INTO `cliente` (`id`, `puntos`) VALUES
(3, 10033),
(4, 0),
(5, 104551),
(8, 104625),
(9, 86),
(10, 0),
(12, 0),
(13, 38),
(14, 5091),
(15, 0),
(16, 46),
(17, 5000),
(18, 60),
(19, 40),
(20, 4697);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clientes`
--

CREATE TABLE `clientes` (
  `id_cliente` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `email` varchar(150) NOT NULL,
  `password` varchar(255) NOT NULL,
  `puntos` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `clientes`
--

INSERT INTO `clientes` (`id_cliente`, `nombre`, `email`, `password`, `puntos`) VALUES
(1, 'Administrador', 'admin@javafood.com', 'admin1234', 0),
(2, 'Cliente Demo', 'cliente@javafood.com', '1234', 0),
(3, 'Miguel Angel', 'mavilches93@gmail.com', '1234', 0),
(5, 'Miguel Angel', 'mavilches933333@gmail.com', '12345678', 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detalle_categoria`
--

CREATE TABLE `detalle_categoria` (
  `id_detalle` int(11) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `id_categoria` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `detalle_categoria`
--

INSERT INTO `detalle_categoria` (`id_detalle`, `descripcion`, `id_categoria`) VALUES
(1, 'Refrescos y Aguas', 2),
(2, 'Cervezas y Alcohol', 2),
(3, 'Cafés e Infusiones', 2),
(4, 'Complementos y Sides', 3),
(5, 'Salsas y Dips', 5),
(6, 'Hamburguesas', 1),
(7, 'Bocatas', 1),
(8, 'Pizzas', 1),
(9, 'Promociones', 1),
(10, 'Postres y Dulces', 4),
(11, 'Productos Promo', 6);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empleados`
--

CREATE TABLE `empleados` (
  `id` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `iniciales` varchar(4) NOT NULL,
  `color` varchar(20) NOT NULL DEFAULT '#d17a22',
  `activo` tinyint(1) NOT NULL DEFAULT 1,
  `pin` varchar(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `empleados`
--

INSERT INTO `empleados` (`id`, `nombre`, `iniciales`, `color`, `activo`, `pin`) VALUES
(1, 'María García', 'MG', '#ffc107', 0, NULL),
(2, 'Carlos López', 'CL', '#64b5f6', 1, NULL),
(3, 'Ana Martínez', 'AM', '#bb8fce', 1, '111111'),
(4, 'JOSELU', 'J', '#ab47bc', 1, NULL),
(5, 'as', 'A', '#e74c3c', 0, NULL),
(6, 'Fernando', 'F', '#86694b', 1, NULL),
(7, 'adsaa', 'A', '#bb8fce', 0, NULL),
(8, 'ass', 'A', '#d17a22', 0, NULL),
(9, 'assss', 'A', '#d17a22', 0, NULL),
(10, 'asss', 'A', '#d17a22', 0, NULL),
(11, 'adsa', 'A', '#d17a22', 0, NULL),
(12, 'asadad', 'A', '#d17a22', 0, NULL),
(13, 'dasadsadsa', 'D', '#d17a22', 0, NULL),
(14, 'ads', 'A', '#d17a22', 0, NULL),
(15, 'adsadsadsa', 'A', '#d17a22', 0, NULL),
(16, 'adsadsasa', 'A', '#d17a22', 0, NULL),
(17, 'dsada', 'D', '#d17a22', 0, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `fichajes`
--

CREATE TABLE `fichajes` (
  `id` int(11) NOT NULL,
  `empleado_id` int(11) NOT NULL,
  `tipo` varchar(10) NOT NULL COMMENT 'entrada | salida',
  `fecha` date NOT NULL,
  `hora` time NOT NULL,
  `horas_calc` decimal(5,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `fichajes`
--

INSERT INTO `fichajes` (`id`, `empleado_id`, `tipo`, `fecha`, `hora`, `horas_calc`) VALUES
(1, 3, 'entrada', '2026-05-22', '18:41:35', NULL),
(2, 3, 'entrada', '2026-05-22', '18:41:36', NULL),
(3, 3, 'entrada', '2026-05-22', '18:41:36', NULL),
(4, 3, 'entrada', '2026-05-22', '18:41:36', NULL),
(5, 3, 'entrada', '2026-05-22', '18:41:37', NULL),
(6, 3, 'entrada', '2026-05-22', '18:41:37', NULL),
(7, 3, 'entrada', '2026-05-22', '18:41:37', NULL),
(8, 3, 'entrada', '2026-05-22', '18:41:41', NULL),
(9, 3, 'entrada', '2026-05-22', '18:41:41', NULL),
(10, 3, 'entrada', '2026-05-22', '18:41:41', NULL),
(11, 3, 'entrada', '2026-05-22', '18:41:41', NULL),
(12, 3, 'entrada', '2026-05-22', '18:41:41', NULL),
(13, 3, 'entrada', '2026-05-22', '18:41:41', NULL),
(14, 3, 'entrada', '2026-05-22', '18:41:41', NULL),
(15, 3, 'entrada', '2026-05-22', '18:41:41', NULL),
(16, 3, 'entrada', '2026-05-22', '18:41:42', NULL),
(17, 3, 'entrada', '2026-05-22', '18:41:42', NULL),
(18, 3, 'entrada', '2026-05-22', '18:41:42', NULL),
(19, 3, 'salida', '2026-05-22', '18:42:05', 0.00),
(20, 3, 'salida', '2026-05-22', '18:42:06', 0.00),
(21, 3, 'salida', '2026-05-22', '18:42:06', 0.00),
(22, 3, 'salida', '2026-05-22', '18:42:06', 0.00),
(23, 3, 'salida', '2026-05-22', '18:42:06', 0.00),
(24, 3, 'salida', '2026-05-22', '18:42:06', 0.00),
(25, 3, 'salida', '2026-05-22', '18:42:06', 0.00),
(26, 3, 'salida', '2026-05-22', '18:42:06', 0.00),
(27, 3, 'salida', '2026-05-22', '18:42:07', 0.00),
(28, 3, 'salida', '2026-05-22', '18:42:07', 0.00),
(29, 3, 'salida', '2026-05-22', '18:42:07', 0.00),
(30, 3, 'salida', '2026-05-22', '18:42:07', 0.00),
(31, 3, 'salida', '2026-05-22', '18:42:07', 0.00),
(32, 3, 'salida', '2026-05-22', '18:42:07', 0.00),
(33, 3, 'salida', '2026-05-22', '18:42:08', 0.00),
(34, 3, 'salida', '2026-05-22', '18:42:08', 0.00),
(35, 3, 'salida', '2026-05-22', '18:42:08', 0.00),
(36, 3, 'salida', '2026-05-22', '18:42:08', 0.00),
(37, 3, 'salida', '2026-05-22', '18:42:08', 0.00),
(38, 3, 'salida', '2026-05-22', '18:42:08', 0.00),
(39, 3, 'salida', '2026-05-22', '18:42:08', 0.00),
(40, 3, 'salida', '2026-05-22', '18:42:09', 0.00),
(41, 3, 'salida', '2026-05-22', '18:42:09', 0.00),
(42, 3, 'salida', '2026-05-22', '18:42:09', 0.00),
(43, 3, 'salida', '2026-05-22', '18:42:09', 0.00),
(44, 3, 'salida', '2026-05-22', '18:42:09', 0.00),
(45, 3, 'entrada', '2026-05-22', '18:42:10', NULL),
(46, 3, 'entrada', '2026-05-22', '18:42:10', NULL),
(47, 3, 'entrada', '2026-05-22', '18:42:10', NULL),
(48, 3, 'entrada', '2026-05-22', '18:42:10', NULL),
(49, 3, 'entrada', '2026-05-22', '18:42:10', NULL),
(50, 3, 'entrada', '2026-05-22', '18:42:10', NULL),
(51, 3, 'salida', '2026-05-22', '18:42:11', 0.00),
(52, 3, 'salida', '2026-05-22', '18:42:11', 0.00),
(53, 3, 'salida', '2026-05-22', '18:42:11', 0.00),
(54, 3, 'salida', '2026-05-22', '18:42:11', 0.00),
(55, 3, 'salida', '2026-05-22', '18:42:11', 0.00),
(56, 3, 'salida', '2026-05-22', '18:42:12', 0.00),
(57, 3, 'salida', '2026-05-22', '18:42:12', 0.00),
(58, 3, 'salida', '2026-05-22', '18:42:12', 0.00),
(59, 3, 'salida', '2026-05-22', '18:42:12', 0.00),
(60, 3, 'salida', '2026-05-22', '18:42:12', 0.00),
(61, 3, 'entrada', '2026-05-22', '18:42:13', NULL),
(62, 3, 'entrada', '2026-05-22', '18:42:13', NULL),
(63, 3, 'entrada', '2026-05-22', '18:42:13', NULL),
(64, 3, 'salida', '2026-05-22', '18:42:13', 0.00),
(65, 3, 'entrada', '2026-05-22', '18:42:14', NULL),
(66, 3, 'salida', '2026-05-22', '18:42:14', 0.00),
(67, 3, 'entrada', '2026-05-22', '18:42:14', NULL),
(68, 3, 'salida', '2026-05-22', '18:42:14', 0.00),
(69, 3, 'entrada', '2026-05-22', '18:42:14', NULL),
(70, 3, 'salida', '2026-05-22', '18:42:14', 0.00),
(71, 3, 'entrada', '2026-05-22', '18:42:15', NULL),
(72, 6, 'entrada', '2026-05-22', '18:42:16', NULL),
(73, 6, 'entrada', '2026-05-22', '18:42:16', NULL),
(74, 6, 'entrada', '2026-05-22', '18:42:16', NULL),
(75, 6, 'salida', '2026-05-22', '18:42:16', 0.00),
(76, 6, 'entrada', '2026-05-22', '18:42:17', NULL),
(77, 6, 'salida', '2026-05-22', '18:42:17', 0.00),
(78, 6, 'entrada', '2026-05-22', '18:42:17', NULL),
(79, 3, 'entrada', '2026-05-22', '18:44:41', NULL),
(80, 3, 'entrada', '2026-05-22', '18:44:42', NULL),
(81, 3, 'entrada', '2026-05-22', '18:45:32', NULL),
(82, 3, 'entrada', '2026-05-22', '18:45:33', NULL),
(86, 3, 'entrada', '2026-05-22', '18:51:56', NULL),
(87, 3, 'entrada', '2026-05-22', '18:51:56', NULL),
(88, 3, 'entrada', '2026-05-22', '18:51:57', NULL),
(89, 3, 'entrada', '2026-05-22', '18:51:57', NULL),
(90, 3, 'entrada', '2026-05-22', '18:52:09', NULL),
(91, 3, 'entrada', '2026-05-22', '18:52:10', NULL),
(92, 3, 'entrada', '2026-05-22', '18:52:10', NULL),
(93, 3, 'salida', '2026-05-22', '18:52:10', 0.00),
(94, 3, 'salida', '2026-05-22', '18:52:11', 0.00),
(95, 3, 'salida', '2026-05-22', '18:52:11', 0.00),
(96, 3, 'salida', '2026-05-22', '18:52:11', 0.00),
(97, 3, 'entrada', '2026-05-22', '18:52:12', NULL),
(98, 3, 'entrada', '2026-05-22', '18:52:13', NULL),
(99, 3, 'salida', '2026-05-22', '18:52:14', 0.00),
(100, 3, 'salida', '2026-05-22', '18:52:15', 0.00),
(101, 3, 'entrada', '2026-05-22', '18:52:17', NULL),
(102, 3, 'entrada', '2026-05-22', '20:03:06', NULL),
(103, 3, 'entrada', '2026-05-22', '20:12:44', NULL),
(104, 3, 'salida', '2026-05-22', '20:12:59', 0.00);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `horarios`
--

CREATE TABLE `horarios` (
  `id` int(11) NOT NULL,
  `empleado_id` int(11) NOT NULL,
  `anyo` int(11) NOT NULL,
  `mes` int(11) NOT NULL,
  `dia` int(11) NOT NULL,
  `turno_cod` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `horarios`
--

INSERT INTO `horarios` (`id`, `empleado_id`, `anyo`, `mes`, `dia`, `turno_cod`) VALUES
(1, 7, 2026, 5, 1, 'noche'),
(4, 3, 2026, 5, 1, 'noche'),
(7, 3, 2026, 5, 2, 'noche'),
(11, 2, 2026, 5, 2, 'noche'),
(12, 2, 2026, 5, 1, 'tarde'),
(13, 7, 2026, 5, 3, 'libre'),
(14, 2, 2026, 5, 7, 'noche'),
(16, 2, 2026, 5, 22, 'noche'),
(17, 3, 2026, 5, 22, 'noche');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pedidos`
--

CREATE TABLE `pedidos` (
  `id_pedido` int(11) NOT NULL,
  `fecha` datetime NOT NULL DEFAULT current_timestamp(),
  `total` decimal(10,2) NOT NULL,
  `estado` varchar(50) NOT NULL DEFAULT 'PAGADO',
  `nombre_cliente` varchar(100) NOT NULL DEFAULT 'Cliente',
  `metodo_pago` varchar(20) NOT NULL DEFAULT 'EFECTIVO',
  `id_cliente` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `pedidos`
--

INSERT INTO `pedidos` (`id_pedido`, `fecha`, `total`, `estado`, `nombre_cliente`, `metodo_pago`, `id_cliente`) VALUES
(1, '2026-05-22 14:13:19', 100.00, 'PAGADO', 'Cliente', 'EFECTIVO', NULL),
(2, '2026-05-22 18:15:49', 506.00, 'PAGADO', 'joselu', 'EFECTIVO', NULL),
(3, '2026-05-22 19:23:51', 1000.00, 'PAGADO', 'hola', 'EFECTIVO', NULL),
(4, '2026-05-22 19:33:47', 500.00, 'PAGADO', 'Miguel Angel', 'EFECTIVO', 3),
(5, '2026-05-22 20:04:00', 2.00, 'PAGADO', 'Miguel Angel', 'EFECTIVO', 3),
(6, '2026-05-24 12:44:01', 502.00, 'PAGADO', 'ass', 'EFECTIVO', NULL),
(7, '2026-05-24 13:40:24', 500.00, 'PAGADO', 'aasass', 'EFECTIVO', NULL),
(9, '2026-05-24 15:34:37', 500.00, 'PAGADO', 'Miguel Angel', 'EFECTIVO', 3),
(10, '2026-05-24 15:36:37', 1200.00, 'PAGADO', 'asss', 'EFECTIVO', NULL),
(11, '2026-05-24 15:56:52', 2.00, 'PAGADO', 'Miguel Angel', 'EFECTIVO', 5),
(12, '2026-05-24 16:04:47', 6000.00, 'PAGADO', 'Miguel Angel', 'EFECTIVO', 5),
(13, '2026-05-24 16:10:15', 60000.00, 'PAGADO', 'Miguel Angel', 'EFECTIVO', 5),
(14, '2026-05-24 16:10:47', 60.00, 'PAGADO', 'Miguel Angel', 'EFECTIVO', 5),
(15, '2026-05-24 16:15:11', 500.00, 'PAGADO', 'Miguel Angel', 'EFECTIVO', 5),
(16, '2026-05-24 16:18:35', 500.00, 'PAGADO', 'Miguel Angel', 'EFECTIVO', 5),
(17, '2026-05-24 16:19:30', 27.50, 'PAGADO', 'Miguel Angel', 'EFECTIVO', 5),
(18, '2026-05-24 16:23:53', 8.00, 'PAGADO', 'Miguel Angel', 'EFECTIVO', 5),
(19, '2026-05-24 16:25:42', 2.10, 'PAGADO', 'Miguel Angel', 'EFECTIVO', 5),
(20, '2026-05-24 16:26:59', 10000.00, 'PAGADO', 'Miguel Angel', 'EFECTIVO', 5),
(21, '2026-05-24 16:45:35', 500.00, 'PAGADO', 'Miguel Angel', 'EFECTIVO', 5),
(22, '2026-05-24 17:21:44', 0.70, 'PAGADO', 'Miguel Angel', 'EFECTIVO', 3),
(27, '2026-05-24 17:26:50', 500.50, 'PAGADO', 'as', 'EFECTIVO', NULL),
(28, '2026-05-24 17:27:15', 3.00, 'PAGADO', 'Miguel Angel', 'EFECTIVO', 5),
(30, '2026-05-24 17:33:25', 3.00, 'PAGADO', 'as', 'EFECTIVO', NULL),
(37, '2026-05-24 18:32:52', 0.60, 'PAGADO', 'Miguel Angel', 'EFECTIVO', NULL),
(38, '2026-05-24 18:33:27', 0.60, 'PAGADO', 'Miguel Angel', 'EFECTIVO', NULL),
(42, '2026-05-24 18:48:17', 0.70, 'PAGADO', 'as', 'EFECTIVO', NULL),
(43, '2026-05-24 18:48:30', 2.00, 'PAGADO', 'Miguel Angel', 'EFECTIVO', 3),
(46, '2026-05-24 18:51:34', 2.00, 'PAGADO', 'nuevo', 'EFECTIVO', NULL),
(48, '2026-05-24 18:52:55', 1.80, 'PAGADO', 'nuevo', 'EFECTIVO', NULL),
(50, '2026-05-24 18:57:56', 4.00, 'PAGADO', 'PRUEBA 3', 'EFECTIVO', NULL),
(52, '2026-05-24 18:59:41', 500.00, 'PAGADO', 'PRUEBA 3', 'EFECTIVO', NULL),
(53, '2026-05-24 19:02:58', 4.50, 'PAGADO', 'Cliente', 'EFECTIVO', NULL),
(55, '2026-05-24 19:06:13', 500.00, 'PAGADO', 'nuevo99', 'EFECTIVO', NULL),
(57, '2026-05-24 19:25:40', 2.00, 'PAGADO', 'PRUEBA 3', 'EFECTIVO', NULL),
(59, '2026-05-24 19:32:09', 2.00, 'PAGADO', 'PRUEBA 3', 'EFECTIVO', NULL),
(60, '2026-05-24 19:33:32', 5.50, 'PAGADO', 'Cliente', 'EFECTIVO', NULL),
(62, '2026-05-24 19:35:48', 0.60, 'PAGADO', 'PRUEBA 3', 'EFECTIVO', NULL),
(64, '2026-05-24 19:42:46', 0.00, 'PAGADO', 'PRUEBA 3', 'EFECTIVO', NULL),
(66, '2026-05-24 19:49:13', 0.60, 'PAGADO', 'DEFINITIVO', 'EFECTIVO', NULL),
(68, '2026-05-24 19:50:15', 4.00, 'PAGADO', 'DEFINITIVO', 'TARJETA', NULL),
(70, '2026-05-24 19:50:57', 500.00, 'PAGADO', 'SEGUNDO', 'EFECTIVO', NULL),
(71, '2026-05-24 19:55:22', -546.00, 'PAGADO', 'Cliente', 'EFECTIVO', NULL),
(73, '2026-05-24 19:59:09', 2.00, 'PAGADO', 'Miguel Angel', 'EFECTIVO', NULL),
(74, '2026-05-24 20:01:08', 4.00, 'PAGADO', 'Miguel Angel', 'EFECTIVO', 18),
(75, '2026-05-24 20:02:01', 4.00, 'PAGADO', 'Miguel Angel', 'EFECTIVO', 19),
(76, '2026-05-24 20:04:29', 3.50, 'PAGADO', 'resena', 'EFECTIVO', 20),
(77, '2026-05-24 20:06:42', 4.50, 'PAGADO', 'resena', 'EFECTIVO', 20),
(78, '2026-05-24 20:09:46', 3.50, 'PAGADO', 'resena', 'EFECTIVO', 20),
(79, '2026-05-24 20:10:09', 3.50, 'PAGADO', 'resena', 'EFECTIVO', 20),
(80, '2026-05-24 20:12:36', 500.00, 'PAGADO', 'resena', 'EFECTIVO', 20),
(81, '2026-05-24 20:12:44', 0.00, 'PAGADO', 'resena', 'EFECTIVO', 20),
(82, '2026-05-24 20:13:25', 1.40, 'PAGADO', 'resena', 'EFECTIVO', 20),
(83, '2026-05-24 20:15:03', 2.50, 'PAGADO', 'resena', 'EFECTIVO', 20),
(84, '2026-05-24 20:16:25', -0.80, 'PAGADO', 'Cliente', 'EFECTIVO', 20),
(86, '2026-05-24 20:22:24', 550.00, 'PAGADO', 'Cliente', 'EFECTIVO', NULL),
(88, '2026-05-24 20:31:36', 3.90, 'PAGADO', 'nuevo99', 'EFECTIVO', 14),
(89, '2026-05-24 20:32:36', 400.00, 'PAGADO', 'Cliente', 'EFECTIVO', NULL),
(90, '2026-05-24 20:32:57', 0.70, 'PAGADO', 'nuevo99', 'EFECTIVO', 14),
(91, '2026-05-24 20:33:13', 4.50, 'PAGADO', 'nuevo99', 'EFECTIVO', 14),
(92, '2026-05-24 20:46:08', 500.00, 'PAGADO', 'Miguel Angel', 'EFECTIVO', 3),
(93, '2026-05-24 20:47:53', 500.00, 'PAGADO', 'Miguel Angel', 'EFECTIVO', 3),
(94, '2026-05-24 20:53:27', 3.90, 'PAGADO', 'PRUEBA 3', 'EFECTIVO', 8),
(95, '2026-05-24 20:54:18', 10000.00, 'PAGADO', 'PRUEBA 3', 'EFECTIVO', 8);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pedido_detalle`
--

CREATE TABLE `pedido_detalle` (
  `id_linea` int(11) NOT NULL,
  `id_pedido` int(11) NOT NULL,
  `nombre_producto` varchar(200) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `precio_unitario` decimal(10,2) NOT NULL,
  `subtotal` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `pedido_detalle`
--

INSERT INTO `pedido_detalle` (`id_linea`, `id_pedido`, `nombre_producto`, `cantidad`, `precio_unitario`, `subtotal`) VALUES
(1, 1, 'asdsdads', -1, -100.00, 100.00),
(2, 2, 'Alitas (4 uds)', 1, 6.00, 6.00),
(3, 2, 'Patatas Fritas', 1, 500.00, 500.00),
(4, 3, 'Patatas Fritas', 1, 500.00, 500.00),
(5, 3, 'Patatas Fritas', 1, 500.00, 500.00),
(6, 4, 'Patatas Fritas', 1, 500.00, 500.00),
(7, 5, 'Mostaza', 4, 0.50, 2.00),
(8, 6, 'Agua Con Gas [0.75 cl]', 1, 2.00, 2.00),
(9, 6, 'Patatas Fritas', 1, 500.00, 500.00),
(10, 7, 'Patatas Fritas', 1, 500.00, 500.00),
(12, 9, 'Patatas Fritas', 1, 500.00, 500.00),
(13, 10, 'CheeseBurger', 1, 1200.00, 1200.00),
(14, 11, 'Agua Con Gas [0.75 cl]', 1, 2.00, 2.00),
(15, 12, 'lechedeburra', 1, 6000.00, 6000.00),
(16, 13, 'lechedeburra', 10, 6000.00, 60000.00),
(17, 14, 'Alitas (4 uds)', 10, 6.00, 60.00),
(18, 15, 'agua bendita', 1, 500.00, 500.00),
(19, 16, 'agua bendita', 1, 500.00, 500.00),
(20, 17, 'Bolas de queso con mermelada', 5, 5.50, 27.50),
(21, 18, 'Agua Con Gas [0.75 cl]', 4, 2.00, 8.00),
(22, 19, 'Alioli', 3, 0.70, 2.10),
(23, 20, 'AGUA PURIFICADA', 1, 10000.00, 10000.00),
(24, 21, 'agua bendita', 1, 500.00, 500.00),
(25, 21, '🎁 Patatas Fritas (Promo)', 1, 0.00, 0.00),
(26, 22, 'Alioli', 1, 0.70, 0.70),
(27, 27, 'Ketchup', 1, 0.50, 0.50),
(28, 27, 'agua bendita', 1, 500.00, 500.00),
(29, 28, 'Aquarius de Limon', 1, 3.00, 3.00),
(30, 30, 'Helado (3 bolas)', 1, 3.00, 3.00),
(31, 37, 'Barbacoa', 1, 0.60, 0.60),
(32, 38, 'Barbacoa', 1, 0.60, 0.60),
(35, 42, 'Alioli', 1, 0.70, 0.70),
(36, 43, 'Agua Con Gas [0.75 cl]', 1, 2.00, 2.00),
(37, 46, 'Agua Con Gas [0.75 cl]', 1, 2.00, 2.00),
(38, 48, 'Agua Natural [0.75 cl]', 1, 1.80, 1.80),
(39, 50, 'Brownie con Helado', 1, 4.00, 4.00),
(40, 52, 'agua bendita', 1, 500.00, 500.00),
(41, 53, 'Bocata Vegetal', 1, 4.50, 4.50),
(42, 55, 'agua bendita', 1, 500.00, 500.00),
(43, 57, 'Agua Con Gas [0.75 cl]', 1, 2.00, 2.00),
(44, 59, 'Agua Con Gas [0.75 cl]', 1, 2.00, 2.00),
(45, 60, 'Bolas de queso con mermelada', 1, 5.50, 5.50),
(46, 62, 'Barbacoa', 1, 0.60, 0.60),
(47, 64, '🎁 Patatas Fritas (Promo)', 1, 0.00, 0.00),
(48, 66, 'Barbacoa', 1, 0.60, 0.60),
(49, 68, 'Brownie con Helado', 1, 4.00, 4.00),
(50, 70, 'agua bendita', 1, 500.00, 500.00),
(51, 71, 'Bocata de Pollo', -100, 5.50, -550.00),
(52, 71, 'Brownie con Helado', 1, 4.00, 4.00),
(53, 73, 'Agua Con Gas [0.75 cl]', 1, 2.00, 2.00),
(54, 74, 'Brownie con Helado', 1, 4.00, 4.00),
(55, 75, 'Brownie con Helado', 1, 4.00, 4.00),
(56, 76, 'Aigua de Valencia', 1, 3.50, 3.50),
(57, 77, 'Coulant de Chocolate', 1, 4.50, 4.50),
(58, 78, 'Crepe con Nutella', 1, 3.50, 3.50),
(59, 79, 'Crepe con Nutella', 1, 3.50, 3.50),
(60, 80, 'agua bendita', 1, 500.00, 500.00),
(61, 81, '🎁 Patatas Fritas (Promo)', 1, 0.00, 0.00),
(62, 82, 'Café Cortado Descafeinado', 1, 1.40, 1.40),
(63, 83, 'CocaCola', 1, 2.50, 2.50),
(68, 84, 'Curry', -1, 0.80, -0.80),
(73, 86, 'Brownie con Helado', 100, 5.50, 550.00),
(74, 88, 'Croquetas', 1, 3.90, 3.90),
(75, 89, 'Brownie con Helado', 100, 4.00, 400.00),
(76, 90, 'Alioli', 1, 0.70, 0.70),
(77, 91, 'Coulant de Chocolate', 1, 4.50, 4.50),
(78, 92, 'agua bendita', 1, 500.00, 500.00),
(79, 93, 'agua bendita', 1, 500.00, 500.00),
(80, 94, 'Croquetas', 1, 3.90, 3.90),
(81, 95, 'AGUA PURIFICADA', 1, 10000.00, 10000.00);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productos`
--

CREATE TABLE `productos` (
  `id_producto` int(11) NOT NULL,
  `nombre` varchar(150) NOT NULL,
  `precio` decimal(10,2) NOT NULL,
  `id_detalle` int(11) NOT NULL,
  `stock` int(11) NOT NULL DEFAULT 15
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `productos`
--

INSERT INTO `productos` (`id_producto`, `nombre`, `precio`, `id_detalle`, `stock`) VALUES
(1, 'Agua Con Gas [0.75 cl]', 2.00, 1, 39),
(2, 'Agua Natural [0.75 cl]', 1.80, 1, 49),
(3, 'CocaCola', 2.50, 1, 39),
(4, 'CocaCola Zero', 2.50, 1, 40),
(5, 'CocaCola Zero Zero', 2.50, 1, 40),
(6, 'CocaCola Light', 2.50, 1, 40),
(7, 'Fanta de Naranja', 2.50, 1, 40),
(8, 'Fanta de Limon', 2.50, 1, 40),
(9, 'Aquarius de Limon', 3.00, 1, 29),
(10, 'Aquarius de Naranja', 3.00, 1, 30),
(11, 'Nestea', 3.00, 1, 30),
(12, 'Sprite', 2.50, 1, 40),
(13, 'Trina', 3.00, 1, 30),
(14, 'Zumo de Melocoton', 2.00, 1, 25),
(15, 'Zumo de Piña', 2.00, 1, 25),
(16, 'Cerveza', 2.20, 2, 40),
(17, 'Cerveza Sin Alcohol', 2.20, 2, 40),
(18, 'Anis Tenis [Chupito]', 1.50, 2, 20),
(19, 'Tinto de Verano', 3.20, 2, 25),
(20, 'Tinto de Verano Sin Alcohol', 3.20, 2, 25),
(21, 'Aigua de Valencia', 3.50, 2, 19),
(22, 'Café Solo', 1.20, 3, 99),
(23, 'Café Cortado', 1.40, 3, 99),
(24, 'Café con Leche', 1.60, 3, 99),
(25, 'Café Solo Descafeinado', 1.20, 3, 99),
(26, 'Café Cortado Descafeinado', 1.40, 3, 98),
(27, 'Café con Leche Descafeinado', 1.60, 3, 99),
(28, 'Te Verde', 1.50, 3, 99),
(29, 'Te Negro', 1.50, 3, 99),
(30, 'Manzanilla', 1.50, 3, 99),
(31, 'Java Fries', 3.50, 4, 30),
(32, 'Nuggets (6 uds)', 5.20, 4, 25),
(33, 'Aros de Cebolla', 4.00, 4, 25),
(34, 'Ensalada', 4.50, 4, 20),
(35, 'Alitas (4 uds)', 6.00, 4, 8),
(36, 'Nachos', 4.80, 4, 25),
(37, 'Croquetas', 3.90, 4, 23),
(38, 'Patatas Gajo', 3.75, 4, 30),
(39, 'Bolas de queso con mermelada', 5.50, 4, 15),
(40, 'Ketchup', 0.50, 5, 98),
(41, 'Mayonesa', 0.50, 5, 99),
(42, 'Barbacoa', 0.60, 5, 95),
(43, 'Mostaza', 0.50, 5, 99),
(44, 'Alioli', 0.70, 5, 93),
(45, 'Salsa Brava', 0.75, 5, 99),
(46, 'Miel y Mostaza', 0.80, 5, 99),
(47, 'Ranch', 0.80, 5, 99),
(48, 'Cheddar', 1.00, 5, 99),
(49, 'Pesto', 1.20, 5, 99),
(50, 'Picante', 0.90, 5, 99),
(51, 'Teriyaki', 1.10, 5, 99),
(52, 'César', 0.85, 5, 99),
(53, 'Yogur', 0.75, 5, 99),
(54, 'Curry', 0.80, 5, 98),
(55, 'Sriracha', 0.95, 5, 99),
(56, 'Tártara', 0.85, 5, 99),
(57, 'Guacamole', 1.50, 5, 99),
(58, 'Soja', 0.60, 5, 99),
(59, 'Llave Ácida', 1.25, 5, 99),
(60, 'Java Burger Clásica', 8.50, 6, 20),
(61, 'Java Burger BBQ', 9.00, 6, 20),
(62, 'Java Burger Doble', 11.50, 6, 15),
(63, 'Java Burger Pollo', 8.00, 6, 20),
(64, 'Java Burger Vegana', 8.50, 6, 15),
(65, 'Java Burger Bacon', 9.50, 6, 20),
(70, 'Bocata de Calamares', 5.50, 7, 20),
(71, 'Bocata de Lomo', 5.00, 7, 20),
(72, 'Bocata de Pollo', 5.50, 7, 20),
(73, 'Bocata Vegetal', 4.50, 7, 20),
(80, 'Pizza Margarita', 9.00, 8, 15),
(81, 'Pizza Pepperoni', 10.50, 8, 15),
(82, 'Pizza Barbacoa', 11.00, 8, 15),
(83, 'Pizza 4 Quesos', 10.50, 8, 15),
(84, 'Pizza Hawaiana', 10.00, 8, 15),
(90, 'Coulant de Chocolate', 4.50, 10, 18),
(91, 'Brownie con Helado', 4.00, 10, 16),
(92, 'Tarta de Queso', 3.50, 10, 20),
(93, 'Helado (3 bolas)', 3.00, 10, 24),
(94, 'Crepe con Nutella', 3.50, 10, 18),
(96, 'Brownie con Helado', 5.50, 9, 15),
(97, 'Coulant de Chocolate', 5.50, 9, 15),
(98, 'Helado (3 bolas)', 4.00, 9, 15),
(99, 'Tarta de Queso', 4.50, 9, 15),
(100, 'Crepe con Nutella', 4.50, 9, 15),
(101, 'agua bendita', 500.00, 1, 991),
(105, 'AGUA PURIFICADA', 10000.00, 1, 13),
(110, 'Patatas Fritas', 500.00, 11, 15),
(111, 'CheeseBurger', 1200.00, 11, 15),
(112, 'Salsa Barbacoa', 300.00, 11, 15),
(113, 'a', 100.00, 6, 15),
(114, 'ME LA SUDAAA', 1000.00, 6, 15);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `resenas`
--

CREATE TABLE `resenas` (
  `id_resena` int(11) NOT NULL,
  `id_producto` int(11) NOT NULL,
  `id_cliente` int(11) DEFAULT NULL,
  `puntuacion` tinyint(4) NOT NULL,
  `comentario` text DEFAULT NULL,
  `fecha` datetime NOT NULL DEFAULT current_timestamp()
) ;

--
-- Volcado de datos para la tabla `resenas`
--

INSERT INTO `resenas` (`id_resena`, `id_producto`, `id_cliente`, `puntuacion`, `comentario`, `fecha`) VALUES
(1, 1, 5, 4, 'de puta madre esta agua', '2026-05-24 15:57:05'),
(2, 101, 5, 4, NULL, '2026-05-24 16:46:04'),
(3, 44, 3, 3, NULL, '2026-05-24 17:21:51'),
(18, 1, 8, 3, 'resena de prueba de prueba3', '2026-05-24 19:25:50'),
(19, 42, 8, 3, 'de prueba3', '2026-05-24 19:35:57'),
(20, 96, 16, 4, 'adsa', '2026-05-24 19:50:19'),
(21, 101, 17, 4, 'as', '2026-05-24 19:51:02'),
(22, 96, 19, 4, 'as', '2026-05-24 20:02:06'),
(23, 91, 19, 4, 'adsa', '2026-05-24 20:02:15'),
(24, 21, 20, 5, 'resena', '2026-05-24 20:04:37'),
(25, 97, 20, 4, 's', '2026-05-24 20:06:47'),
(26, 90, 20, 5, 'a', '2026-05-24 20:07:00'),
(27, 100, 20, 5, NULL, '2026-05-24 20:09:51'),
(28, 26, 20, 5, NULL, '2026-05-24 20:13:29'),
(29, 3, 20, 5, 'de 10 resena', '2026-05-24 20:15:10'),
(30, 54, 20, 5, 'muy picante', '2026-05-24 20:16:32'),
(31, 37, 14, 3, 'asda', '2026-05-24 20:27:16'),
(32, 34, 14, 4, 'adsaa', '2026-05-24 20:27:21'),
(33, 10, 14, 4, 'a', '2026-05-24 20:31:21'),
(34, 44, 14, 5, 'as', '2026-05-24 20:33:01'),
(35, 97, 14, 5, '100', '2026-05-24 20:33:19'),
(36, 35, 14, 4, 'a', '2026-05-24 20:40:13'),
(37, 1, 14, 4, NULL, '2026-05-24 20:40:23'),
(38, 101, 3, 5, 'a', '2026-05-24 20:47:57'),
(39, 9, 8, 5, NULL, '2026-05-24 20:49:38'),
(40, 37, 8, 5, 'de 10', '2026-05-24 20:53:32');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `turnos_tipo`
--

CREATE TABLE `turnos_tipo` (
  `id` int(11) NOT NULL,
  `codigo` varchar(20) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `emoji` varchar(10) DEFAULT NULL,
  `horas` varchar(40) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `turnos_tipo`
--

INSERT INTO `turnos_tipo` (`id`, `codigo`, `nombre`, `emoji`, `horas`) VALUES
(1, 'manana', 'Mañana', '🌅', '07:00–15:00'),
(2, 'tarde', 'Tarde', '🌆', '15:00–23:00'),
(3, 'noche', 'Noche', '🌙', '23:00–07:00'),
(4, 'partido', 'Partido', '⚡', '10:00–14:00 / 18:00–22:00'),
(5, 'libre', 'Libre', '✓', '—'),
(6, 'baja', 'Baja', '🏥', '—');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `id` int(11) NOT NULL,
  `nom` varchar(100) NOT NULL,
  `apellidos` varchar(150) NOT NULL,
  `direccion` varchar(255) NOT NULL,
  `telefono` int(11) NOT NULL,
  `email` varchar(150) NOT NULL,
  `contrasena` varchar(255) NOT NULL,
  `tipo` enum('ADMIN','EMPLEADO','CLIENTE') NOT NULL DEFAULT 'CLIENTE'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`id`, `nom`, `apellidos`, `direccion`, `telefono`, `email`, `contrasena`, `tipo`) VALUES
(1, 'Admin', 'JavaFood', 'Sede Central', 600000000, 'admin@javafood.es', 'admin1234', 'ADMIN'),
(3, 'Miguel Angel', 'Martinez VILCHES', 'Calle de la Virgen de la Almudena 31', 661232502, 'akatonilol@gmail.com', '1234', 'ADMIN'),
(4, 'Miguel Angel', 'Martinez VILCHES', 'Calle de la Virgen de la Almudena 31', 661232502, 'miguelprogramacion2026@gmail.com', '12345678', 'EMPLEADO'),
(5, 'Miguel Angel', 'Martinez VILCHES', 'Calle de la Virgen de la Almudena 31', 661232502, 'mavilches933@gmail.com', '1234', 'CLIENTE'),
(8, 'PRUEBA 3', 'Martinez VILCHES', 'Calle de la Virgen de la Almudena 31', 661232502, 'prueba3@gmail.com', '1234', 'CLIENTE'),
(9, 'Miguel Angel', 'Martinez VILCHES', 'Calle de la Virgen de la Almudena 31', 661232502, 'mavilches66123@gmail.com', '1234', 'CLIENTE'),
(10, '1234', 'Martinez VILCHES', 'Calle de la Virgen de la Almudena 31', 661232502, '1234@gmail.com', '1234', 'CLIENTE'),
(12, 'Miguel Angel', 'Martinez VILCHES', 'Calle de la Virgen de la Almudena 31', 661232502, '12345@gmail.com', '1234', 'CLIENTE'),
(13, 'nuevo', 'Martinez VILCHES', 'Calle de la Virgen de la Almudena 31', 661232502, 'nuevo@gmail.com', '1234', 'CLIENTE'),
(14, 'nuevo99', 'Martinez VILCHES', 'Calle de la Virgen de la Almudena 31', 661232502, 'nuevo99@gmail.com', '1234', 'CLIENTE'),
(15, 'Miguel Angel', 'Martinez VILCHES', 'Calle de la Virgen de la Almudena 31', 661232502, 'prueba33@gmail.com', '1234', 'CLIENTE'),
(16, 'DEFINITIVO', 'Martinez VILCHES', 'Calle de la Virgen de la Almudena 31', 661232502, 'definitivo@gmail.com', '1234', 'CLIENTE'),
(17, 'SEGUNDO', 'Martinez VILCHES', 'Calle de la Virgen de la Almudena 31', 661232502, 'segundo@gmail.com', '1234', 'CLIENTE'),
(18, 'Miguel Angel', 'Martinez VILCHES', 'Calle de la Virgen de la Almudena 31', 661232502, 'pruebafinal@gmail.com', '1234', 'CLIENTE'),
(19, 'Miguel Angel', 'Martinez VILCHES', 'Calle de la Virgen de la Almudena 31', 661232502, 'jose@gmail.com', '1234', 'CLIENTE'),
(20, 'resena', 'Martinez VILCHES', 'Calle de la Virgen de la Almudena 31', 661232502, 'resena@gmail.com', '1234', 'CLIENTE');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `categorias`
--
ALTER TABLE `categorias`
  ADD PRIMARY KEY (`id_categoria`);

--
-- Indices de la tabla `cliente`
--
ALTER TABLE `cliente`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `clientes`
--
ALTER TABLE `clientes`
  ADD PRIMARY KEY (`id_cliente`),
  ADD UNIQUE KEY `uq_email` (`email`);

--
-- Indices de la tabla `detalle_categoria`
--
ALTER TABLE `detalle_categoria`
  ADD PRIMARY KEY (`id_detalle`),
  ADD KEY `fk_detcat_cat` (`id_categoria`);

--
-- Indices de la tabla `empleados`
--
ALTER TABLE `empleados`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `fichajes`
--
ALTER TABLE `fichajes`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_fich_emp` (`empleado_id`);

--
-- Indices de la tabla `horarios`
--
ALTER TABLE `horarios`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uk_horario` (`empleado_id`,`anyo`,`mes`,`dia`);

--
-- Indices de la tabla `pedidos`
--
ALTER TABLE `pedidos`
  ADD PRIMARY KEY (`id_pedido`),
  ADD KEY `fk_pedidos_cliente` (`id_cliente`);

--
-- Indices de la tabla `pedido_detalle`
--
ALTER TABLE `pedido_detalle`
  ADD PRIMARY KEY (`id_linea`),
  ADD KEY `fk_detalle_pedido` (`id_pedido`);

--
-- Indices de la tabla `productos`
--
ALTER TABLE `productos`
  ADD PRIMARY KEY (`id_producto`),
  ADD KEY `fk_prod_detalle` (`id_detalle`);

--
-- Indices de la tabla `resenas`
--
ALTER TABLE `resenas`
  ADD PRIMARY KEY (`id_resena`),
  ADD UNIQUE KEY `uq_cliente_producto` (`id_cliente`,`id_producto`),
  ADD KEY `fk_resena_prod` (`id_producto`);

--
-- Indices de la tabla `turnos_tipo`
--
ALTER TABLE `turnos_tipo`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_codigo` (`codigo`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `categorias`
--
ALTER TABLE `categorias`
  MODIFY `id_categoria` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `clientes`
--
ALTER TABLE `clientes`
  MODIFY `id_cliente` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `detalle_categoria`
--
ALTER TABLE `detalle_categoria`
  MODIFY `id_detalle` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT de la tabla `empleados`
--
ALTER TABLE `empleados`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT de la tabla `fichajes`
--
ALTER TABLE `fichajes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=105;

--
-- AUTO_INCREMENT de la tabla `horarios`
--
ALTER TABLE `horarios`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT de la tabla `pedidos`
--
ALTER TABLE `pedidos`
  MODIFY `id_pedido` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=96;

--
-- AUTO_INCREMENT de la tabla `pedido_detalle`
--
ALTER TABLE `pedido_detalle`
  MODIFY `id_linea` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=82;

--
-- AUTO_INCREMENT de la tabla `productos`
--
ALTER TABLE `productos`
  MODIFY `id_producto` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=115;

--
-- AUTO_INCREMENT de la tabla `resenas`
--
ALTER TABLE `resenas`
  MODIFY `id_resena` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `turnos_tipo`
--
ALTER TABLE `turnos_tipo`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `cliente`
--
ALTER TABLE `cliente`
  ADD CONSTRAINT `fk_cliente_usuario` FOREIGN KEY (`id`) REFERENCES `usuario` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `detalle_categoria`
--
ALTER TABLE `detalle_categoria`
  ADD CONSTRAINT `fk_detcat_cat` FOREIGN KEY (`id_categoria`) REFERENCES `categorias` (`id_categoria`) ON DELETE CASCADE;

--
-- Filtros para la tabla `fichajes`
--
ALTER TABLE `fichajes`
  ADD CONSTRAINT `fk_fich_emp` FOREIGN KEY (`empleado_id`) REFERENCES `empleados` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `horarios`
--
ALTER TABLE `horarios`
  ADD CONSTRAINT `fk_hor_emp` FOREIGN KEY (`empleado_id`) REFERENCES `empleados` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `pedido_detalle`
--
ALTER TABLE `pedido_detalle`
  ADD CONSTRAINT `fk_detalle_pedido` FOREIGN KEY (`id_pedido`) REFERENCES `pedidos` (`id_pedido`) ON DELETE CASCADE;

--
-- Filtros para la tabla `productos`
--
ALTER TABLE `productos`
  ADD CONSTRAINT `fk_prod_detalle` FOREIGN KEY (`id_detalle`) REFERENCES `detalle_categoria` (`id_detalle`);

--
-- Filtros para la tabla `resenas`
--
ALTER TABLE `resenas`
  ADD CONSTRAINT `fk_resena_prod` FOREIGN KEY (`id_producto`) REFERENCES `productos` (`id_producto`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
