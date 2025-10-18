CREATE USER hcen_user WITH PASSWORD 'hcen_pass';
CREATE USER hcen_master WITH PASSWORD 'hcen_pass';

GRANT CREATE, CONNECT, TEMP ON DATABASE hcen TO hcen_master;
GRANT ALL PRIVILEGES ON DATABASE hcen TO hcen_master;

GRANT ALL PRIVILEGES ON SCHEMA central TO hcen_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA central TO hcen_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA central TO hcen_user;
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA central TO hcen_user;

CREATE SCHEMA IF NOT EXISTS central
    AUTHORIZATION hcen_user;

GRANT ALL PRIVILEGES ON DATABASE hcen TO hcen_user;

SET search_path TO central;

CREATE TABLE tenant (
    tenant_id BIGSERIAL PRIMARY KEY,
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
    tenant_id BIGINT PRIMARY KEY REFERENCES tenant(tenant_id),
    uri_base VARCHAR(255) NOT NULL,
    tipo_auth VARCHAR(50),
    hash_cliente VARCHAR(255),
    activo BOOLEAN NOT NULL
);

CREATE TABLE admin_global (
    admin_id BIGSERIAL PRIMARY KEY,
    gubuy_id VARCHAR(100),
    email VARCHAR(50) UNIQUE NOT NULL,
    estado VARCHAR(50),
    fecha_creacion TIMESTAMP
);

CREATE TABLE usuario_global (
    user_id BIGSERIAL PRIMARY KEY,
    gubuy_id VARCHAR(100),
    ci VARCHAR(11) UNIQUE NOT NULL,
    nombre VARCHAR(50),
    fecha_nacimiento DATE,
    email VARCHAR(50) UNIQUE NOT NULL,
    fecha_creacion TIMESTAMP,
    fecha_modificacion TIMESTAMP,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE documento_clinico_metadata (
    doc_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES usuario_global(user_id),
    tenant_id BIGINT NOT NULL REFERENCES tenant(tenant_id),
    id_externa_doc VARCHAR(100) NOT NULL,
    tipo VARCHAR(50),
    fecha_creacion TIMESTAMP
);

CREATE TABLE solicitud_acceso (
    solicitud_id BIGSERIAL PRIMARY KEY,
    requester_tenant_id BIGINT NOT NULL REFERENCES tenant(tenant_id),
    target_user_id BIGINT NOT NULL REFERENCES usuario_global(user_id),
    doc_id BIGINT REFERENCES documento_clinico_metadata(doc_id),
    motivo TEXT,
    estado VARCHAR(50) NOT NULL,
    fecha_solicitud TIMESTAMP
);

CREATE TABLE notificacion (
    notif_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES usuario_global(user_id),
    solicitud_id BIGINT REFERENCES solicitud_acceso(solicitud_id),
    evento VARCHAR(50),
    estado VARCHAR(50),
    fecha_envio TIMESTAMP
);

CREATE TABLE pref_notificacion (
    pref_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES usuario_global(user_id),
    alerta_acceso BOOLEAN,
    solicitud_acceso BOOLEAN,
    canal VARCHAR(50)
);

CREATE TABLE politica_acceso (
    politica_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES usuario_global(user_id),
    tenant_id BIGINT REFERENCES tenant(tenant_id),
    accion VARCHAR(50),
    ventana_desde TIMESTAMP,
    ventana_hasta TIMESTAMP
);

CREATE TABLE audit_log (
    audit_id BIGSERIAL PRIMARY KEY,
    tipo_actor VARCHAR(50),
    actor_id BIGINT NOT NULL,
    accion VARCHAR(100),
    recurso_id BIGINT,
    resultado VARCHAR(100),
    ip VARCHAR(50),
    fecha_creacion TIMESTAMP
);
