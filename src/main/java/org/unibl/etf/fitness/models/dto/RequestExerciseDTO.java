package org.unibl.etf.fitness.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestExerciseDTO {

    private String exercise;
    private Integer sets;
    private Integer reps;
    private Double weight;
    private Date date;
}
