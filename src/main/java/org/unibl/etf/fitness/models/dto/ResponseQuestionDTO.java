package org.unibl.etf.fitness.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseQuestionDTO {
    private Long id;
    private String question;
    private Long clientSenderId;
    private String clientSenderUsername;
    private Long clientSenderProfileImageId;
}
