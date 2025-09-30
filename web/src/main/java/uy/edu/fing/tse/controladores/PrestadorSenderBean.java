package uy.edu.fing.tse.controladores;

import java.util.logging.Logger;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.Queue;

@Named @RequestScoped
public class PrestadorSenderBean {

  private String rut;
  private String nombre;
  private String fecha;

  @Resource(lookup = "java:/ConnectionFactory")
  private ConnectionFactory cf;

  @Resource(lookup = "java:/jms/queue/queue_alta_prestador")
  private Queue queue;

  public void enviarAlta(String rut, String nombre, String fecha) {
    Logger.getLogger(PrestadorSenderBean.class.getName())
            .info("Enviando a JMS: " + String.join("|", rut, nombre, fecha));
    try (JMSContext ctx = cf.createContext()) {
      
      ctx.createProducer().send(queue, String.join("|", rut, nombre, fecha));
    }
  }

  // atajo para probar rápido
  public void enviarPrueba() { enviarAlta("215123450019", "Sanatorio Central", "2025-09-20"); }

  public String getRut() { return rut; }
  public void setRut(String rut) { this.rut = rut; }
  public String getNombre() { return nombre; }
  public void setNombre(String nombre) { this.nombre = nombre; }
  public String getFecha() { return fecha; }
  public void setFecha(String fecha) { this.fecha = fecha; }
  
}
