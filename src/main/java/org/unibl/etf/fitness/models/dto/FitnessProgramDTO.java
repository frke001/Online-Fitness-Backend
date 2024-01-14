package org.unibl.etf.fitness.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FitnessProgramDTO extends ResponseFitnessProgramDTO{

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String difficultyLevel;
    private String location;
    private String concreteLocation;
    private String instructorName;
    private String instructorSurname;
    private String contact;
    private Long days;
    private Long imageId;
    private String categoryName;
    private Long clientId;
    private String link;
    private List<ResponseCategoryAttributeValueDTO> categoryAttributeValues;
    private List<ResponseQuestionDTO> questions;
}
