package org.unibl.etf.fitness.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseFitnessProgramDTO {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String difficultyLevel;
    private String location;
    private String instructorName;
    private String instructorSurname;
    private String contact;
    private Long days;
    private Long imageId;
    private Long categoryId;
   // private List<CategoryAttributeValueDTO> categoryAttributeValues;
}
