# Laboratorio

## Multitenant POC

Este proyecto incluye un **Proof of Concept (POC) de Multitenancy** que demuestra dos enfoques para implementar multi-inquilinato en aplicaciones JavaEE:

1. **Tabla única con Foreign Key a Tenant** (Implementado) ✅
2. **Multi-esquema con múltiples esquemas de BD** (Documentado) 📚

### 🚀 Acceso Rápido al POC

Después de desplegar la aplicación, accede a:
- **Gestión de Tenants:** `http://localhost:8080/Laboratorio-web/tenant`

### 📖 Documentación

- Ver documentación completa en: **[MULTITENANT_POC.md](MULTITENANT_POC.md)**
- Ejemplos de configuración en: **[docs/](docs/)**

### Características del POC

- ✅ Creación y gestión de tenants
- ✅ Selección de tenant activo por sesión
- ✅ Filtrado automático de datos por tenant
- ✅ Asignación automática de tenant al crear entidades
- ✅ UI actualizada con información de tenant