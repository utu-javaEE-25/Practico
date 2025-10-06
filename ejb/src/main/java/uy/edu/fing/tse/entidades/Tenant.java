package uy.edu.fing.tse.entidades;

import java.io.Serializable;

/**
 * Entidad que representa un Tenant (inquilino) en un sistema multitenant.
 * Un tenant es típicamente una organización o empresa que usa el sistema.
 */
public class Tenant implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private String codigo; // Identificador único del tenant (ej: "MUTUALISTA_A", "HOSPITAL_B")
    private String nombre;
    private String esquema; // Nombre del esquema de BD para enfoque multi-esquema
    private boolean activo;

    public Tenant() {
    }

    public Tenant(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.activo = true;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEsquema() {
        return esquema;
    }

    public void setEsquema(String esquema) {
        this.esquema = esquema;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Tenant{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", esquema='" + esquema + '\'' +
                ", activo=" + activo +
                '}';
    }
}
