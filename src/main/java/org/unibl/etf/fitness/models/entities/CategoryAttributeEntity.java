package org.unibl.etf.fitness.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "category_attribute")
public class CategoryAttributeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    protected Long id;

    @Basic
    @Column(name = "name", nullable = false, length = 50)
    protected String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id") // spajamo kolonu category_id u ovoj klasi i id u drugoj tabeli
    private CategoryEntity category;
}
