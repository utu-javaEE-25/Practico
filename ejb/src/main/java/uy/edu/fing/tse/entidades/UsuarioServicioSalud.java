package uy.edu.fing.tse.entidades;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "usuario_servicio_salud")
public class UsuarioServicioSalud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column(name = "nombre_completo", nullable = false, length = 200)
    private String nombreCompleto;
    
    @Column(name = "cedula_identidad", nullable = false, unique = true, length = 8)
    private String cedulaIdentidad;
    
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;
    
    private boolean activo;
    
    @Column(name = "prestador_rut", nullable = false, length = 12)
    private String prestadorRUT;

    // Getters y Setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCedulaIdentidad() {
        return cedulaIdentidad;
    }

    public void setCedulaIdentidad(String cedulaIdentidad) {
        this.cedulaIdentidad = cedulaIdentidad;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getPrestadorRUT() {
        return prestadorRUT;
    }

    public void setPrestadorRUT(String prestadorRUT) {
        this.prestadorRUT = prestadorRUT;
    }
}