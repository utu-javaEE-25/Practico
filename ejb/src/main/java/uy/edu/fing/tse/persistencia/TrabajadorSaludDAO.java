package uy.edu.fing.tse.persistencia;

import uy.edu.fing.tse.entidades.TrabajadorSalud;
import uy.edu.fing.tse.api.TrabajadorSaludPerLocal;
import uy.edu.fing.tse.api.TrabajadorSaludPerRemote;
import uy.edu.fing.tse.util.TenantContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

@Singleton
@Startup
public class TrabajadorSaludDAO implements TrabajadorSaludPerLocal, TrabajadorSaludPerRemote {

    private List<TrabajadorSalud> trabajadores;
    private final AtomicLong sequence = new AtomicLong(1);

    @PostConstruct
    public void init() {
        trabajadores = Collections.synchronizedList(new ArrayList<>());
    }

    @Override
    public void agregarTrabajador(TrabajadorSalud trabajador) {
        trabajador.setId(sequence.getAndIncrement());
        trabajadores.add(trabajador);
    }

    @Override
    public List<TrabajadorSalud> listarTrabajadores() {
        String tenantRUT = TenantContext.getCurrentTenant();
        if (tenantRUT == null) {
            return new ArrayList<>(trabajadores);
        }
        List<TrabajadorSalud> result = new ArrayList<>();
        for (TrabajadorSalud t : trabajadores) {
            if (tenantRUT.equals(t.getPrestadorRUT())) {
                result.add(t);
            }
        }
        return result;
    }

    @Override
    public TrabajadorSalud buscarTrabajadorPorCedula(String cedula) {
        String tenantRUT = TenantContext.getCurrentTenant();
        for (TrabajadorSalud t : trabajadores) {
            if (t.getCedula().equals(cedula)) {
                if (tenantRUT == null || tenantRUT.equals(t.getPrestadorRUT())) {
                    return t;
                }
            }
        }
        return null;
    }
}