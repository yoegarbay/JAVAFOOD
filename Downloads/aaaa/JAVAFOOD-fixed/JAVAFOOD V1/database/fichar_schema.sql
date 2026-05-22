-- ============================================================
--  MÓDULO FICHAJE / HORARIO — JavaFood Intermodular
--  Ejecutar DESPUÉS del schema principal
-- ============================================================

CREATE TABLE IF NOT EXISTS empleados (
    id        INT          PRIMARY KEY AUTO_INCREMENT,
    nombre    VARCHAR(100) NOT NULL,
    iniciales VARCHAR(4)   NOT NULL,
    color     VARCHAR(20)  NOT NULL DEFAULT '#d17a22',
    activo    TINYINT(1)   NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS turnos_tipo (
    id     INT         PRIMARY KEY AUTO_INCREMENT,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(50) NOT NULL,
    emoji  VARCHAR(10),
    horas  VARCHAR(40)
);

CREATE TABLE IF NOT EXISTS horarios (
    id          INT         PRIMARY KEY AUTO_INCREMENT,
    empleado_id INT         NOT NULL,
    anyo        INT         NOT NULL,
    mes         INT         NOT NULL,
    dia         INT         NOT NULL,
    turno_cod   VARCHAR(20) NOT NULL,
    UNIQUE KEY uk_horario (empleado_id, anyo, mes, dia),
    CONSTRAINT fk_hor_emp FOREIGN KEY (empleado_id) REFERENCES empleados(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS fichajes (
    id          INT         PRIMARY KEY AUTO_INCREMENT,
    empleado_id INT         NOT NULL,
    tipo        VARCHAR(10) NOT NULL,
    fecha       DATE        NOT NULL,
    hora        TIME        NOT NULL,
    horas_calc  DECIMAL(5,2),
    CONSTRAINT fk_fich_emp FOREIGN KEY (empleado_id) REFERENCES empleados(id) ON DELETE CASCADE
);

-- Columna stock en productos (si no existe)
ALTER TABLE productos ADD COLUMN IF NOT EXISTS stock INT NOT NULL DEFAULT 15;

-- Datos de ejemplo para turnos
INSERT IGNORE INTO turnos_tipo (codigo, nombre, emoji, horas) VALUES
('manana',  'Mañana',  '🌅', '07:00–15:00'),
('tarde',   'Tarde',   '🌆', '15:00–23:00'),
('noche',   'Noche',   '🌙', '23:00–07:00'),
('partido', 'Partido', '⚡', '10:00–14:00 / 18:00–22:00'),
('libre',   'Libre',   '✓',  '—'),
('baja',    'Baja',    '🏥', '—');
