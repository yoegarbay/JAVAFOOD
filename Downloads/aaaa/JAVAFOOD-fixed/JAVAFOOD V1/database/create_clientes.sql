-- Crear tabla de clientes
-- Ejecutar ANTES de arrancar la aplicación

CREATE TABLE IF NOT EXISTS clientes (
  id_cliente INT         NOT NULL AUTO_INCREMENT,
  nombre     VARCHAR(100) NOT NULL,
  email      VARCHAR(150) NOT NULL UNIQUE,
  password   VARCHAR(100) NOT NULL,
  PRIMARY KEY (id_cliente)
);
