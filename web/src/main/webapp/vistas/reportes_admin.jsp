<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="uy.edu.fing.tse.dto.reportes.ResumenGeneralDTO" %>
<%@ page import="uy.edu.fing.tse.dto.reportes.ActividadUsuariosDTO" %>
<%@ page import="uy.edu.fing.tse.dto.reportes.EventoActividadDTO" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Reportes | HCEN Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        .navbar {
        background-color: #8b3a55;
        box-shadow: 0 2px 6px rgba(0,0,0,0.1);
        }
        .footer {
        text-align: center;
        color: #888;
        font-size: 0.9rem;
        padding: 1rem 0;
        margin-top: auto;
        }
        .admin-title {
        color: #8b3a55 !important;
        }
        .table-container {
            border-radius: 0.5rem;
            max-height: calc(8 * 3.5rem + 3.5rem);
            overflow-y: auto;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }
        .table-container table {
            margin-bottom: 0;
        }
    </style>
</head>
<body class="bg-light">
<%
    boolean esAdmin = Boolean.TRUE.equals(session.getAttribute("isAdmin"));
    if (!esAdmin) {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }

    ResumenGeneralDTO resumen = (ResumenGeneralDTO) request.getAttribute("resumenGeneral");
    ActividadUsuariosDTO actividad = (ActividadUsuariosDTO) request.getAttribute("actividadUsuarios");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
%>

<nav class="navbar navbar-expand-lg navbar-dark mb-4">
    <div class="container-fluid">
        <a class="navbar-brand" href="<%=request.getContextPath()%>/index_admin">HCEN Admin</a>
        <div class="ms-auto d-flex gap-2">
            <a href="<%=request.getContextPath()%>/index_admin" class="btn btn-outline-light btn-sm">
                <i class="bi bi-speedometer"></i> Panel principal
            </a>
            <a href="<%=request.getContextPath()%>/faces/vistas/prestadorSalud.xhtml" class="btn btn-outline-light btn-sm">
                <i class="bi bi-hospital"></i> Prestadores
            </a>
            <a href="<%=request.getContextPath()%>/logout" class="btn btn-outline-light btn-sm">Cerrar sesión</a>
        </div>
    </div>
</nav>

