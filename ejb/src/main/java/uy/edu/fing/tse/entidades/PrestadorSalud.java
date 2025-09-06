package uy.edu.fing.tse.entidades;

import java.time.LocalDate;

public class PrestadorSalud {
    private long id;
    private String nombre;
    private String rut;
    private LocalDate fechaAlta;


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
