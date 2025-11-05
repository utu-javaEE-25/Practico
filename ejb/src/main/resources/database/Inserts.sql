BEGIN;
SET search_path TO central;

select * from Tenant;

-- -- Solo si aún existe la FK mal definida:
-- ALTER TABLE tenant DROP CONSTRAINT IF EXISTS fk_admin_tenant;
-- 1) Admins globales
INSERT INTO admin_global (gubuy_id, email, estado, fecha_creacion)
VALUES 
('gubuy_admin1', 'admin1@hcen.uy', , NOW()),
('gubuy_admin2', 'admin2@hcen.uy', 'ACTIVO', NOW())
ON CONFLICT (email) DO NOTHING;

-- 2) Tenants (usa claves únicas: nombre_schema, nombre, rut)
INSERT INTO tenant (nombre_schema, nombre, rut, estado, contacto_mail, tipo, fecha_creacion, fecha_modificacion)
VALUES
('tenant_clinica1', 'Clínica del Sol',  '21456789011', TRUE, 'contacto@clinicasol.uy', 'privado',     NOW(), NOW()),
('tenant_clinica2', 'Laboratorio Norte','21999887766', TRUE, 'info@labnorte.uy',       'laboratorio', NOW(), NOW())
ON CONFLICT (nombre_schema) DO UPDATE
SET estado = EXCLUDED.estado,
    contacto_mail = EXCLUDED.contacto_mail,
    tipo = EXCLUDED.tipo,
    fecha_modificacion = NOW();

-- 3) Endpoints (busca el ID real por nombre_schema, NO hardcodees 1/2)

-- Clínica del Sol
INSERT INTO tenant_endpoint (tenant_id, uri_base, tipo_auth, hash_cliente, activo)
SELECT t.tenant_id, 'https://api.clinicasol.uy', 'API_KEY', 'hashkey123', TRUE
FROM tenant t
WHERE t.nombre_schema = 'tenant_clinica1'
ON CONFLICT (tenant_id) DO UPDATE
SET uri_base = EXCLUDED.uri_base,
    tipo_auth = EXCLUDED.tipo_auth,
    hash_cliente = EXCLUDED.hash_cliente,
    activo    = EXCLUDED.activo;

-- Laboratorio Norte
INSERT INTO tenant_endpoint (tenant_id, uri_base, tipo_auth, hash_cliente, activo)
SELECT t.tenant_id, 'https://api.labnorte.uy', 'MTLS', 'hashkey456', TRUE
FROM tenant t
WHERE t.nombre_schema = 'tenant_clinica2'
ON CONFLICT (tenant_id) DO UPDATE
SET uri_base = EXCLUDED.uri_base,
    tipo_auth = EXCLUDED.tipo_auth,
    hash_cliente = EXCLUDED.hash_cliente,
    activo    = EXCLUDED.activo;

-- 4) Usuarios globales
INSERT INTO usuario_global (gubuy_id, email, estado, fecha_creacion, fecha_modificacion)
VALUES
('gubuy_user1', 'juan.perez@gmail.com',  'ACTIVO', NOW(), NOW()),
('gubuy_user2', 'maria.gomez@gmail.com', 'ACTIVO', NOW(), NOW()),
('gubuy_user3', 'andres.lopez@gmail.com','SUSPENDIDO', NOW(), NOW())
ON CONFLICT (email) DO UPDATE
SET estado = EXCLUDED.estado,
    fecha_modificacion = NOW();

-- 5) Documentos clínicos (metadatos) referenciando tenant y usuario por claves naturales

-- DOC001: Juan en Clínica del Sol
INSERT INTO documento_clinico_metadata (user_id, tenant_id, id_externa_doc, tipo, fecha_creacion)
SELECT u.user_id, t.tenant_id, 'DOC001', 'Informe de laboratorio', NOW()
FROM usuario_global u
JOIN tenant t ON t.nombre_schema = 'tenant_clinica1'
WHERE u.email = 'juan.perez@gmail.com';

-- DOC002: Juan en Laboratorio Norte
INSERT INTO documento_clinico_metadata (user_id, tenant_id, id_externa_doc, tipo, fecha_creacion)
SELECT u.user_id, t.tenant_id, 'DOC002', 'Radiografía de tórax', NOW()
FROM usuario_global u
JOIN tenant t ON t.nombre_schema = 'tenant_clinica2'
WHERE u.email = 'juan.perez@gmail.com';

