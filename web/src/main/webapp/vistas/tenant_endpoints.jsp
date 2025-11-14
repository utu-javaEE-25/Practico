<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*, uy.edu.fing.tse.entidades.PrestadorSalud, uy.edu.fing.tse.entidades.TenantEndpoint" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Administrar endpoints de tenants</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body class="bg-light">
<%
    List<PrestadorSalud> prestadores = (List<PrestadorSalud>) request.getAttribute("prestadores");
    List<PrestadorSalud> prestadoresSinEndpoint = (List<PrestadorSalud>) request.getAttribute("prestadoresSinEndpoint");
    Map<Long, TenantEndpoint> endpointsPorTenant = (Map<Long, TenantEndpoint>) request.getAttribute("endpointsPorTenant");
    TenantEndpoint endpointEnEdicion = (TenantEndpoint) request.getAttribute("endpointEnEdicion");
    PrestadorSalud prestadorEnEdicion = (PrestadorSalud) request.getAttribute("prestadorEnEdicion");
    String success = (String) request.getAttribute("endpoint_success");
    String error = (String) request.getAttribute("endpoint_error");
    String ctx = request.getContextPath();
    String nombre = (String) session.getAttribute("nombre");
    String apellido = (String) session.getAttribute("apellido");
    String multiUriPrefix = (String) request.getAttribute("multiUriPrefix");
    String multiSuffixEnEdicion = (String) request.getAttribute("multiSuffixEnEdicion");
    long activos = 0;
    if (endpointsPorTenant != null) {
        for (TenantEndpoint te : endpointsPorTenant.values()) {
            if (te != null && te.isActivo()) {
                activos++;
            }
        }
    }
%>

<nav class="navbar navbar-expand-lg navbar-dark bg-primary mb-4">
    <div class="container-fluid">
        <a class="navbar-brand" href="<%=ctx%>/index_admin">HCEN Admin</a>
        <div class="d-flex align-items-center ms-auto text-white gap-2">
            <a href="<%=ctx%>/index_admin" class="btn btn-outline-light btn-sm">
                <i class="bi bi-speedometer2"></i> Panel principal
            </a>
            <a href="<%=ctx%>/faces/vistas/prestadorSalud.xhtml" class="btn btn-outline-light btn-sm">
                <i class="bi bi-hospital"></i> Prestadores
            </a>
            <a href="<%=ctx%>/reportes_admin" class="btn btn-outline-light btn-sm">
                <i class="bi bi-graph-up"></i> Reportes
            </a>
            <span class="me-3">Admin: <strong><%= nombre != null ? nombre : "" %> <%= apellido != null ? apellido : "" %></strong></span>
            <a href="<%=ctx%>/logout" class="btn btn-outline-light btn-sm">Cerrar sesión</a>
        </div>
    </div>
</nav>

