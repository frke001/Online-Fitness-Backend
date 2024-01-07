package org.unibl.etf.fitness.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="validation_token")
public class ValidationTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "token", nullable = false, length = 100)
    private String token;

    @OneToOne // dovoljno sa jedne strane
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private ClientEntity client;
}
