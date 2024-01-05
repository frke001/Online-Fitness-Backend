package org.unibl.etf.fitness.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.unibl.etf.fitness.models.enums.Role;

@Data
@Entity
@Table(name = "client")
public class ClientEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    protected Long id;

    @Basic
    @Column(name = "name", nullable = false, length = 50)
    protected String name;

    @Basic
    @Column(name = "surname", nullable = false, length = 50)
    protected String surname;

    @Basic
    @Column(name = "username", nullable = false, length = 50, unique = true)
    protected String username;

    @Basic
    @Column(name = "password", nullable = false, length = 512)
    protected String password;

    @Basic
    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @Basic
    @Column(name = "mail", nullable = false, length = 50, unique = true)
    private String mail;

    @Basic
    @Column(name = "account_status", nullable = false)
    private Boolean accountStatus;

    @Enumerated(EnumType.ORDINAL)
    @Column
    protected Role role;

    @Basic
    @Column(name = "deleted", nullable = false)
    protected Boolean deleted;
}
