# Proyecto Multitenant - Implementación

Este proyecto implementa un sistema multitenant para la gestión de prestadores de salud, usuarios, trabajadores y documentos clínicos.

## Concepto de Multitenancy

El sistema permite que múltiples prestadores de salud (tenants) compartan la misma aplicación mientras mantienen sus datos completamente aislados. Cada prestador de salud se identifica por su RUT único.

## Arquitectura

### Componentes Clave

1. **TenantContext** (`ejb/src/main/java/uy/edu/fing/tse/util/TenantContext.java`)
   - Utilidad que gestiona el contexto del tenant actual usando ThreadLocal
   - Almacena el RUT del prestador actual por cada request

2. **TenantFilter** (`web/src/main/java/uy/edu/fing/tse/filtros/TenantFilter.java`)
   - Filtro servlet que intercepta todas las peticiones
   - Establece el tenant actual basado en:
     - Parámetro de request `prestadorRUT`
     - Atributo de sesión `currentTenantRUT`
   - Limpia el contexto al finalizar cada request

### Entidades Actualizadas

Todas las entidades ahora incluyen el campo `prestadorRUT` para asociación con el tenant:

- **UsuarioServicioSalud**: Usuarios del servicio de salud
- **TrabajadorSalud**: Empleados del prestador
- **DocumentoClinico**: Documentos clínicos (ya tenía el campo)

### Capa de Persistencia

Los siguientes beans de persistencia filtran automáticamente los datos por tenant:

- **UsuarioServicioSaludPerBean**: Filtra usuarios por `prestadorRUT`
- **TrabajadorSaludDAO**: Filtra trabajadores por `prestadorRUT`
- **DocumentoClinicoPerBean**: Filtra documentos por `prestadorRUT`
- **PrestadorSaludPerBean**: No se filtra (gestiona los propios tenants)

## Uso del Sistema

### Seleccionar un Tenant

1. Acceder a la página principal (`index.xhtml`)
2. Ingresar el RUT del prestador en el formulario
3. El sistema recordará el tenant seleccionado en la sesión

### Cambiar de Tenant

Para cambiar de tenant, simplemente ingrese un nuevo RUT:
```
?prestadorRUT=123456789012
```

### Vistas por Tenant

Una vez seleccionado un tenant:
- **Usuarios**: Solo verá usuarios asociados a ese prestador
- **Trabajadores**: Solo verá trabajadores de ese prestador
- **Documentos Clínicos**: Solo verá documentos emitidos por ese prestador
- **Prestadores**: Verá todos los prestadores (para seleccionar tenant)

## Flujo de Trabajo

1. **Crear Prestador** (Admin)
   - Ir a "Administrar Prestadores de Salud"
   - Crear un nuevo prestador con su RUT

2. **Seleccionar Tenant** (Usuario)
   - En la página principal, ingresar el RUT del prestador
   - El sistema establece el contexto de tenant

3. **Gestionar Datos** (Usuario)
   - Crear usuarios, trabajadores o documentos
   - El sistema automáticamente asocia los datos al tenant actual
   - Solo se visualizan datos del tenant actual

## Características de Seguridad

- **Aislamiento de Datos**: Los datos de diferentes tenants están completamente aislados
- **Filtrado Automático**: La capa de persistencia filtra automáticamente por tenant
- **Validación**: Todos los métodos de consulta verifican el tenant actual

## Modo Sin Tenant

Si no hay tenant seleccionado (`prestadorRUT` es null):
- Se mostrarán TODOS los datos (modo administrador)
- Una advertencia visual indica que no hay tenant seleccionado
- Útil para tareas de administración general

## Desarrollo Futuro

Posibles mejoras:
- Autenticación y autorización por tenant
- Dashboard de administración de tenants
- Migración de datos entre tenants
- Auditoría de acceso por tenant
- Límites de recursos por tenant
