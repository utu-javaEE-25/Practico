# Guía de Uso del Sistema Multitenant

## ¿Qué es Multitenancy?

El sistema ahora soporta **múltiples prestadores de salud** (tenants) trabajando de forma aislada en la misma aplicación. Cada prestador solo puede ver y gestionar sus propios datos.

## Inicio Rápido

### 1. Crear un Prestador de Salud

Primero, necesitas crear al menos un prestador de salud:

1. Ve a: `http://localhost:8080/Laboratorio-web/prestadorSalud`
2. Completa el formulario:
   - **Nombre**: Ej. "Hospital Central"
   - **RUT**: Ej. "123456789012"
3. Click en "Crear"

### 2. Seleccionar el Tenant

Desde la página principal o agregando el parámetro en la URL:

```
http://localhost:8080/Laboratorio-web/index.xhtml?prestadorRUT=123456789012
```

O desde el formulario en `index.xhtml`:
1. Ingresa el RUT del prestador: `123456789012`
2. Click en "Establecer Tenant"

### 3. Gestionar Datos

Una vez seleccionado el tenant, todos tus datos quedarán asociados a ese prestador:

#### Usuarios de Servicios de Salud
- URL: `/usuarioServicioSalud`
- Los usuarios creados se asociarán automáticamente al prestador actual
- Solo verás usuarios de tu prestador

#### Trabajadores de Salud
- URL: `/vistas/TrabajadorSaludVista.xhtml`
- Los trabajadores se asociarán al prestador actual
- Solo verás trabajadores de tu prestador

#### Documentos Clínicos
- URL: `/documentoClinico`
- Los documentos se asociarán al prestador actual
- Solo verás documentos de tu prestador

## Ejemplos de Uso

### Ejemplo 1: Dos Prestadores Independientes

**Prestador A: Hospital Central (RUT: 111111111111)**
1. Seleccionar tenant: `?prestadorRUT=111111111111`
2. Crear usuario: "Juan Pérez"
3. Crear trabajador: "Dr. García"

**Prestador B: Clínica Sur (RUT: 222222222222)**
1. Seleccionar tenant: `?prestadorRUT=222222222222`
2. Crear usuario: "María López"
3. Crear trabajador: "Dra. Rodríguez"

**Resultado**: 
- Hospital Central solo ve a Juan Pérez y Dr. García
- Clínica Sur solo ve a María López y Dra. Rodríguez

### Ejemplo 2: Cambiar de Tenant

```
# Ver datos del Hospital Central
http://localhost:8080/Laboratorio-web/usuarioServicioSalud?prestadorRUT=111111111111

# Cambiar a Clínica Sur
http://localhost:8080/Laboratorio-web/usuarioServicioSalud?prestadorRUT=222222222222
```

## Indicadores Visuales

### Tenant Activo
Cuando hay un tenant seleccionado, verás un cuadro azul en la parte superior:
```
┌────────────────────────────────────────┐
│ Prestador actual (RUT): 123456789012  │
│ Solo verá datos de este prestador     │
└────────────────────────────────────────┘
```

### Sin Tenant
Sin tenant seleccionado, verás un cuadro rojo:
```
┌────────────────────────────────────────┐
│ ⚠ No hay prestador seleccionado       │
│ Verá todos los datos (modo admin)     │
└────────────────────────────────────────┘
```

## Preguntas Frecuentes

**P: ¿Puedo ver datos de otro prestador?**
R: No. El sistema solo muestra datos del prestador actual.

**P: ¿Qué pasa si no selecciono un tenant?**
R: Verás TODOS los datos de todos los prestadores (modo administrador).

**P: ¿El tenant se mantiene entre páginas?**
R: Sí, el tenant se guarda en la sesión y se mantiene mientras navegas.

**P: ¿Cómo cambio de tenant?**
R: Simplemente ingresa un nuevo RUT en el formulario o usa el parámetro `?prestadorRUT=` en la URL.

**P: ¿Puedo tener varios tenants en diferentes pestañas?**
R: Sí, cada pestaña/sesión del navegador puede tener un tenant diferente.

## Troubleshooting

### No veo mis datos
- Verifica que has seleccionado el tenant correcto
- Asegúrate de que el RUT es el mismo que usaste al crear los datos

### Veo datos de otros prestadores
- Probablemente no has seleccionado un tenant
- Verifica el cuadro de estado en la parte superior de la página

### Error al crear datos
- Verifica que el RUT del prestador existe
- Si no hay tenant seleccionado, los datos podrían no tener un prestador asociado
