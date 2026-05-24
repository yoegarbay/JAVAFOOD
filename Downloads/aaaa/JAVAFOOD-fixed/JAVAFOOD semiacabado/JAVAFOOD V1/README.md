# JAVAFOOD

Sistema de gestión y pedidos para un restaurante, desarrollado como proyecto intermodular del ciclo DAW/DAM en el IES Pere Maria Orts i Bosch (Benidorm).

---

## Descripción

JAVAFOOD es una aplicación web de tipo kiosko digital que permite a los clientes explorar el menú, realizar pedidos y canjear puntos de fidelización. El personal de administración dispone de un panel completo para gestionar productos, pedidos, reseñas y promociones en tiempo real.



Para arrancarlo ir a ![](C:\Users\vilch\AppData\Roaming\marktext\images\2026-05-24-21-15-10-image.png)

---







La linea spring.datasource.url=jdbc:mysql://localhost:3306/javafoodfinalv2 debe cambiarse javafoodfinalv2 por la base de datos a utilizar 



Primero ejecutar el schema.sql

![](C:\Users\vilch\AppData\Roaming\marktext\images\2026-05-24-21-17-53-image.png)

Seguido de datajavafood.sql



Y arrancar  el ide 



con xampp  encendido y apache y mysql![](C:\Users\vilch\AppData\Roaming\marktext\images\2026-05-24-21-19-00-image.png)



## Tecnologías utilizadas

**Backend**

- Java 17
- Spring Boot 4.0.3 (Spring Web MVC, Spring JDBC)
- MySQL 8 + HikariCP (pool de conexiones)
- JDBC puro — sin ORM

**Frontend**

- HTML5, CSS3, JavaScript vanilla
- Space Grotesk (Google Fonts)
- Arquitectura SPA-like sobre recursos estáticos de Spring Boot

**Herramientas**

- Maven (gestión de dependencias)
- phpMyAdmin (administración de la base de datos)
- Eclipse / VS Code

---

## Estructura del proyecto

```
JAVAFOOD V1/
├── src/
│   └── main/
│       ├── java/com/productos/
│       │   ├── controller/      # REST Controllers
│       │   ├── dto/             # Data Transfer Objects
│       │   ├── entity/          # Entidades de dominio
│       │   ├── repository/      # Acceso a datos con JDBC
│       │   ├── exception/       # Manejo de errores
│       │   └── fichar/          # Módulo de fichajes
│       └── resources/
│           ├── static/          # Frontend (HTML, CSS, JS, imágenes)
│           └── application.properties
├── database/
│   ├── schema.sql               # Estructura de la BD
│   ├── data.sql                 # Datos iniciales
│   └── *.sql                    # Scripts de configuración adicionales
└── pom.xml
```

---

## Funcionalidades

### Kiosko (cliente)

- Navegación por categorías: Menús, Principales, Complementos, Salsas, Bebidas, Postres, Promociones
- Carrito de compra persistente (localStorage)
- Sistema de puntos de fidelización (Java Points)
- Registro e inicio de sesión de clientes
- Historial de pedidos propios
- Sistema de reseñas por producto (1-5 estrellas), con verificación de compra previa
- Canje de promociones con puntos acumulados

### Panel de administración

- CRUD completo de productos por categoría (Hamburguesas, Pizzas, Bocatas, Complementos, Salsas, Bebidas, Postres, Promociones)
- Gestión de stock con bloqueo pesimista (`SELECT FOR UPDATE`)
- Gestión de pedidos: listado, edición, eliminación
- Creación manual de pedidos desde el panel admin
- Gestión de reseñas: visualización y eliminación de contenido inapropiado
- Gestión de fichajes y horarios del personal

### Seguridad y acceso

- Autenticación por rol: CLIENTE, EMPLEADO, ADMIN
- Rutas del panel admin protegidas por guard de sesión
- Separación de vistas según rol del usuario autenticado

---

## Base de datos

El sistema utiliza MySQL con las siguientes tablas principales:

| Tabla                                 | Descripción                                        |
| ------------------------------------- | -------------------------------------------------- |
| `usuario`                             | Usuarios registrados (clientes, empleados, admins) |
| `cliente`                             | Extensión con puntos de fidelización               |
| `productos`                           | Catálogo de productos                              |
| `categorias` / `detalle_categoria`    | Jerarquía de categorías                            |
| `pedidos` / `pedido_detalle`          | Cabecera y líneas de cada pedido                   |
| `resenas`                             | Reseñas de productos por cliente                   |
| `empleados` / `fichajes` / `horarios` | Módulo de RRHH                                     |

---

## Instalación y arranque

### Requisitos previos

- Java 17+
- MySQL 8+
- Maven 3.6+

### Pasos

1. Clonar o descomprimir el proyecto

2. Crear la base de datos en MySQL:
   
   ```sql
   CREATE DATABASE javafoodfinalv2 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

3. Ejecutar los scripts SQL en orden:
   
   ```
   database/schema.sql
   database/data.sql
   database/create_clientes.sql
   database/create_resenas.sql
   database/setup_promociones.sql   -- si se usa el módulo de promociones
   ```

4. Configurar credenciales en `src/main/resources/application.properties`:
   
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/javafoodfinalv2
   spring.datasource.username=root
   spring.datasource.password=
   ```

5. Arrancar la aplicación:
   
   ```bash
   ./mvnw spring-boot:run
   ```

6. Abrir en el navegador: [http://localhost:8080](http://localhost:8080)

---

## API REST — endpoints principales

| Método   | Ruta                                | Descripción               |
| -------- | ----------------------------------- | ------------------------- |
| `GET`    | `/api/productos/categoria/{nombre}` | Productos por categoría   |
| `POST`   | `/api/clientes/registro`            | Registro de cliente       |
| `POST`   | `/api/clientes/login`               | Inicio de sesión          |
| `POST`   | `/api/pedidos`                      | Crear nuevo pedido        |
| `GET`    | `/api/pedidos/mis/{id}`             | Pedidos del cliente       |
| `GET`    | `/api/resenas/producto/{id}`        | Reseñas de un producto    |
| `POST`   | `/api/resenas`                      | Crear reseña              |
| `GET`    | `/api/admin/pedidos`                | Todos los pedidos (admin) |
| `DELETE` | `/api/admin/resenas/{id}`           | Eliminar reseña (admin)   |

---

## Autores

Proyecto desarrollado por:

- **Miguel Angel** — Backend, base de datos, arquitectura REST
- **Yoel** — Frontend, diseño del kiosko, sistema de puntos
- **Adrian** — Panel de administración, módulo de fichajes, integraciones

IES Pere Maria Orts i Bosch · Benidorm · 2025-2026
