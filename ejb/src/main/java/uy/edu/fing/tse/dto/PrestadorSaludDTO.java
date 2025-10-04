package uy.edu.fing.tse.dto;

public class PrestadorSaludDTO {
    private Long id;
    private String nombre;
    private String direccion;

    public PrestadorSaludDTO() {
    }

    //  constructor
    public PrestadorSaludDTO(Long id, String nombre, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    // toString method
    @Override
    public String toString() {
        return "PrestadorSaludDTO{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                '}';
    }
}