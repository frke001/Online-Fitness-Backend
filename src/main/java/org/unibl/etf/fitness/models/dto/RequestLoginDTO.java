package org.unibl.etf.fitness.models.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestLoginDTO {
    @NotBlank
    private String username;
    @NotBlank
    private  String password;
}
