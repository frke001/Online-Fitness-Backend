package org.unibl.etf.fitness.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "question")
public class QuestionEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Basic
    @Column(name = "question", length = 1000)
    private String question;

    @ManyToOne
    @JoinColumn(name = "client_sender", referencedColumnName = "id")
    private ClientEntity clientSender;

    @ManyToOne
    @JoinColumn(name = "fitness_program", referencedColumnName = "id")
    private FitnessProgramEntity fitnessProgram;
}
