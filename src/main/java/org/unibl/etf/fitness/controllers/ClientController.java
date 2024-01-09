package org.unibl.etf.fitness.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.fitness.models.dto.ChangePasswordDTO;
import org.unibl.etf.fitness.models.dto.UpdateClientRequestDTO;
import org.unibl.etf.fitness.models.dto.UpdateClientResponseDTO;
import org.unibl.etf.fitness.models.dto.UpdatePictureDTO;
import org.unibl.etf.fitness.services.ClientService;

@RestController
@RequestMapping("api/v1/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/{id}")
    public UpdateClientResponseDTO getDetails(@PathVariable Long id, Authentication auth){
        return clientService.getDetails(id,auth);
    }
    @PutMapping("/{id}/profile")
    public UpdateClientResponseDTO updateProfile(@PathVariable Long id, @RequestBody UpdateClientRequestDTO request, Authentication auth){
        return clientService.updateProfile(id,request,auth);
    }
    @PutMapping("/{id}/profile-image")
    public boolean updateProfileImage(@PathVariable Long id, @RequestBody UpdatePictureDTO request, Authentication auth){
        return clientService.updateProfilePicture(id,request,auth);
    }
    @GetMapping("/{id}/profile-image")
    public Long getImageId(@PathVariable Long id, Authentication auth){
        return clientService.getImageId(id,auth);
    }
    @PutMapping("/{id}/change-password")
    public boolean changePassword(@PathVariable Long id, @RequestBody ChangePasswordDTO request, Authentication auth){
        return clientService.changePassword(id,request,auth);
    }
}
