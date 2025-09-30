package uy.edu.fing.tse.mdb;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.EJB;
import jakarta.ejb.MessageDriven;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;
import uy.edu.fing.tse.api.PrestadorSaludServiceLocal;
import java.util.logging.Logger;

@MessageDriven(
    activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:/jms/queue/queue_alta_prestador"),
        @ActivationConfigProperty(propertyName = "destinationType",   propertyValue = "jakarta.jms.Queue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode",   propertyValue = "Auto-acknowledge")
    }
)
public class AltaPrestadorMDB implements MessageListener {

    @EJB
    private PrestadorSaludServiceLocal servicio;

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage tm) {
                String payload = tm.getText();
                // Esperado: RUT|NOMBRE|FECHA
                String[] parts = payload.split("\\|");
                if (parts.length < 3) {
                    throw new IllegalArgumentException("Formato inválido. Esperado: RUT|NOMBRE|FECHA(YYYY-MM-DD)");
                }
                String rut    = parts[0].trim();
                String nombre = parts[1].trim();
                String fecha  = parts[2].trim();

                servicio.altaDesdeJms(rut, nombre, fecha);
            } else {
                throw new IllegalArgumentException("Mensaje no es TextMessage");
            }
        } catch (JMSException e) {
            throw new RuntimeException("Error leyendo JMS", e);
        }
    }
}