<div class="container pb-4">
    <div class="d-flex align-items-center mb-3">
        <h1 class="h4 mb-0 admin-title">Reportes operativos</h1>
        <% if (resumen != null && resumen.getUltimaActualizacion() != null) { %>
        <span class="ms-auto text-muted small">
            Actualizado: <%= resumen.getUltimaActualizacion().format(formatter) %>
        </span>
        <% } %>
    </div>

    <section class="mb-4">
        <div class="d-flex align-items-center mb-2">
            <h2 class="h5 mb-0">Resumen general</h2>
            <% if (resumen != null) { %>
            <span class="badge bg-primary ms-2">Vista global</span>
            <% } %>
        </div>
        <% if (resumen == null) { %>
            <div class="alert alert-warning">No se pudo cargar el resumen.</div>
        <% } else { %>
        <div class="row g-3">
            <div class="col-md-4">
                <div class="card shadow-sm h-100">
                    <div class="card-body">
                        <div class="d-flex align-items-center justify-content-between mb-2">
                            <span class="text-muted text-uppercase small">Prestadores</span>
                            <i class="bi bi-building text-primary fs-4"></i>
                        </div>
                        <h3 class="mb-0"><%= resumen.getPrestadoresTotales() %></h3>
                        <p class="text-success mb-0">
                            <i class="bi bi-dot"></i>
                            <%= resumen.getPrestadoresActivos() %> activos
                        </p>
                        <p class="text-muted small mb-0">
                            +<%= resumen.getPrestadoresNuevosMes() %> altas en los últimos 30 días
                        </p>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card shadow-sm h-100">
                    <div class="card-body">
                        <div class="d-flex align-items-center justify-content-between mb-2">
                            <span class="text-muted text-uppercase small">Usuarios globales</span>
                            <i class="bi bi-people text-primary fs-4"></i>
                        </div>
                        <h3 class="mb-0"><%= resumen.getUsuariosTotales() %></h3>
                        <p class="text-success mb-0">
                            <i class="bi bi-dot"></i>
                            <%= resumen.getUsuariosActivos() %> activos
                        </p>
                        <p class="text-muted small mb-0">
                            +<%= resumen.getUsuariosNuevosSemana() %> nuevos en 7 días
                        </p>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card shadow-sm h-100">
                    <div class="card-body">
                        <div class="d-flex align-items-center justify-content-between mb-2">
                            <span class="text-muted text-uppercase small">Administradores</span>
                            <i class="bi bi-shield-lock text-primary fs-4"></i>
                        </div>
                        <h3 class="mb-0"><%= resumen.getAdministradoresTotales() %></h3>
                        <p class="text-success mb-0">
                            <i class="bi bi-dot"></i>
                            <%= resumen.getAdministradoresActivos() %> activos
                        </p>
                        <p class="text-muted small mb-0">Control de accesos globales</p>
                    </div>
                </div>
            </div>
        </div>
        <% } %>
    </section>

    <section>
        <div class="d-flex align-items-center mb-2">
            <h2 class="h5 mb-0">Actividad de usuarios</h2>
            <% if (actividad != null && actividad.getFechaCorte() != null) { %>
            <span class="text-muted small ms-2">
                Corte: <%= actividad.getFechaCorte().format(formatter) %>
            </span>
            <% } %>
        </div>
        <% if (actividad == null) { %>
            <div class="alert alert-warning">No se pudo cargar la actividad de usuarios.</div>
        <% } else { %>
        <div class="row g-3 mb-3">
            <div class="col-md-6 col-lg-3">
                <div class="card border-0 shadow-sm h-100">
                    <div class="card-body">
                        <div class="text-muted small text-uppercase">Logins exitosos (7 días)</div>
                        <div class="display-6 text-success"><%= actividad.getLoginsExitososSemana() %></div>
                    </div>
                </div>
            </div>
            <div class="col-md-6 col-lg-3">
                <div class="card border-0 shadow-sm h-100">
                    <div class="card-body">
                        <div class="text-muted small text-uppercase">Logins fallidos (7 días)</div>
                        <div class="display-6 text-danger"><%= actividad.getLoginsFallidosSemana() %></div>
                    </div>
                </div>
            </div>
            <div class="col-md-6 col-lg-3">
                <div class="card border-0 shadow-sm h-100">
                    <div class="card-body">
                        <div class="text-muted small text-uppercase">Logins exitosos (30 días)</div>
                        <div class="display-6 text-success"><%= actividad.getLoginsExitososMes() %></div>
                    </div>
                </div>
            </div>
            <div class="col-md-6 col-lg-3">
                <div class="card border-0 shadow-sm h-100">
                    <div class="card-body">
                        <div class="text-muted small text-uppercase">Logins fallidos (30 días)</div>
                        <div class="display-6 text-danger"><%= actividad.getLoginsFallidosMes() %></div>
                    </div>
                </div>
            </div>
        </div>

        <div class="card shadow-sm">
            <div class="card-header bg-white">
                <div class="d-flex align-items-center">
                    <span class="fw-semibold">Eventos auditados recientes</span>
                    <span class="badge bg-secondary ms-2">
                        <%= actividad.getEventosRecientes() != null ? actividad.getEventosRecientes().size() : 0 %>
                    </span>
                </div>
            </div>
            <div class="card-body p-0">
                <% if (actividad.getEventosRecientes() == null || actividad.getEventosRecientes().isEmpty()) { %>
                    <div class="p-4 text-center text-muted">No se registran eventos recientes.</div>
                <% } else { %>
                <div class="table-responsive table-container">
                    <table class="table table-striped mb-0">
                        <thead class="table-light">
                        <tr>
                            <th>Fecha</th>
                            <th>Acción</th>
                            <th>Resultado</th>
                            <th>Actor</th>
                            <th>IP</th>
                        </tr>
                        </thead>
                        <tbody>
                        <% for (EventoActividadDTO evento : actividad.getEventosRecientes()) { %>
                            <tr>
                                <td>
                                    <%= evento.getFecha() != null ? evento.getFecha().format(formatter) : "-" %>
                                </td>
                                <td><%= evento.getAccion() %></td>
                                <%
                                    String resultado = evento.getResultado();
                                    boolean exito = resultado != null && (resultado.equalsIgnoreCase("OK")
                                            || resultado.equalsIgnoreCase("SUCCESS")
                                            || resultado.equalsIgnoreCase("EXITOSO"));
                                    String badgeClass = exito ? "badge bg-success" : "badge bg-dark";
                                %>
                                <td>
                                    <span class="<%= badgeClass %>">
                                        <%= resultado != null ? resultado : "" %>
                                    </span>
                                </td>
                                <td>
                                    <div class="small text-muted">
                                        <%= evento.getTipoActor() != null ? evento.getTipoActor() : "N/D" %>
                                    </div>
                                    <div><%= evento.getActorId() != null ? evento.getActorId() : "-" %></div>
                                </td>
                                <td><%= evento.getIp() != null ? evento.getIp() : "-" %></td>
                            </tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>
                <% } %>
            </div>
        </div>
        <% } %>
    </section>
</div>
<div class="footer">
    &copy; 2025 HCEN - Administración del Sistema
  </div>
</body>
</html>
