<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*, java.time.*, java.time.format.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Historial de accesos a documentos clínicos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(180deg, #f4f8ff 0%, #eaf1ff 100%);
            min-height: 100vh;
            font-family: 'Segoe UI', sans-serif;
        }
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
        .card {
            border: none;
            border-radius: 1rem;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            transition: all 0.25s ease;
            width: 18rem;
            text-align: center;
        }
        .card:hover {
            transform: translateY(-6px);
            box-shadow: 0 8px 16px rgba(0,0,0,0.15);
        }
        .footer {
            text-align: center;
            color: #888;
            font-size: 0.85rem;
            margin-top: 3rem;
        }
    </style>
</head>
<body class="bg-light">
<%
    String nombre = (String) session.getAttribute("nombre");
    String apellido = (String) session.getAttribute("apellido");
    String email = (String) session.getAttribute("email");
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // Datos HARDCODEADOS de ejemplo
    List<Map<String, String>> accesos = new ArrayList<>();

    Map<String, String> a1 = new HashMap<>();
    a1.put("nombre", "Dr. Juan Pérez");
    a1.put("docId", "DOC-12345");
    a1.put("fecha", "2025-11-08 14:23");
    accesos.add(a1);

    Map<String, String> a2 = new HashMap<>();
    a2.put("nombre", "Lic. Ana Gómez");
    a2.put("docId", "DOC-98765");
    a2.put("fecha", "2025-11-08 16:45");
    accesos.add(a2);

    Map<String, String> a3 = new HashMap<>();
    a3.put("nombre", "Enf. Carlos Silva");
    a3.put("docId", "DOC-45678");
    a3.put("fecha", "2025-11-07 10:12");
    accesos.add(a3);

    // Enviamos lista a JSTL
    request.setAttribute("accesos", accesos);
%>

<nav class="navbar navbar-expand-lg">
    <div class="container-fluid">
        <a class="navbar-brand" href="<%=request.getContextPath()%>/index.jsp">HCEN</a>
        <div class="d-flex align-items-center ms-auto">
            <span class="welcome">Bienvenido, <%= nombre %></span>
            <a href="<%=request.getContextPath()%>/logout?login_type=user" class="btn btn-outline-light btn-sm">
                <i class="bi bi-box-arrow-right"></i> Cerrar sesión
            </a>
        </div>
    </div>
</nav>

<div class="container my-5">
    <h2 class="mb-4 text-primary">Historial de accesos a tus documentos clínicos</h2>

    <c:choose>
        <c:when test="${empty accesos}">
            <div class="alert alert-info">No se encontraron accesos registrados.</div>
        </c:when>
        <c:otherwise>
            <table class="table table-striped table-bordered shadow-sm">
                <thead class="table-primary">
                    <tr>
                        <th>Nombre</th>
                        <th>ID Documento</th>
                        <th>Fecha</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="a" items="${accesos}">
                        <tr>
                            <td>${a.nombre}</td>
                            <td>${a.docId}</td>
                            <td>${a.fecha}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>

</body>
</html>