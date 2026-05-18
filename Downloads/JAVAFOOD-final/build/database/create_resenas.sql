-- ══════════════════════════════════════════════════════════════
-- Sistema de reseñas JAVAFOOD
-- Ejecutar UNA SOLA VEZ antes de arrancar la aplicación
-- ══════════════════════════════════════════════════════════════

-- 1. Enlazar pedidos con el cliente registrado (nullable: pedidos anónimos siguen funcionando)
ALTER TABLE pedidos
  ADD COLUMN IF NOT EXISTS id_cliente INT NULL,
  ADD CONSTRAINT IF NOT EXISTS fk_pedidos_cliente
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente)
    ON DELETE SET NULL;

-- 2. Tabla de reseñas
CREATE TABLE IF NOT EXISTS resenas (
  id_resena   INT          NOT NULL AUTO_INCREMENT,
  id_producto INT          NOT NULL,
  id_cliente  INT          NOT NULL,
  puntuacion  TINYINT      NOT NULL COMMENT '1–5 estrellas',
  comentario  TEXT,
  fecha       DATETIME     NOT NULL DEFAULT NOW(),
  PRIMARY KEY (id_resena),
  -- Un cliente sólo puede reseñar cada producto una vez
  UNIQUE KEY uq_cliente_producto (id_cliente, id_producto),
  CONSTRAINT fk_resena_producto FOREIGN KEY (id_producto)
    REFERENCES productos(id_producto) ON DELETE CASCADE,
  CONSTRAINT fk_resena_cliente  FOREIGN KEY (id_cliente)
    REFERENCES clientes(id_cliente)  ON DELETE CASCADE,
  CONSTRAINT ck_puntuacion CHECK (puntuacion BETWEEN 1 AND 5)
);
