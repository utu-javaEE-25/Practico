package uy.edu.fing.tse.ws;

import jakarta.ejb.EJB;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import uy.edu.fing.tse.api.PrestadorSaludServiceLocal;
import uy.edu.fing.tse.entidades.PrestadorSalud;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@WebService(serviceName = "PrestadorSaludWS")
public class PrestadorSaludWebService {
    
    @EJB
    private PrestadorSaludServiceLocal servicio;

    @WebMethod
    public PrestadorSalud crear(
        @WebParam(name="rut") String rut,
        @WebParam(name="nombre") String nombre,
        @WebParam(name="fechaAlta") String fechaAlta
    ) {
        PrestadorSalud p = new PrestadorSalud();
        p.setRut(rut);
        p.setNombre(nombre);

        LocalDateTime fechaCreacion = LocalDate.now().atStartOfDay();
        if (fechaAlta != null && !fechaAlta.isBlank()) {
            fechaCreacion = LocalDate.parse(fechaAlta).atStartOfDay();
        }
        p.setFechaCreacion(fechaCreacion);
        p.setFechaModificacion(fechaCreacion);
        p.setEstado(Boolean.TRUE);

        return servicio.crear(p);
    }

    @WebMethod
    public List<PrestadorSalud> listar() {
        return servicio.listar();
    }

    @WebMethod
    public PrestadorSalud obtener(@WebParam(name="rut") String rut) {
        return servicio.obtener(rut);
    }

    @WebMethod
    public void eliminar(@WebParam(name="rut") String rut) {
        servicio.desactivar(rut);
    }

    @WebMethod
    public void activar(@WebParam(name = "rut") String rut) {
        servicio.activar(rut);
    }

}
