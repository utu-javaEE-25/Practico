package uy.edu.fing.tse.exception;

import java.io.Serial;
import java.io.Serializable;

/**
 * Excepción personalizada para errores específicos del cliente PDI/Dnic.
 * Hereda de RuntimeException para evitar declarar 'throws' en los métodos de servicio.
 */
public class PDIClientException extends RuntimeException implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructor que acepta un mensaje de error.
     *
     * @param message Mensaje detallado del error.
     */
    public PDIClientException(String message) {
        super(message);
    }

    /**
     * Constructor que acepta un mensaje y la causa original (Throwable).
     *
     * @param message Mensaje detallado del error.
     * @param cause   La excepción original que causó el fallo.
     */
    public PDIClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
