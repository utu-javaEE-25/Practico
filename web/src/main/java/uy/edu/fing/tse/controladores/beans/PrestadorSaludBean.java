package uy.edu.fing.tse.controladores.beans;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private static final Logger LOGGER = Logger.getLogger(PrestadorSaludBean.class.getName());

    @EJB
    private PrestadorSaludServiceLocal prestadorService;

    private List<PrestadorSalud> listaCompleta;
    private List<PrestadorSalud> prestadoresFiltrados;
    private PrestadorSalud nuevoPrestador;
    private PrestadorSalud prestadorEnEdicion;
    private boolean modoEdicion;

    private String buscarRut;
    private String buscarNombre;

    @PostConstruct
    public void init() {
        this.nuevoPrestador = new PrestadorSalud();
        this.nuevoPrestador.setEstado(Boolean.TRUE);
        this.prestadorEnEdicion = new PrestadorSalud();
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
            this.nuevoPrestador.setTenantId(null);

            prestadorService.crear(this.nuevoPrestador);
            this.nuevoPrestador = new PrestadorSalud(); // Limpiar el formulario
            this.nuevoPrestador.setEstado(Boolean.TRUE);
            this.cargarListaCompleta();
            this.buscar(); // Actualizar la lista mostrada
            addMessage(FacesMessage.SEVERITY_INFO, "Exito", "Prestador de salud creado correctamente.");
        } catch (IllegalArgumentException e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage());
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo crear el prestador: " + e.getMessage());
        }
    }

    public void prepararEdicion(PrestadorSalud prestador) {
        if (prestador == null) {
            return;
        }

        this.prestadorEnEdicion = copiarPrestador(prestador);
        this.modoEdicion = true;
    }

    public void cancelarEdicion() {
        this.modoEdicion = false;
        this.prestadorEnEdicion = new PrestadorSalud();
    }

    public void actualizar() {
        if (!this.modoEdicion || this.prestadorEnEdicion == null || this.prestadorEnEdicion.getRut() == null) {
            addMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Seleccione un prestador para modificar.");
            return;
        }

        try {
            PrestadorSalud actual = prestadorService.obtener(this.prestadorEnEdicion.getRut());
            if (actual == null) {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error", "El prestador a modificar no existe.");
                return;
            }

            this.prestadorEnEdicion.setTenantId(actual.getTenantId());
            this.prestadorEnEdicion.setNombreSchema(actual.getNombreSchema());
            this.prestadorEnEdicion.setFechaCreacion(actual.getFechaCreacion());

            this.prestadorEnEdicion.setFechaModificacion(LocalDateTime.now());
            prestadorService.actualizar(this.prestadorEnEdicion);

            this.cargarListaCompleta();
            this.buscar();

            this.cancelarEdicion();
            addMessage(FacesMessage.SEVERITY_INFO, "Exito", "Prestador de salud actualizado correctamente.");
        } catch (IllegalArgumentException e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage());
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo actualizar el prestador: " + e.getMessage());
        }
    }

    public void desactivar(String rut) {
        LOGGER.info("--- INTENTANDO DESACTIVAR PRESTADOR CON RUT: " + rut);
        try {
            prestadorService.desactivar(rut);
            LOGGER.info("--- PRESTADOR DESACTIVADO EXITOSAMENTE.");

            this.cargarListaCompleta();
            LOGGER.info("--- LISTA COMPLETA RECARGADA. TAMANO: " + this.listaCompleta.size());

            this.buscar();
            LOGGER.info("--- LISTA FILTRADA ACTUALIZADA. TAMANO: " + this.prestadoresFiltrados.size());

            addMessage(FacesMessage.SEVERITY_INFO, "Exito", "Prestador de salud desactivado.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "--- ERROR AL DESACTIVAR PRESTADOR ---", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ocurrio un error al desactivar: " + e.getMessage());
        }
    }

    public void activar(String rut) {
        LOGGER.info("--- INTENTANDO ACTIVAR PRESTADOR CON RUT: " + rut);
        try {
            prestadorService.activar(rut);
            LOGGER.info("--- PRESTADOR ACTIVADO EXITOSAMENTE.");

            this.cargarListaCompleta();
            this.buscar();

            addMessage(FacesMessage.SEVERITY_INFO, "Exito", "Prestador de salud activado.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "--- ERROR AL ACTIVAR PRESTADOR ---", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ocurrio un error al activar: " + e.getMessage());
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

    private PrestadorSalud copiarPrestador(PrestadorSalud origen) {
        PrestadorSalud copia = new PrestadorSalud();
        copia.setTenantId(origen.getTenantId());
        copia.setNombreSchema(origen.getNombreSchema());
        copia.setNombre(origen.getNombre());
        copia.setRut(origen.getRut());
        copia.setEstado(origen.getEstado());
        copia.setContactoMail(origen.getContactoMail());
        copia.setTipo(origen.getTipo());
        copia.setFechaCreacion(origen.getFechaCreacion());
        copia.setFechaModificacion(origen.getFechaModificacion());
        return copia;
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

    public PrestadorSalud getPrestadorEnEdicion() {
        return prestadorEnEdicion;
    }

    public void setPrestadorEnEdicion(PrestadorSalud prestadorEnEdicion) {
        this.prestadorEnEdicion = prestadorEnEdicion;
    }

    public boolean isModoEdicion() {
        return modoEdicion;
    }

    public void setModoEdicion(boolean modoEdicion) {
        this.modoEdicion = modoEdicion;
    }
}
