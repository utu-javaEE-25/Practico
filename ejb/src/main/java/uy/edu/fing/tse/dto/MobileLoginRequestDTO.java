package uy.edu.fing.tse.dto;

import jakarta.validation.constraints.NotBlank;

public class MobileLoginRequestDTO {

    @NotBlank
    private String idToken;

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
