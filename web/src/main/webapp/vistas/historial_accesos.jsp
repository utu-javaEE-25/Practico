<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*, java.time.*, java.time.format.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Historial de Accesos a Documentos</title>
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
        .table-container {
            border-radius: 0.5rem;
            overflow: hidden;
            max-height: calc(6 * 3.5rem + 3.5rem);
            overflow-y: auto;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }
        .table-container table {
            margin-bottom: 0;
        }
        tbody tr:nth-child(odd) {
            background-color: white;
        }
        tbody tr:nth-child(even) {
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
    <div class="mb-4">
        <h1>Historial de Accesos a Mis Documentos</h1>
    </div>

    <div class="card shadow-sm">
        <div class="card-body">
            <c:choose>
                <c:when test="${not empty accesos}">
                    <div class="table-container">
                        <div class="table-responsive">
                            <table class="table table-hover align-middle">
                                <thead class="table-light">
                                    <tr>
                                        <th>Fecha y Hora</th>
                                        <th>Documento</th>
                                        <th>Clínica</th>
                                        <th>Profesional</th>
                                        <th>Resultado</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="acceso" items="${accesos}">
                                        <tr>
                                            <%-- Cada ${acceso[i]} corresponde a una columna del SELECT --%>
                                            <td><c:out value="${acceso[0]}"/></td>
                                            <td><c:out value="${acceso[1]}"/></td>
                                            <td><c:out value="${acceso[2] != null ? acceso[2] : 'N/A'}"/></td>
                                            <td><c:out value="${acceso[3] != null ? acceso[3] : 'N/A'}"/></td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${acceso[4] == 'SUCCESS'}">
                                                        <span class="badge bg-success">Exitoso</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-danger">Fallido</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <p class="text-center text-muted">No se han registrado accesos a sus documentos.</p>
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