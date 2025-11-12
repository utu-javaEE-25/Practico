package uy.edu.fing.tse.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.ejb.Startup;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Logger;

@Startup
public class ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(ServletContextListener.class.getName());

    public void initializeFirebase() {

        try {
            FileInputStream serviceAccount =
                    new FileInputStream("src/main/resources/serviceAccountKey.json");

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
            LOGGER.info("Firebase Admin SDK inicializado correctamente.");

        } catch (IOException e) {
            LOGGER.warning("Error al inicializar Firebase Admin SDK: " + e.getMessage());
        }
    }
}
