package uy.edu.fing.tse.api;

import java.util.List;

import jakarta.ejb.Local;
import uy.edu.fing.tse.entidades.PrestadorSalud;

@Local
public interface PrestadorSaludServiceLocal {

    PrestadorSalud crear(PrestadorSalud prestador);
    PrestadorSalud obtener(String rut);
    void actualizar(PrestadorSalud prestador);
    void eliminar(String rut);
    List<PrestadorSalud> listar();
    void altaDesdeJms(String rut, String nombre, String fecha);


}
