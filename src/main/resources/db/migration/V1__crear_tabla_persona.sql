-- V1: Crear tabla persona
CREATE TABLE IF NOT EXISTS persona (
    id          BIGSERIAL       PRIMARY KEY,
    nombre      VARCHAR(100)    NOT NULL,
    apellido    VARCHAR(100)    NOT NULL,
    email       VARCHAR(150)    NOT NULL UNIQUE,
    fecha_nacimiento DATE       NOT NULL,
    fecha_creacion TIMESTAMP     DEFAULT CURRENT_TIMESTAMP
);
 