<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*, uy.edu.fing.tse.entidades.UsuarioServicioSalud, uy.edu.fing.tse.entidades.AdminHcen" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Panel de Administrador</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body class="bg-light">
<%
    String nombre = (String) session.getAttribute("nombre");
    String apellido = (String) session.getAttribute("apellido");
    String email = (String) session.getAttribute("email");
    boolean esAdmin = Boolean.TRUE.equals(session.getAttribute("isAdmin"));

    if (!esAdmin) {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }

    List<UsuarioServicioSalud> usuarios = (List<UsuarioServicioSalud>) request.getAttribute("usuarios");
    List<AdminHcen> administradores = (List<AdminHcen>) request.getAttribute("administradores");
    Set<String> adminSubs = (Set<String>) request.getAttribute("adminSubs");
    Set<String> adminEmails = (Set<String>) request.getAttribute("adminEmails");

    String success = (String) request.getAttribute("admin_success");
    String error = (String) request.getAttribute("admin_error");
%>

<nav class="navbar navbar-expand-lg navbar-dark bg-primary mb-4">
    <div class="container-fluid">
        <a class="navbar-brand" href="<%=request.getContextPath()%>/index_admin">HCEN Admin</a>
        <div class="d-flex align-items-center ms-auto text-white gap-3">
            <a href="<%=request.getContextPath()%>/faces/vistas/prestadorSalud.xhtml" class="btn btn-outline-light btn-sm">
                <i class="bi bi-hospital"></i> Gestionar prestadores
            </a>
            <a href="<%=request.getContextPath()%>/reportes_admin" class="btn btn-outline-light btn-sm">
                <i class="bi bi-graph-up"></i> Reportes
            </a>
            <span class="me-3">Admin: <strong><%= nombre != null ? nombre : "" %> <%= apellido != null ? apellido : "" %></strong></span>
            <a href="<%=request.getContextPath()%>/logout" class="btn btn-outline-light btn-sm">Cerrar sesión</a>
        </div>
    </div>
</nav>

<div class="container">
    <% if (success != null) { %>
    <div class="alert alert-success"><%= success %></div>
    <% } %>
    <% if (error != null) { %>
    <div class="alert alert-danger"><%= error %></div>
    <% } %>

    <div class="row">
        <div class="col-lg-5 mb-4">
            <div class="card shadow-sm">
                <div class="card-header bg-secondary text-white">
                    Administradores registrados
                </div>
                <div class="card-body">
                    <p class="text-muted">Listado de usuarios con privilegios administrativos.</p>
                    <div class="table-responsive">
                        <table class="table table-sm table-hover">
                            <thead>
                            <tr>
                                <th>Email</th>
                                <th>Gub.Uy ID</th>
                                <th>Estado</th>
                                <th>Alta</th>
                            </tr>
                            </thead>
                            <tbody>
                            <% if (administradores != null && !administradores.isEmpty()) {
                                for (AdminHcen admin : administradores) { %>
                                    <tr>
                                        <td><%= admin.getEmail() %></td>
                                        <td><%= admin.getGubUyId() %></td>
                                        <td><span class="badge bg-success"><%= admin.getEstado() %></span></td>
                                        <td><%= admin.getFechaCreacion() %></td>
                                    </tr>
                            <%      }
                               } else { %>
                                <tr><td colspan="4" class="text-center">No hay administradores registrados.</td></tr>
                            <% } %>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-lg-7 mb-4">
            <div class="card shadow-sm">
                <div class="card-header bg-primary text-white">
                    Usuarios globales
                </div>
                <div class="card-body">
                    <p class="text-muted">Seleccione un usuario para otorgarle permisos de administrador global.</p>
                    <div class="table-responsive">
                        <table class="table table-striped table-hover align-middle">
                            <thead>
                            <tr>
                                <th>Nombre</th>
                                <th>Email</th>
                                <th>CI</th>
                                <th>Activo</th>
                                <th></th>
                            </tr>
                            </thead>
                            <tbody>
                            <% if (usuarios != null && !usuarios.isEmpty()) {
                                for (UsuarioServicioSalud u : usuarios) {
                                    boolean yaEsAdmin = (adminSubs != null && u.getSub() != null && adminSubs.contains(u.getSub()))
                                            || (adminEmails != null && u.getEmail() != null && adminEmails.contains(u.getEmail().toLowerCase()));
                            %>
                                <tr>
                                    <td><%= u.getNombreCompleto() %></td>
                                    <td><%= u.getEmail() %></td>
                                    <td><%= u.getCedulaIdentidad() %></td>
                                    <td>
                                        <% if (u.isActivo()) { %>
                                            <span class="badge bg-success">Si</span>
                                        <% } else { %>
                                            <span class="badge bg-secondary">No</span>
                                        <% } %>
                                    </td>
                                    <td>
                                        <form method="post" action="<%=request.getContextPath()%>/index_admin" class="d-inline">
                                            <input type="hidden" name="userId" value="<%= u.getId() %>"/>
                                            <button type="submit" class="btn btn-sm btn-outline-primary" <%= yaEsAdmin ? "disabled" : "" %>>
                                                <i class="bi bi-person-fill-up"></i> Convertir administrador
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            <%      }
                               } else { %>
                                <tr><td colspan="5" class="text-center">No hay usuarios registrados.</td></tr>
                            <% } %>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>

