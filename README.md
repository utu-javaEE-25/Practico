# Laboratorio - Health Services System

Sistema de gestión de servicios de salud con arquitectura JavaEE.

## Módulos

- **ejb**: Contiene la lógica de negocio, entidades y servicios
- **web**: Contiene las interfaces web (JSF, Servlets) y REST API
- **ear**: Empaqueta la aplicación completa

## REST API para Móviles

El proyecto incluye una REST API completa para permitir el acceso desde aplicaciones móviles. 

### Endpoints Disponibles

- `/api/usuarios` - Gestión de usuarios de servicios de salud
- `/api/trabajadores` - Gestión de trabajadores de salud
- `/api/prestadores` - Gestión de prestadores de salud
- `/api/documentos` - Gestión de documentos clínicos

Ver [API_DOCUMENTATION.md](API_DOCUMENTATION.md) para documentación completa de la API.

## Despliegue

Para compilar e instalar:
```bash
mvn clean install
```

Para desplegar en WildFly:
```bash
mvn wildfly:deploy
```

Para desplegar:
```bash
mvn wildfly:undeploy
```

## Acceso

- **Aplicación Web**: http://localhost:8080/Laboratorio-web/
- **REST API**: http://localhost:8080/Laboratorio-web/api

## Desarrollo

El proyecto usa:
- Jakarta EE 10
- WildFly 37
- Maven 3
- Java 17