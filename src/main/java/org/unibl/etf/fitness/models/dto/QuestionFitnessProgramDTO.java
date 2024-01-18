package org.unibl.etf.fitness.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionFitnessProgramDTO {

    private Long id;
    private String name;
    private BigDecimal price;
    private String difficultyLevel;
}
