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
      display: flex;
      flex-direction: column;
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

    /* Contenedor principal */
    .container {
      flex: 1; /* Ocupa todo el espacio disponible */
      display: flex;
      justify-content: center;
      align-items: center;
      padding: 3rem 1rem;
    }

    .main-container {
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: 3rem;
      max-width: 1100px;
      width: 100%;
      padding: 0 2rem;
    }

    .info-text {
      flex: 1;
      max-width: 600px;
      font-size: 1.1rem;
      color: #333;
    }

    .card {
      flex-shrink: 0;
      width: 22rem;
      border: none;
      border-radius: 1rem;
      box-shadow: 0 4px 12px rgba(0,0,0,0.1);
      text-align: center;
      background-color: white;
      transition: transform 0.25s ease;
    }

    .card:hover {
      transform: translateY(-5px);
    }

    .footer {
      text-align: center;
      color: #888;
      font-size: 0.9rem;
      padding: 1rem 0;
      background-color: transparent;
      margin-top: auto; /* Empuja el footer hacia abajo */
    }

    /* ✅ Diseño responsive */
    @media (max-width: 768px) {
      .main-container {
        flex-direction: column;
        text-align: center;
        gap: 2rem;
      }

      .info-text {
        max-width: 100%;
      }

      .card {
        width: 100%;
        max-width: 22rem;
      }
    }
  </style>
</head>

<body>
  <nav class="navbar navbar-expand-lg">
    <div class="container-fluid">
      <a class="navbar-brand" href="<%=request.getContextPath()%>/index.jsp">HCEN</a>
    </div>
  </nav>

  <div class="container">
    <div class="main-container">
      <div class="info-text">
        <h3 class="mb-3 text-primary fw-bold">Historia Clínica Electrónica Nacional (HCEN)</h3>
        <p>
          La Historia Clínica Electrónica Nacional (HCEN) es una plataforma que permite acceder a la Historia Clínica Digital 
          de los usuarios del sistema de salud y que posibilita el registro de cualquier evento médico independientemente 
          del lugar geográfico y prestador de salud en donde se dé la asistencia.
        </p>
      </div>

      <div class="card p-4">
        <h4 class="card-title mb-3">Acceso al sistema</h4>
        <p class="text-muted mb-4">Inicie sesión con su identidad digital GUB.uy</p>
        <a href="<%=request.getContextPath()%>/login?type=user" class="btn btn-primary btn-lg w-100">
          Iniciar sesión
        </a>
      </div>
    </div>
  </div>

  <div class="footer">
    &copy; 2025 HCEN - Taller de Sistemas Empresariales
  </div>
</body>
</html>