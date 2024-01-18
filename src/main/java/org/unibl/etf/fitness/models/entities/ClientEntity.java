package org.unibl.etf.fitness.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.unibl.etf.fitness.models.enums.Role;

import java.util.List;

@Data
@Entity
@Table(name = "client")
public class ClientEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Basic
    @Column(name = "surname", nullable = false, length = 50)
    private String surname;

    @Basic
    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;

    @Basic
    @Column(name = "password", nullable = false, length = 512)
    private String password;

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
    private Role role;

    @Basic
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @OneToOne
    @JoinColumn(name="phofile_image_id",referencedColumnName = "id")
    private ImageEntity profileImage;

    @OneToMany(mappedBy = "client")
    private List<FitnessProgramEntity> fitnessPrograms;

    @OneToMany(mappedBy = "client")
    private List<ParticipateEntity> participation;

    @OneToMany(mappedBy = "clientReceiver")
    private List<MessageEntity> messages;

    @OneToMany(mappedBy = "client")
    private List<SubscriptionEntity> subscriptions;

    @OneToMany(mappedBy = "clientSender")
    private List<AdvisorQuestionEntity> advisorQuestions;
}
