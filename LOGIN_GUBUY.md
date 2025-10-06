# Login con gub.uy - Documentación

## Descripción

Este módulo implementa la funcionalidad de autenticación para el Sistema de Salud, incluyendo soporte para login con **gub.uy** (identidad digital del gobierno uruguayo) y login tradicional con cédula.

## Características Implementadas

### 1. Página de Login (`/login.jsp`)
- Interfaz moderna y responsiva
- Dos métodos de autenticación:
  - **Login con gub.uy**: Botón destacado para autenticación con identidad digital
  - **Login tradicional**: Formulario con cédula y contraseña

### 2. Servlet de Login (`LoginServlet.java`)
- Maneja ambos tipos de autenticación
- Valida credenciales
- Crea sesiones de usuario
- Redirige al menú principal tras autenticación exitosa

#### Login con gub.uy
En una implementación de producción, este método:
1. Redirigiría al proveedor OAuth de gub.uy
2. Manejaría el callback con el token de autenticación
3. Validaría el token y obtendría los datos del usuario

**Implementación actual (educativa):**
- Simula el flujo OAuth
- Autentica con el primer usuario activo del sistema
- Marca la sesión con método "gub.uy"

#### Login Tradicional
- Valida la cédula de identidad (7-8 dígitos)
- Busca el usuario en el sistema por cédula
- Verifica que el usuario esté activo
- En producción, verificaría contraseña hasheada

### 3. Servlet de Logout (`LogoutServlet.java`)
- Invalida la sesión del usuario
- Redirige a la página de login

### 4. Filtro de Autenticación (`AuthenticationFilter.java`)
- Protege todas las páginas de la aplicación
- Permite acceso libre a `/login` y `/logout`
- Redirige usuarios no autenticados al login
- Verifica la sesión en cada petición

### 5. Menú Principal Actualizado (`index.xhtml`)
- Muestra información del usuario autenticado:
  - Nombre completo
  - Cédula de identidad
  - Método de login utilizado
- Botón para cerrar sesión

## Flujo de Autenticación

```
1. Usuario accede a la aplicación
   ↓
2. AuthenticationFilter verifica sesión
   ↓
3. Si no está autenticado → redirige a /login
   ↓
4. Usuario selecciona método de login
   ↓
5. LoginServlet procesa credenciales
   ↓
6. Si es válido → crea sesión y redirige a index.xhtml
   ↓
7. Usuario puede navegar por la aplicación
   ↓
8. Al hacer logout → sesión se invalida
```

## Configuración

### URLs
- **Login**: `/login` o `/login.jsp`
- **Logout**: `/logout`
- **Menú principal**: `/index.xhtml` (protegido)

### Sesión
- Duración: 30 minutos de inactividad
- Atributos almacenados:
  - `usuario`: Objeto `UsuarioServicioSalud`
  - `loginMethod`: "gub.uy" o "traditional"

## Testing

### Prerrequisitos
1. Tener al menos un usuario activo en el sistema
2. Crear usuario mediante `/usuarioServicioSalud` antes de probar el login

### Escenarios de Prueba

#### 1. Login con gub.uy
```
- Acceder a /login
- Hacer clic en "Ingresar con gub.uy"
- El sistema autentica con el primer usuario activo
- Redirige al menú principal mostrando datos del usuario
```

#### 2. Login tradicional
```
- Acceder a /login
- Ingresar cédula de un usuario registrado (ej: 12345678)
- Ingresar cualquier contraseña (no validada en versión educativa)
- Sistema busca usuario por cédula
- Si existe y está activo → autentica
- Redirige al menú principal
```

#### 3. Protección de páginas
```
- Intentar acceder a /index.xhtml sin login
- Sistema redirige automáticamente a /login
- Tras autenticarse, acceso permitido
```

#### 4. Logout
```
- Estando autenticado, hacer clic en "Cerrar Sesión"
- Sesión se invalida
- Redirige a página de login
- Intentar acceder a páginas protegidas → redirige a login
```

## Estructura de Archivos

```
web/src/main/
├── java/uy/edu/fing/tse/
│   ├── controladores/
│   │   ├── LoginServlet.java          # Maneja autenticación
│   │   └── LogoutServlet.java         # Maneja cierre de sesión
│   └── filtros/
│       └── AuthenticationFilter.java  # Protege páginas
└── webapp/
    ├── login.jsp                      # Página de login
    └── index.xhtml                    # Menú principal (actualizado)
```

## Mejoras para Producción

### Seguridad
1. **Passwords**: Implementar hashing con bcrypt o similar
2. **gub.uy OAuth**: Integrar con el proveedor real de gub.uy
3. **HTTPS**: Forzar conexiones seguras
4. **CSRF**: Agregar tokens anti-CSRF
5. **Rate limiting**: Prevenir ataques de fuerza bruta

### Funcionalidad
1. **Recordar sesión**: Implementar "remember me"
2. **Recuperación de contraseña**: Sistema de reset
3. **Doble factor**: Autenticación 2FA
4. **Logs de auditoría**: Registrar intentos de login
5. **Roles y permisos**: Control de acceso granular

### Integración Real con gub.uy

Para integrar con el sistema real de gub.uy:

1. **Registrar aplicación** en el portal de desarrolladores de gub.uy
2. **Obtener credenciales**: client_id y client_secret
3. **Configurar endpoints**:
   - Authorization: `https://auth.gub.uy/oidc/v1/authorize`
   - Token: `https://auth.gub.uy/oidc/v1/token`
   - UserInfo: `https://auth.gub.uy/oidc/v1/userinfo`
4. **Implementar flujo OAuth2/OIDC**:
   - Redirigir a gub.uy para autenticación
   - Recibir código de autorización
   - Intercambiar código por token de acceso
   - Obtener información del usuario
5. **Validar tokens JWT** recibidos

## Compatibilidad

- Jakarta EE 9+
- WildFly 26+
- Java 17+

## Notas Importantes

⚠️ **Esta es una implementación educativa**. La validación de contraseñas y la integración con gub.uy están simuladas para propósitos de aprendizaje.

✅ **Para uso en producción**, se debe implementar:
- Autenticación real con gub.uy OAuth
- Almacenamiento seguro de contraseñas (hash + salt)
- Validación de tokens JWT
- Manejo de sesiones distribuidas (para clusters)
- Logs de seguridad y auditoría
