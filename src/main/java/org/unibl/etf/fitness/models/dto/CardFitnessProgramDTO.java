package org.unibl.etf.fitness.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardFitnessProgramDTO {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Long imageId;
}
