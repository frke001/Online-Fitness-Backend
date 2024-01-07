package org.unibl.etf.fitness.models.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ValidationDTO {

    @NotBlank
    private String token;
}
