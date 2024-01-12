package org.unibl.etf.fitness.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "participate")
public class ParticipateEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "start_date", nullable = false)
    private Date startDate;


    @ManyToOne
    @JoinColumn(name = "fitness_program_id", referencedColumnName = "id")
    private FitnessProgramEntity fitnessProgram;

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private ClientEntity client;


}
