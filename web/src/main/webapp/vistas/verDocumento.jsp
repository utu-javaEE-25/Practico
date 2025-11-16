<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Consulta de Documento Clínico</title>
    <style>
        body { font-family: sans-serif; margin: 20px; color: #333; background-color: #f4f4f4; }
        .container { max-width: 850px; margin: auto; border: 1px solid #ccc; padding: 20px; background-color: #fff; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
        h2 { text-align: center; margin-bottom: 20px; color: #333; }
        .info-table { width: 100%; border-collapse: collapse; margin-bottom: 20px; border: 1px solid #0056b3; }
        .info-table td { border: 1px solid #999; padding: 8px; }
        .info-table td:first-child { background-color: #007bff; color: white; font-weight: bold; width: 180px; }
        .content-section { margin-top: 30px; }
        .content-section h3 { color: #007bff; border-bottom: 2px solid #007bff; padding-bottom: 5px; margin-bottom: 15px;}
        .diagnostico-table { background-color: #ffffe0; width: 100%; border-collapse: collapse; margin-top: 10px; border: 1px solid #ddd; margin-bottom: 15px; }
        .diagnostico-table td { border: 1px solid #ddd; padding: 8px; }
        .diagnostico-table td:first-child { font-weight: bold; width: 200px; }
        ul { list-style-type: '• '; padding-left: 20px; margin: 0; }
    </style>
</head>
<body>
<div class="container">
    <a href="${pageContext.request.contextPath}/historia" style="float: right; margin-bottom: 10px;">Volver al Índice</a>
    <h2>Consulta de Documento Clínico</h2>

    <c:if test="${not empty error}">
        <p style="color:red; font-weight: bold;">${error}</p>
    </c:if>

    <c:if test="${not empty documento}">
        <table class="info-table">
            <tbody>
                <tr><td>PACIENTE</td><td><c:out value="${documento.pacienteNombre}"/></td></tr>
                <tr><td>Nro. documento</td><td><c:out value="${documento.pacienteNroDocumento}"/></td></tr>
                <tr><td>Fecha de nacimiento</td><td><c:out value="${documento.pacienteFechaNacimiento}"/></td></tr>
                <tr><td>Sexo</td><td><c:out value="${documento.pacienteSexo}"/></td></tr>
                <tr><td>INSTANCIA MEDICA</td><td><c:out value="${documento.instanciaMedica}"/></td></tr>
                <tr><td>Fecha atención</td><td><c:out value="${documento.fechaAtencion}"/></td></tr>
                <tr><td>Lugar</td><td><c:out value="${documento.lugar}"/></td></tr>
                <tr><td>Autor</td><td><c:out value="${documento.autor}"/></td></tr>
                <tr><td>DOCUMENTO</td><td><c:out value="${documento.documentoId}"/></td></tr>
                <tr><td>Fecha generación</td><td><c:out value="${documento.fechaGeneracion}"/></td></tr>
                <tr><td>Custodio</td><td><c:out value="${documento.custodio}"/></td></tr>
            </tbody>
        </table>

        <c:if test="${not empty documento.motivosDeConsulta}">
            <div class="content-section">
                <h3>Motivos de consulta</h3>
                <ul>
                    <c:forEach var="motivo" items="${documento.motivosDeConsulta}">
                        <li><c:out value="${motivo}" /></li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>

        <c:if test="${not empty documento.diagnosticos}">
            <div class="content-section">
                <h3>Diagnósticos</h3>
                <c:forEach var="diagnosticoItem" items="${documento.diagnosticos}">
                    <table class="diagnostico-table">
                        <tbody>
                            <tr><td>Descripción del diagnóstico</td><td><c:out value="${diagnosticoItem.descripcion}"/></td></tr>
                            <tr><td>Fecha de inicio</td><td><c:out value="${diagnosticoItem.fechaInicio}"/></td></tr>
                            <tr><td>Estado del problema</td><td><c:out value="${diagnosticoItem.estadoProblema}"/></td></tr>
                            <tr><td>Grado de certeza</td><td><c:out value="${diagnosticoItem.gradoCerteza}"/></td></tr>
                        </tbody>
                    </table>
                </c:forEach>
            </div>
        </c:if>

        <c:if test="${not empty documento.instruccionesSeguimiento}">
            <div class="content-section">
                <h3>Instrucciones de seguimiento</h3>
                <ul>
                    <c:forEach var="linea" items="${documento.instruccionesSeguimiento}">
                        <li><c:out value="${linea}" /></li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>
    </c:if>
</div>
</body>
</html>