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
</head>
<body class="bg-light">

<div class="container mt-5">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1>Centro de Notificaciones y Permisos</h1>
        <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-secondary">Volver al Portal</a>
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
                                    <p class="mb-1">El profesional <strong>${solicitud.nombreProfesionalSolicitante}</strong> del prestador <strong>(Nombre Prestador Aquí)</strong> solicita acceso a su documento.</p>
                                    <small class="text-muted">Motivo: "${solicitud.motivo}"</small>
                                </div>
                                <form method="post" action="${pageContext.request.contextPath}/notificaciones" class="d-flex align-items-center gap-2 mt-2 mt-md-0">
                                    <input type="hidden" name="solicitudId" value="${solicitud.id}" />
                                    <select name="diasVigencia" class="form-select form-select-sm" style="width: auto;">
                                        <option value="1">Permitir por 1 día</option>
                                        <option value="7" selected>Permitir por 7 días</option>
                                        <option value="30">Permitir por 30 días</option>
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
</body>
</html>