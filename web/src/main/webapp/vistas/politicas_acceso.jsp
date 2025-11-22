<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*, uy.edu.fing.tse.entidades.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Politicas de Acceso</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container py-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h1 class="h4 mb-1">Gestion de politicas de acceso</h1>
            <p class="text-muted mb-0">Autorice a tenants o profesionales para acceder a sus documentos.</p>
        </div>
        <a href="${pageContext.request.contextPath}/vistas/index_user.jsp" class="btn btn-outline-secondary btn-sm">
            Volver al portal
        </a>
    </div>

    <c:if test="${not empty successMessage}">
        <div class="alert alert-success">${successMessage}</div>
    </c:if>
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger">${errorMessage}</div>
    </c:if>

    <div class="card shadow-sm mb-4">
        <div class="card-header">
            <h2 class="h6 mb-0">Crear nueva politica</h2>
        </div>
        <div class="card-body">
            <form method="post" action="${pageContext.request.contextPath}/politicas" class="row g-3">
                <div class="col-md-4">
                    <label class="form-label">Tenant solicitante *</label>
                    <select name="tenantId" class="form-select" required>
                        <option value="">Seleccione...</option>
                        <c:forEach var="p" items="${prestadores}">
                            <option value="${p.tenantId}">${p.nombre} (${p.nombreSchema})</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-4">
                    <label class="form-label">ID profesional (opcional)</label>
                    <input type="number" name="profesionalId" class="form-control" placeholder="Todos los profesionales" min="1" step="1">
                    <div class="form-text">Debe ser mayor a cero. Si se deja vacio, se aplica a todos los profesionales del tenant.</div>
                </div>
                <div class="col-md-4">
                    <label class="form-label">Documento (opcional)</label>
                    <select name="docId" class="form-select">
                        <option value="">Todos los documentos</option>
                        <c:forEach var="doc" items="${documentos}">
                            <option value="${doc.docId}">#${doc.docId} - ${doc.tipo} (${doc.idExternaDoc})</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-4">
                    <label class="form-label">Vigencia</label>
                    <select name="vigenciaDias" class="form-select">
                        <option value="1">1 dia</option>
                        <option value="7" selected>7 dias</option>
                        <option value="30">30 dias</option>
                        <option value="">Sin fecha de vencimiento</option>
                    </select>
                </div>
                <div class="col-12">
                    <button type="submit" class="btn btn-primary">Crear politica</button>
                </div>
            </form>
        </div>
    </div>

    <div class="card shadow-sm">
        <div class="card-header d-flex justify-content-between align-items-center">
            <h2 class="h6 mb-0">Politicas vigentes</h2>
        </div>
        <div class="card-body">
            <c:choose>
                <c:when test="${not empty politicas}">
                    <div class="table-responsive">
                        <table class="table table-hover align-middle">
                            <thead class="table-light">
                                <tr>
                                    <th>Tenant</th>
                                    <th>Profesional</th>
                                    <th>Documento</th>
                                    <th>Desde</th>
                                    <th>Hasta</th>
                                    <th>Accion</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="p" items="${politicas}">
                                    <tr>
                                        <td>
                                            <c:set var="prestador" value="${prestadoresMapa[p.tenantId]}"/>
                                            <c:choose>
                                                <c:when test="${prestador != null}">${prestador.nombre} (${prestador.nombreSchema})</c:when>
                                                <c:otherwise>Tenant ${p.tenantId}</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${p.idProfesionalAutorizado != null}">ID ${p.idProfesionalAutorizado}</c:when>
                                                <c:otherwise>Todos los profesionales</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${p.docMetadataId != null}">Doc #${p.docMetadataId}</c:when>
                                                <c:otherwise>Todos los documentos</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td><c:out value="${p.ventanaDesde}"/></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${p.ventanaHasta != null}"><c:out value="${p.ventanaHasta}"/></c:when>
                                                <c:otherwise>Sin vencimiento</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <form method="post" action="${pageContext.request.contextPath}/politicas" class="d-inline">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="politicaId" value="${p.id}">
                                                <button type="submit" class="btn btn-outline-danger btn-sm">Revocar</button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:when>
                <c:otherwise>
                    <p class="text-muted mb-0">No tiene politicas registradas.</p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
</body>
</html>
