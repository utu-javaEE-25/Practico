package uy.edu.fing.tse.dto.reportes;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ActividadUsuariosDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private long loginsExitososSemana;
    private long loginsFallidosSemana;
    private long loginsExitososMes;
    private long loginsFallidosMes;
    private LocalDateTime fechaCorte;
    private List<EventoActividadDTO> eventosRecientes = new ArrayList<>();

    public long getLoginsExitososSemana() {
        return loginsExitososSemana;
    }

    public void setLoginsExitososSemana(long loginsExitososSemana) {
        this.loginsExitososSemana = loginsExitososSemana;
    }

    public long getLoginsFallidosSemana() {
        return loginsFallidosSemana;
    }

    public void setLoginsFallidosSemana(long loginsFallidosSemana) {
        this.loginsFallidosSemana = loginsFallidosSemana;
    }

    public long getLoginsExitososMes() {
        return loginsExitososMes;
    }

    public void setLoginsExitososMes(long loginsExitososMes) {
        this.loginsExitososMes = loginsExitososMes;
    }

    public long getLoginsFallidosMes() {
        return loginsFallidosMes;
    }

    public void setLoginsFallidosMes(long loginsFallidosMes) {
        this.loginsFallidosMes = loginsFallidosMes;
    }

    public LocalDateTime getFechaCorte() {
        return fechaCorte;
    }

    public void setFechaCorte(LocalDateTime fechaCorte) {
        this.fechaCorte = fechaCorte;
    }

    public List<EventoActividadDTO> getEventosRecientes() {
        return eventosRecientes;
    }

    public void setEventosRecientes(List<EventoActividadDTO> eventosRecientes) {
        this.eventosRecientes = eventosRecientes;
    }
}
