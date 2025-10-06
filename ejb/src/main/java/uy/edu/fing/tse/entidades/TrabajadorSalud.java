package uy.edu.fing.tse.entidades;

import java.io.Serializable;
import java.time.LocalDate;

public class TrabajadorSalud implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String cedula;
    private String nombreCompleto;
    private LocalDate fechaIngreso;
    private String especialidad;
    private String prestadorRUT;

    public TrabajadorSalud() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getPrestadorRUT() {
        return prestadorRUT;
    }

    public void setPrestadorRUT(String prestadorRUT) {
        this.prestadorRUT = prestadorRUT;
    }

    @Override
    public String toString() {
        return "TrabajadorSalud [id=" + id + ", cedula=" + cedula + ", nombreCompleto=" + nombreCompleto + "]";
    }
}
