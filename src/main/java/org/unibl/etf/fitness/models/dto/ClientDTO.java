package org.unibl.etf.fitness.models.dto;

import lombok.Data;
import org.unibl.etf.fitness.models.enums.Role;

@Data
public class ClientDTO {

    private Long id;
    private String name;
    private String surname;
    private String username;
    private String city;
    private String mail;
    private Boolean accountStatus;
    private Role role;
    private Long profileImageId;
    private String token;
}
