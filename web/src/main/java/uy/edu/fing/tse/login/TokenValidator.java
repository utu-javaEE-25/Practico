package uy.edu.fing.tse.login;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.time.Instant;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.SignedJWT;

public final class TokenValidator {

    private static final String JWKS_URI = "https://auth-testing.iduruguay.gub.uy/oidc/v1/jwks";
    private static final String EXPECTED_ISSUER = "https://auth-testing.iduruguay.gub.uy/oidc/v1";
    private static final String CLIENT_ID = "890192";
    private static final String CLIENT_SECRET = "457d52f181bf11804a3365b49ae4d29a2e03bbabe74997a2f510b179";
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    private TokenValidator() {
        // Utility class
    }

    public static JSONObject validar(String idToken) throws Exception {
        SignedJWT signedJWT = SignedJWT.parse(idToken);
        JWSHeader header = signedJWT.getHeader();
        JWSAlgorithm algorithm = header.getAlgorithm();

        boolean firmaValida;
        if (JWSAlgorithm.RS256.equals(algorithm)) {
            JSONObject jwk = obtenerJwk(header.getKeyID())
                    .orElseThrow(() -> new IllegalStateException("No key found for kid=" + header.getKeyID()));
            RSAPublicKey publicKey = construirClavePublica(jwk);
            JWSVerifier verifier = new RSASSAVerifier(publicKey);
            firmaValida = signedJWT.verify(verifier);
        } else if (JWSAlgorithm.HS256.equals(algorithm)) {
            JWSVerifier verifier = new MACVerifier(CLIENT_SECRET.getBytes(StandardCharsets.UTF_8));
            firmaValida = signedJWT.verify(verifier);
        } else {
            throw new IllegalArgumentException("Unsupported algorithm: " + algorithm);
        }

        if (!firmaValida) {
            throw new IllegalStateException("Invalid token signature (" + algorithm + ")");
        }

        JSONObject claims = new JSONObject(signedJWT.getPayload().toJSONObject());
        validarIssuer(claims);
        validarAudience(claims);
        validarExpiracion(claims);
        return claims;
    }

    private static Optional<JSONObject> obtenerJwk(String kid) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(JWKS_URI))
                .GET()
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IllegalStateException("JWKS endpoint returned status " + response.statusCode());
        }

        JSONObject jwksJson = new JSONObject(response.body());
        JSONArray keys = jwksJson.optJSONArray("keys");
        if (keys == null) {
            return Optional.empty();
        }

        for (int i = 0; i < keys.length(); i++) {
            JSONObject jwk = keys.getJSONObject(i);
            if (kid != null && kid.equals(jwk.optString("kid"))) {
                return Optional.of(jwk);
            }
        }
        return Optional.empty();
    }

    private static RSAPublicKey construirClavePublica(JSONObject jwk) throws GeneralSecurityException {
        Base64URL n = new Base64URL(jwk.getString("n"));
        Base64URL e = new Base64URL(jwk.getString("e"));

        BigInteger modulus = n.decodeToBigInteger();
        BigInteger exponent = e.decodeToBigInteger();
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    private static void validarIssuer(JSONObject claims) {
        String issuer = claims.optString("iss", null);
        if (!EXPECTED_ISSUER.equals(issuer)) {
            throw new IllegalStateException("Unexpected issuer: " + issuer);
        }
    }

    private static void validarAudience(JSONObject claims) {
        Object audClaim = claims.opt("aud");
        if (audClaim instanceof String) {
            if (!CLIENT_ID.equals(audClaim)) {
                throw new IllegalStateException("Audience mismatch: " + audClaim);
            }
        } else if (audClaim instanceof JSONArray) {
            JSONArray audArray = (JSONArray) audClaim;
            boolean match = false;
            for (int i = 0; i < audArray.length(); i++) {
                String value = audArray.optString(i, null);
                if (CLIENT_ID.equals(value)) {
                    match = true;
                    break;
                }
            }
            if (!match) {
                throw new IllegalStateException("Audience does not contain expected client id");
            }
        } else if (audClaim != null) {
            throw new IllegalStateException("Unsupported audience claim type: " + audClaim.getClass().getName());
        }
    }

    private static void validarExpiracion(JSONObject claims) {
        long exp = claims.optLong("exp", 0L);
        if (exp > 0 && Instant.now().getEpochSecond() > exp) {
            throw new IllegalStateException("Token has expired");
        }
    }
}
