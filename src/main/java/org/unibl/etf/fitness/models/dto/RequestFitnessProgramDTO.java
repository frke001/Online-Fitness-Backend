package org.unibl.etf.fitness.models.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.fitness.models.entities.CategoryEntity;
import org.unibl.etf.fitness.models.entities.FitnessProgramCategoryAttributeEntity;
import org.unibl.etf.fitness.models.entities.ImageEntity;
import org.unibl.etf.fitness.models.enums.DifficultyLevel;
import org.unibl.etf.fitness.models.enums.Location;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestFitnessProgramDTO {

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 1000)
    private String description;

    @NotNull
    private BigDecimal price;

    @NotBlank
    private String difficultyLevel;

    @NotBlank
    private String location;

    @NotBlank
    private String concreteLocation;

    @NotBlank
    @Size(max = 50)
    private String instructorName;

    @NotBlank
    @Size(max = 50)
    private String instructorSurname;

    @NotBlank
    @Size(max = 50)
    private String contact;

    @NotNull
    private Long days;

    @NotNull
    private Long imageId;

    @NotNull
    private Long categoryId;

    @NotNull
    private List<CategoryAttributeValueDTO> categoryAttributeValues;

    private String link;

}
