package org.unibl.etf.fitness.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Table(name = "message")
public class MessageEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Basic
    @Column(name = "text", nullable = false, length = 1000)
    private String text;

    @Basic
    @Column(name = "is_read", nullable = false)
    private Boolean isRead;

    @Basic
    @Column(name = "creation_date", nullable = false)
    private Date creationDate;

    @ManyToOne
    @JoinColumn(name = "client_sender_id", referencedColumnName = "id")
    private ClientEntity clientSender;

    @ManyToOne
    @JoinColumn(name = "client_receiver_id", referencedColumnName = "id")
    private ClientEntity clientReceiver;
}
