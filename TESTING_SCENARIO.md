# Escenario de Prueba - POC Multitenancy

Este documento describe un escenario paso a paso para probar el POC de multitenancy.

## Escenario: Sistema de Salud Multi-Institución

Imagina que tienes un sistema de salud compartido por varias instituciones:
- **Mutualista del Centro** (MUTUALISTA_A)
- **Hospital Universitario** (HOSPITAL_B)
- **Clínica San José** (CLINICA_C)

Cada institución necesita:
- Mantener sus datos aislados de las otras
- Gestionar sus propios prestadores de salud
- Registrar sus propios pacientes/usuarios

## Pasos de Prueba

### 1. Desplegar la Aplicación

```bash
cd /ruta/al/proyecto
mvn clean install wildfly:deploy
```

### 2. Acceder al Sistema de Gestión de Tenants

Abrir en navegador: `http://localhost:8080/Laboratorio-web/tenant`

### 3. Crear los Tenants

**Tenant 1:**
- Código: `MUTUALISTA_A`
- Nombre: `Mutualista del Centro`
- Esquema: `mutualista_a` (opcional)

**Tenant 2:**
- Código: `HOSPITAL_B`
- Nombre: `Hospital Universitario`
- Esquema: `hospital_b` (opcional)

**Tenant 3:**
- Código: `CLINICA_C`
- Nombre: `Clínica San José`
- Esquema: `clinica_c` (opcional)

### 4. Trabajar con MUTUALISTA_A

#### 4.1 Seleccionar el Tenant
- Hacer clic en "Seleccionar" junto a MUTUALISTA_A
- Verificar que aparece el banner verde: "✅ Tenant activo: MUTUALISTA_A"

#### 4.2 Crear Prestadores para MUTUALISTA_A
Ir a "Prestadores de Salud" y crear:

1. **Prestador 1:**
   - Nombre: `Dr. Juan Pérez`
   - RUT: `123456`

2. **Prestador 2:**
   - Nombre: `Dra. María González`
   - RUT: `789012`

#### 4.3 Crear Usuarios para MUTUALISTA_A
Ir a "Usuarios de Servicio de Salud" y crear:

1. **Usuario 1:**
   - Nombre: `Carlos Rodríguez`
   - Cédula: `12345678`
   - Fecha Nacimiento: `1980-05-15`

2. **Usuario 2:**
   - Nombre: `Ana Martínez`
   - Cédula: `87654321`
   - Fecha Nacimiento: `1992-08-20`

#### 4.4 Verificar
- Todos los registros deben mostrar `MUTUALISTA_A` en la columna "Tenant"

### 5. Trabajar con HOSPITAL_B

#### 5.1 Cambiar de Tenant
- Volver a "Gestión de Tenants"
- Hacer clic en "Seleccionar" junto a HOSPITAL_B
- Verificar el cambio de tenant activo

#### 5.2 Verificar Aislamiento de Datos
- Ir a "Prestadores de Salud" → Debe estar vacío
- Ir a "Usuarios de Servicio de Salud" → Debe estar vacío

#### 5.3 Crear Datos para HOSPITAL_B
Crear prestadores:
1. `Dr. Roberto Silva` - RUT: `345678`
2. `Dra. Laura Fernández` - RUT: `901234`

Crear usuarios:
1. `Pedro López` - CI: `11111111` - Fecha: `1975-03-10`
2. `Sofía Torres` - CI: `22222222` - Fecha: `1988-11-25`

### 6. Trabajar con CLINICA_C

Repetir el proceso del paso 5 para CLINICA_C:

Prestadores:
1. `Dr. Miguel Ángel Castro` - RUT: `567890`

Usuarios:
1. `Valentina Suárez` - CI: `33333333` - Fecha: `1995-06-30`

### 7. Verificar Aislamiento Completo

#### 7.1 Vista por Tenant
Para cada tenant (A, B, C):
- Seleccionar el tenant
- Verificar que solo se ven sus propios datos
- Contar los registros (deberían coincidir con lo creado)

#### 7.2 Vista Global
- Hacer clic en "Limpiar selección de tenant"
- Verificar que ahora se ven TODOS los registros de todos los tenants
- La columna "Tenant" debe mostrar claramente a qué tenant pertenece cada registro

**Conteo esperado:**
- Prestadores: 5 total (2 + 2 + 1)
- Usuarios: 5 total (2 + 2 + 1)

### 8. Verificar Filtrado Automático

#### Test de Cambio Rápido:
1. Seleccionar MUTUALISTA_A → Debe mostrar 2 prestadores, 2 usuarios
2. Seleccionar HOSPITAL_B → Debe mostrar 2 prestadores, 2 usuarios
3. Seleccionar CLINICA_C → Debe mostrar 1 prestador, 1 usuario
4. Limpiar selección → Debe mostrar 5 prestadores, 5 usuarios

## Resultados Esperados

✅ **Aislamiento de Datos:** Cada tenant solo ve sus propios datos
✅ **Asignación Automática:** Los datos nuevos se asignan automáticamente al tenant activo
✅ **Sin Fugas de Datos:** No es posible ver datos de otro tenant
✅ **Vista Administrativa:** Sin tenant seleccionado, se pueden ver todos los datos para administración

## Escenarios Avanzados (Opcional)

### Test de Seguridad
1. Seleccionar MUTUALISTA_A
2. Intentar modificar manualmente un prestador para asignarle otro tenant
   - **Resultado esperado:** No es posible (el tenantId se asigna automáticamente en el servidor)

### Test de Sesión
1. Abrir dos navegadores o ventanas en modo incógnito
2. En navegador 1: Seleccionar MUTUALISTA_A
3. En navegador 2: Seleccionar HOSPITAL_B
4. Verificar que cada sesión mantiene su tenant independientemente

### Test de Búsqueda
1. Seleccionar un tenant
2. Usar las funciones de búsqueda (por RUT, nombre, etc.)
3. Verificar que la búsqueda solo busca dentro del tenant activo

## Troubleshooting

### Problema: No se filtran los datos
**Solución:** 
- Verificar que el TenantFilter está activo
- Revisar los logs de WildFly
- Asegurar que la sesión HTTP está activa

### Problema: Datos de otro tenant son visibles
**Solución:**
- Verificar la lógica de filtrado en los beans de persistencia
- Asegurar que el TenantContext está configurado correctamente

### Problema: No se puede crear un tenant
**Solución:**
- Verificar que el código del tenant es único
- Revisar validaciones en TenantPerBean

## Métricas de Éxito

El POC es exitoso si:
- ✅ Se pueden crear múltiples tenants
- ✅ Los datos se aíslan correctamente por tenant
- ✅ El cambio de tenant es instantáneo y funcional
- ✅ La asignación automática funciona correctamente
- ✅ La vista administrativa (sin tenant) muestra todos los datos

## Conclusión

Este escenario demuestra:
1. **Enfoque 1 (Tabla única + FK):** Completamente funcional
2. **Facilidad de uso:** Simple y directo
3. **Aislamiento efectivo:** Los datos están correctamente segregados
4. **Flexibilidad:** Fácil agregar nuevos tenants dinámicamente

Para información técnica detallada, ver: **MULTITENANT_POC.md**
