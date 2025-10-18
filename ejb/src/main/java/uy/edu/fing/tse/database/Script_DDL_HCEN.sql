CREATE USER hcen_user WITH PASSWORD 'hcen_pass';
CREATE USER hcen_master WITH PASSWORD 'hcen_pass';

CREATE SCHEMA IF NOT EXISTS central
    AUTHORIZATION hcen_user;

GRANT ALL PRIVILEGES ON DATABASE hcen TO hcen_user;


SET search_path TO central;

CREATE TABLE tenant (
    tenant_id SERIAL PRIMARY KEY,
    nombre_schema VARCHAR(15) UNIQUE NOT NULL,
    nombre VARCHAR(50) UNIQUE NOT NULL,
    rut VARCHAR(11) UNIQUE NOT NULL,
    estado VARCHAR(20),
    contacto_mail VARCHAR(50),
    tipo VARCHAR(15),
    fecha_creacion TIMESTAMP,
    fecha_modificacion TIMESTAMP
);

CREATE TABLE tenant_endpoint (
    tenant_id INT PRIMARY KEY REFERENCES tenant(tenant_id),
    uri_base VARCHAR(255) NOT NULL,
    tipo_auth VARCHAR(50), -- API_KEY, MTLS, CLI_CREDENTIAL, etc.
    hash_cliente VARCHAR(255),
    activo BOOLEAN NOT NULL
);

CREATE TABLE admin_global (
    admin_id SERIAL PRIMARY KEY,
    gubuy_id VARCHAR(100),
    email VARCHAR(50) UNIQUE NOT NULL,
    estado VARCHAR(50),
    fecha_creacion TIMESTAMP
);

CREATE TABLE usuario_global (
    user_id SERIAL PRIMARY KEY,
    gubuy_id VARCHAR(100),
    email VARCHAR(50) UNIQUE NOT NULL,
    estado VARCHAR(50),
    fecha_creacion TIMESTAMP,
    fecha_modificacion TIMESTAMP
);

CREATE TABLE documento_clinico_metadata (
    doc_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES usuario_global(user_id),
    tenant_id INT NOT NULL REFERENCES tenant(tenant_id),
    id_externa_doc VARCHAR(100) NOT NULL,
    tipo VARCHAR(50),
    fecha_creacion TIMESTAMP
);

CREATE TABLE solicitud_acceso (
    solicitud_id SERIAL PRIMARY KEY,
    requester_tenant_id INT NOT NULL REFERENCES tenant(tenant_id),
    target_user_id INT NOT NULL REFERENCES usuario_global(user_id),
    doc_id INT REFERENCES documento_clinico_metadata(doc_id),
    motivo TEXT,
    estado VARCHAR(50) NOT NULL,
    fecha_solicitud TIMESTAMP
);

CREATE TABLE notificacion (
    notif_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES usuario_global(user_id),
    solicitud_id INT REFERENCES solicitud_acceso(solicitud_id),
    evento VARCHAR(50), -- acceso, solicitado, ocurrido, garantizado, denegado
    estado VARCHAR(50), -- enviada, pendiente, fallido
    fecha_envio TIMESTAMP
);

CREATE TABLE pref_notificacion (
    pref_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES usuario_global(user_id),
    alerta_acceso BOOLEAN,
    solicitud_acceso BOOLEAN,
    canal VARCHAR(50) -- SMS, MAIL, PUSH, etc.
);

CREATE TABLE politica_acceso (
    politica_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES usuario_global(user_id),
    tenant_id INT REFERENCES tenant(tenant_id),
    accion VARCHAR(50), -- denegar, permitir, preguntar
    ventana_desde TIMESTAMP,
	ventana_hasta TIMESTAMP
);

CREATE TABLE audit_log (
    audit_id SERIAL PRIMARY KEY,
    tipo_actor VARCHAR(50),
    actor_id INT NOT NULL,
    accion VARCHAR(100),
    recurso_id INT,
    resultado VARCHAR(100),
    ip VARCHAR(50),
    fecha_creacion TIMESTAMP
);
