package uy.edu.fing.tse.seguridad;

import org.json.JSONObject;
import org.jose4j.jwk.HttpsJwks;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.resolvers.HttpsJwksVerificationKeyResolver;

public class OidcTokenValidator {

    private static final String JWKS_URL = "https://auth.iduruguay.gub.uy/oidc/v1/jwks";

    public static JSONObject validateIdToken(String idToken) {
        try {
            // 1) Cargar JWKS remoto de ID Uruguay
            HttpsJwks httpsJwks = new HttpsJwks(JWKS_URL);

            // 2) Resolver de claves para verificar firmas RS256
            HttpsJwksVerificationKeyResolver keyResolver = new HttpsJwksVerificationKeyResolver(httpsJwks);

            // 3) Configurar el consumidor JWT
            JwtConsumer consumer = new JwtConsumerBuilder()
                    .setRequireExpirationTime()
                    .setSkipDefaultAudienceValidation() // después podemos afinar esto
                    .setVerificationKeyResolver(keyResolver)
                    .build();

            // 4) Validar token y obtener claims
            JwtClaims claims = consumer.processToClaims(idToken);

            // 5) Devolver claims como JSON para uso en MobileAuthService
            return new JSONObject(claims.toJson());

        } catch (Exception e) {
            throw new RuntimeException("Error validando idToken: " + e.getMessage(), e);
        }
    }
}
