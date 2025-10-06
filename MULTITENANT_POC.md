# POC Multitenancy - Documentación

## Resumen

Este Proof of Concept (POC) implementa y demuestra dos enfoques principales de multitenancy en aplicaciones JavaEE:

1. **Enfoque 1: Tabla única con Foreign Key (FK) a Tenant** - Implementado completamente
2. **Enfoque 2: Multi-esquema con múltiples esquemas en una BD** - Configuración de ejemplo

## ¿Qué es Multitenancy?

Multitenancy (multi-inquilinato) es un patrón arquitectónico donde una sola instancia de software sirve a múltiples clientes (tenants/inquilinos). Cada tenant tiene sus datos aislados de los demás, aunque compartan la misma infraestructura.

### Ejemplos de uso:
- Sistemas de salud donde cada hospital o mutualista es un tenant
- Software SaaS donde cada empresa cliente es un tenant
- Plataformas educativas donde cada universidad es un tenant

## Enfoques Implementados

### Enfoque 1: Tabla Única con FK a Tenant (Implementado)

**Descripción:** Todos los tenants comparten las mismas tablas de base de datos. Cada registro tiene un campo `tenantId` que identifica a qué tenant pertenece.

**Ventajas:**
- ✅ Más simple de implementar y mantener
- ✅ Fácil escalabilidad
- ✅ Menor overhead de infraestructura
- ✅ Backups y migraciones más simples

**Desventajas:**
- ❌ Mayor riesgo de "data leakage" si hay bugs
- ❌ Todos los tenants comparten recursos de BD

**Implementación en este POC:**

1. **Entidad Tenant** (`Tenant.java`):
   - Representa un inquilino del sistema
   - Campos: id, codigo, nombre, esquema, activo

2. **Campos tenantId en entidades principales**:
   - `PrestadorSalud.tenantId`
   - `UsuarioServicioSalud.tenantId`
   - `DocumentoClinico.tenantId`
   - `TrabajadorSalud.tenantId`

3. **TenantContext** (`TenantContext.java`):
   - Mantiene el tenant actual usando ThreadLocal
   - Permite acceso al tenant actual en cualquier capa de la aplicación

4. **TenantFilter** (`TenantFilter.java`):
   - Filtro web que establece el tenant desde la sesión HTTP
   - Se ejecuta en cada request y limpia el contexto al terminar

5. **Persistencia con filtrado automático**:
   - `PrestadorSaludPerBean.listar()` filtra por tenantId
   - `UsuarioServicioSaludPerBean.listar()` filtra por tenantId
   - Uso de Java Streams para filtrado eficiente

6. **Servicio con asignación automática**:
   - `PrestadorSaludServiceBean.crear()` asigna el tenant actual
   - `UsuarioServicioSaludServiceBean.crear()` asigna el tenant actual

7. **Interfaz de usuario**:
   - `/tenant` - Gestión de tenants y selección del tenant activo
   - Páginas actualizadas muestran el tenant actual y la columna de tenant

## Estructura del Código

```
ejb/src/main/java/uy/edu/fing/tse/
├── entidades/
│   ├── Tenant.java                     # Nueva: Entidad Tenant
│   ├── PrestadorSalud.java            # Modificada: +tenantId
│   ├── UsuarioServicioSalud.java      # Modificada: +tenantId
│   ├── DocumentoClinico.java          # Modificada: +tenantId
│   └── TrabajadorSalud.java           # Modificada: +tenantId
├── multitenant/                        # Nuevo paquete
│   └── TenantContext.java             # Nueva: Contexto de tenant
├── api/
│   └── TenantPerLocal.java            # Nueva: API de persistencia
├── persistencia/
│   ├── TenantPerBean.java             # Nueva: Persistencia de Tenant
│   ├── PrestadorSaludPerBean.java     # Modificada: Filtrado por tenant
│   └── UsuarioServicioSaludPerBean.java # Modificada: Filtrado por tenant
└── servicios/
    ├── PrestadorSaludServiceBean.java # Modificada: Asigna tenant
    └── UsuarioServicioSaludServiceBean.java # Modificada: Asigna tenant

web/src/main/java/uy/edu/fing/tse/
├── filters/
│   └── TenantFilter.java              # Nuevo: Filtro de tenant
└── controladores/
    └── TenantServlet.java             # Nuevo: Gestión de tenants

web/src/main/webapp/vistas/
├── tenant.jsp                          # Nueva: UI de gestión
├── prestadorSalud.jsp                 # Modificada: Muestra tenant
└── usuarioServicioSalud.jsp           # Modificada: Muestra tenant
```

