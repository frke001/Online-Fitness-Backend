package org.unibl.etf.fitness.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestMessageDTO {
    @NotNull
    private Long receiverId;
    @NotBlank
    private String text;
}
