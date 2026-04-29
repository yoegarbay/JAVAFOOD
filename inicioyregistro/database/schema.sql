
CREATE TABLE registrarse(
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(20) NOT NULL,
    apellidos VARCHAR(50) NOT NULL,
    direccion TEXT NOT NULL,
    telefono DECIMAL(9,0) NOT NULL,
    correo_electronico VARCHAR(50) NOT NULL UNIQUE,
    contrasenya VARCHAR(30) NOT NULL CHECK (CHAR_LENGTH(contrasenya) > 6),
    INDEX (contrasenya)
);

CREATE TABLE iniciar_sesion(
    id INT PRIMARY KEY AUTO_INCREMENT,
    correo_electronico VARCHAR(50),
    contrasenya VARCHAR(30) NOT NULL,
    CONSTRAINT fk_correo FOREIGN KEY (correo_electronico) REFERENCES registrarse (correo_electronico),
    CONSTRAINT fk_contrasenya FOREIGN KEY (contrasenya) REFERENCES registrarse (contrasenya)
);