## Cómo Usar el POC

### 1. Compilar y Desplegar

```bash
mvn clean install wildfly:deploy
```

### 2. Acceder a la Aplicación

Navegar a: `http://localhost:8080/Laboratorio-web/tenant`

### 3. Probar el Enfoque 1 (Tabla Única con FK)

**Paso 1: Crear Tenants**
1. Ir a la página de gestión de tenants
2. Crear varios tenants, por ejemplo:
   - Código: `MUTUALISTA_A`, Nombre: `Mutualista del Centro`
   - Código: `HOSPITAL_B`, Nombre: `Hospital Universitario`
   - Código: `CLINICA_C`, Nombre: `Clínica San José`

**Paso 2: Seleccionar un Tenant**
1. Hacer clic en "Seleccionar" junto a `MUTUALISTA_A`
2. Se guardará en la sesión HTTP

**Paso 3: Crear Datos para el Tenant**
1. Ir a "Prestadores de Salud"
2. Crear algunos prestadores (ej: "Dr. Juan Pérez", RUT: "123456")
3. Ir a "Usuarios de Servicio de Salud"
4. Crear algunos usuarios

**Paso 4: Verificar el Filtrado**
1. Notar que todos los registros muestran `MUTUALISTA_A` en la columna Tenant
2. Cambiar al tenant `HOSPITAL_B`
3. Ver que la lista está vacía (no hay datos para ese tenant)
4. Crear algunos registros para `HOSPITAL_B`

**Paso 5: Ver Todos los Datos**
1. Hacer clic en "Limpiar selección de tenant"
2. Ahora verás registros de todos los tenants mezclados
3. La columna "Tenant" muestra a qué tenant pertenece cada registro

## Enfoque 2: Multi-esquema (Configuración de Ejemplo)

**Descripción:** Cada tenant tiene su propio esquema en la base de datos. Los datos están físicamente separados.

**Ventajas:**
- ✅ Mayor aislamiento de datos
- ✅ Más fácil cumplir con regulaciones de privacidad
- ✅ Cada tenant puede tener configuraciones específicas

**Desventajas:**
- ❌ Más complejo de implementar
- ❌ Más overhead de gestión
- ❌ Migraciones y backups más complejos

**Configuración de Ejemplo para WildFly:**

### Paso 1: Definir Datasources por Tenant

En `standalone.xml` o usando CLI:

```xml
<datasource jndi-name="java:/jdbc/TenantA" pool-name="TenantADS">
    <connection-url>jdbc:h2:mem:tenantA;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE</connection-url>
    <driver>h2</driver>
    <security>
        <user-name>sa</user-name>
        <password>sa</password>
    </security>
</datasource>

<datasource jndi-name="java:/jdbc/TenantB" pool-name="TenantBDS">
    <connection-url>jdbc:h2:mem:tenantB;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE</connection-url>
    <driver>h2</driver>
    <security>
        <user-name>sa</user-name>
        <password>sa</password>
    </security>
</datasource>
```

### Paso 2: Múltiples Persistence Units

En `persistence.xml`:

```xml
<persistence-unit name="TenantAPU">
    <jta-data-source>java:/jdbc/TenantA</jta-data-source>
    <!-- ... -->
</persistence-unit>

<persistence-unit name="TenantBPU">
    <jta-data-source>java:/jdbc/TenantB</jta-data-source>
    <!-- ... -->
</persistence-unit>
```

