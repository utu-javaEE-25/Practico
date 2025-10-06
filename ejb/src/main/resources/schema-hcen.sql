-- Schema for HCEN (Historia Clínica Electrónica Nacional)
-- Database schema generated from JPA entities

-- Table: prestador_salud
-- Stores healthcare providers (hospitals, clinics, etc.)
CREATE TABLE prestador_salud (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL,
    rut VARCHAR(255) NOT NULL UNIQUE,
    fecha_alta DATE,
    activo BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id)
);

-- Table: usuario_servicio_salud
-- Stores patients/users of the health service
CREATE TABLE usuario_servicio_salud (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre_completo VARCHAR(255) NOT NULL,
    cedula_identidad VARCHAR(255) NOT NULL UNIQUE,
    fecha_nacimiento DATE,
    activo BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id)
);

-- Table: trabajador_salud
-- Stores healthcare workers (doctors, nurses, etc.)
CREATE TABLE trabajador_salud (
    id BIGINT NOT NULL AUTO_INCREMENT,
    cedula VARCHAR(255) NOT NULL UNIQUE,
    nombre_completo VARCHAR(255) NOT NULL,
    fecha_ingreso DATE,
    especialidad VARCHAR(255),
    PRIMARY KEY (id)
);

-- Table: documento_clinico
-- Stores clinical documents (prescriptions, medical reports, etc.)
CREATE TABLE documento_clinico (
    id BIGINT NOT NULL AUTO_INCREMENT,
    codigo VARCHAR(255) NOT NULL UNIQUE,
    paciente_ci VARCHAR(255) NOT NULL,
    prestador_rut VARCHAR(255) NOT NULL,
    fecha_emision DATE,
    firmado BOOLEAN NOT NULL DEFAULT FALSE,
    tipo VARCHAR(255),
    contenido VARCHAR(2000),
    PRIMARY KEY (id)
);

-- Indexes for improved query performance
CREATE INDEX idx_prestador_rut ON prestador_salud(rut);
CREATE INDEX idx_usuario_ci ON usuario_servicio_salud(cedula_identidad);
CREATE INDEX idx_trabajador_cedula ON trabajador_salud(cedula);
CREATE INDEX idx_documento_codigo ON documento_clinico(codigo);
CREATE INDEX idx_documento_paciente ON documento_clinico(paciente_ci);
CREATE INDEX idx_documento_prestador ON documento_clinico(prestador_rut);
