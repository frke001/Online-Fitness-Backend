package org.unibl.etf.fitness.models.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RequestRegisterClientDTO {

    @NotBlank
    @Size(max = 50)
    private String name;
    @NotBlank
    @Size(max = 50)
    private String surname;
    @NotBlank
    @Size(max = 50)
    private String username;
    @NotBlank
    @Size(min = 8)
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9]).*$", message = "Password must contain at least one uppercase letter and one digit")
    private String password;
    @NotBlank
    @Size(max = 50)
    private String city;
    @Email
    @Size(max = 50)
    private String mail;

    private Long profileImageId;
}
