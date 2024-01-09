package org.unibl.etf.fitness.models.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateClientResponseDTO {

    private String name;
    private String surname;
    private String city;
    private String mail;
    private String username;
}
