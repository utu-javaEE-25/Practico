package uy.edu.fing.tse.controladores.DTO;

import jakarta.validation.constraints.NotBlank;

public class MobileTokenRequestDTO {

    // Asumimos que el token es una cadena de texto requerida
    @NotBlank(message = "El valor del token no puede estar vacío.")
    private String tokenValue;

    // Getters y Setters
    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }
}
