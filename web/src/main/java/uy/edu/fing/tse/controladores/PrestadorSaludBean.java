package uy.edu.fing.tse.controladores;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

import uy.edu.fing.tse.api.PrestadorSaludServiceLocal;
import uy.edu.fing.tse.entidades.PrestadorSalud;

@Named("prestadorSaludBean")
@ViewScoped
public class PrestadorSaludBean implements Serializable {

    @EJB
    private PrestadorSaludServiceLocal servicio;

    private List<PrestadorSalud> prestadores;
    private PrestadorSalud nuevoPrestadorSalud;
    private String rutBuscar;
    private PrestadorSalud buscarPrestadorSalud;

    @PostConstruct
    public void postConstruct() {
        this.nuevoPrestadorSalud = new PrestadorSalud();
        this.nuevoPrestadorSalud.setNombre(".");
        this.nuevoPrestadorSalud.setRut(".");
        this.prestadores = servicio.listar();
    }

    public void crear() {
        System.out.println("Método crear invocado desde: " + Thread.currentThread().getStackTrace()[2]);
        try {
            servicio.crear(nuevoPrestadorSalud);
            prestadores = servicio.listar();
            nuevoPrestadorSalud = new PrestadorSalud();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Prestador de salud creado exitosamente.", null));
        } catch (IllegalArgumentException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        }
    }

    public void obtener() {
        this.buscarPrestadorSalud = servicio.obtener(this.rutBuscar);

        if (this.buscarPrestadorSalud == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "No se encontró el prestador de salud con RUT: " + this.rutBuscar, null));
        }
    }

    private boolean validar(String s) {
        return s != null && !s.trim().isEmpty();
    }

    public List<PrestadorSalud> getPrestadores() {
        return prestadores;
    }

    public PrestadorSalud getPrestadorSalud() {
        return nuevoPrestadorSalud;
    }

    public void setPrestadorSalud(PrestadorSalud prestadorSalud) {
        this.nuevoPrestadorSalud = prestadorSalud;
    }

    public void setPrestadores(List<PrestadorSalud> prestadores) {
        this.prestadores = prestadores;
    }

    public String getRutBuscar() {
        return rutBuscar;
    }
    
}


