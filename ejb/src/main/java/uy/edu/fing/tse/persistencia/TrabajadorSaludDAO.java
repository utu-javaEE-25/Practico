package uy.edu.fing.tse.persistencia;

import uy.edu.fing.tse.entidades.TrabajadorSalud;
import uy.edu.fing.tse.api.TrabajadorSaludPerLocal;
import uy.edu.fing.tse.api.TrabajadorSaludPerRemote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;

@Stateless 
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
        return new ArrayList<>(trabajadores);
    }

    @Override
    public TrabajadorSalud buscarTrabajadorPorCedula(String cedula) {
        for (TrabajadorSalud t : trabajadores) {
            if (t.getCedula().equals(cedula)) {
                return t;
            }
        }
        return null;
    }
}