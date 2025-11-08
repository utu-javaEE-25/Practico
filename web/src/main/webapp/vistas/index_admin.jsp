<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*, uy.edu.fing.tse.entidades.AdminHcen" %>
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

    List<AdminHcen> administradores = (List<AdminHcen>) request.getAttribute("administradores");

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
            <a href="<%=request.getContextPath()%>/tenant_endpoints" class="btn btn-outline-light btn-sm">
                <i class="bi bi-link-45deg"></i> Endpoints
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
                    Alta manual de administradores
                </div>
                <div class="card-body">
                    <p class="text-muted">
                        Ingresa los datos del nuevo administrador HCEN. Debe existir en Gub.uy y usar un email valido.
                    </p>
                    <form method="post" action="<%=request.getContextPath()%>/index_admin">
                        <div class="mb-3">
                            <label for="gubUyId" class="form-label">Gub.uy ID</label>
                            <input type="text" class="form-control" id="gubUyId" name="gubUyId" placeholder="urn:fdc:gub.uy:persona:123" required>
                        </div>
                        <div class="mb-3">
                            <label for="email" class="form-label">Email</label>
                            <input type="email" class="form-control" id="email" name="email" placeholder="admin@salud.gub.uy" required>
                        </div>
                        <div class="text-end">
                            <button type="submit" class="btn btn-success">
                                <i class="bi bi-person-plus-fill"></i> Registrar administrador
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>

