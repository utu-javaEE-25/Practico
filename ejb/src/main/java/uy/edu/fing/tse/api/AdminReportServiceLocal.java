package uy.edu.fing.tse.api;

import jakarta.ejb.Local;
import uy.edu.fing.tse.dto.reportes.ActividadUsuariosDTO;
import uy.edu.fing.tse.dto.reportes.ResumenGeneralDTO;

@Local
public interface AdminReportServiceLocal {

    ResumenGeneralDTO obtenerResumenGeneral();

    ActividadUsuariosDTO obtenerActividadUsuarios();
}
