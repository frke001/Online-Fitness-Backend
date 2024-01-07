package org.unibl.etf.fitness.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "advisor")
public class AdvisorEntity{

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Basic
    @Column(name = "surname", nullable = false, length = 50)
    protected String surname;

    @Basic
    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;

    @Basic
    @Column(name = "password", nullable = false, length = 512)
    private String password;

    @Basic
    @Column(name = "mail", nullable = false, length = 50, unique = true)
    private String mail;

    @Basic
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;
}
