package org.unibl.etf.fitness.services;

import org.springframework.security.core.Authentication;
import org.unibl.etf.fitness.models.dto.*;
import org.unibl.etf.fitness.models.entities.ParticipateEntity;

import java.util.List;

public interface ClientService {

    UpdateClientResponseDTO getDetails(Long id, Authentication auth);
    UpdateClientResponseDTO updateProfile(Long id, UpdateClientRequestDTO request, Authentication auth);
    boolean updateProfilePicture(Long id, UpdatePictureDTO request, Authentication auth);

    Long getImageId(Long id, Authentication auth);

    boolean changePassword(Long id, ChangePasswordDTO request, Authentication auth);

    ResponseFitnessProgramDTO insertFitnessProgram(Long id, RequestFitnessProgramDTO request, Authentication auth);

    List<CardFitnessProgramDTO> getAllProgramsForClient(Long id, Authentication auth);

    boolean deleteFitnessProgram(Long clientId, Long programId, Authentication auth);

    ResponseParticipateEntityDTO participateInProgram(Long clientId, Long programId, Authentication auth);

    boolean isParticipating(Long clientId, Long programId, Authentication auth);

    List<CardFitnessProgramDTO> getAllProgramsInProgress(Long id, Authentication auth);
    List<CardFitnessProgramDTO> getAllProgramsFinished(Long id, Authentication auth);
    List<ClientChatDTO> getAllClients();
    List<MessageDTO> getAllMessages(Long id, Authentication auth);

    MessageDTO insertMessage(Long id, RequestMessageDTO request, Authentication auth);

    MessageDTO updateMessage(Long clientId, Long messageId, Authentication auth);

    void subscribe(Long clientId, Long categoryId, Authentication auth);
    void unsubscribe(Long clientId, Long categoryId, Authentication auth);

    boolean isSubscribed(Long clientId, Long categoryId, Authentication auth);

    AdvisorQuestionDTO askAdvisor(Long clientId, RequestAdvisorQuestionDTO request,Authentication auth);

}
