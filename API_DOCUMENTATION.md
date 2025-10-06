# REST API Documentation - Mobile Project

## Overview

This REST API provides mobile access to the health services system. All endpoints return JSON responses and accept JSON request bodies.

**Base URL**: `http://localhost:8080/Laboratorio-web/api`

## Authentication

Currently, the API does not require authentication. This should be implemented before production use.

## Common Response Codes

- `200 OK` - Request successful
- `201 Created` - Resource created successfully
- `204 No Content` - Resource deleted successfully
- `400 Bad Request` - Invalid request data
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

## Error Response Format

All error responses follow this format:
```json
{
  "error": "Error message description"
}
```

---

## 1. Usuarios de Servicio de Salud (Health Service Users)

### List All Users
```
GET /api/usuarios
```

**Response:**
```json
[
  {
    "id": 1,
    "nombreCompleto": "Juan Pérez",
    "cedulaIdentidad": "12345678",
    "fechaNacimiento": "1990-01-15",
    "activo": true
  }
]
```

### Get User by Cedula
```
GET /api/usuarios/{ci}
```

**Parameters:**
- `ci` (path) - Cedula de Identidad

**Response:**
```json
{
  "id": 1,
  "nombreCompleto": "Juan Pérez",
  "cedulaIdentidad": "12345678",
  "fechaNacimiento": "1990-01-15",
  "activo": true
}
```

### Create User
```
POST /api/usuarios
Content-Type: application/json
```

**Request Body:**
```json
{
  "nombreCompleto": "Juan Pérez",
  "cedulaIdentidad": "12345678",
  "fechaNacimiento": "1990-01-15",
  "activo": true
}
```

**Response:** `201 Created` with created user object

### Update User
```
PUT /api/usuarios/{ci}
Content-Type: application/json
```

**Parameters:**
- `ci` (path) - Cedula de Identidad

**Request Body:**
```json
{
  "nombreCompleto": "Juan Pérez Actualizado",
  "fechaNacimiento": "1990-01-15",
  "activo": true
}
```

**Response:** `200 OK` with updated user object

### Delete User
```
DELETE /api/usuarios/{ci}
```

**Parameters:**
- `ci` (path) - Cedula de Identidad

**Response:** `204 No Content`

---

## 2. Trabajadores de Salud (Health Workers)

### List All Workers
```
GET /api/trabajadores
```

**Response:**
```json
[
  {
    "id": 1,
    "cedula": "87654321",
    "nombreCompleto": "Dra. María González",
    "fechaIngreso": "2020-05-10",
    "especialidad": "Cardiología"
  }
]
```

### Get Worker by Cedula
```
GET /api/trabajadores/{cedula}
```

**Parameters:**
- `cedula` (path) - Cedula del trabajador

**Response:**
```json
{
  "id": 1,
  "cedula": "87654321",
  "nombreCompleto": "Dra. María González",
  "fechaIngreso": "2020-05-10",
  "especialidad": "Cardiología"
}
```

### Create Worker
```
POST /api/trabajadores
Content-Type: application/json
```

**Request Body:**
```json
{
  "cedula": "87654321",
  "nombreCompleto": "Dra. María González",
  "fechaIngreso": "2020-05-10",
  "especialidad": "Cardiología"
}
```

**Response:** `201 Created` with created worker object

---

## 3. Prestadores de Salud (Health Providers)

### List All Providers
```
GET /api/prestadores
```

**Response:**
```json
[
  {
    "id": 1,
    "nombre": "Hospital Central",
    "rut": "211234567890",
    "fechaAlta": "2010-01-01",
    "activo": true
  }
]
```

### Get Provider by RUT
```
GET /api/prestadores/{rut}
```

**Parameters:**
- `rut` (path) - RUT del prestador

**Response:**
```json
{
  "id": 1,
  "nombre": "Hospital Central",
  "rut": "211234567890",
  "fechaAlta": "2010-01-01",
  "activo": true
}
```

