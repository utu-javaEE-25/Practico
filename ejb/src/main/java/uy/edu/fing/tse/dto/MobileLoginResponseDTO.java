package uy.edu.fing.tse.dto;

public class MobileLoginResponseDTO {

    private Long id;
    private String ci;
    private String nombre;
    private String apellido;
    private String email;
    private boolean returningUser;

    public MobileLoginResponseDTO(Long id, String ci, String nombre, String apellido, String email,
            boolean returningUser) {
        this.id = id;
        this.ci = ci;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.returningUser = returningUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isReturningUser() {
        return returningUser;
    }

    public void setReturningUser(boolean returningUser) {
        this.returningUser = returningUser;
    }
}
