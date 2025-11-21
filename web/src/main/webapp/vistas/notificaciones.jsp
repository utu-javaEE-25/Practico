<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*, uy.edu.fing.tse.entidades.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mis Notificaciones - HCEN</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        .navbar {
            background-color: #0d6efd;
            box-shadow: 0 2px 6px rgba(0,0,0,0.1);
        }
        .navbar-brand {
            font-weight: bold;
            font-size: 1.6rem;
            color: white !important;
        }
        .welcome {
            color: white;
            margin-right: 1rem;
            font-weight: 500;
        }
        .footer {
            text-align: center;
            color: #888;
            font-size: 0.85rem;
            margin-top: 3rem;
        }
        .list-group-item:nth-child(odd) {
            background-color: white;
        }
        .list-group-item:nth-child(even) {
            background-color: #f8f9fa;
        }
    </style>
</head>
<body class="bg-light">
    <%
    String nombre = (String) session.getAttribute("nombre");
    String apellido = (String) session.getAttribute("apellido");
    String email = (String) session.getAttribute("email");

    if (nombre == null) {
        // Si no hay sesión, redirige al home
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }
%>

<nav class="navbar navbar-expand-lg">
    <div class="container-fluid">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/vistas/index_user.jsp">HCEN</a>
        <div class="d-flex align-items-center ms-auto">
            <span class="welcome">Bienvenido, <%= nombre %></span>
            <a href="${pageContext.request.contextPath}/vistas/index_user.jsp" class="btn btn-outline-light btn-sm me-2">
                <i class="bi bi-house-door"></i> Inicio
            </a>
            <a href="${pageContext.request.contextPath}/logout?login_type=usuario" class="btn btn-outline-light btn-sm">
                <i class="bi bi-box-arrow-right"></i> Cerrar sesión
            </a>
        </div>
    </div>
</nav>

<div class="container mt-5">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1>Centro de Notificaciones y Permisos</h1>
    </div>

    <c:if test="${not empty successMessage}">
        <div class="alert alert-success">${successMessage}</div>
    </c:if>
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger">${errorMessage}</div>
    </c:if>

    <div class="card shadow-sm">
        <div class="card-header">
            <h5 class="mb-0">Solicitudes de Acceso Pendientes</h5>
        </div>
        <div class="card-body">
            <c:choose>
                <c:when test="${not empty solicitudesPendientes}">
                    <ul class="list-group">
                        <c:forEach var="solicitud" items="${solicitudesPendientes}">
                            <li class="list-group-item d-flex justify-content-between align-items-center flex-wrap">
                                <div>
                                    <c:set var="prestadorNombre" value="${prestadoresPorId[solicitud.requesterTenantId]}" />
                                    <p class="mb-1">
                                        El profesional <strong>${solicitud.nombreProfesionalSolicitante}</strong>
                                        del prestador <strong>${empty prestadorNombre ? 'Prestador desconocido' : prestadorNombre}</strong>
                                        solicita acceso a su documento.
                                    </p>
                                    <small class="text-muted d-block">Motivo: "${solicitud.motivo}"</small>
                                    <small class="text-muted d-block">Solicitud realizada el ${empty solicitud.fechaSolicitudFormateada ? 'Fecha no disponible' : solicitud.fechaSolicitudFormateada}</small>
                                </div>
                                <form method="post" action="${pageContext.request.contextPath}/notificaciones" class="d-flex align-items-center gap-2 mt-2 mt-md-0">
                                    <input type="hidden" name="solicitudId" value="${solicitud.id}" />
                                    <select name="diasVigencia" class="form-select form-select-sm" style="width: auto;">
                                        <option value="1">Permitir por 1 d&iacute;a</option>
                                        <option value="7" selected>Permitir por 7 d&iacute;as</option>
                                        <option value="30">Permitir por 30 d&iacute;as</option>
                                    </select>
                                    <button type="submit" class="btn btn-success btn-sm">Aprobar</button>
                                </form>
                            </li>
                        </c:forEach>
                    </ul>
                </c:when>
                <c:otherwise>
                    <p class="text-muted">No tienes solicitudes de acceso pendientes.</p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<div class="footer">
    &copy; 2025 HCEN - Taller de Sistemas Empresariales
    <br>
    <small><%= email %></small>
</div>
</body>
</html>
