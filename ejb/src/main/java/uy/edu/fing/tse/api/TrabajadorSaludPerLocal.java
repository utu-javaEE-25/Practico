package uy.edu.fing.tse.api;

import java.util.List;
import jakarta.ejb.Local;


import uy.edu.fing.tse.entidades.TrabajadorSalud;

@Local
public interface TrabajadorSaludPerLocal {

    void agregarTrabajador(TrabajadorSalud trabajador);
    
    List<TrabajadorSalud> listarTrabajadores();

    TrabajadorSalud buscarTrabajadorPorCedula(String cedula);
    
}
