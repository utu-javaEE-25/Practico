package uy.edu.fing.tse.controladores.beans;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import uy.edu.fing.tse.api.PrestadorSaludServiceLocal;
import uy.edu.fing.tse.entidades.PrestadorSalud;

@Named("prestadorSaludBean")
@ViewScoped
public class PrestadorSaludBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private PrestadorSaludServiceLocal prestadorService;

    private List<PrestadorSalud> listaCompleta;
    private List<PrestadorSalud> prestadoresFiltrados;
    private PrestadorSalud nuevoPrestador;

    private String buscarRut;
    private String buscarNombre;

    @PostConstruct
    public void init() {
        this.nuevoPrestador = new PrestadorSalud();
        this.cargarListaCompleta();
        this.prestadoresFiltrados = this.listaCompleta;
    }

    private void cargarListaCompleta() {
        this.listaCompleta = prestadorService.listar();
    }

    public void crear() {
        try {
            LocalDateTime ahora = LocalDateTime.now();
            if (this.nuevoPrestador.getFechaCreacion() == null) {
                this.nuevoPrestador.setFechaCreacion(ahora);
            }
            this.nuevoPrestador.setFechaModificacion(ahora);

            prestadorService.crear(this.nuevoPrestador);
            this.nuevoPrestador = new PrestadorSalud(); // Limpiar el formulario
            this.cargarListaCompleta();
            this.buscar(); // Actualizar la lista mostrada
            addMessage(FacesMessage.SEVERITY_INFO, "Exito", "Prestador de salud creado correctamente.");
        } catch (IllegalArgumentException e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage());
        }
    }

    public void eliminar(String rut) {
        System.out.println("--- INTENTANDO ELIMINAR PRESTADOR CON RUT: " + rut);
        try {
            prestadorService.eliminar(rut);
            System.out.println("--- ELIMINACION DE LA BASE DE DATOS EXITOSA.");

            this.cargarListaCompleta();
            System.out.println("--- LISTA COMPLETA RECARGADA. TAMANO: " + this.listaCompleta.size());

            this.buscar();
            System.out.println("--- LISTA FILTRADA ACTUALIZADA. TAMANO: " + this.prestadoresFiltrados.size());

            addMessage(FacesMessage.SEVERITY_INFO, "Exito", "Prestador de salud eliminado.");
        } catch (Exception e) {
            System.err.println("--- ERROR AL ELIMINAR PRESTADOR ---");
            e.printStackTrace();
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ocurrio un error al eliminar: " + e.getMessage());
        }
    }

    public void buscar() {
        this.prestadoresFiltrados = this.listaCompleta.stream()
                .filter(p -> {
                    boolean match = true;
                    if (buscarRut != null && !buscarRut.trim().isEmpty()) {
                        match = match && p.getRut() != null && p.getRut().trim().equalsIgnoreCase(buscarRut.trim());
                    }
                    if (buscarNombre != null && !buscarNombre.trim().isEmpty()) {
                        match = match && p.getNombre() != null
                                && p.getNombre().toLowerCase().contains(buscarNombre.trim().toLowerCase());
                    }
                    return match;
                })
                .collect(Collectors.toList());
    }

    public void limpiarBusqueda() {
        this.buscarRut = null;
        this.buscarNombre = null;
        this.prestadoresFiltrados = this.listaCompleta;
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

    // --- Getters y Setters ---

    public List<PrestadorSalud> getPrestadoresFiltrados() {
        return prestadoresFiltrados;
    }

    public void setPrestadoresFiltrados(List<PrestadorSalud> prestadoresFiltrados) {
        this.prestadoresFiltrados = prestadoresFiltrados;
    }

    public PrestadorSalud getNuevoPrestador() {
        return nuevoPrestador;
    }

    public void setNuevoPrestador(PrestadorSalud nuevoPrestador) {
        this.nuevoPrestador = nuevoPrestador;
    }

    public String getBuscarRut() {
        return buscarRut;
    }

    public void setBuscarRut(String buscarRut) {
        this.buscarRut = buscarRut;
    }

    public String getBuscarNombre() {
        return buscarNombre;
    }

    public void setBuscarNombre(String buscarNombre) {
        this.buscarNombre = buscarNombre;
    }
}
