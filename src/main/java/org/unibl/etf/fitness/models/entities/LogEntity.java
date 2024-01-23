package org.unibl.etf.fitness.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "log")
public class LogEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Basic
    @Column(name = "level", nullable = false)
    private String level;

    @Basic
    @Column(name = "message", nullable = false, length = 1000)
    private String message;

    @Basic
    @Column(name = "creation_date", nullable = false)
    private Date creationDate;
}
