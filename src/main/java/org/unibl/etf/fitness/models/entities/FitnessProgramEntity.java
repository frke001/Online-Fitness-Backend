package org.unibl.etf.fitness.models.entities;


import jakarta.persistence.*;
import lombok.Data;
import org.unibl.etf.fitness.models.enums.DifficultyLevel;
import org.unibl.etf.fitness.models.enums.Location;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "fintess_program")
public class FitnessProgramEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    protected Long id;

    @Basic
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Basic
    @Column(name = "description", length = 1000, nullable = false)
    private String description;

    @Basic
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "difficulty_level",nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private DifficultyLevel difficultyLevel;

    @Column(name = "location",nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Location location;

    @Basic
    @Column(name = "concrete_location", length = 100)
    private String concreteLocation;

    @Basic
    @Column(name = "instructor_name", length = 50, nullable = false)
    private String instructorName;

    @Basic
    @Column(name = "instructor_surname",length = 50,nullable = false)
    private String instructorSurname;

    @Basic
    @Column(name = "contact", length = 50,nullable = false)
    private String contact;

    @Basic
    @Column(name = "link")
    private String link;

    @Basic
    @Column(name = "days", nullable = false)
    private Long days;

    @Basic
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @OneToOne
    @JoinColumn(name="image_id",referencedColumnName = "id")
    private ImageEntity image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private CategoryEntity category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private ClientEntity client;

    @OneToMany(mappedBy = "fitnessProgram")
    private List<FitnessProgramCategoryAttributeEntity> categoryAttributeValues;

    @OneToMany(mappedBy = "fitnessProgram")
    private List<ParticipateEntity> participation;

}
