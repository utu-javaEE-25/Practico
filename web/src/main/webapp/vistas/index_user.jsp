<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>HCEN - Panel del Usuario</title>
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
        }
        .card:hover {
            transform: translateY(-6px);
            box-shadow: 0 8px 16px rgba(0,0,0,0.15);
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

    if (nombre == null) {
        // Si no hay sesión, redirige al home
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }
%>
<nav class="navbar navbar-expand-lg">
    <div class="container-fluid">
        <a class="navbar-brand" href="<%=request.getContextPath()%>/index.jsp">HCEN</a>
        <div class="d-flex align-items-center ms-auto">
            <span class="welcome">Bienvenido, <%= nombre %></span>
            <a href="<%=request.getContextPath()%>/logout?login_type=usuario" class="btn btn-outline-light btn-sm">
                <i class="bi bi-box-arrow-right"></i> Cerrar sesión
            </a>
        </div>
    </div>
</nav>

<div class="main-container">
    <div class="card p-4">
        <i class="bi bi-file-medical"></i>
        <h5 class="card-title">Visualización de historia clínica</h5>
        <p class="card-text text-muted">Consulte su historia clínica y los registros médicos asociados.</p>
        <a href="#" class="btn btn-primary">Acceder</a>
    </div>

    <div class="card p-4">
        <i class="bi bi-shield-lock"></i>
        <h5 class="card-title">Gestión de políticas de acceso</h5>
        <p class="card-text text-muted">Administre quién puede acceder a su historia clínica y configure permisos.</p>
        <a href="#" class="btn btn-primary">Administrar</a>
    </div>

    <div class="card p-4">
        <i class="bi bi-eye"></i>
        <h5 class="card-title">Visualización de accesos</h5>
        <p class="card-text text-muted">Revise el historial de accesos a su historia clínica por terceros autorizados.</p>
        <a href="<%=request.getContextPath()%>/historialAccesos" class="btn btn-primary">Ver historial</a>
    </div>

    <div class="card p-4">
        <i class="bi bi-bell"></i>
        <h5 class="card-title">Configuración de notificaciones</h5>
        <p class="card-text text-muted">Elija qué notificaciones recibir en su dispositivo móvil.</p>
        <a href="#" class="btn btn-primary">Configurar</a>
    </div>
</div>

<div class="footer">
    &copy; 2025 HCEN - Taller de Sistemas Empresariales
    <br>
    <small><%= email %></small>
</div>

</body>
</html>