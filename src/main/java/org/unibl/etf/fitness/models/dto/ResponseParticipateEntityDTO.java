package org.unibl.etf.fitness.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseParticipateEntityDTO {

    private Long id;
    private Date startDate;
    private Long clientId;
    private Long fitnessProgramId;
}
