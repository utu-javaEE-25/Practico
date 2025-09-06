package uy.edu.fing.tse.api;

import java.util.*;

import jakarta.ejb.Local;
import uy.edu.fing.tse.entidades.PrestadorSalud;

@Local
public interface PrestadorSaludPerLocal {
    PrestadorSalud crear(PrestadorSalud prestador);
    PrestadorSalud obtener(String rut);
    void actualizar(PrestadorSalud prestador);
    void eliminar(String rut);
    List<PrestadorSalud> listar();
    PrestadorSalud obtenerPorRut(String rut);
    boolean existeRut(String rut);

}
