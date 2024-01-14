package org.unibl.etf.fitness.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestQuestionDTO {

    private Long clientSenderId;
    private String question;
}
