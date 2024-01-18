package org.unibl.etf.fitness.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "category")
public class CategoryEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Basic
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @OneToMany(mappedBy = "category")
    private List<CategoryAttributeEntity> attributes;

    @OneToMany(mappedBy = "category")
    private List<FitnessProgramEntity> fitnessPrograms;

    @OneToMany(mappedBy = "category")
    private List<SubscriptionEntity> subscriptions;

}
