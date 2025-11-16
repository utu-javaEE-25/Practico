package uy.edu.fing.tse.servicios;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import jakarta.ejb.Stateless;

import java.util.logging.Logger;

@Stateless
public class FirebaseNotificationService {

    private static final Logger LOGGER = Logger.getLogger(FirebaseNotificationService.class.getName());

    public void sendPushNotification(String deviceToken, String title, String body) {

        try {
            Message message = Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .setToken(deviceToken)
                    .build();
            // Enviar el mensaje
            String response = FirebaseMessaging.getInstance().send(message);

            LOGGER.info("Mensaje FCM enviado exitosamente: " + response);

        } catch (Exception e) {
            LOGGER.warning("Error al enviar mensaje FCM: " + e.getMessage());
        }
    }

}
