package org.unibl.etf.fitness.models.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Builder
@Entity
@Table(name = "image")
public class ImageEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id",nullable = false)
    private Long id;
    @Basic
    @Column(name="name",nullable = false)
    private String name;
    @Basic
    @Column(name="type",nullable = false)
    private String type;
    @Basic
    @Column(name="size",nullable = false)
    private Long size;
}
