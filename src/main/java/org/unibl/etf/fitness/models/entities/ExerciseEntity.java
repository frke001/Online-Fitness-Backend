package org.unibl.etf.fitness.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "exercise")
public class ExerciseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Basic
    @Column(name = "exercise", nullable = false)
    private String exercise;

    @Basic
    @Column(name = "sets", nullable = false)
    private Integer sets;

    @Basic
    @Column(name = "reps", nullable = false)
    private Integer reps;

    @Basic
    @Column(name = "weight", nullable = false)
    private Double weight;

    @Basic
    @Column(name = "date", nullable = false)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private ClientEntity client;
}
