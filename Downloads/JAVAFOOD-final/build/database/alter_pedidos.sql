-- ⚠️ Ejecutar UNA SOLA VEZ antes de arrancar la aplicación
-- Añade las columnas que faltan en la tabla pedidos

ALTER TABLE pedidos
  ADD COLUMN IF NOT EXISTS nombre_cliente VARCHAR(100) NOT NULL DEFAULT 'Cliente',
  ADD COLUMN IF NOT EXISTS metodo_pago    VARCHAR(20)  NOT NULL DEFAULT 'EFECTIVO';
