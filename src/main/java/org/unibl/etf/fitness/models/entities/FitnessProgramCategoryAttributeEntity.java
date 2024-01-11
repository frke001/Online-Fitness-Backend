package org.unibl.etf.fitness.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "fitness_program_category_attribute")
public class FitnessProgramCategoryAttributeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "value",nullable = false)
    private String value;

    @ManyToOne
    @JoinColumn(name = "fitness_program_id", referencedColumnName = "id")
    private FitnessProgramEntity fitnessProgram;

    @ManyToOne
    @JoinColumn(name = "category_attribute_id", referencedColumnName = "id")
    private CategoryAttributeEntity categoryAttribute;

}
