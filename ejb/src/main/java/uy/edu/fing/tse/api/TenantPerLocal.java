package uy.edu.fing.tse.api;

import jakarta.ejb.Local;
import uy.edu.fing.tse.entidades.Tenant;
import java.util.List;

@Local
public interface TenantPerLocal {
    Tenant crear(Tenant tenant);
    Tenant obtenerPorCodigo(String codigo);
    List<Tenant> listar();
    void actualizar(Tenant tenant);
    void eliminar(String codigo);
    boolean existeCodigo(String codigo);
}
