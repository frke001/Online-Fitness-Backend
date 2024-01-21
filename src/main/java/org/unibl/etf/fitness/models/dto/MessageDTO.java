package org.unibl.etf.fitness.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private Long id;
    private String text;
    private String creationDate;
    private Boolean isRead;
    private Long clientSenderId;
    private Long clientSenderProfileImageId;
    private String clientSenderUsername;
    private String clientReceiverUsername;
}