<div class="container mb-4">
    <h1 class="h3 mb-4">Administración de endpoints por tenant</h1>

    <% if (success != null) { %>
    <div class="alert alert-success"><%= success %></div>
    <% } %>
    <% if (error != null) { %>
    <div class="alert alert-danger"><%= error %></div>
    <% } %>

    <div class="row">
        <div class="col-12 col-xl-3 col-lg-4 mb-4">
            <div class="card shadow-sm">
                <div class="card-header bg-secondary text-white">
                    <% if (endpointEnEdicion != null && prestadorEnEdicion != null) { %>
                        Editar endpoint
                    <% } else { %>
                        Crear endpoint
                    <% } %>
                </div>
                <div class="card-body">
                    <% if (endpointEnEdicion != null && prestadorEnEdicion != null) { %>
                        <p class="text-muted">Configurando endpoint para <strong><%= prestadorEnEdicion.getNombre() %></strong></p>
                        <%
                            boolean endpointEsMulti = endpointEnEdicion.isEsMultitenant();
                        %>
                        <form method="post" action="<%=ctx%>/tenant_endpoints" data-multitenant-form="true">
                            <input type="hidden" name="accion" value="actualizar"/>
                            <input type="hidden" name="tenantId" value="<%= endpointEnEdicion.getTenantId() %>"/>
                            <div class="form-check form-switch mb-3">
                                <input class="form-check-input js-multitenant-toggle" type="checkbox" role="switch" id="editEsMultitenant" name="esMultitenant" <%= endpointEsMulti ? "checked" : "" %>>
                                <label class="form-check-label" for="editEsMultitenant">Endpoint multitenant</label>
                            </div>
                            <div class="mb-3 js-multitenant-field <%= endpointEsMulti ? "" : "d-none" %>">
                                <label class="form-label">Sufijo de la URI multitenant</label>
                                <div class="input-group">
                                    <span class="input-group-text"><%= multiUriPrefix != null ? multiUriPrefix : "" %></span>
                                    <input type="text" name="uriMultiSuffix" class="form-control" value="<%= multiSuffixEnEdicion != null ? multiSuffixEnEdicion : "" %>" <%= endpointEsMulti ? "" : "disabled" %>>
                                </div>
                                <div class="form-text">El prefijo fijo se concatenara con este valor y se cerrara automaticamente con '/'.</div>
                            </div>
                            <div class="mb-3 js-custom-field <%= endpointEsMulti ? "d-none" : "" %>">
                                <label class="form-label">URI base</label>
                                <input type="text" name="uriBase" class="form-control" value="<%= endpointEnEdicion.getUriBase() %>" <%= endpointEsMulti ? "disabled" : "required" %>/>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Tipo de autenticacion</label>
                                <input type="text" name="tipoAuth" class="form-control" value="<%= endpointEnEdicion.getTipoAuth() != null ? endpointEnEdicion.getTipoAuth() : "" %>"/>
                            </div>
                            <div class="mb-3 js-custom-field <%= endpointEsMulti ? "d-none" : "" %>">
                                <label class="form-label">Hash / secreto cliente</label>
                                <input type="text" name="hashCliente" class="form-control" value="<%= endpointEnEdicion.getHashCliente() != null ? endpointEnEdicion.getHashCliente() : "" %>" <%= endpointEsMulti ? "disabled" : "required" %>/>
                            </div>
                            <div class="mb-3 js-multitenant-field <%= endpointEsMulti ? "" : "d-none" %>">
                                <label class="form-label">Hash / secreto cliente</label>
                                <div class="form-control-plaintext border rounded bg-light px-3 py-2">
                                    Se utilizara el secreto compartido configurado en el periferico.
                                </div>
                            </div>
                            <div class="form-check form-switch mb-3">
                                <input class="form-check-input" type="checkbox" role="switch" id="activoSwitch" name="activo" <%= endpointEnEdicion.isActivo() ? "checked" : "" %>>
                                <label class="form-check-label" for="activoSwitch">Endpoint activo</label>
                            </div>
                            <div class="d-flex gap-2">
                                <button type="submit" class="btn btn-primary">
                                    <i class="bi bi-save"></i> Guardar cambios
                                </button>
                                <a href="<%=ctx%>/tenant_endpoints" class="btn btn-outline-secondary">
                                    Cancelar
                                </a>
                            </div>
                        </form>
                    <% } else { %>
                        <p class="text-muted">Asocia un endpoint al tenant seleccionado. Solo se permite uno por tenant.</p>
                        <form method="post" action="<%=ctx%>/tenant_endpoints" data-multitenant-form="true">
                            <input type="hidden" name="accion" value="crear"/>
                            <div class="mb-3">
                                <label class="form-label">Tenant</label>
                                <select class="form-select" name="tenantId" <%= (prestadoresSinEndpoint == null || prestadoresSinEndpoint.isEmpty()) ? "disabled" : "" %> required>
                                    <option value="">Seleccione un tenant...</option>
                                    <% if (prestadoresSinEndpoint != null) {
                                           for (PrestadorSalud p : prestadoresSinEndpoint) { %>
                                        <option value="<%= p.getTenantId() %>"><%= p.getNombre() %> (RUT <%= p.getRut() %>)</option>
                                    <%   }
                                       } %>
                                </select>
                                <% if (prestadoresSinEndpoint == null || prestadoresSinEndpoint.isEmpty()) { %>
                                    <small class="text-muted">Todos los tenants ya cuentan con un endpoint configurado.</small>
                                <% } %>
                            </div>
                            <div class="form-check form-switch mb-3">
                                <input class="form-check-input js-multitenant-toggle" type="checkbox" role="switch" id="createEsMultitenant" name="esMultitenant">
                                <label class="form-check-label" for="createEsMultitenant">Endpoint multitenant</label>
                                <div class="form-text">Selecciona esta opcion si el prestador usa el portal compartido.</div>
                            </div>
                            <div class="mb-3 js-multitenant-field d-none">
                                <label class="form-label">Sufijo de la URI multitenant</label>
                                <div class="input-group">
                                    <span class="input-group-text"><%= multiUriPrefix != null ? multiUriPrefix : "" %></span>
                                    <input type="text" name="uriMultiSuffix" class="form-control" disabled/>
                                </div>
                                <div class="form-text">Solo ingresa el sufijo (por ejemplo, el nombre del schema del tenant).</div>
                            </div>
                            <div class="mb-3 js-custom-field">
                                <label class="form-label">URI base</label>
                                <input type="text" name="uriBase" class="form-control" placeholder="https://api.prestador.com/" required/>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Tipo de autenticacion</label>
                                <input type="text" name="tipoAuth" class="form-control" placeholder="API_KEY, OAuth2, etc."/>
                            </div>
                            <div class="mb-3 js-custom-field">
                                <label class="form-label">Hash / secreto cliente</label>
                                <input type="text" name="hashCliente" class="form-control" placeholder="Valor sensible provisto por el prestador" required/>
                            </div>
                            <div class="mb-3 js-multitenant-field d-none">
                                <label class="form-label">Hash / secreto cliente</label>
                                <div class="form-control-plaintext border rounded bg-light px-3 py-2">
                                    Se reutilizara el secreto compartido configurado en el periferico.
                                </div>
                            </div>
                            <button type="submit" class="btn btn-success w-100" <%= (prestadoresSinEndpoint == null || prestadoresSinEndpoint.isEmpty()) ? "disabled" : "" %>>
                                <i class="bi bi-plus-circle"></i> Crear endpoint
                            </button>
                        </form>
                    <% } %>
                </div>
            </div>
        </div>

        <div class="col-12 col-xl-9 col-lg-8 mb-4">
            <div class="card shadow-sm">
                <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
                    <span>Tenants y endpoints configurados</span>
                    <span class="badge bg-light text-dark">
                        <%= activos %> endpoints activos
                    </span>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-hover align-middle">
                            <thead>
                            <tr>
                                <th>Tenant</th>
                                <th>RUT</th>
                                <th>URI base</th>
                                <th>Tipo</th>
                                <th>Tipo auth</th>
                                <th>Estado</th>
                                <th class="text-end">Acciones</th>
                            </tr>
                            </thead>
                            <tbody>
                            <% if (prestadores != null && !prestadores.isEmpty()) {
                                   for (PrestadorSalud p : prestadores) {
                                       TenantEndpoint endpoint = endpointsPorTenant != null ? endpointsPorTenant.get(p.getTenantId()) : null;
                            %>
                                <tr>
                                    <td>
                                        <div><strong><%= p.getNombre() %></strong></div>
                                        <small class="text-muted">ID <%= p.getTenantId() %> | Schema <%= p.getNombreSchema() %></small>
                                    </td>
                                    <td><%= p.getRut() %></td>
                                    <td><%= endpoint != null ? endpoint.getUriBase() : "Sin configurar" %></td>
                                    <td>
                                        <% if (endpoint == null) { %>
                                            <span class="badge bg-secondary">Sin definir</span>
                                        <% } else if (endpoint.isEsMultitenant()) { %>
                                            <span class="badge bg-info text-dark">Multitenant</span>
                                        <% } else { %>
                                            <span class="badge bg-dark">Portal propio</span>
                                        <% } %>
                                    </td>
                                    <td><%= endpoint != null && endpoint.getTipoAuth() != null ? endpoint.getTipoAuth() : "-" %></td>
                                    <td>
                                        <% if (endpoint == null) { %>
                                            <span class="badge bg-warning text-dark">Pendiente</span>
                                        <% } else if (endpoint.isActivo()) { %>
                                            <span class="badge bg-success">Activo</span>
                                        <% } else { %>
                                            <span class="badge bg-secondary">Inactivo</span>
                                        <% } %>
                                    </td>
                                    <td class="text-end">
                                        <div class="d-flex justify-content-end gap-2">
                                            <a href="<%=ctx%>/tenant_endpoints?editTenantId=<%= p.getTenantId() %>"
                                               class="btn btn-sm btn-outline-primary <%= (endpoint == null) ? "disabled" : "" %>">
                                                <i class="bi bi-pencil-square"></i>
                                            </a>
                                            <form method="post" action="<%=ctx%>/tenant_endpoints" class="m-0">
                                                <input type="hidden" name="accion" value="desactivar"/>
                                                <input type="hidden" name="tenantId" value="<%= p.getTenantId() %>"/>
                                                <button type="submit" class="btn btn-sm btn-outline-danger"
                                                        <%= (endpoint == null || !endpoint.isActivo()) ? "disabled" : "" %>>
                                                    <i class="bi bi-slash-circle"></i>
                                                </button>
                                            </form>
                                        </div>
                                    </td>
                                </tr>
                            <%       }
                                   } else { %>
                                <tr>
                                    <td colspan="7" class="text-center text-muted">No hay prestadores registrados.</td>
                                </tr>
                            <% } %>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