### Create Provider
```
POST /api/prestadores
Content-Type: application/json
```

**Request Body:**
```json
{
  "nombre": "Hospital Central",
  "rut": "211234567890",
  "fechaAlta": "2010-01-01",
  "activo": true
}
```

**Response:** `201 Created` with created provider object

### Update Provider
```
PUT /api/prestadores/{rut}
Content-Type: application/json
```

**Parameters:**
- `rut` (path) - RUT del prestador

**Request Body:**
```json
{
  "nombre": "Hospital Central Actualizado",
  "fechaAlta": "2010-01-01",
  "activo": true
}
```

**Response:** `200 OK` with updated provider object

### Delete Provider
```
DELETE /api/prestadores/{rut}
```

**Parameters:**
- `rut` (path) - RUT del prestador

**Response:** `204 No Content`

---

## 4. Documentos Clínicos (Clinical Documents)

### List All Documents
```
GET /api/documentos
```

**Query Parameters:**
- `pacienteCI` (optional) - Filter by patient's cedula

**Response:**
```json
[
  {
    "id": 1,
    "codigo": "DOC001",
    "pacienteCI": "12345678",
    "prestadorRUT": "211234567890",
    "fechaEmision": "2024-01-15",
    "firmado": true,
    "tipo": "Receta",
    "contenido": "Medicamento X, 2 veces al día"
  }
]
```

### Get Document by Code
```
GET /api/documentos/{codigo}
```

**Parameters:**
- `codigo` (path) - Document code

**Response:**
```json
{
  "id": 1,
  "codigo": "DOC001",
  "pacienteCI": "12345678",
  "prestadorRUT": "211234567890",
  "fechaEmision": "2024-01-15",
  "firmado": true,
  "tipo": "Receta",
  "contenido": "Medicamento X, 2 veces al día"
}
```

### Create Document
```
POST /api/documentos
Content-Type: application/json
```

**Request Body:**
```json
{
  "codigo": "DOC001",
  "pacienteCI": "12345678",
  "prestadorRUT": "211234567890",
  "fechaEmision": "2024-01-15",
  "firmado": false,
  "tipo": "Receta",
  "contenido": "Medicamento X, 2 veces al día"
}
```

**Response:** `201 Created` with created document object

### Update Document
```
PUT /api/documentos/{codigo}
Content-Type: application/json
```

**Parameters:**
- `codigo` (path) - Document code

**Request Body:**
```json
{
  "pacienteCI": "12345678",
  "prestadorRUT": "211234567890",
  "fechaEmision": "2024-01-15",
  "firmado": true,
  "tipo": "Receta",
  "contenido": "Medicamento X, 2 veces al día - ACTUALIZADO"
}
```

**Response:** `200 OK` with updated document object

### Delete Document
```
DELETE /api/documentos/{codigo}
```

**Parameters:**
- `codigo` (path) - Document code

**Response:** `204 No Content`

---

## Testing the API

You can test the API using tools like:
- **curl** (command line)
- **Postman** (GUI)
- **Thunder Client** (VS Code extension)

### Example with curl:

```bash
# List all users
curl http://localhost:8080/Laboratorio-web/api/usuarios

# Create a new user
curl -X POST http://localhost:8080/Laboratorio-web/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nombreCompleto":"Test User","cedulaIdentidad":"11111111","fechaNacimiento":"1995-05-05","activo":true}'

# Get user by cedula
curl http://localhost:8080/Laboratorio-web/api/usuarios/11111111

# Delete user
curl -X DELETE http://localhost:8080/Laboratorio-web/api/usuarios/11111111
```

## Deployment

To deploy the application:

```bash
mvn clean install wildfly:deploy
```

The REST API will be available at: `http://localhost:8080/Laboratorio-web/api`

## Notes

- All dates use ISO-8601 format (YYYY-MM-DD)
- All endpoints return UTF-8 encoded JSON
- The API is designed to be consumed by mobile applications
- Consider implementing authentication and authorization for production use
