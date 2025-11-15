<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*, java.time.*, java.time.format.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Historial de Accesos a Documentos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container mt-5">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1>Historial de Accesos a Mis Documentos</h1>
        <a href="${pageContext.request.contextPath}/vistas/index_user.jsp" class="btn btn-secondary">Volver al Portal</a>
    </div>

    <div class="card shadow-sm">
        <div class="card-body">
            <c:choose>
                <c:when test="${not empty accesos}">
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
                </c:when>
                <c:otherwise>
                    <p class="text-center text-muted">No se han registrado accesos a sus documentos.</p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
</body>
</html>