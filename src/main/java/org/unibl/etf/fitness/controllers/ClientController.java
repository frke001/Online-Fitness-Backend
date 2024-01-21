package org.unibl.etf.fitness.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.fitness.models.dto.*;
import org.unibl.etf.fitness.services.ClientService;

import java.util.List;

@RestController
@RequestMapping("api/v1/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping()
    public List<ClientChatDTO> getAllClients(){
        return clientService.getAllClients();
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

    @PostMapping("/{id}/fitness-programs")
    public ResponseFitnessProgramDTO insertFitnessProgramForClient(@PathVariable Long id, @RequestBody RequestFitnessProgramDTO request, Authentication auth){
        return clientService.insertFitnessProgram(id,request,auth);
    }

    @GetMapping("/{id}/fitness-programs")
    public List<CardFitnessProgramDTO> getAllProgramsForClient(@PathVariable Long id, Authentication auth){
        return clientService.getAllProgramsForClient(id,auth);
    }

    @DeleteMapping("/{clientId}/fitness-programs/{programId}")
    public boolean insertFitnessProgramForClient(@PathVariable Long clientId, @PathVariable Long programId, Authentication auth){
        return clientService.deleteFitnessProgram(clientId,programId,auth);
    }

    @PostMapping("/{clientId}/fitness-programs/{programId}/participate")
    public ResponseParticipateEntityDTO participateInProgram(@PathVariable Long clientId, @PathVariable Long programId, Authentication auth){
        return clientService.participateInProgram(clientId,programId,auth);
    }

    @GetMapping("/{clientId}/fitness-programs/{programId}/participate")
    public boolean isParticipating(@PathVariable Long clientId, @PathVariable Long programId, Authentication auth){
        return clientService.isParticipating(clientId,programId,auth);
    }

    @GetMapping("/{id}/fitness-programs/in-progress")
    public List<CardFitnessProgramDTO> getAllProgramsInProgress(@PathVariable Long id, Authentication auth){
        return clientService.getAllProgramsInProgress(id,auth);
    }

    @GetMapping("/{id}/fitness-programs/finished")
    public List<CardFitnessProgramDTO> getAllProgramsFinished(@PathVariable Long id, Authentication auth){
        return clientService.getAllProgramsFinished(id,auth);
    }
    @GetMapping("/{id}/messages")
    public List<MessageDTO> getAllMessages(@PathVariable Long id, Authentication auth){
        return clientService.getAllMessages(id,auth);
    }
    @PostMapping("/{id}/messages")
    public MessageDTO insertMessage(@PathVariable Long id, @RequestBody RequestMessageDTO request, Authentication auth){
        return clientService.insertMessage(id,request,auth);
    }
    @PutMapping("/{clientId}/messages/{messageId}")
    public MessageDTO insertMessage(@PathVariable Long clientId, @PathVariable Long messageId, Authentication auth){
        return clientService.updateMessage(clientId,messageId,auth);
    }

    @PostMapping("/{clientId}/subscribe/{categoryId}")
    public void subscribe(@PathVariable Long clientId, @PathVariable Long categoryId, Authentication auth){
        clientService.subscribe(clientId,categoryId,auth);
    }
    @DeleteMapping("/{clientId}/unsubscribe/{categoryId}")
    public void unsubscribe(@PathVariable Long clientId, @PathVariable Long categoryId, Authentication auth){
        clientService.unsubscribe(clientId,categoryId,auth);
    }
    @GetMapping("/{clientId}/subscribe/{categoryId}")
    public boolean isSubscribed(@PathVariable Long clientId, @PathVariable Long categoryId, Authentication auth){
        return clientService.isSubscribed(clientId,categoryId,auth);
    }

    @PostMapping("/{id}/ask-advisor")
    public AdvisorQuestionDTO askAdvisor(@PathVariable Long id, @RequestBody RequestAdvisorQuestionDTO request, Authentication auth){
        return  clientService.askAdvisor(id,request,auth);
    }
    @GetMapping("/{id}/exercises")
    public List<ResponseExerciseDTO> getAllExercisesForClient(@PathVariable Long id, Authentication auth){
        return clientService.getAllExercisesForClient(id,auth);
    }

    @PostMapping("/{id}/exercises")
    public ResponseExerciseDTO insertExercise(@PathVariable Long id, @RequestBody RequestExerciseDTO request, Authentication auth){
        return  clientService.insertExercise(id,request,auth);
    }

    @DeleteMapping("/{clientId}/exercises/{exerciseId}")
    public void deleteExercise(@PathVariable Long clientId, @PathVariable Long exerciseId, Authentication auth){
        clientService.deleteExercise(clientId,exerciseId,auth);
    }

    @PostMapping("/{id}/progress")
    public ResponseProgressChartDTO insertProgressEntity(@PathVariable Long id, @RequestBody RequestProgressDTO request, Authentication auth){
        return clientService.insertProgressEntry(id,request,auth);
    }

    @PostMapping("/{id}/progress/filter")
    public ResponseProgressChartDTO getProgressChartValues(@PathVariable Long id, @RequestBody RequestChartDTO request, Authentication auth){
        return clientService.getChartValues(id,request,auth);
    }

}
