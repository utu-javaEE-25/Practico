# Laboratorio - Sistema de Salud

Sistema de gestión para servicios de salud con funcionalidad de autenticación integrada.

## 🔐 Login con gub.uy

Este proyecto incluye autenticación con **gub.uy** (identidad digital del gobierno uruguayo).

### Características:
- ✅ Login con gub.uy (OAuth2 simulado)
- ✅ Login tradicional con cédula
- ✅ Filtro de autenticación global
- ✅ Gestión de sesiones
- ✅ Interfaz responsive moderna

### Inicio Rápido:

1. **Compilar el proyecto:**
   ```bash
   mvn clean package
   ```

2. **Desplegar en WildFly:**
   ```bash
   mvn wildfly:deploy
   ```

3. **Acceder a la aplicación:**
   ```
   http://localhost:8080/Laboratorio-web/
   ```

4. **Login:**
   - El sistema redirigirá automáticamente a `/login`
   - Opción 1: Clic en "Ingresar con gub.uy"
   - Opción 2: Ingresar cédula de usuario registrado

### Documentación:

- 📄 [LOGIN_GUBUY.md](LOGIN_GUBUY.md) - Documentación técnica completa
- 📄 [IMPLEMENTATION_SUMMARY.md](IMPLEMENTATION_SUMMARY.md) - Resumen de implementación

### Estructura del Proyecto:

```
Laboratorio/
├── ejb/              # Lógica de negocio (EJB)
├── web/              # Capa de presentación
│   ├── controladores/
│   │   ├── LoginServlet.java
│   │   └── LogoutServlet.java
│   ├── filtros/
│   │   └── AuthenticationFilter.java
│   └── webapp/
│       ├── login.jsp
│       └── index.xhtml
└── ear/              # Empaquetado de aplicación

```

### Requisitos:

- Java 17+
- Maven 3.6+
- WildFly 26+ (o compatible Jakarta EE 9+)

### Testing:

Para probar el login:
1. Crear un usuario en `/usuarioServicioSalud`
2. Navegar a cualquier página
3. Sistema redirige a login
4. Autenticarse con gub.uy o cédula

---

**Nota:** Esta es una implementación educativa. Para producción, integrar con el proveedor OAuth real de gub.uy.