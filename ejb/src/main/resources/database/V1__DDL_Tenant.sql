SET search_path TO clinica1;

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
    nombre VARCHAR(50),
    apellido VARCHAR(50),
    telefono VARCHAR(15),
    email VARCHAR(50),
    fecha_creacion TIMESTAMP,
    fecha_modificacion TIMESTAMP
);

CREATE TABLE documento_clinico (
    doc_id BIGSERIAL PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    profesional_id BIGINT NOT NULL,
    id_externa_doc VARCHAR(120) UNIQUE,
    tipo VARCHAR(30),
    estado VARCHAR(30),
    fecha_creacion TIMESTAMP,
    CONSTRAINT fk_doc_paciente
        FOREIGN KEY (paciente_id) REFERENCES paciente(paciente_id),
    CONSTRAINT fk_doc_profesional
        FOREIGN KEY (profesional_id) REFERENCES profesional(profesional_id)
);

CREATE TABLE tenant_config (
    config_id BIGSERIAL PRIMARY KEY,
    nombre_visible VARCHAR(120),
    color_principal VARCHAR(40),
    logourl VARCHAR(300),
    email_contacto VARCHAR(150),
    fecha_creacion TIMESTAMP,
    fecha_modificacion TIMESTAMP
);

CREATE TABLE central_endpoint (
    endpoint_id BIGSERIAL PRIMARY KEY,
    url_base VARCHAR(300) NOT NULL,
    tipo_auth VARCHAR(40),
    hash_cliente VARCHAR(255)
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
