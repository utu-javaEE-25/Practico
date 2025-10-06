<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*, uy.edu.fing.tse.entidades.Tenant" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestión de Tenants - POC Multitenancy</title>
    <style>
        body { 
            font-family: Arial, sans-serif; 
            margin: 20px;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
            background-color: white;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        h1 { 
            color: #333; 
            border-bottom: 3px solid #4CAF50;
            padding-bottom: 10px;
        }
        h2 {
            color: #555;
            margin-top: 30px;
        }
        .info-box {
            background-color: #e7f3fe;
            border-left: 4px solid #2196F3;
            padding: 15px;
            margin: 20px 0;
        }
        .warning-box {
            background-color: #fff3cd;
            border-left: 4px solid #ffc107;
            padding: 15px;
            margin: 20px 0;
        }
        .success-box {
            background-color: #d4edda;
            border-left: 4px solid #28a745;
            padding: 15px;
            margin: 20px 0;
        }
        .current-tenant {
            background-color: #4CAF50;
            color: white;
            padding: 10px 15px;
            border-radius: 5px;
            display: inline-block;
            margin: 10px 0;
        }
        form { 
            margin: 20px 0; 
            padding: 15px;
            background-color: #f9f9f9;
            border-radius: 5px;
        }
        label { 
            display: inline-block; 
            width: 150px; 
            margin: 10px 0;
            font-weight: bold;
        }
        input[type="text"] { 
            padding: 8px; 
            width: 300px;
            border: 1px solid #ddd;
            border-radius: 3px;
        }
        button, input[type="submit"] { 
            padding: 10px 20px; 
            margin: 10px 5px 10px 0; 
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 3px;
            cursor: pointer;
        }
        button:hover, input[type="submit"]:hover {
            background-color: #45a049;
        }
        .btn-secondary {
            background-color: #6c757d;
        }
        .btn-secondary:hover {
            background-color: #5a6268;
        }
        .btn-danger {
            background-color: #dc3545;
        }
        .btn-danger:hover {
            background-color: #c82333;
        }
        table { 
            border-collapse: collapse; 
            width: 100%; 
            margin: 20px 0;
            background-color: white;
        }
        th, td { 
            border: 1px solid #ddd; 
            padding: 12px; 
            text-align: left; 
        }
        th { 
            background-color: #4CAF50; 
            color: white;
        }
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
        tr:hover {
            background-color: #f5f5f5;
        }
        .error { 
            color: red; 
            font-weight: bold;
            padding: 10px;
            background-color: #f8d7da;
            border: 1px solid #f5c6cb;
            border-radius: 3px;
            margin: 10px 0;
        }
        .nav-links {
            margin: 20px 0;
            padding: 10px;
            background-color: #f0f0f0;
            border-radius: 5px;
        }
        .nav-links a {
            margin-right: 15px;
            color: #007bff;
            text-decoration: none;
        }
        .nav-links a:hover {
            text-decoration: underline;
        }
        code {
            background-color: #f4f4f4;
            padding: 2px 5px;
            border-radius: 3px;
            font-family: monospace;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>🏢 POC Multitenancy - Gestión de Tenants</h1>

    <div class="info-box">
        <strong>📚 Acerca de este POC:</strong><br>
        Este es un Proof of Concept (POC) para demostrar dos enfoques de multitenancy:
        <ul>
            <li><strong>Enfoque 1: Tabla única con FK a tenant</strong> - Los datos de todos los tenants están en las mismas tablas, con un campo <code>tenantId</code> para filtrar.</li>
            <li><strong>Enfoque 2: Multi-esquema</strong> - Cada tenant tiene su propio esquema de base de datos (campo <code>esquema</code>).</li>
        </ul>
        Este POC implementa principalmente el <strong>Enfoque 1</strong> (tabla única con FK).
    </div>

    <% 
    String error = (String) request.getAttribute("error");
    if (error != null) { 
    %>
        <p class="error">❌ <%= error %></p>
    <% } %>

    <% 
    String currentTenantId = (String) request.getAttribute("currentTenantId");
    if (currentTenantId != null) { 
    %>
        <div class="success-box">
            <strong>✅ Tenant activo:</strong>
            <div class="current-tenant">
                <%= currentTenantId %>
            </div>
            <form method="post" action="<%= request.getContextPath() %>/tenant" style="display: inline; background: none; padding: 0;">
                <input type="hidden" name="accion" value="limpiarTenant">
                <button type="submit" class="btn-danger">Limpiar selección de tenant</button>
            </form>
        </div>
    <% } else { %>
        <div class="warning-box">
            <strong>⚠️ Sin tenant seleccionado:</strong> Verás datos de todos los tenants. Selecciona un tenant abajo para filtrar.
        </div>
    <% } %>

    <div class="nav-links">
        <strong>🔗 Navegar a:</strong>
        <a href="<%= request.getContextPath() %>/prestadorSalud">Prestadores de Salud</a>
        <a href="<%= request.getContextPath() %>/usuarioServicioSalud">Usuarios de Servicio de Salud</a>
        <a href="<%= request.getContextPath() %>/tenant">Tenants</a>
    </div>

    <h2>➕ Crear Nuevo Tenant</h2>
    <form method="post" action="<%= request.getContextPath() %>/tenant">
        <input type="hidden" name="accion" value="crear">
        <label>Código*:</label>
        <input type="text" name="codigo" placeholder="Ej: MUTUALISTA_A" required><br>
        <label>Nombre*:</label>
        <input type="text" name="nombre" placeholder="Ej: Mutualista del Centro" required><br>
        <label>Esquema (opcional):</label>
        <input type="text" name="esquema" placeholder="Ej: tenant_a"><br>
        <button type="submit">Crear Tenant</button>
    </form>

    <h2>📋 Tenants Disponibles</h2>
    <% 
    @SuppressWarnings("unchecked")
    List<Tenant> lista = (List<Tenant>) request.getAttribute("listaTenants");
    if (lista != null && !lista.isEmpty()) {
    %>
        <table>
            <tr>
                <th>ID</th>
                <th>Código</th>
                <th>Nombre</th>
                <th>Esquema</th>
                <th>Activo</th>
                <th>Acciones</th>
            </tr>
            <% for (Tenant t : lista) { %>
                <tr>
                    <td><%= t.getId() %></td>
                    <td><strong><%= t.getCodigo() %></strong></td>
                    <td><%= t.getNombre() %></td>
                    <td><%= t.getEsquema() != null ? t.getEsquema() : "-" %></td>
                    <td><%= t.isActivo() ? "✅ Sí" : "❌ No" %></td>
                    <td>
                        <form method="get" action="<%= request.getContextPath() %>/tenant" style="display: inline; background: none; padding: 0;">
                            <input type="hidden" name="accion" value="seleccionar">
                            <input type="hidden" name="tenantId" value="<%= t.getCodigo() %>">
                            <button type="submit" class="btn-secondary">Seleccionar</button>
                        </form>
                    </td>
                </tr>
            <% } %>
        </table>
    <% } else { %>
        <div class="warning-box">
            ℹ️ No hay tenants creados. Crea uno para empezar.
        </div>
    <% } %>

    <h2>📖 Instrucciones de Uso</h2>
    <div class="info-box">
        <ol>
            <li><strong>Crear Tenants:</strong> Usa el formulario arriba para crear varios tenants (ej: MUTUALISTA_A, HOSPITAL_B).</li>
            <li><strong>Seleccionar Tenant:</strong> Haz clic en "Seleccionar" para activar un tenant. Los datos se filtrarán automáticamente.</li>
            <li><strong>Crear Datos:</strong> Ve a "Prestadores de Salud" o "Usuarios" y crea registros. Se asociarán automáticamente al tenant activo.</li>
            <li><strong>Ver Filtrado:</strong> Con un tenant seleccionado, solo verás los datos de ese tenant.</li>
            <li><strong>Ver Todos:</strong> Haz clic en "Limpiar selección" para ver datos de todos los tenants.</li>
        </ol>
    </div>

</div>
</body>
</html>
