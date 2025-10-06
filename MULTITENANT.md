# Esquema de Base de Datos Multi-Tenant

## Descripción General

Este proyecto implementa un esquema de base de datos multi-tenant (multi-inquilino) donde cada **PrestadorSalud** (Proveedor de Salud) actúa como un tenant independiente. Esto permite que múltiples proveedores de salud utilicen la misma aplicación mientras mantienen sus datos completamente aislados.

## Arquitectura Multi-Tenant

### Concepto de Tenant

En este sistema, un **tenant** es un **PrestadorSalud** identificado de manera única por su **RUT** (Registro Único Tributario). Cada proveedor de salud puede tener:

- Sus propios usuarios afiliados
- Sus propios trabajadores de salud
- Sus propios documentos clínicos

### Patrón de Diseño

Se utiliza el patrón **Shared Database, Shared Schema** con discriminador de tenant:

- **Una sola base de datos** para todos los tenants
- **Tablas compartidas** entre todos los tenants
- **Campo discriminador** (`prestador_rut`) para aislar los datos de cada tenant

## Estructura de Tablas

### Tabla Principal (Tenant)

#### `prestador_salud`
Tabla que representa los tenants del sistema.

| Campo       | Tipo         | Descripción                          |
|-------------|--------------|--------------------------------------|
| id          | BIGINT (PK)  | Identificador único del prestador    |
| nombre      | VARCHAR(100) | Nombre del prestador                 |
| rut         | VARCHAR(12)  | RUT único del prestador (UNIQUE)     |
| fecha_alta  | DATE         | Fecha de alta en el sistema          |
| activo      | BOOLEAN      | Estado del prestador                 |

### Tablas con Datos del Tenant

#### `usuario_servicio_salud`
Usuarios/pacientes afiliados a un prestador.

| Campo              | Tipo         | Descripción                          |
|--------------------|--------------|--------------------------------------|
| id                 | BIGINT (PK)  | Identificador único del usuario      |
| nombre_completo    | VARCHAR(200) | Nombre completo del usuario          |
| cedula_identidad   | VARCHAR(8)   | Cédula de identidad (UNIQUE)         |
| fecha_nacimiento   | DATE         | Fecha de nacimiento                  |
| activo             | BOOLEAN      | Estado del usuario                   |
| **prestador_rut**  | VARCHAR(12)  | **RUT del prestador (FK, TENANT)**   |

#### `trabajador_salud`
Empleados de salud de un prestador.

| Campo              | Tipo         | Descripción                          |
|--------------------|--------------|--------------------------------------|
| id                 | BIGINT (PK)  | Identificador único del trabajador   |
| cedula             | VARCHAR(8)   | Cédula del trabajador (UNIQUE)       |
| nombre_completo    | VARCHAR(200) | Nombre completo                      |
| fecha_ingreso      | DATE         | Fecha de ingreso                     |
| especialidad       | VARCHAR(100) | Especialidad médica                  |
| **prestador_rut**  | VARCHAR(12)  | **RUT del prestador (FK, TENANT)**   |

#### `documento_clinico`
Documentos clínicos generados por un prestador.

| Campo              | Tipo         | Descripción                          |
|--------------------|--------------|--------------------------------------|
| id                 | BIGINT (PK)  | Identificador único del documento    |
| codigo             | VARCHAR(50)  | Código único del documento (UNIQUE)  |
| paciente_ci        | VARCHAR(8)   | Cédula del paciente                  |
| **prestador_rut**  | VARCHAR(12)  | **RUT del prestador (FK, TENANT)**   |
| fecha_emision      | DATE         | Fecha de emisión del documento       |
| firmado            | BOOLEAN      | Estado de firma                      |
| tipo               | VARCHAR(50)  | Tipo de documento                    |
| contenido          | TEXT         | Contenido del documento              |

## Índices para Optimización

Para optimizar las consultas multi-tenant, se crean índices en:

1. `prestador_salud.rut` - Búsqueda rápida de tenants
2. `usuario_servicio_salud.prestador_rut` - Filtrado por tenant
3. `trabajador_salud.prestador_rut` - Filtrado por tenant
4. `documento_clinico.prestador_rut` - Filtrado por tenant

## Claves Foráneas (Foreign Keys)

Todas las tablas de datos de tenant tienen una FK a `prestador_salud(rut)`:

```sql
FOREIGN KEY (prestador_rut) REFERENCES prestador_salud(rut)
```

