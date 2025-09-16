<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
    import="java.util.List,uy.edu.fing.tse.entidades.DocumentoClinico" %>
    <html>

    <head>
        <title>Documento Clínico</title>
    </head>

    <body>
        <h1>Documento Clínico</h1>

        <% String error=(String) request.getAttribute("error"); if (error !=null) { %>
            <p style="color:red;">
                <%= error %>
            </p>
            <% } %>

                <h3>Agregar</h3>
                <form method="post" action="${pageContext.request.contextPath}/documentoClinico">
                    Código: <input name="codigo" required />
                    Paciente CI: <input name="pacienteCI" required />
                    Prestador RUT: <input name="prestadorRUT" />
                    Tipo: <input name="tipo" />
                    Fecha Emisión:
                    <input type="date" name="fechaEmision" max="<%= java.time.LocalDate.now() %>" required />
                    Firmado: <input type="checkbox" name="firmado" />
                    <br />Contenido:<br />
                    <textarea name="contenido" rows="3" cols="60"></textarea><br />
                    <button type="submit">Crear</button>
                </form>

                <h3>Buscar</h3>
                <form method="get" action="${pageContext.request.contextPath}/documentoClinico">
                    Por código: <input name="buscarCodigo" />
                    o por CI Paciente: <input name="buscarCI" />
                    <button type="submit">Buscar</button>
                </form>

                <h3>Listado</h3>
                <table border="1" cellpadding="5">
                    <tr>
                        <th>Código</th>
                        <th>CI Paciente</th>
                        <th>RUT Prestador</th>
                        <th>Tipo</th>
                        <th>Fecha</th>
                        <th>Firmado</th>
                        <th>Acciones</th>
                    </tr>
                    <% List<DocumentoClinico> lista =
                        (List<DocumentoClinico>) request.getAttribute("listaDocs");
                            if (lista != null) {
                            for (DocumentoClinico d : lista) {
                            %>
                            <tr>
                                <td>
                                    <%= d.getCodigo() %>
                                </td>
                                <td>
                                    <%= d.getPacienteCI() %>
                                </td>
                                <td>
                                    <%= d.getPrestadorRUT() %>
                                </td>
                                <td>
                                    <%= d.getTipo() %>
                                </td>
                                <td>
                                    <%= d.getFechaEmision() %>
                                </td>
                                <td>
                                    <%= d.isFirmado() %>
                                </td>
                                <td>
                                    <form method="post" action="${pageContext.request.contextPath}/documentoClinico"
                                        style="display:inline;">
                                        <input type="hidden" name="accion" value="eliminar" />
                                        <input type="hidden" name="codigo" value="<%= d.getCodigo() %>" />
                                        <button type="submit">Eliminar</button>
                                    </form>
                                </td>
                            </tr>
                            <% } } %>
                </table>

                <p><a href="${pageContext.request.contextPath}/index.xhtml">Volver</a></p>
    </body>

    </html>