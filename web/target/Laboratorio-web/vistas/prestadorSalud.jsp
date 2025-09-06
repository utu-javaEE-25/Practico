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

<form method="post" action="<%= request.getContextPath() %>/prestadorSalud">
  <label>Nombre: <input type="text" name="nombre" required></label><br>
  <label>RUT: <input type="text" name="rut" required></label><br>
  <button type="submit">Crear</button>
</form>

<hr>
<table border="1">
  <tr><th>ID</th><th>Nombre</th><th>RUT</th><th>Fecha Alta</th><th>Activo</th></tr>
  <%
    List<PrestadorSalud> lista = (List<PrestadorSalud>) request.getAttribute("prestadorSalud");
    if (lista != null) {
      for (PrestadorSalud p : lista) {
  %>
        <tr>
          <td><%= p.getId() %></td>
          <td><%= p.getNombre() %></td>
          <td><%= p.getRut() %></td>
          <td><%= p.getFechaAlta() %></td>
          <td><%= p.isActivo() %></td>
        </tr>
  <%  } } %>
</table>
</body>
</html>
