package org.unibl.etf.fitness.models.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestProgressDTO {

    @NotNull
    private Double weight;
    @NotNull
    private Date date;
}
