package org.unibl.etf.fitness.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Size(max = 1000)
    private String text;
}
