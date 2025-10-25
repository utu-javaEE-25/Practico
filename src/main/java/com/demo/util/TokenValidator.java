package com.demo.util;

import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.SignedJWT;

public class TokenValidator {

    private static final String JWKS_URI = "https://auth-testing.iduruguay.gub.uy/oidc/v1/jwks";
    private static final String EXPECTED_ISSUER = "https://auth-testing.iduruguay.gub.uy/oidc/v1";
    private static final String CLIENT_ID = "890192";
    private static final String CLIENT_SECRET = "457d52f181bf11804a3365b49ae4d29a2e03bbabe74997a2f510b179";

    public static JSONObject validar(String idToken) throws Exception {
        SignedJWT signedJWT = SignedJWT.parse(idToken);
        String alg = signedJWT.getHeader().getAlgorithm().getName();
        String kid = signedJWT.getHeader().getKeyID();

        boolean firmaValida = false;

        if (JWSAlgorithm.RS256.getName().equals(alg)) {
            // ============================================================
            // Obtener JWKS
            // ============================================================
            HttpClient http = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(JWKS_URI))
                    .GET()
                    .build();
            HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());

            JSONObject jwksJson = new JSONObject(res.body());
            JSONArray keys = jwksJson.getJSONArray("keys");

            JSONObject jwkJson = null;
            for (int i = 0; i < keys.length(); i++) {
                JSONObject k = keys.getJSONObject(i);
                if (kid.equals(k.optString("kid"))) {
                    jwkJson = k;
                    break;
                }
            }

            if (jwkJson == null) {
                throw new RuntimeException("Clave pública no encontrada para kid=" + kid);
            }

            // ============================================================
            // Construir manualmente la clave pública RSA (sin usar x5c)
            // ============================================================
            String nStr = jwkJson.getString("n");
            String eStr = jwkJson.getString("e");

            Base64URL n = new Base64URL(nStr);
            Base64URL e = new Base64URL(eStr);

            BigInteger modulus = n.decodeToBigInteger();
            BigInteger exponent = e.decodeToBigInteger();

            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);

            // ============================================================
            // Verificar la firma
            // ============================================================
            JWSVerifier verifier = new RSASSAVerifier(publicKey);
            firmaValida = signedJWT.verify(verifier);

        } else if (JWSAlgorithm.HS256.getName().equals(alg)) {
            JWSVerifier verifier = new MACVerifier(CLIENT_SECRET.getBytes(StandardCharsets.UTF_8));
            firmaValida = signedJWT.verify(verifier);
        } else {
            throw new RuntimeException("Algoritmo no soportado: " + alg);
        }

        if (!firmaValida) {
            throw new RuntimeException("Firma inválida del ID Token (" + alg + ")");
        }

        // ============================================================
        // Validar issuer, audience y expiración
        // ============================================================
        JSONObject claims = new JSONObject(signedJWT.getPayload().toJSONObject());

        String issuer = claims.optString("iss");
        if (!EXPECTED_ISSUER.equals(issuer)) {
            throw new RuntimeException("Emisor no válido: " + issuer);
        }

        Object audClaim = claims.get("aud");
        if (audClaim instanceof String) {
            if (!CLIENT_ID.equals(audClaim)) {
                throw new RuntimeException("Audience no coincide: " + audClaim);
            }
        } else if (audClaim instanceof List) {
            List<?> audList = (List<?>) audClaim;
            if (!audList.contains(CLIENT_ID)) {
                throw new RuntimeException("Audience no contiene el client_id esperado");
            }
        }

        long exp = claims.optLong("exp", 0);
        long now = System.currentTimeMillis() / 1000L;
        if (exp > 0 && now > exp) {
            throw new RuntimeException("El token ha expirado");
        }

        return claims;
    }
}