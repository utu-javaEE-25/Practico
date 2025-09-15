package uy.edu.fing.tse.api;

import java.util.List;
import jakarta.ejb.Remote;

import uy.edu.fing.tse.entidades.TrabajadorSalud;

public interface TrabajadorSaludPerRemote {

    void agregarTrabajador(TrabajadorSalud trabajador);
    
    List<TrabajadorSalud> listarTrabajadores();

    TrabajadorSalud buscarTrabajadorPorCedula(String cedula);
    
}
