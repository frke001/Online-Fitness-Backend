package org.unibl.etf.fitness.services;

import org.springframework.security.core.Authentication;
import org.unibl.etf.fitness.models.dto.ChangePasswordDTO;
import org.unibl.etf.fitness.models.dto.UpdateClientRequestDTO;
import org.unibl.etf.fitness.models.dto.UpdateClientResponseDTO;
import org.unibl.etf.fitness.models.dto.UpdatePictureDTO;

public interface ClientService {

    UpdateClientResponseDTO getDetails(Long id, Authentication auth);
    UpdateClientResponseDTO updateProfile(Long id, UpdateClientRequestDTO request, Authentication auth);
    boolean updateProfilePicture(Long id, UpdatePictureDTO request, Authentication auth);

    Long getImageId(Long id, Authentication auth);

    boolean changePassword(Long id, ChangePasswordDTO request, Authentication auth);
}
