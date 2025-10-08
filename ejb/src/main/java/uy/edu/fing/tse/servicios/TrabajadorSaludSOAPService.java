package uy.edu.fing.tse.servicios;

import java.util.List;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import uy.edu.fing.tse.api.TrabajadorSaludServiceLocal;
import uy.edu.fing.tse.entidades.TrabajadorSalud;

@Stateless
@WebService(serviceName = "TrabajadorSaludSOAP")
public class TrabajadorSaludSOAPService {

    @EJB
    private TrabajadorSaludServiceLocal trabajadorService;

    @WebMethod
    public void altaTrabajador(@WebParam(name = "trabajador") TrabajadorSalud trabajador) throws Exception {
        trabajadorService.altaTrabajador(trabajador);
    }

    @WebMethod
    public List<TrabajadorSalud> obtenerTodosLosTrabajadores() {
        return trabajadorService.obtenerTodosLosTrabajadores();
    }

    @WebMethod
    public TrabajadorSalud obtenerTrabajadorPorCedula(@WebParam(name = "cedula") String cedula) {
        return trabajadorService.obtenerTrabajadorPorCedula(cedula);
    }
}
