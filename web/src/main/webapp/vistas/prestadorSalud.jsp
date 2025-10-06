<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*, uy.edu.fing.tse.entidades.PrestadorSalud" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Prestadores de Salud</title>
</head>
<body>
<h1>Prestadores de Salud</h1>

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
  <tr><th>ID</th><th>Nombre</th><th>RUT</th><th>Fecha Alta</th><th>Activo</th><th>Acciones</th></tr>
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
      <tr><td colspan="6">Sin resultados.</td></tr>
  <%
    }
  %>
</table>

<p><a href="<%= request.getContextPath() %>/index.xhtml">Volver al Menú Principal</a></p>

</body>
</html>