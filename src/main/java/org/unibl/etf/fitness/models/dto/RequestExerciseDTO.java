package org.unibl.etf.fitness.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestExerciseDTO {

    @NotBlank
    private String exercise;
    @NotNull
    private Integer sets;
    @NotNull
    private Integer reps;
    @NotNull
    private Double weight;
    @NotNull
    private Date date;
}
