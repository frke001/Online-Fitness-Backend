package org.unibl.etf.fitness.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdvisorMailDTO {

    private String to;
    private String subject;
    private String message;
    private Long attachmentId;
}
