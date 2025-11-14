<%@ page session="true" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>HCEN - Portal del Usuario</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
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
            letter-spacing: 1px;
        }

        .welcome {
            color: white;
            margin-right: 1rem;
            font-weight: 500;
        }

        .main-container {
            display: flex;
            justify-content: center;
            align-items: flex-start;
            flex-wrap: wrap;
            padding: 3rem 1rem;
            gap: 2rem;
        }

        .card {
            border: none;
            border-radius: 1rem;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            transition: all 0.25s ease;
            width: 18rem;
            text-align: center;
            background-color: white;
        }

        .card:hover {
            transform: translateY(-6px);
            box-shadow: 0 8px 16px rgba(0,0,0,0.15);
        }

        .card-title {
            color: #0d6efd;
            font-weight: 600;
            margin-top: 0.5rem;
        }

        .card i {
            font-size: 2.5rem;
            color: #0d6efd;
            margin-bottom: 1rem;
        }

        .footer {
            text-align: center;
            color: #888;
            font-size: 0.85rem;
            margin-top: 3rem;
        }
    </style>
</head>
<body>

<%
    String nombre = (String) session.getAttribute("nombre");
    String apellido = (String) session.getAttribute("apellido");
    String email = (String) session.getAttribute("email");
    String loggedOut = request.getParameter("logged_out");
%>

<nav class="navbar navbar-expand-lg">
    <div class="container-fluid">
        <a class="navbar-brand" href="<%=request.getContextPath()%>/index.jsp">HCEN</a>
        <div class="d-flex align-items-center ms-auto">
            <% if (nombre != null) { %>
                <span class="welcome">Bienvenido, <%= nombre %></span>
                <a href="<%=request.getContextPath()%>/logout" class="btn btn-outline-light btn-sm">
                    <i class="bi bi-box-arrow-right"></i> Cerrar sesion
                </a>
            <% } %>
        </div>
    </div>
</nav>

<% if (loggedOut != null) { %>
    <div class="alert alert-success text-center m-3">
        Sesion cerrada correctamente.
    </div>
<% } %>

<div class="main-container">
    <% if (nombre == null) { %>
        <div class="card p-4 text-center">
            <i class="bi bi-person-circle"></i>
            <h4 class="card-title mb-3">Acceso al sistema</h4>
            <p class="text-muted mb-4">Inicie sesion con su identidad digital GUB.uy</p>
            <a href="<%=request.getContextPath()%>/login" class="btn btn-primary btn-lg w-100">
                Iniciar sesion
            </a>
        </div>
    <% } else { %>

        <div class="card p-4">
            <i class="bi bi-file-medical"></i>
            <h5 class="card-title">Visualizacion de historia clinica</h5>
            <p class="card-text text-muted">Consulte su historia clinica y los registros medicos asociados.</p>
            <a href="${pageContext.request.contextPath}/historia" class="btn btn-primary">Acceder</a>
        </div>

        <div class="card p-4">
            <i class="bi bi-bell-fill"></i>
            <h5 class="card-title">Notificaciones y Permisos</h5>
            <p class="card-text text-muted">Gestione las solicitudes de acceso a sus documentos clínicos.</p>
            <a href="${pageContext.request.contextPath}/notificaciones" class="btn btn-primary">Gestionar</a>
        </div>

        <div class="card p-4">
            <i class="bi bi-shield-lock"></i>
            <h5 class="card-title">Gestion de politicas de acceso</h5>
            <p class="card-text text-muted">Administre quien puede acceder a su historia clinica y configure permisos.</p>
            <a href="#" class="btn btn-primary">Administrar</a>
        </div>

        <div class="card p-4">
            <i class="bi bi-eye"></i>
            <h5 class="card-title">Visualizacion de accesos</h5>
            <p class="card-text text-muted">Revise el historial de accesos a su historia clinica por terceros autorizados.</p>
            <a href="#" class="btn btn-primary">Ver historial</a>
        </div>

        <div class="card p-4">
            <i class="bi bi-bell"></i>
            <h5 class="card-title">Configuracion de notificaciones</h5>
            <p class="card-text text-muted">Elija que notificaciones recibir en su dispositivo movil.</p>
            <a href="#" class="btn btn-primary">Configurar</a>
        </div>
    <% } %>
</div>

<div class="footer">
    &copy; 2025 HCEN - Taller de Sistemas Empresariales
    <br>
    <small><%= email != null ? email : "Autentiquese para ver sus datos" %></small>
</div>

</body>
</html>
