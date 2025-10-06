<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login - Sistema de Salud</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
        }
        .login-container {
            background-color: white;
            padding: 40px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            max-width: 400px;
            width: 100%;
        }
        h1 {
            text-align: center;
            color: #333;
            margin-bottom: 30px;
        }
        .error {
            background-color: #f8d7da;
            color: #721c24;
            padding: 10px;
            border-radius: 4px;
            margin-bottom: 20px;
            border: 1px solid #f5c6cb;
        }
        .login-form {
            display: flex;
            flex-direction: column;
        }
        label {
            margin-bottom: 5px;
            color: #555;
            font-weight: bold;
        }
        input[type="text"], input[type="password"] {
            padding: 10px;
            margin-bottom: 20px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
        }
        .btn {
            padding: 12px;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            cursor: pointer;
            margin-bottom: 10px;
            font-weight: bold;
        }
        .btn-primary {
            background-color: #007bff;
            color: white;
        }
        .btn-primary:hover {
            background-color: #0056b3;
        }
        .btn-gubuy {
            background-color: #0033a0;
            color: white;
        }
        .btn-gubuy:hover {
            background-color: #002080;
        }
        .divider {
            text-align: center;
            margin: 20px 0;
            color: #999;
        }
        .info {
            background-color: #d1ecf1;
            color: #0c5460;
            padding: 10px;
            border-radius: 4px;
            margin-top: 20px;
            border: 1px solid #bee5eb;
            font-size: 12px;
        }
    </style>
</head>
<body>
    <div class="login-container">
        <h1>Sistema de Salud</h1>
        
        <% String error = (String) request.getAttribute("error");
           if (error != null) { %>
            <div class="error"><%= error %></div>
        <% } %>
        
        <!-- Botón para login con gub.uy -->
        <form action="<%= request.getContextPath() %>/login" method="post">
            <input type="hidden" name="loginType" value="gubuy">
            <button type="submit" class="btn btn-gubuy">
                🔐 Ingresar con gub.uy
            </button>
        </form>
        
        <div class="divider">- o -</div>
        
        <!-- Formulario de login tradicional -->
        <form class="login-form" action="<%= request.getContextPath() %>/login" method="post">
            <input type="hidden" name="loginType" value="traditional">
            
            <label for="cedula">Cédula de Identidad:</label>
            <input type="text" id="cedula" name="cedula" required 
                   placeholder="Ej: 12345678" 
                   pattern="[0-9]{7,8}">
            
            <label for="password">Contraseña:</label>
            <input type="password" id="password" name="password" required>
            
            <button type="submit" class="btn btn-primary">Ingresar</button>
        </form>
        
        <div class="info">
            <strong>Nota para testing:</strong><br>
            • Login con gub.uy: Simula autenticación con identidad digital gubernamental<br>
            • Login tradicional: Usar cédula registrada en el sistema
        </div>
    </div>
</body>
</html>
