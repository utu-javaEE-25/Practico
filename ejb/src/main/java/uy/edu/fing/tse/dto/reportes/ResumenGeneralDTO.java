package uy.edu.fing.tse.dto.reportes;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ResumenGeneralDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private long prestadoresTotales;
    private long prestadoresActivos;
    private long usuariosTotales;
    private long usuariosActivos;
    private long administradoresTotales;
    private long administradoresActivos;
    private long usuariosNuevosSemana;
    private long prestadoresNuevosMes;
    private LocalDateTime ultimaActualizacion;

    public long getPrestadoresTotales() {
        return prestadoresTotales;
    }

    public void setPrestadoresTotales(long prestadoresTotales) {
        this.prestadoresTotales = prestadoresTotales;
    }

    public long getPrestadoresActivos() {
        return prestadoresActivos;
    }

    public void setPrestadoresActivos(long prestadoresActivos) {
        this.prestadoresActivos = prestadoresActivos;
    }

    public long getUsuariosTotales() {
        return usuariosTotales;
    }

    public void setUsuariosTotales(long usuariosTotales) {
        this.usuariosTotales = usuariosTotales;
    }

    public long getUsuariosActivos() {
        return usuariosActivos;
    }

    public void setUsuariosActivos(long usuariosActivos) {
        this.usuariosActivos = usuariosActivos;
    }

    public long getAdministradoresTotales() {
        return administradoresTotales;
    }

    public void setAdministradoresTotales(long administradoresTotales) {
        this.administradoresTotales = administradoresTotales;
    }

    public long getAdministradoresActivos() {
        return administradoresActivos;
    }

    public void setAdministradoresActivos(long administradoresActivos) {
        this.administradoresActivos = administradoresActivos;
    }

    public long getUsuariosNuevosSemana() {
        return usuariosNuevosSemana;
    }

    public void setUsuariosNuevosSemana(long usuariosNuevosSemana) {
        this.usuariosNuevosSemana = usuariosNuevosSemana;
    }

    public long getPrestadoresNuevosMes() {
        return prestadoresNuevosMes;
    }

    public void setPrestadoresNuevosMes(long prestadoresNuevosMes) {
        this.prestadoresNuevosMes = prestadoresNuevosMes;
    }

    public LocalDateTime getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }
}