### Paso 3: Selector de EntityManager

```java
@Stateless
public class MultiSchemaDAO {
    
    @PersistenceContext(unitName = "TenantAPU")
    private EntityManager emTenantA;
    
    @PersistenceContext(unitName = "TenantBPU")
    private EntityManager emTenantB;
    
    private EntityManager getEntityManager() {
        String tenant = TenantContext.getCurrentTenant();
        switch(tenant) {
            case "TENANT_A": return emTenantA;
            case "TENANT_B": return emTenantB;
            default: throw new IllegalStateException("Unknown tenant");
        }
    }
}
```

**Nota:** Este enfoque requiere conocer todos los tenants en tiempo de despliegue, lo que lo hace menos flexible que el Enfoque 1.

## Comparación de Enfoques

| Característica | Tabla Única + FK | Multi-esquema |
|----------------|------------------|---------------|
| **Aislamiento** | Lógico | Físico |
| **Seguridad** | Media | Alta |
| **Complejidad** | Baja | Alta |
| **Escalabilidad** | Excelente | Buena |
| **Mantenimiento** | Simple | Complejo |
| **Costo** | Bajo | Medio/Alto |
| **Migración de datos** | Fácil | Difícil |
| **Flexibilidad** | Alta (tenants dinámicos) | Baja (requiere reconfiguración) |

## Consideraciones de Seguridad

### Para Enfoque 1 (Tabla Única):

1. **Siempre validar tenantId:**
   ```java
   if (!entity.getTenantId().equals(TenantContext.getCurrentTenant())) {
       throw new SecurityException("Acceso denegado");
   }
   ```

2. **Usar índices en tenantId:**
   ```sql
   CREATE INDEX idx_tenant ON prestador_salud(tenant_id);
   ```

3. **Tests exhaustivos:** Asegurar que el filtrado funciona en todos los casos

4. **Auditoría:** Log de accesos cross-tenant

### Para Enfoque 2 (Multi-esquema):

1. **Validar permisos de esquema** en la BD
2. **Usar usuarios de BD diferentes** por tenant
3. **Implementar circuit breakers** para fallas de conexión

## Testing del POC

### Test Manual:

1. ✅ Crear 3 tenants diferentes
2. ✅ Seleccionar Tenant A y crear 2 prestadores y 2 usuarios
3. ✅ Seleccionar Tenant B y crear 1 prestador y 1 usuario
4. ✅ Verificar que al cambiar de tenant solo se ven los datos correspondientes
5. ✅ Limpiar selección y verificar que se ven todos los datos
6. ✅ Verificar que la columna Tenant muestra el tenant correcto

### Test de Seguridad:

1. Intentar manipular el tenantId en forms (debe fallar - se asigna automáticamente)
2. Verificar que no se pueden ver datos de otro tenant

## Próximos Pasos (Fuera del Alcance del POC)

1. **JPA/Hibernate Filters:** Usar `@Filter` para filtrado automático a nivel de BD
2. **Interceptores:** Validar tenant en todas las operaciones
3. **Caché por Tenant:** Implementar caché separado por tenant
4. **Métricas:** Monitorear uso por tenant
5. **Multi-base de datos:** Un tenant por BD completa (no solo esquema)

## Referencias

- [Multi-Tenancy in Java EE](https://www.baeldung.com/hibernate-5-multitenancy)
- [WildFly Datasource Configuration](https://docs.wildfly.org/31/Admin_Guide.html#DataSource)
- [JPA Multi-Tenancy](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#multitenacy)

## Conclusión

Este POC demuestra exitosamente el enfoque de **tabla única con FK a tenant**, que es:
- ✅ Más práctico para la mayoría de los casos
- ✅ Fácil de implementar y mantener
- ✅ Suficientemente seguro con las validaciones adecuadas
- ✅ Escalable y flexible

El enfoque de **multi-esquema** se documenta para referencia, pero requiere configuración de servidor específica que está fuera del alcance de este POC básico.
