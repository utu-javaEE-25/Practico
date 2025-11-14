CREATE TABLE admin_tenant (
    admin_id BIGSERIAL PRIMARY KEY,
    nombre_usuario VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    nombre VARCHAR(50),
    apellido VARCHAR(50),
    email VARCHAR(50),
    estado VARCHAR(30)
);

CREATE TABLE profesional (
    profesional_id BIGSERIAL PRIMARY KEY,
    nombre_usuario VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    nombre VARCHAR(50),
    apellido VARCHAR(50),
    email VARCHAR(50),
    estado VARCHAR(50),
    especializacion VARCHAR(30)
);

CREATE TABLE paciente (
    paciente_id BIGSERIAL PRIMARY KEY,
    globaluser_id BIGINT,
    nro_documento VARCHAR(50) UNIQUE,
    nombre VARCHAR(50),
    apellido VARCHAR(50),
    telefono VARCHAR(15),
    email VARCHAR(50),
    sexo VARCHAR(50),
    fecha_nacimiento DATE,
    fecha_creacion TIMESTAMP,
    fecha_modificacion TIMESTAMP,
    estado BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE documento_clinico (
    doc_id BIGSERIAL PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    profesional_id BIGINT NOT NULL,
    id_externa_doc VARCHAR(255) UNIQUE,
    tipo VARCHAR(30),
    instancia_medica TEXT,
    estado VARCHAR(30),
    lugar VARCHAR(255),
    fecha_atencion_inicio TIMESTAMP,
    fecha_atencion_fin TIMESTAMP,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    motivos JSONB,
    diagnosticos JSONB,
    instrucciones JSONB,
    CONSTRAINT fk_doc_paciente
        FOREIGN KEY (paciente_id) REFERENCES paciente(paciente_id),
    CONSTRAINT fk_doc_profesional
        FOREIGN KEY (profesional_id) REFERENCES profesional(profesional_id)
);

CREATE TABLE tenant_config (
    config_id BIGSERIAL PRIMARY KEY,
    nombre_visible VARCHAR(120),
    color_principal VARCHAR(40),
    color_fondo VARCHAR(40),
    logourl TYPE TEXT,
    email_contacto VARCHAR(150),
    fecha_creacion TIMESTAMP,
    fecha_modificacion TIMESTAMP
);

CREATE TABLE audit_log (
    audit_id BIGSERIAL PRIMARY KEY,
    tipo_actor VARCHAR(50),
    actor_id BIGINT NOT NULL,
    accion VARCHAR(120),
    recurso_id BIGINT,
    resultado VARCHAR(80),
    ip VARCHAR(45),
    fecha_creacion TIMESTAMP
);
