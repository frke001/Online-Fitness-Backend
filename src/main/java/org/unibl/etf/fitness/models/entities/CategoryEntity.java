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
    protected String name;

    @Basic
    @Column(name = "deleted", nullable = false)
    protected Boolean deleted;

    @OneToMany(mappedBy = "category")
    List<CategoryAttributeEntity> attributes;

}
