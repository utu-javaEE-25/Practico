<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*, uy.edu.fing.tse.entidades.PrestadorSalud" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Prestadores de Salud - Multitenant POC</title>
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
<h1>Prestadores de Salud - POC Multitenant</h1>

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
    (Los prestadores se crearán y filtrarán automáticamente para este tenant)
  </div>
<% } else { %>
  <div class="tenant-info" style="background-color: #fff3cd; border-color: #ffc107;">
    <strong>⚠️ Sin tenant seleccionado:</strong> Verás todos los prestadores. 
    <a href="<%= request.getContextPath() %>/tenant">Selecciona un tenant aquí</a>
  </div>
<% } %>

<% String error = (String) request.getAttribute("error");
   if (error != null) { %>
   <p style="color:red;"><%= error %></p>
<% } %>

<!-- Crear -->
<form method="post" action="<%= request.getContextPath() %>/prestadorSalud">
  <label>Nombre: <input type="text" name="nombre" required></label><br>
  <label>RUT: <input type="text" name="rut" required></label><br>
  <button type="submit">Crear</button>
</form>


<!-- Buscar -->
<form method="get" action="<%= request.getContextPath() %>/prestadorSalud" style="margin-bottom: 8px;">
  <label>Buscar por RUT: <input type="text" name="buscarRut" value="<%= request.getParameter("buscarRut") != null ? request.getParameter("buscarRut") : "" %>"></label>
  &nbsp;&nbsp;
  <label>o por Nombre: <input type="text" name="buscarNombre" value="<%= request.getParameter("buscarNombre") != null ? request.getParameter("buscarNombre") : "" %>"></label>
  <button type="submit">Buscar</button>
  <a href="<%= request.getContextPath() %>/prestadorSalud">Limpiar</a>
</form>

<!-- Resultados -->
<table border="1" cellpadding="6">
  <tr><th>ID</th><th>Nombre</th><th>RUT</th><th>Fecha Alta</th><th>Activo</th><th>Tenant</th><th>Acciones</th></tr>
  <%
    List<PrestadorSalud> lista = (List<PrestadorSalud>) request.getAttribute("listaPrestadorSalud");
    if (lista != null && !lista.isEmpty()) {
      for (PrestadorSalud p : lista) {
  %>
        <tr>
          <td><%= p.getId() %></td>
          <td><%= p.getNombre() %></td>
          <td><%= p.getRut() %></td>
          <td><%= p.getFechaAlta() %></td>
          <td><%= p.isActivo() %></td>
          <td><strong><%= p.getTenantId() != null ? p.getTenantId() : "-" %></strong></td>
          <td>
            <form method="post" action="<%= request.getContextPath() %>/prestadorSalud" style="display:inline;">
              <input type="hidden" name="rut" value="<%= p.getRut() %>">
              <button type="submit" name="accion" value="eliminar">Eliminar</button>
            </form>
          </td>
        </tr>
  <%
      }
    } else {
  %>
      <tr><td colspan="7">Sin resultados.</td></tr>
  <%
    }
  %>
</table>

</body>
</html>