package uy.edu.fing.tse.controladores.beans;

import java.util.logging.Logger;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.Queue;

@Named("trabajadorSaludSenderBean")
@RequestScoped
public class TrabajadorSaludSenderBean {

    private static final Logger log = Logger.getLogger(TrabajadorSaludSenderBean.class.getName());

    // Atributos para el formulario
    private String cedula;
    private String nombreCompleto;
    private String especialidad;

    // Inyectar los recursos de JMS
    @Resource(lookup = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "java:/jms/queue/queue_alta_trabajador")
    private Queue queue;

    public void enviarAltaAsync() {
        // Construir el mensaje con el formato "cedula|nombre|especialidad"
        String payload = String.join("|", cedula, nombreCompleto, especialidad);

        log.info("Enviando mensaje a la cola queue_alta_trabajador: " + payload);

        try (JMSContext context = connectionFactory.createContext()) {
            // Enviar el mensaje
            context.createProducer().send(queue, payload);
            log.info("Mensaje enviado con éxito.");
            
            // Limpiar campos del formulario
            this.cedula = null;
            this.nombreCompleto = null;
            this.especialidad = null;
            
            // Añadir mensaje de éxito para el usuario
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "La solicitud de alta fue enviada. El trabajador aparecerá en la lista en breve."));

        } catch (Exception e) {
            log.severe("Error al enviar mensaje JMS: " + e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo enviar la solicitud de alta."));
        }
    }

    // --- Getters y Setters ---
    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
}