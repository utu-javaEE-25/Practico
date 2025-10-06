<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*, uy.edu.fing.tse.entidades.UsuarioServicioSalud" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Usuarios de Servicios de Salud - Multitenant POC</title>
  <style>
    body { font-family: Arial, sans-serif; margin: 20px; }
    h1 { color: #333; }
    .tenant-info { 
      background-color: #e7f3fe; 
      border-left: 4px solid #2196F3; 
      padding: 10px; 
      margin: 15px 0; 
    }
    .nav-links { margin: 15px 0; }
    .nav-links a { margin-right: 15px; }
  </style>
</head>
<body>
<h1>Gestión de Usuarios de Servicios de Salud - POC Multitenant</h1>

<div class="nav-links">
  <a href="<%= request.getContextPath() %>/tenant">🏢 Gestión de Tenants</a> | 
  <a href="<%= request.getContextPath() %>/prestadorSalud">Prestadores</a> | 
  <a href="<%= request.getContextPath() %>/usuarioServicioSalud">Usuarios</a>
</div>

<% 
String currentTenantId = (String) session.getAttribute("tenantId");
if (currentTenantId != null) { 
%>
  <div class="tenant-info">
    <strong>🏢 Tenant activo:</strong> <%= currentTenantId %> 
    (Los usuarios se crearán y filtrarán automáticamente para este tenant)
  </div>
<% } else { %>
  <div class="tenant-info" style="background-color: #fff3cd; border-color: #ffc107;">
    <strong>⚠️ Sin tenant seleccionado:</strong> Verás todos los usuarios. 
    <a href="<%= request.getContextPath() %>/tenant">Selecciona un tenant aquí</a>
  </div>
<% } %>

<%-- Bloque para mostrar errores --%>
<% String error = (String) request.getAttribute("error");
  if (error != null) { %>
<p style="color:red; font-weight: bold;"><%= error %></p>
<% } %>

<%-- Formulario para crear un nuevo usuario --%>
<h2>Crear Nuevo Usuario</h2>
<form method="post" action="<%= request.getContextPath() %>/usuarioServicioSalud">
  <label>Nombre Completo: <input type="text" name="nombreCompleto" required></label><br>
  <label>Cédula de Identidad (sin puntos ni guion): <input type="text" name="cedulaIdentidad" required></label><br>
  <label>Fecha de Nacimiento: <input type="date" name="fechaNacimiento" required></label><br>
  <button type="submit">Crear Usuario</button>
</form>

<hr>

<%-- Tabla para listar los usuarios existentes --%>
<h2>Usuarios Registrados</h2>
<table border="1" style="width:100%;">
  <thead>
  <tr>
    <th>ID</th>
    <th>Nombre Completo</th>
    <th>Cédula</th>
    <th>Fecha de Nacimiento</th>
    <th>Activo</th>
    <th>Tenant</th>
    <th>Acciones</th>
  </tr>
  </thead>
  <tbody>
  <%
    List<UsuarioServicioSalud> listaUsuarios = (List<UsuarioServicioSalud>) request.getAttribute("listaUsuarios");
    if (listaUsuarios != null && !listaUsuarios.isEmpty()) {
      for (UsuarioServicioSalud u : listaUsuarios) {
  %>
  <tr>
    <td><%= u.getId() %></td>
    <td><%= u.getNombreCompleto() %></td>
    <td><%= u.getCedulaIdentidad() %></td>
    <td><%= u.getFechaNacimiento() %></td>
    <td><%= u.isActivo() %></td>
    <td><strong><%= u.getTenantId() != null ? u.getTenantId() : "-" %></strong></td>
    <td>
      <%-- Formulario individual para eliminar --%>
      <form method="post" action="<%= request.getContextPath() %>/usuarioServicioSalud" style="margin:0;">
        <input type="hidden" name="cedulaIdentidad" value="<%= u.getCedulaIdentidad() %>">
        <input type="hidden" name="accion" value="eliminar">
        <button type="submit">Eliminar</button>
      </form>
    </td>
  </tr>
  <%    }
  } else { %>
  <tr><td colspan="7">No hay usuarios registrados.</td></tr>
  <%  } %>
  </tbody>
</table>

</body>
</html>