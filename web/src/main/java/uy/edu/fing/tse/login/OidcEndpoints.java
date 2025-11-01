package uy.edu.fing.tse.login;

import jakarta.servlet.http.HttpServletRequest;

final class OidcEndpoints {

    private OidcEndpoints() {
        // Utility class
    }

    static String buildRedirectUri(HttpServletRequest request, String path) {
        String scheme = request.getScheme();
        int port = request.getServerPort();

        StringBuilder uri = new StringBuilder();
        uri.append(scheme)
                .append("://")
                .append(request.getServerName());

        boolean standardHttp = "http".equalsIgnoreCase(scheme) && port == 80;
        boolean standardHttps = "https".equalsIgnoreCase(scheme) && port == 443;
        if (!standardHttp && !standardHttps && port > 0) {
            uri.append(':').append(port);
        }

        uri.append(request.getContextPath()).append(path);
        return uri.toString();
    }
}
