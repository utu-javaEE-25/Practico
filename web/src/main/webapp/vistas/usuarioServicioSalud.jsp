<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*, uy.edu.fing.tse.entidades.UsuarioServicioSalud" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Usuarios de Servicios de Salud</title>
</head>
<body>
<h1>Gestión de Usuarios de Servicios de Salud</h1>

<%-- Display current tenant --%>
<% 
  String currentTenant = (String) session.getAttribute("currentTenantRUT");
  if (currentTenant != null && !currentTenant.isEmpty()) {
%>
<p style="background-color: #e0f0ff; padding: 10px; border: 1px solid #0066cc;">
  <strong>Prestador actual (RUT):</strong> <%= currentTenant %>
  <br>
  <small>Solo verá usuarios asociados a este prestador</small>
</p>
<% } else { %>
<p style="background-color: #ffe0e0; padding: 10px; border: 1px solid #cc0000;">
  <strong>Advertencia:</strong> No hay prestador seleccionado. Verá todos los usuarios.
</p>
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
    <th>Prestador RUT</th>
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
    <td><%= u.getPrestadorRUT() != null ? u.getPrestadorRUT() : "N/A" %></td>
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