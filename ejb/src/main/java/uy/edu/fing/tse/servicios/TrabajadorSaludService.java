package uy.edu.fing.tse.servicios;

import java.util.List;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import uy.edu.fing.tse.api.TrabajadorSaludPerLocal;
import uy.edu.fing.tse.entidades.TrabajadorSalud;
import uy.edu.fing.tse.api.TrabajadorSaludServiceLocal;


@Stateless
public class TrabajadorSaludService implements TrabajadorSaludServiceLocal {

    @EJB
    private TrabajadorSaludPerLocal trabajadorDAO;

    @Override
    public void altaTrabajador(TrabajadorSalud trabajador) throws Exception {

        if (trabajador.getCedula() == null || trabajador.getCedula().trim().isEmpty()) {
            throw new Exception("La cédula no puede estar vacía.");
        }

        if (trabajadorDAO.buscarTrabajadorPorCedula(trabajador.getCedula()) != null) {
            throw new Exception("Ya existe un trabajador con la cédula " + trabajador.getCedula());
        }

        trabajadorDAO.agregarTrabajador(trabajador);
    }

    @Override
    public List<TrabajadorSalud> obtenerTodosLosTrabajadores() {
        return trabajadorDAO.listarTrabajadores();
    }

    @Override
    public TrabajadorSalud obtenerTrabajadorPorCedula(String cedula) {
        return trabajadorDAO.buscarTrabajadorPorCedula(cedula);
    }
}
