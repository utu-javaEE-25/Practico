CREATE USER clinica1_user WITH PASSWORD 'pass1';

CREATE SCHEMA IF NOT EXISTS tenant_clinica1
    AUTHORIZATION clinica1_user;

GRANT ALL PRIVILEGES ON SCHEMA tenant_clinica1 TO clinica1_user;
GRANT ALL PRIVILEGES ON SCHEMA tenant_clinica1 TO clinica1_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA tenant_clinica1         TO clinica1_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA tenant_clinica1         TO clinica1_user;
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA tenant_clinica1           TO clinica1_user;

SET search_path TO tenant_clinica1;

CREATE TABLE admin_tenant (
    admin_id SERIAL PRIMARY KEY,
    nombre_usuario VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    nombre VARCHAR(50),
    apellido VARCHAR(50),
    email VARCHAR(50),
    estado VARCHAR(30)
);

CREATE TABLE profesional (
    profesional_id SERIAL PRIMARY KEY,
    nombre_usuario VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    nombre VARCHAR(50),
    apellido VARCHAR(50),
    email VARCHAR(50),
    estado VARCHAR(50),
    especializacion VARCHAR(30)
);

CREATE TABLE paciente (
    paciente_id SERIAL PRIMARY KEY,
    globaluser_id INT,
    nombre VARCHAR(50),
    apellido VARCHAR(50),
    telefono VARCHAR(15),
    email VARCHAR(50),
    fecha_creacion TIMESTAMP,
    fecha_modificacion TIMESTAMP
);

CREATE TABLE documento_clinico (
    doc_id SERIAL PRIMARY KEY,
    paciente_id INT NOT NULL,
    profesional_id INT NOT NULL,
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
    config_id SERIAL PRIMARY KEY,
    nombre_visible VARCHAR(120),
    color_principal VARCHAR(40),
    logourl VARCHAR(300),
    email_contacto VARCHAR(150),
    fecha_creacion TIMESTAMP,
    fecha_modificacion TIMESTAMP
);

CREATE TABLE central_endpoint (
    endpoint_id SERIAL PRIMARY KEY,
    url_base VARCHAR(300) NOT NULL,
    tipo_auth VARCHAR(40),
    hash_cliente VARCHAR(255)
);

CREATE TABLE audit_log (
    audit_id SERIAL PRIMARY KEY,
    tipo_actor VARCHAR(50),
    actor_id INT NOT NULL,
    accion VARCHAR(120),
    recurso_id INT,
    resultado VARCHAR(80),
    ip VARCHAR(45),
    fecha_creacion TIMESTAMP
);
