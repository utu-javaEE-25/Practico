package uy.edu.fing.tse.api;

import java.util.List;

import jakarta.ejb.Local;
import uy.edu.fing.tse.entidades.PrestadorSalud;

@Local
public interface PrestadorSaludServiceLocal {

    PrestadorSalud crear(PrestadorSalud prestador);
    PrestadorSalud obtener(long id);
    void actualizar(PrestadorSalud prestador);
    void eliminar(long id);
    List<PrestadorSalud> listar();

}
