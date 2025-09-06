package uy.edu.fing.tse.api;

import jakarta.ejb.Remote;
import uy.edu.fing.tse.entidades.PrestadorSalud;
import java.util.List;


@Remote
public interface PrestadorSaludPerRemote extends PrestadorSaludPerLocal {
    PrestadorSalud crear(PrestadorSalud prestador);
    PrestadorSalud obtener(long id);
    void actualizar(PrestadorSalud prestador);
    void eliminar(long id);
    List<PrestadorSalud> listar();
}
