-- ============================================================================
-- Multi-Tenant Database Schema
-- ============================================================================
-- This schema implements a multi-tenancy pattern where PrestadorSalud acts 
-- as the tenant. All tenant-specific data is isolated by prestador_rut.
-- ============================================================================

-- ============================================================================
-- TENANT TABLE (Main tenant entity)
-- ============================================================================

CREATE TABLE prestador_salud (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    rut VARCHAR(12) NOT NULL UNIQUE,
    fecha_alta DATE,
    activo BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (id)
);

CREATE INDEX idx_prestador_rut ON prestador_salud(rut);

-- ============================================================================
-- TENANT-SPECIFIC TABLES
-- ============================================================================

-- Usuarios del servicio de salud afiliados a un prestador
CREATE TABLE usuario_servicio_salud (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre_completo VARCHAR(200) NOT NULL,
    cedula_identidad VARCHAR(8) NOT NULL UNIQUE,
    fecha_nacimiento DATE,
    activo BOOLEAN DEFAULT TRUE,
    prestador_rut VARCHAR(12) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (prestador_rut) REFERENCES prestador_salud(rut)
);

CREATE INDEX idx_usuario_prestador ON usuario_servicio_salud(prestador_rut);
CREATE INDEX idx_usuario_cedula ON usuario_servicio_salud(cedula_identidad);

-- Trabajadores de salud empleados por un prestador
CREATE TABLE trabajador_salud (
    id BIGINT NOT NULL AUTO_INCREMENT,
    cedula VARCHAR(8) NOT NULL UNIQUE,
    nombre_completo VARCHAR(200) NOT NULL,
    fecha_ingreso DATE,
    especialidad VARCHAR(100),
    prestador_rut VARCHAR(12) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (prestador_rut) REFERENCES prestador_salud(rut)
);

CREATE INDEX idx_trabajador_prestador ON trabajador_salud(prestador_rut);
CREATE INDEX idx_trabajador_cedula ON trabajador_salud(cedula);

-- Documentos clínicos asociados a un prestador y paciente
CREATE TABLE documento_clinico (
    id BIGINT NOT NULL AUTO_INCREMENT,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    paciente_ci VARCHAR(8) NOT NULL,
    prestador_rut VARCHAR(12) NOT NULL,
    fecha_emision DATE NOT NULL,
    firmado BOOLEAN DEFAULT FALSE,
    tipo VARCHAR(50),
    contenido TEXT,
    PRIMARY KEY (id),
    FOREIGN KEY (prestador_rut) REFERENCES prestador_salud(rut)
);

CREATE INDEX idx_documento_prestador ON documento_clinico(prestador_rut);
CREATE INDEX idx_documento_paciente ON documento_clinico(paciente_ci);
CREATE INDEX idx_documento_codigo ON documento_clinico(codigo);

-- ============================================================================
-- MULTI-TENANCY NOTES
-- ============================================================================
-- 1. Each PrestadorSalud (Health Provider) is a tenant identified by RUT
-- 2. All queries should filter by prestador_rut to ensure tenant isolation
-- 3. Foreign keys enforce referential integrity between tenants and their data
-- 4. Indexes on prestador_rut optimize tenant-specific queries
-- 5. Application layer must enforce tenant context in all operations
-- ============================================================================
