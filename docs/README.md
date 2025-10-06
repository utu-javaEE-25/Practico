# Documentación de Configuración Multitenant

Este directorio contiene archivos de ejemplo para configurar el enfoque de multi-esquema/multi-database para multitenancy.

## Archivos

### persistence-multischema-example.xml
Ejemplo de cómo configurar múltiples persistence units en `persistence.xml`, cada uno apuntando a un datasource diferente para cada tenant.

**Uso:** Copiar las configuraciones relevantes a `ejb/src/main/resources/META-INF/persistence.xml`

### wildfly-datasources-example.xml
Ejemplo de cómo configurar datasources en WildFly para cada tenant.

**Uso:** 
1. Agregar las configuraciones a `WILDFLY_HOME/standalone/configuration/standalone.xml`
2. O usar los comandos CLI incluidos en el archivo

## Notas Importantes

⚠️ **Estos son archivos de ejemplo educativos**. El POC implementado usa el enfoque de **tabla única con FK a tenant**, que es más simple y práctico.

El enfoque de multi-esquema requiere:
- Configuración de servidor específica
- Conocimiento previo de todos los tenants
- Mayor complejidad de mantenimiento

Para la mayoría de los casos, **se recomienda usar el Enfoque 1 (tabla única con FK)** implementado en el POC.

## Comparación Rápida

| Enfoque | Complejidad | Flexibilidad | Implementado |
|---------|-------------|--------------|--------------|
| Tabla única + FK | Baja | Alta | ✅ Sí |
| Multi-esquema | Alta | Media | 📚 Solo docs |

Ver `MULTITENANT_POC.md` en la raíz del proyecto para documentación completa.
