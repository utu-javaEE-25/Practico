# Esquema de Base de Datos - HCEN (Historia Clínica Electrónica Nacional)

## Entidades Principales

### 1. PrestadorSalud (Prestador de Salud)
Representa a las instituciones de salud (hospitales, clínicas, mutualistas, etc.)

**Tabla:** `prestador_salud`

| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | BIGINT | Identificador único (PK, auto-generado) |
| nombre | VARCHAR(255) | Nombre del prestador (NOT NULL) |
| rut | VARCHAR(255) | RUT del prestador (UNIQUE, NOT NULL) |
| fecha_alta | DATE | Fecha de alta en el sistema |
| activo | BOOLEAN | Estado del prestador |

**Restricciones:**
- `rut` debe ser único
- `nombre` es obligatorio

---

### 2. UsuarioServicioSalud (Usuario del Servicio de Salud)
Representa a los pacientes/usuarios del sistema de salud

**Tabla:** `usuario_servicio_salud`

| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | BIGINT | Identificador único (PK, auto-generado) |
| nombre_completo | VARCHAR(255) | Nombre completo del usuario (NOT NULL) |
| cedula_identidad | VARCHAR(255) | Cédula de identidad (UNIQUE, NOT NULL) |
| fecha_nacimiento | DATE | Fecha de nacimiento |
| activo | BOOLEAN | Estado del usuario |

**Restricciones:**
- `cedula_identidad` debe ser única
- `nombre_completo` es obligatorio

---

### 3. TrabajadorSalud (Trabajador de Salud)
Representa a los profesionales de la salud (médicos, enfermeros, etc.)

**Tabla:** `trabajador_salud`

| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | BIGINT | Identificador único (PK, auto-generado) |
| cedula | VARCHAR(255) | Cédula del trabajador (UNIQUE, NOT NULL) |
| nombre_completo | VARCHAR(255) | Nombre completo (NOT NULL) |
| fecha_ingreso | DATE | Fecha de ingreso al sistema |
| especialidad | VARCHAR(255) | Especialidad médica |

**Restricciones:**
- `cedula` debe ser única
- `nombre_completo` es obligatorio

---

### 4. DocumentoClinico (Documento Clínico)
Representa documentos clínicos (recetas, órdenes, informes médicos, etc.)

**Tabla:** `documento_clinico`

| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | BIGINT | Identificador único (PK, auto-generado) |
| codigo | VARCHAR(255) | Código único del documento (UNIQUE, NOT NULL) |
| paciente_ci | VARCHAR(255) | CI del paciente (NOT NULL) |
| prestador_rut | VARCHAR(255) | RUT del prestador (NOT NULL) |
| fecha_emision | DATE | Fecha de emisión del documento |
| firmado | BOOLEAN | Indica si está firmado digitalmente |
| tipo | VARCHAR(255) | Tipo de documento (receta, orden, informe) |
| contenido | VARCHAR(2000) | Contenido del documento |

**Restricciones:**
- `codigo` debe ser único
- `paciente_ci` es obligatorio
- `prestador_rut` es obligatorio

---

## Relaciones (Lógicas)

Aunque las relaciones no están implementadas actualmente con Foreign Keys en el código, el modelo lógico contempla:

1. **DocumentoClinico → UsuarioServicioSalud**
   - `paciente_ci` referencia a `cedula_identidad` de `usuario_servicio_salud`

2. **DocumentoClinico → PrestadorSalud**
   - `prestador_rut` referencia a `rut` de `prestador_salud`

---

## Índices

Para optimizar el rendimiento de las consultas, se recomiendan los siguientes índices:

- `idx_prestador_rut` en `prestador_salud(rut)`
- `idx_usuario_ci` en `usuario_servicio_salud(cedula_identidad)`
- `idx_trabajador_cedula` en `trabajador_salud(cedula)`
- `idx_documento_codigo` en `documento_clinico(codigo)`
- `idx_documento_paciente` en `documento_clinico(paciente_ci)`
- `idx_documento_prestador` en `documento_clinico(prestador_rut)`

---

## Notas Técnicas

- **JPA/Hibernate**: Las entidades están anotadas con JPA (Jakarta Persistence API)
- **Estrategia de Generación de ID**: `IDENTITY` (auto-incremento)
- **Persistencia**: Configurada en `persistence.xml` con unidad de persistencia `LaboratorioPersistenceUnit`
- **DataSource**: Utiliza `java:comp/DefaultDataSource` (H2 embebido en desarrollo)
- **Schema Generation**: Configurado con `drop-and-create` en el despliegue

---

## Diagrama Conceptual

```
┌──────────────────────┐
│  PrestadorSalud      │
│  - id (PK)           │
│  - nombre            │
│  - rut (UNIQUE)      │
│  - fechaAlta         │
│  - activo            │
└──────────┬───────────┘
           │
           │ prestador_rut
           │
┌──────────▼───────────┐       paciente_ci      ┌─────────────────────┐
│  DocumentoClinico    │◄──────────────────────►│ UsuarioServicioSalud│
│  - id (PK)           │                         │  - id (PK)          │
│  - codigo (UNIQUE)   │                         │  - nombreCompleto   │
│  - paciente_ci       │                         │  - cedulaIdentidad  │
│  - prestador_rut     │                         │      (UNIQUE)       │
│  - fechaEmision      │                         │  - fechaNacimiento  │
│  - firmado           │                         │  - activo           │
│  - tipo              │                         └─────────────────────┘
│  - contenido         │
└──────────────────────┘

┌──────────────────────┐
│  TrabajadorSalud     │
│  - id (PK)           │
│  - cedula (UNIQUE)   │
│  - nombreCompleto    │
│  - fechaIngreso      │
│  - especialidad      │
└──────────────────────┘
```
