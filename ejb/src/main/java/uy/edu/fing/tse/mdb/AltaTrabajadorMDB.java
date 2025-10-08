package uy.edu.fing.tse.mdb;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.EJB;
import jakarta.ejb.MessageDriven;
import jakarta.jms.JMSException;
import jakarta.jms.JMSDestinationDefinition;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;
import uy.edu.fing.tse.api.TrabajadorSaludServiceLocal;
import uy.edu.fing.tse.entidades.TrabajadorSalud;

@JMSDestinationDefinition(
    name = "java:/jms/queue/queue_alta_trabajador",
    interfaceName = "jakarta.jms.Queue",
    destinationName = "queue_alta_trabajador"
)

@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:/jms/queue/queue_alta_trabajador"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue"),
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
})
public class AltaTrabajadorMDB implements MessageListener {

    private static final Logger log = Logger.getLogger(AltaTrabajadorMDB.class.getName());

    @EJB
    private TrabajadorSaludServiceLocal trabajadorService;

    @Override
    public void onMessage(Message message) {
        try {
            Thread.sleep(60000);
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String payload = textMessage.getText();
                log.info("MDB recibió un mensaje de alta: " + payload);

                // 1. Parsear el mensaje
                String[] parts = payload.split("\\|");

                if (parts.length != 3) {
                    log.warning("El mensaje no tiene el formato esperado (cedula|nombre|especialidad). Mensaje recibido: " + payload);
                    return; // Abortar si el formato es incorrecto
                }

                String cedula = parts[0];
                String nombreCompleto = parts[1];
                String especialidad = parts[2];

                // 2. Crear la entidad
                TrabajadorSalud nuevoTrabajador = new TrabajadorSalud();
                nuevoTrabajador.setCedula(cedula);
                nuevoTrabajador.setNombreCompleto(nombreCompleto);
                nuevoTrabajador.setEspecialidad(especialidad);
                nuevoTrabajador.setFechaIngreso(LocalDate.now());

                // 3. Llamar al servicio para dar de alta
                trabajadorService.altaTrabajador(nuevoTrabajador);

                log.info("Trabajador con cédula " + cedula + " ha sido dado de alta exitosamente por el MDB.");

            } else {
                log.warning("Se recibió un mensaje que no es de tipo TextMessage.");
            }
        } catch (JMSException e) {
            log.log(Level.SEVERE, "Error al procesar mensaje JMS", e);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error al intentar dar de alta al trabajador desde el MDB", e);
            // Aquí se podría reenviar el mensaje a una cola de errores (Dead Letter Queue)
        }
    }
}