-- DOC003: María en Clínica del Sol
INSERT INTO documento_clinico_metadata (user_id, tenant_id, id_externa_doc, tipo, fecha_creacion)
SELECT u.user_id, t.tenant_id, 'DOC003', 'Análisis de sangre', NOW()
FROM usuario_global u
JOIN tenant t ON t.nombre_schema = 'tenant_clinica1'
WHERE u.email = 'maria.gomez@gmail.com';

-- 6) Solicitudes de acceso (busca IDs reales)
-- Lab Norte solicita DOC002 de Juan
INSERT INTO solicitud_acceso (requester_tenant_id, target_user_id, doc_id, motivo, estado, fecha_solicitud)
SELECT tr.tenant_id, u.user_id, d.doc_id, 'Consulta de antecedentes', 'PENDIENTE', NOW()
FROM tenant tr
JOIN usuario_global u ON u.email = 'juan.perez@gmail.com'
JOIN documento_clinico_metadata d ON d.id_externa_doc = 'DOC002'
WHERE tr.nombre_schema = 'tenant_clinica2';

-- Clínica del Sol solicita DOC003 de María (ejemplo aprobado)
INSERT INTO solicitud_acceso (requester_tenant_id, target_user_id, doc_id, motivo, estado, fecha_solicitud)
SELECT tr.tenant_id, u.user_id, d.doc_id, 'Control de seguimiento', 'APROBADA', NOW()
FROM tenant tr
JOIN usuario_global u ON u.email = 'maria.gomez@gmail.com'
JOIN documento_clinico_metadata d ON d.id_externa_doc = 'DOC003'
WHERE tr.nombre_schema = 'tenant_clinica1';

-- 7) Notificaciones (referenciando por subselect)
INSERT INTO notificacion (user_id, solicitud_id, evento, estado, fecha_envio)
SELECT u.user_id, s.solicitud_id, 'solicitado', 'ENVIADA', NOW()
FROM usuario_global u
JOIN solicitud_acceso s ON s.estado = 'PENDIENTE'
WHERE u.email = 'juan.perez@gmail.com';

INSERT INTO notificacion (user_id, solicitud_id, evento, estado, fecha_envio)
SELECT u.user_id, s.solicitud_id, 'acceso', 'ENVIADA', NOW()
FROM usuario_global u
JOIN solicitud_acceso s ON s.estado = 'APROBADA'
WHERE u.email = 'maria.gomez@gmail.com';

-- 8) Preferencias
INSERT INTO pref_notificacion (user_id, alerta_acceso, solicitud_acceso, canal)
SELECT u.user_id, TRUE, TRUE, 'PUSH'
FROM usuario_global u WHERE u.email = 'juan.perez@gmail.com';

INSERT INTO pref_notificacion (user_id, alerta_acceso, solicitud_acceso, canal)
SELECT u.user_id, TRUE, FALSE, 'MAIL'
FROM usuario_global u WHERE u.email = 'maria.gomez@gmail.com';

-- 9) Políticas (permitir/preguntar)
INSERT INTO politica_acceso (user_id, tenant_id, accion, ventana_desde, ventana_hasta)
SELECT u.user_id, t.tenant_id, 'permitir', NOW() - INTERVAL '30 days', NOW() + INTERVAL '30 days'
FROM usuario_global u
JOIN tenant t ON t.nombre_schema = 'tenant_clinica1'
WHERE u.email = 'juan.perez@gmail.com';

INSERT INTO politica_acceso (user_id, tenant_id, accion, ventana_desde, ventana_hasta)
SELECT u.user_id, t.tenant_id, 'preguntar', NOW(), NOW() + INTERVAL '10 days'
FROM usuario_global u
JOIN tenant t ON t.nombre_schema = 'tenant_clinica2'
WHERE u.email = 'maria.gomez@gmail.com';

-- 10) Auditoría
INSERT INTO audit_log (tipo_actor, actor_id, accion, recurso_id, resultado, ip, fecha_creacion)
VALUES
('ADMIN_GLOBAL', 1, 'SEED_CENTRAL', NULL, 'OK', '127.0.0.1', NOW());

COMMIT;