Esto garantiza:
- **Integridad referencial**: No se pueden crear datos para un prestador inexistente
- **Cascada (opcional)**: Si se elimina un prestador, se pueden eliminar sus datos relacionados

## Implementación JPA

### Anotaciones de Entidades

Todas las entidades están anotadas con JPA:

```java
@Entity
@Table(name = "nombre_tabla")
public class Entidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "prestador_rut", nullable = false)
    private String prestadorRUT;  // Campo de tenant
    
    // ... otros campos
}
```

### Persistence Unit

El archivo `persistence.xml` lista todas las entidades:

```xml
<persistence-unit name="LaboratorioPersistenceUnit">
    <class>uy.edu.fing.tse.entidades.PrestadorSalud</class>
    <class>uy.edu.fing.tse.entidades.DocumentoClinico</class>
    <class>uy.edu.fing.tse.entidades.TrabajadorSalud</class>
    <class>uy.edu.fing.tse.entidades.UsuarioServicioSalud</class>
    <!-- ... -->
</persistence-unit>
```

## Consideraciones de Seguridad

### Aislamiento de Datos

1. **Cada consulta debe filtrar por `prestador_rut`**:
   ```java
   // Correcto
   entityManager.createQuery(
       "SELECT t FROM TrabajadorSalud t WHERE t.prestadorRUT = :rut")
       .setParameter("rut", currentTenantRUT)
       .getResultList();
   ```

2. **La aplicación debe mantener el contexto del tenant actual**:
   - En sesión de usuario
   - En token de autenticación
   - En contexto de transacción

3. **Validar que el usuario tiene acceso al tenant**:
   - Verificar que el usuario pertenece al prestador
   - Rechazar operaciones cross-tenant

### Ventajas del Patrón Shared Database

✅ **Pros:**
- Un solo servidor de BD
- Mantenimiento simplificado
- Backup centralizado
- Costos reducidos

⚠️ **Contras:**
- Requiere disciplina en queries (siempre filtrar por tenant)
- Riesgo de fuga de datos si se olvida el filtro
- Performance puede degradarse con muchos tenants

## Generación del Schema

El schema se genera automáticamente al desplegar la aplicación gracias a la configuración en `persistence.xml`:

```xml
<property name="jakarta.persistence.schema-generation.database.action" 
          value="drop-and-create"/>
```

### Para producción:

Cambiar a:
```xml
<property name="jakarta.persistence.schema-generation.database.action" 
          value="validate"/>
```

Y ejecutar el script SQL manualmente.

## Archivos Relacionados

- **Entidades**: `ejb/src/main/java/uy/edu/fing/tse/entidades/`
  - `PrestadorSalud.java`
  - `DocumentoClinico.java`
  - `TrabajadorSalud.java`
  - `UsuarioServicioSalud.java`

- **Configuración**: `ejb/src/main/resources/META-INF/persistence.xml`

- **Schema SQL**: `ejb/src/main/resources/META-INF/schema-multitenant.sql`

## Ejemplo de Uso

### Crear un Prestador (Tenant)

```java
PrestadorSalud prestador = new PrestadorSalud();
prestador.setNombre("Hospital Central");
prestador.setRut("123456789012");
prestador.setFechaAlta(LocalDate.now());
prestador.setActivo(true);
entityManager.persist(prestador);
```

### Crear un Trabajador para el Prestador

```java
TrabajadorSalud trabajador = new TrabajadorSalud();
trabajador.setCedula("12345678");
trabajador.setNombreCompleto("Dr. Juan Pérez");
trabajador.setEspecialidad("Cardiología");
trabajador.setPrestadorRUT("123456789012");  // ← Asignar al tenant
entityManager.persist(trabajador);
```

### Consultar Datos del Tenant

```java
// Obtener todos los trabajadores de un prestador específico
List<TrabajadorSalud> trabajadores = entityManager
    .createQuery("SELECT t FROM TrabajadorSalud t WHERE t.prestadorRUT = :rut",
                 TrabajadorSalud.class)
    .setParameter("rut", "123456789012")
    .getResultList();
```

## Próximos Pasos

1. ✅ Entidades JPA definidas
2. ✅ Schema multi-tenant documentado
3. ⚠️ Implementar filtros automáticos de tenant (EntityListener o Interceptor)
4. ⚠️ Agregar validaciones en capa de servicio
5. ⚠️ Configurar auditoría por tenant
6. ⚠️ Implementar pruebas de aislamiento de datos
