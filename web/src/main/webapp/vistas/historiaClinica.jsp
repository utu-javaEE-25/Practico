<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Índice de Historia Clínica</title>
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
            border-radius: 1rem;
        }
        tbody tr:nth-child(odd) {
            background-color: white;
        }
        tbody tr:nth-child(even) {
            background-color: #f8f9fa;
        }
        /* Center the actions column (last column) */
        .table-container table td:last-child,
        .table-container table th:last-child {
            text-align: center;
            vertical-align: middle;
            white-space: nowrap;
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
        <h1>Índice de Mi Historia Clínica</h1>
    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-danger" role="alert">
            <strong>Error:</strong> ${error}
        </div>
    </c:if>

    <c:choose>
        <c:when test="${not empty historiaMetadata}">
            <div class="card shadow-sm">
                <div class="card-body">
                    <div class="table-container">
                        <table class="table table-hover table-bordered bg-white">
                            <thead class="table-primary">
                                <tr>
                                    <th>Tipo de Documento</th>
                                    <th>Fecha de Creación</th>
                                    <th>Prestador Custodio</th>
                                    <th>Acciones</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="meta" items="${historiaMetadata}">
                                    <tr>
                                        <td><c:out value="${meta.tipoDocumento}"/></td>
                                        <td><c:out value="${meta.fechaCreacion}"/></td>
                                        <td><c:out value="${meta.nombrePrestador}"/></td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/documento?docId=${meta.idExternaDoc}&custodioId=${meta.idCustodio}" 
                                               class="btn btn-primary btn-sm">Ver Documento</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="alert alert-info">
                No se encontraron documentos en su historia clínica.
            </div>
        </c:otherwise>
    </c:choose>
</div>

<div class="footer">
    &copy; 2025 HCEN - Taller de Sistemas Empresariales
    <br>
    <small><%= email %></small>
</div>
</body>
</html>