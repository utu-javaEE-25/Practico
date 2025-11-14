package uy.edu.fing.tse.servicios;

import jakarta.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class AccesoNoAutorizadoException extends RuntimeException {
    public AccesoNoAutorizadoException(String message) {
        super(message);
    }
}