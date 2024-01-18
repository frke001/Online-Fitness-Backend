package org.unibl.etf.fitness.models.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdvisorQuestionDTO {

    private Long id;
    private String question;
    private Boolean isRead;
    private Long clientSenderId;
    private Long fitnessProgramId;
    private Date creationDate;
}
