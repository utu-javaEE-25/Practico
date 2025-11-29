<!-- JSP generic error page that shows title, message, optional details and a button to go home -->

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html lang="es">

    <head>
        <meta charset="UTF-8" />
        <title>${errorTitle}</title>
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body {
                background: linear-gradient(180deg, #f4f8ff 0%, #eaf1ff 100%);
                min-height: 100vh;
                font-family: 'Segoe UI', sans-serif;
            }

            .navbar {
                background-color: #0d6efd;
                box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
            }

            .navbar-brand {
                font-weight: bold;
                font-size: 1.6rem;
                color: white !important;
                letter-spacing: 1px;
            }

            .error-card {
                max-width: 700px;
                margin: 4rem auto;
            }

            pre.error-details {
                background: #f8f9fa;
                padding: 1rem;
                border-radius: .25rem;
                white-space: pre-wrap;
                word-wrap: break-word;
            }
        </style>
    </head>

    <body>
        <nav class="navbar navbar-expand-lg">
            <div class="container-fluid">
                <a class="navbar-brand" href="${pageContext.request.contextPath}/index.jsp">HCEN</a>
            </div>
        </nav>
        <div class="container">
            <div class="card border-danger error-card">
                <div class="card-header bg-danger text-white">
                    <h4>${errorTitle}</h4>
                </div>
                <div class="card-body">
                    <p>${errorMessage}</p>
                    <c:if test="${not empty errorDetails}">
                        <h6>Details:</h6>
                        <pre class="error-details">${errorDetails}</pre>
                    </c:if>
                    <a class="btn btn-primary" href="${returnUrl}">Ir al inicio</a>
                </div>
            </div>
        </div>
    </body>

    </html>