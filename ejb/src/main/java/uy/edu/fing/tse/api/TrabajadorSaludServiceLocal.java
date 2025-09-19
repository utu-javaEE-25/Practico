package uy.edu.fing.tse.api;

import java.util.List;
import jakarta.ejb.Local;

import uy.edu.fing.tse.entidades.TrabajadorSalud;

@Local
public interface TrabajadorSaludServiceLocal {

    void altaTrabajador(TrabajadorSalud trabajador) throws Exception;

    List<TrabajadorSalud> obtenerTodosLosTrabajadores();

    TrabajadorSalud obtenerTrabajadorPorCedula(String cedula);
    
}