(function() {
    function actualizar(form) {
        var toggle = form.querySelector('.js-multitenant-toggle');
        if (!toggle) { return; }
        var esMulti = toggle.checked;
        form.querySelectorAll('.js-multitenant-field').forEach(function(el) {
            el.classList.toggle('d-none', !esMulti);
        });
        form.querySelectorAll('.js-custom-field').forEach(function(el) {
            el.classList.toggle('d-none', esMulti);
        });
        var sufijo = form.querySelector('input[name="uriMultiSuffix"]');
        if (sufijo) {
            sufijo.disabled = !esMulti;
            sufijo.required = esMulti;
        }
        var uriBase = form.querySelector('input[name="uriBase"]');
        if (uriBase) {
            uriBase.disabled = esMulti;
            uriBase.required = !esMulti;
        }
        var hash = form.querySelector('input[name="hashCliente"]');
        if (hash) {
            hash.disabled = esMulti;
            hash.required = !esMulti;
        }
    }
    document.addEventListener('DOMContentLoaded', function() {
        document.querySelectorAll('[data-multitenant-form="true"]').forEach(function(form) {
            var toggle = form.querySelector('.js-multitenant-toggle');
            if (!toggle) { return; }
            toggle.addEventListener('change', function() { actualizar(form); });
            actualizar(form);
        });
    });
})();
</script>
</body>
</html>

