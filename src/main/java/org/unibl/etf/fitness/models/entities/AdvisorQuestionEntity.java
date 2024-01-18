package org.unibl.etf.fitness.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "advisor_question")
public class AdvisorQuestionEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Basic
    @Column(name = "question", length = 1000)
    private String question;

    @Basic
    @Column(name = "is_read", nullable = false)
    private Boolean isRead;

    @ManyToOne
    @JoinColumn(name = "client_sender_id", referencedColumnName = "id")
    private ClientEntity clientSender;

    @ManyToOne
    @JoinColumn(name = "fitness_program_id", referencedColumnName = "id")
    private FitnessProgramEntity fitnessProgram;

    @Basic
    @Column(name = "creation_date", nullable = false)
    private Date creationDate;
}
