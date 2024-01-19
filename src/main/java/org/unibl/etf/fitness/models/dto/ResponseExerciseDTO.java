package org.unibl.etf.fitness.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseExerciseDTO {

    private Long id;
    private String exercise;
    private Integer sets;
    private Integer reps;
    private Double weight;
    private String date;
    private Long clientId;
}
