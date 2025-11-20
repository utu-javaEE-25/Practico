package uy.edu.fing.tse.servicios;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import uy.edu.fing.tse.client.PDIClient;
import uy.edu.fing.tse.client.dnic.Persona;
import uy.edu.fing.tse.exception.PDIClientException;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

@Stateless
public class VerificacionEdadService {

    private static final int MAYORIA_DE_EDAD = 18;

    // El formato de fecha CRÍTICO basado en la respuesta SOAP (DD/MM/YYYY)
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Inject
    private PDIClient pdiClient;

    /**
     * Verifica si una persona identificada por su documento es mayor de edad.
     *
     * @param nroDocumento Número de documento.
     * @return true si la persona es mayor o igual a 18 años, false si es menor.
     * @throws PDIClientException Si falla la comunicación o el formato de fecha es inválido.
     */
    public boolean esMayorDeEdad(String nroDocumento) throws PDIClientException {

        // 1. Consultar los datos de la persona
        Persona persona = pdiClient.obtenerPersonaPorDocumento(nroDocumento);

        // 2. Verificar datos esenciales
        String fechaNacimientoStr = persona != null ? persona.getFechaNacimiento() : null;

        if (fechaNacimientoStr == null || fechaNacimientoStr.trim().isEmpty()) {
            // Si el servicio no devuelve la fecha, asumimos que no se puede verificar.
            return false;
        }

        // 3. Convertir y Calcular la Edad
        try {
            LocalDate fechaNacimiento = LocalDate.parse(fechaNacimientoStr, DATE_FORMATTER);
            LocalDate fechaActual = LocalDate.now();

            // Calcular la edad en años
            int edad = Period.between(fechaNacimiento, fechaActual).getYears();

            // 4. Comparar con la mayoría de edad
            return edad >= MAYORIA_DE_EDAD;

        } catch (java.time.format.DateTimeParseException e) {
            // Lanza una excepción controlada si el formato del servicio SOAP es inesperado
            throw new PDIClientException("El formato de fecha recibido del PDI es inválido. Dato: " + fechaNacimientoStr, e);
        }
    }
}
