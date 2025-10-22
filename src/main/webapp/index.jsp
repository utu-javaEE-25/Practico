<html>
  <head>
    <title>Login con GUB.uy</title>
  </head>
  <body>
    <%
      String nombre = (String) session.getAttribute("nombre");
      String apellido = (String) session.getAttribute("apellido");
      String email = (String) session.getAttribute("email");
      String loggedOut = request.getParameter("logged_out");
    %>
    <h1>Login con GUB.uy</h1>
    <% if (loggedOut != null) { %>
      <p style="color: green; font-weight: bold;">
        Sesion cerrada correctamente.
      </p>
    <% } %>
    <%
      if (nombre != null) {
    %>
        <h2>Bienvenido, <%= nombre %> <%= apellido %>!</h2>
        <p>Email: <%= email %></p>
        <a href="<%=request.getContextPath()%>/logout">Cerrar sesion</a>
    <%
      } else {
    %>
        <a href="<%=request.getContextPath()%>/login">Iniciar sesion con Gub.uy</a>
    <%
      }
    %>
  </body>
</html>
