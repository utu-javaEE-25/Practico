# HCEN Central - Laboratorio TSE 2025

HCEN Central es la plataforma que integra la Historia Clinica Electronica Nacional. En un solo lugar se gestionan los padrones de prestadores, los identificadores unicos, el registro nacional de documentos clinicos y los paneles para ciudadanos y administradores. El objetivo de este laboratorio es mostrar el flujo extremo a extremo sin necesidad de conocer cada clase del proyecto.

## Que resuelve

- Mantiene actualizado el padron de prestadores y los datos tecnicos que necesita cada tenant (esquema, endpoint y secreto de integracion).
- Centraliza las notificaciones clinicas (RNDC) y permite que cualquier prestador consulte el indice antes de ir a buscar el documento a quien lo custodio.
- Permite que los administradores controlen altas de prestadores, den permisos a nuevos administradores y configuren endpoints perimetrales desde un panel unico.
- Ofrece a la ciudadania una bandeja simple para revisar solicitudes de acceso, aprobarlas y auditar quien vio cada documento.
- Registra cada operacion sensible para que luego puedan elaborarse reportes y detectar actividad sospechosa.

## Recorrido rapido

### Ciudadania

- Inicia sesion con su documento en ID Uruguay y llega a un panel donde ve su historia clinica indexada y las notificaciones pendientes.
- Desde la bandeja de solicitudes puede aceptar o rechazar el acceso de un profesional y definir por cuanto tiempo desea habilitarlo.
- En el historial de auditoria consulta quien vio cada documento y cuando lo hizo.

### Prestadores de salud

- Registran a su organizacion aportando RUT, nombre comercial y datos de contacto.
- Una vez aprobados, reciben su entorno aislado (schema propio), las credenciales del endpoint y la ruta que el sistema central utilizara para pedir documentos.
- Publican nuevos documentos clinicos enviando solo los metadatos al servicio central; el archivo real queda en su periferico.

### Administradores

- Dan de alta prestadores y administradores locales desde un unico tablero.
- Gestionan los endpoints publicados por cada tenant y definen si requieren secreto compartido o modo multitenant.
- Acceden a reportes con resumen de actividad: cantidad de logins, eventos recientes, altas de prestadores y alertas.

### Observabilidad y gobierno

- Todas las operaciones sensibles se registran con actor, hora, IP y resultado para facilitar auditorias posteriores.
- Los reportes consolidados permiten detectar fraudes o comportamientos inusuales sin revisar bases de datos manualmente.
- El modelo de permisos obliga a contar con una politica valida antes de entregar un documento clinico a terceros.

## API principales

| Endpoint | Metodo | Para que sirve |
| --- | --- | --- |
| `/api/prestadores` | GET/POST/PUT/DELETE | Gestiona los prestadores desde el centro (altas, bajas y actualizaciones). |
| `/api/receiver/metadata` | POST | Recibe los metadatos de cada documento que los prestadores generan en su entorno. |
| `/api/historia-clinica/{cedula}` | GET | Devuelve el indice RNDC para un ciudadano especifico. |
| `/api/historia-clinica/documento-externo` | GET | Entrega el documento al prestador que lo solicita si existe una politica vigente. |
| `/api/usuarios/ci/{cedula}` | GET | Permite validar si una persona ya figura en el padron global de usuarios. |
| `/api/accesos/solicitar` | POST | Registra solicitudes de acceso y dispara la notificacion al ciudadano. |

Todas las rutas REST se agrupan bajo `/api` y comparten la misma configuracion de seguridad.

## Ponerlo en marcha

1. **Prerequisitos**: JDK 21, Maven 3.9 o superior, WildFly 31+ y PostgreSQL accesible para los tres datasources (central, master y tenant).
2. **Preparar la base**: ejecutar los scripts `V1_DDL_HCEN.sql`, `V1__DDL_Tenant.sql` sobre la base central; el aprovisionamiento de cada tenant aplica sus propias migraciones automaticamente.
3. **Configurar identidad digital**: registrar el cliente de ID Uruguay, obtener `CLIENT_ID`, `CLIENT_SECRET` y `REDIRECT_URI`, y cargarlos en las variables de entorno o en la configuracion que use el servidor.
4. **Compilar**: correr `mvn clean install -DskipTests` desde la raiz para generar el EAR en `ear/target`.
5. **Desplegar**: utilizar `mvn wildfly:deploy -pl ear` o la consola de administracion de WildFly para subir `Laboratorio.ear`. Una vez desplegado, el portal se encuentra en `/Laboratorio/index.jsp`.


## Login con ID Uruguay

El proyecto incluye un componente que demuestra el inicio y cierre de sesion con ID Uruguay usando OpenID Connect. El flujo completo contempla:

1. El usuario elige iniciar sesion y se lo redirige al proveedor de identidad.
2. Al regresar, se validan `state` y `nonce`, se canjea el `code` por tokens y se crea la sesion local.
3. Durante el logout se avisa tanto al proveedor como a la aplicacion para cerrar todos los contextos.

Sirve como referencia para otras aplicaciones Jakarta EE que necesiten sumar autenticacion con ID Uruguay sin reescribir todo el flujo.

## Tecnologias y herramientas

- Java 21 y Jakarta EE para los servicios EJB, REST y Servlets.
- Maven multimodulo (`ejb`, `web`, `ear`) para empaquetar y desplegar en WildFly.
- PostgreSQL como base de datos central y por tenant, con migraciones orquestadas via Flyway.
- GitHub Actions y SonarCloud para compilacion, analisis y despliegue automatizado.

Con esta informacion deberias poder entender el alcance funcional del laboratorio y ponerlo a correr sin tener que revisar cada clase del codigo fuente.
