<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Índice de Historia Clínica</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container mt-5">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1>Índice de Mi Historia Clínica</h1>
        <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-secondary">Volver al Inicio</a>
    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-danger" role="alert">
            <strong>Error:</strong> ${error}
        </div>
    </c:if>

    <c:choose>
        <c:when test="${not empty historiaMetadata}">
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
        </c:when>
        <c:otherwise>
            <div class="alert alert-info">
                No se encontraron documentos en su historia clínica.
            </div>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>