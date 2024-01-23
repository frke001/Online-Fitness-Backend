package org.unibl.etf.fitness.services.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.unibl.etf.fitness.exceptions.NotFoundException;
import org.unibl.etf.fitness.exceptions.UnauthorizedException;
import org.unibl.etf.fitness.models.dto.*;
import org.unibl.etf.fitness.models.entities.*;
import org.unibl.etf.fitness.models.enums.DifficultyLevel;
import org.unibl.etf.fitness.models.enums.Location;
import org.unibl.etf.fitness.repositories.*;
import org.unibl.etf.fitness.services.ClientService;
import org.unibl.etf.fitness.services.ImageService;
import org.unibl.etf.fitness.services.LogService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;
    private final ImageService imageService;
    private final PasswordEncoder passwordEncoder;
    private final FitnessProgramRepository fitnessProgramRepository;
    private final ParticipateRepository participateRepository;

    private final FitnessProgramCategoryAttributeRepository fitnessProgramCategoryAttributeRepository;
    private final MessageRepository messageRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final AdvisorQuestionRepository advisorQuestionRepository;
    private final ExerciseRepository exerciseRepository;

    private final ProgressRepository progressRepository;

    private final LogService logService;
    @PersistenceContext
    private EntityManager entityManager;

    public ClientServiceImpl(ClientRepository clientRepository, ModelMapper modelMapper, ImageService imageService, PasswordEncoder passwordEncoder, FitnessProgramRepository fitnessProgramRepository, FitnessProgramCategoryAttributeRepository fitnessProgramCategoryAttributeRepository, ParticipateRepository participateRepository, MessageRepository messageRepository, SubscriptionRepository subscriptionRepository, AdvisorQuestionRepository advisorQuestionRepository, ExerciseRepository exerciseRepository, ProgressRepository progressRepository, LogService logService) {
        this.clientRepository = clientRepository;
        this.modelMapper = modelMapper;
        this.imageService = imageService;
        this.passwordEncoder = passwordEncoder;
        this.fitnessProgramRepository = fitnessProgramRepository;
        this.fitnessProgramCategoryAttributeRepository = fitnessProgramCategoryAttributeRepository;
        this.participateRepository = participateRepository;
        this.messageRepository = messageRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.advisorQuestionRepository = advisorQuestionRepository;
        this.exerciseRepository = exerciseRepository;
        this.progressRepository = progressRepository;
        this.logService = logService;
    }

    @Override
    public UpdateClientResponseDTO getDetails(Long id, Authentication auth) {
        var jwtUser=(JwtUserDTO) auth.getPrincipal();
        if(!jwtUser.getId().equals(id)) {
            logService.warning("Access to someone else's account attempted! User: " + jwtUser.getUsername() + ".");
            throw new UnauthorizedException();
        }
        var entity=clientRepository.findById(id).orElseThrow(NotFoundException::new);
        //logService.info("Profile details fetched. User: " + entity.getUsername() + ".");
        return modelMapper.map(entity,UpdateClientResponseDTO.class);
    }

    @Override
    public UpdateClientResponseDTO updateProfile(Long id, UpdateClientRequestDTO request, Authentication auth) {
        var jwtUser=(JwtUserDTO) auth.getPrincipal();
        if(!jwtUser.getId().equals(id))
        {
            logService.warning("Access to someone else's account attempted! User: " + jwtUser.getUsername() + ".");
            throw new UnauthorizedException();
        }
        var entity=clientRepository.findById(id).orElseThrow(NotFoundException::new);
        if(request.getCity() != null)
            entity.setCity(request.getCity());
        if(request.getMail() != null)
            entity.setMail(request.getMail());
        if(request.getSurname() != null)
            entity.setSurname(request.getSurname());
        if(request.getName() != null)
            entity.setName(request.getName());
        logService.info("Profile updated successfully. User: " + entity.getUsername() + ".");
        return modelMapper.map(entity,UpdateClientResponseDTO.class);
    }

    @Override
    public boolean updateProfilePicture(Long id, UpdatePictureDTO request, Authentication auth) {
        var jwtUser=(JwtUserDTO) auth.getPrincipal();
        if(!jwtUser.getId().equals(id))
        {
            logService.warning("Access to someone else's account attempted! User: " + jwtUser.getUsername() + ".");
            throw new UnauthorizedException();
        }
        var entity=clientRepository.findById(id).orElseThrow(NotFoundException::new);
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setId(request.getProfilePictureId());
        if(entity.getProfileImage() != null) {
            try {

                imageService.deleteImage(entity.getProfileImage());
                logService.info("Old profile picture deleted. User: " + entity.getUsername() + ".");
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return false;
            }
        }
        logService.info("Profile picture updated successfully. User: " + entity.getUsername() + ".");
        entity.setProfileImage(imageEntity);
        return true;
    }

    @Override
    public Long getImageId(Long id, Authentication auth) {
        var jwtUser=(JwtUserDTO) auth.getPrincipal();
        if(!jwtUser.getId().equals(id))
        {
            logService.warning("Access to someone else's account attempted! User: " + jwtUser.getUsername() + ".");
            throw new UnauthorizedException();
        }
        var entity=clientRepository.findById(id).orElseThrow(NotFoundException::new);
        logService.info("Image id fetched. User: " + entity.getUsername() + ".");
        return entity.getProfileImage() != null? entity.getProfileImage().getId() : null;
    }

    @Override
    public boolean changePassword(Long id, ChangePasswordDTO request, Authentication auth) {
        var user = clientRepository.findById(id).orElseThrow(NotFoundException::new);
        var jwtUser =(JwtUserDTO)auth.getPrincipal();
        if(!jwtUser.getId().equals(user.getId()))
        {
            logService.warning("Access to someone else's account attempted! User: " + jwtUser.getUsername() + ".");
            throw new UnauthorizedException();
        }

        if(!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            logService.warning("Change password attempted with wrong credentials. User: " + user.getUsername() + ".");
            return false;
        }
        logService.info("Change password successful. User: " + user.getUsername() + ".");
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        return true;
    }

    @Override
    public ResponseFitnessProgramDTO insertFitnessProgram(Long id, RequestFitnessProgramDTO request, Authentication auth) {
        var user = clientRepository.findById(id).orElseThrow(NotFoundException::new);
        var jwtUser =(JwtUserDTO)auth.getPrincipal();
        if(!jwtUser.getId().equals(user.getId()))
        {
            logService.warning("Access to someone else's account attempted! User: " + jwtUser.getUsername() + ".");
            throw new UnauthorizedException();
        }

        var entity = new FitnessProgramEntity();/* modelMapper.map(request, FitnessProgramEntity.class);*/
        entity.setName(request.getName());
        entity.setDays(request.getDays());
        entity.setContact(request.getContact());
        entity.setDescription(request.getDescription());
        entity.setPrice(request.getPrice());
        entity.setInstructorName(request.getInstructorName());
        entity.setInstructorSurname(request.getInstructorSurname());
        entity.setLink(request.getLink());
        entity.setConcreteLocation(request.getConcreteLocation());
        entity.setDeleted(false);
        entity.setCreationDate(new Date());
        entity.setId(null);
        ClientEntity client = new ClientEntity();
        client.setId(id);
        entity.setClient(client);
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(request.getCategoryId());
        entity.setCategory(categoryEntity);
        entity.setLocation(Location.getByLocation(request.getLocation()));
        entity.setDifficultyLevel(DifficultyLevel.getByLevel(request.getDifficultyLevel()));
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setId(request.getImageId());
        entity.setImage(imageEntity);
        entity = fitnessProgramRepository.saveAndFlush(entity);
        entityManager.refresh(entity);
        for(var values: request.getCategoryAttributeValues()){
            FitnessProgramCategoryAttributeEntity fitnessProgramCategoryAttributeEntity = new FitnessProgramCategoryAttributeEntity();
            fitnessProgramCategoryAttributeEntity.setFitnessProgram(entity);
            fitnessProgramCategoryAttributeEntity.setValue(values.getValue());
            CategoryAttributeEntity categoryAttributeEntity = new CategoryAttributeEntity();
            categoryAttributeEntity.setId(values.getId());
            fitnessProgramCategoryAttributeEntity.setCategoryAttribute(categoryAttributeEntity);
            fitnessProgramCategoryAttributeEntity = fitnessProgramCategoryAttributeRepository.saveAndFlush(fitnessProgramCategoryAttributeEntity);
            entityManager.refresh(fitnessProgramCategoryAttributeEntity);
        }
        logService.info("New fitness program created. User: " + user.getUsername() + ". Fitness program: " + entity.getName() + ".");
        return modelMapper.map(entity, ResponseFitnessProgramDTO.class);
    }

    @Override
    public List<CardFitnessProgramDTO> getAllProgramsForClient(Long id, Authentication auth) {
        var user = clientRepository.findById(id).orElseThrow(NotFoundException::new);
        var jwtUser =(JwtUserDTO)auth.getPrincipal();
        if(!jwtUser.getId().equals(user.getId()))
        {
            logService.warning("Access to someone else's account attempted! User: " + jwtUser.getUsername() + ".");
            throw new UnauthorizedException();
        }
        //logService.info("All client's fitness programs fetched. User: " + user.getUsername() + ".");
        return fitnessProgramRepository.getAllByClientIdAndDeleted(id,false).stream()
                .map(el -> modelMapper.map(el,CardFitnessProgramDTO.class)).collect(Collectors.toList());
    }

    @Override
    public boolean deleteFitnessProgram(Long clientId, Long programId, Authentication auth) {
        var user = clientRepository.findById(clientId).orElseThrow(NotFoundException::new);
        var jwtUser =(JwtUserDTO)auth.getPrincipal();
        if(!jwtUser.getId().equals(user.getId()))
        {
            logService.warning("Access to someone else's account attempted! User: " + jwtUser.getUsername() + ".");
            throw new UnauthorizedException();
        }
        if(fitnessProgramRepository.existsById(programId)){
            var entity = fitnessProgramRepository.findById(programId).get();
            entity.setDeleted(true);
            logService.info("Fitness program deleted. User: " + user.getUsername() + ". Fitness program: " + entity.getName() + ".");
            return true;
        }
        return false;
    }

    @Override
    public ResponseParticipateEntityDTO participateInProgram(Long clientId, Long programId, Authentication auth) {
        var user = clientRepository.findById(clientId).orElseThrow(NotFoundException::new);
        var jwtUser =(JwtUserDTO)auth.getPrincipal();
        if(!jwtUser.getId().equals(user.getId()))
        {
            logService.warning("Access to someone else's account attempted! User: " + jwtUser.getUsername() + ".");
            throw new UnauthorizedException();
        }
        if(!fitnessProgramRepository.existsById(programId))
            throw new NotFoundException();
        ParticipateEntity participateEntity;
        if(participateRepository.existsByClientIdAndFitnessProgramId(clientId, programId)){
            participateEntity = participateRepository.findByClientIdAndFitnessProgramId(clientId,programId);
            participateEntity.setStartDate(new Date());
        }else {
            participateEntity = new ParticipateEntity();
            participateEntity.setId(null);
            FitnessProgramEntity fitnessProgramEntity = new FitnessProgramEntity();
            fitnessProgramEntity.setId(programId);
            ClientEntity clientEntity = new ClientEntity();
            clientEntity.setId(clientId);
            participateEntity.setFitnessProgram(fitnessProgramEntity);
            participateEntity.setClient(clientEntity);
            participateEntity.setStartDate(new Date());
        }
        participateEntity = participateRepository.saveAndFlush(participateEntity);
        entityManager.refresh(participateEntity);
        logService.info("New participation in fitness program. User: " + user.getUsername() + ".");
        return modelMapper.map(participateEntity,ResponseParticipateEntityDTO.class);
    }

    @Override
    public boolean isParticipating(Long clientId, Long programId, Authentication auth) {
        var user = clientRepository.findById(clientId).orElseThrow(NotFoundException::new);
        var jwtUser =(JwtUserDTO)auth.getPrincipal();
        if(!jwtUser.getId().equals(user.getId()))
        {
            logService.warning("Access to someone else's account attempted! User: " + jwtUser.getUsername() + ".");
            throw new UnauthorizedException();
        }
        var isParticipating = participateRepository.existsByClientIdAndFitnessProgramId(clientId, programId);
        if(isParticipating){
            FitnessProgramEntity fitnessProgramEntity = fitnessProgramRepository.findById(programId).orElseThrow(NotFoundException::new);
            ParticipateEntity participateEntity = participateRepository.findByClientIdAndFitnessProgramId(clientId, programId);
            if(!this.checkDate(fitnessProgramEntity, participateEntity)){ // ako nije prosao program(jos ucestvuje)
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public List<CardFitnessProgramDTO> getAllProgramsInProgress(Long id, Authentication auth) {
        return getFinishedInProgress(id, auth, false);
    }

    @Override
    public List<CardFitnessProgramDTO> getAllProgramsFinished(Long id, Authentication auth) {
        return getFinishedInProgress(id, auth, true);
    }

    @Override
    public List<ClientChatDTO> getAllClients() {
        var temp = clientRepository.findAll().stream().map(el->modelMapper.map(el,ClientChatDTO.class)).collect(Collectors.toList());
        return clientRepository.findAll().stream().map(el->modelMapper.map(el,ClientChatDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<MessageDTO> getAllMessages(Long id, Authentication auth) {
        var user = clientRepository.findById(id).orElseThrow(NotFoundException::new);
        var jwtUser =(JwtUserDTO)auth.getPrincipal();
        if(!jwtUser.getId().equals(user.getId()))
        {
            logService.warning("Access to someone else's account attempted! User: " + jwtUser.getUsername() + ".");
            throw new UnauthorizedException();
        }
        List<MessageDTO> result = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy HH:mm");
        var received = messageRepository.findAllByClientReceiverId(id).stream().map(el -> {
            MessageDTO messageDTO = modelMapper.map(el, MessageDTO.class);
            messageDTO.setCreationDate(dateFormat.format(el.getCreationDate()));
            return messageDTO;
        }).collect(Collectors.toList());;
        var sent = messageRepository.findAllByClientSenderId(id).stream().map(el -> {
            MessageDTO messageDTO = modelMapper.map(el, MessageDTO.class);
            messageDTO.setCreationDate(dateFormat.format(el.getCreationDate()));
            return messageDTO;
        }).collect(Collectors.toList());;
        result.addAll(received);
        result.addAll(sent);
        return result.stream().sorted(Comparator.comparing(MessageDTO::getCreationDate, Collections.reverseOrder())).collect(Collectors.toList());
    }

    @Override
    public MessageDTO insertMessage(Long id, RequestMessageDTO request, Authentication auth) {
        var user = clientRepository.findById(id).orElseThrow(NotFoundException::new);
        var jwtUser =(JwtUserDTO)auth.getPrincipal();
        if(!jwtUser.getId().equals(user.getId()))
        {
            logService.warning("Access to someone else's account attempted! User: " + jwtUser.getUsername() + ".");
            throw new UnauthorizedException();
        }
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setId(null);
        messageEntity.setClientSender(user);
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(request.getReceiverId());
        messageEntity.setClientReceiver(clientEntity);
        messageEntity.setIsRead(false);
        messageEntity.setText(request.getText());
        messageEntity.setCreationDate(new Date());
        messageEntity = messageRepository.saveAndFlush(messageEntity);
        entityManager.refresh(messageEntity);
        var result = modelMapper.map(messageEntity,MessageDTO.class);
        result.setCreationDate(new SimpleDateFormat("dd MMM, yyyy HH:mm").format(messageEntity.getCreationDate()));
        logService.info("New message created! User: " + user.getUsername() + ".");
        return result;
    }

    @Override
    public MessageDTO updateMessage(Long clientId, Long messageId, Authentication auth) {
        var user = clientRepository.findById(clientId).orElseThrow(NotFoundException::new);
        var jwtUser =(JwtUserDTO)auth.getPrincipal();
        if(!jwtUser.getId().equals(user.getId()))
        {
            logService.warning("Access to someone else's account attempted! User: " + jwtUser.getUsername() + ".");
            throw new UnauthorizedException();
        }
        MessageEntity messageEntity = messageRepository.findById(messageId).orElseThrow(NotFoundException::new);
        messageEntity.setIsRead(true);
        messageEntity = messageRepository.saveAndFlush(messageEntity);
        entityManager.refresh(messageEntity);
        var result = modelMapper.map(messageEntity,MessageDTO.class);
        result.setCreationDate(new SimpleDateFormat("dd MMM, yyyy HH:mm").format(messageEntity.getCreationDate()));
        logService.info("Message has been read! User: " + user.getUsername() + ".");
        return result;
    }

    @Override
    public void subscribe(Long clientId, Long categoryId, Authentication auth) {
        var user = clientRepository.findById(clientId).orElseThrow(NotFoundException::new);
        var jwtUser =(JwtUserDTO)auth.getPrincipal();
        if(!jwtUser.getId().equals(user.getId()))
        {
            logService.warning("Access to someone else's account attempted! User: " + jwtUser.getUsername() + ".");
            throw new UnauthorizedException();
        }
        SubscriptionEntity subscriptionEntity = new SubscriptionEntity();
        subscriptionEntity.setId(null);
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(clientId);
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(categoryId);
        subscriptionEntity.setClient(clientEntity);
        subscriptionEntity.setCategory(categoryEntity);
        logService.info("New category subscription! User: " + user.getUsername() + ".");
        subscriptionRepository.saveAndFlush(subscriptionEntity);
    }

    @Override
    public void unsubscribe(Long clientId, Long categoryId, Authentication auth) {
        var user = clientRepository.findById(clientId).orElseThrow(NotFoundException::new);
        var jwtUser =(JwtUserDTO)auth.getPrincipal();
        if(!jwtUser.getId().equals(user.getId()))
        {
            logService.warning("Access to someone else's account attempted! User: " + jwtUser.getUsername() + ".");
            throw new UnauthorizedException();
        }
        if(subscriptionRepository.existsByClientIdAndCategoryId(clientId, categoryId)){
            subscriptionRepository.deleteByCategoryIdAndClientId(categoryId,clientId);
            logService.info("Unsubscribing from a category! User: " + user.getUsername() + ".");
        }else{
            throw new NotFoundException();
        }
    }

    @Override
    public boolean isSubscribed(Long clientId, Long categoryId, Authentication auth) {
        return subscriptionRepository.existsByClientIdAndCategoryId(clientId, categoryId);
    }

    @Override
    public AdvisorQuestionDTO askAdvisor(Long clientId, RequestAdvisorQuestionDTO request, Authentication auth) {
        var user = clientRepository.findById(clientId).orElseThrow(NotFoundException::new);
        var jwtUser =(JwtUserDTO)auth.getPrincipal();
        if(!jwtUser.getId().equals(user.getId()))
        {
            logService.warning("Access to someone else's account attempted! User: " + jwtUser.getUsername() + ".");
            throw new UnauthorizedException();
        }
        AdvisorQuestionEntity advisorQuestionEntity = new AdvisorQuestionEntity();
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(clientId);
        advisorQuestionEntity.setClientSender(clientEntity);
        FitnessProgramEntity fitnessProgramEntity = new FitnessProgramEntity();
        fitnessProgramEntity.setId(request.getFitnessProgramId());
        advisorQuestionEntity.setFitnessProgram(fitnessProgramEntity);
        advisorQuestionEntity.setQuestion(request.getQuestion());
        advisorQuestionEntity.setIsRead(false);
        advisorQuestionEntity.setCreationDate(new Date());
        advisorQuestionEntity = advisorQuestionRepository.saveAndFlush(advisorQuestionEntity);
        entityManager.refresh(advisorQuestionEntity);
        logService.info("New advisor question sent! User: " + user.getUsername() + ".");
        return modelMapper.map(advisorQuestionEntity,AdvisorQuestionDTO.class);

    }

    @Override
    public ResponseExerciseDTO insertExercise(Long clientId, RequestExerciseDTO request, Authentication auth) {
        var user = clientRepository.findById(clientId).orElseThrow(NotFoundException::new);
        var jwtUser =(JwtUserDTO)auth.getPrincipal();
        if(!jwtUser.getId().equals(user.getId()))
        {
            logService.warning("Access to someone else's account attempted! User: " + jwtUser.getUsername() + ".");
            throw new UnauthorizedException();
        }
        ExerciseEntity exerciseEntity = new ExerciseEntity();
        exerciseEntity.setId(null);
        exerciseEntity.setClient(user);
        exerciseEntity.setExercise(request.getExercise());
        exerciseEntity.setSets(request.getSets());
        exerciseEntity.setReps(request.getReps());
        exerciseEntity.setDate(request.getDate());
        exerciseEntity.setWeight(request.getWeight());
        exerciseEntity = exerciseRepository.saveAndFlush(exerciseEntity);
        entityManager.refresh(exerciseEntity);
        logService.info("New exercise created! User: " + user.getUsername() + ". Exercise: " + exerciseEntity.getExercise() + ".");
        return modelMapper.map(exerciseEntity, ResponseExerciseDTO.class);
    }

    @Override
    public void deleteExercise(Long clientId, Long exerciseId, Authentication auth) {
        var user = clientRepository.findById(clientId).orElseThrow(NotFoundException::new);
        var jwtUser =(JwtUserDTO)auth.getPrincipal();
        if(!jwtUser.getId().equals(user.getId()))
        {
            logService.warning("Access to someone else's account attempted! User: " + jwtUser.getUsername() + ".");
            throw new UnauthorizedException();
        }
        if(exerciseRepository.existsById(exerciseId)){
            logService.info("Exercise deleted! User: " + user.getUsername() + ".");
            exerciseRepository.deleteById(exerciseId);
        }else {
            throw new NotFoundException();
        }
    }

    @Override
    public List<ResponseExerciseDTO> getAllExercisesForClient(Long id, Authentication auth) {
        var user = clientRepository.findById(id).orElseThrow(NotFoundException::new);
        var jwtUser =(JwtUserDTO)auth.getPrincipal();
        if(!jwtUser.getId().equals(user.getId()))
        {
            logService.warning("Access to someone else's account attempted! User: " + jwtUser.getUsername() + ".");
            throw new UnauthorizedException();
        }
        return exerciseRepository.findAllByClientId(id).stream()
                .map(el->{
                    ResponseExerciseDTO responseExerciseDTO = modelMapper.map(el,ResponseExerciseDTO.class);
                    responseExerciseDTO.setDate(new SimpleDateFormat("dd MMM, yyyy").format(el.getDate()));
                    return responseExerciseDTO;
                }).collect(Collectors.toList());
    }

    @Override
    public ResponseProgressChartDTO insertProgressEntry(Long id, RequestProgressDTO request, Authentication auth) {
        var user = clientRepository.findById(id).orElseThrow(NotFoundException::new);
        var jwtUser =(JwtUserDTO)auth.getPrincipal();
        if(!jwtUser.getId().equals(user.getId()))
        {
            logService.warning("Access to someone else's account attempted! User: " + jwtUser.getUsername() + ".");
            throw new UnauthorizedException();
        }
        ProgressEntity progressEntity = new ProgressEntity();
        if(progressRepository.existsByDateAndClientId(request.getDate(), id)){
            progressEntity = progressRepository.getByDate(request.getDate());
            progressEntity.setWeight(request.getWeight());
            progressEntity = progressRepository.saveAndFlush(progressEntity);
        }else{
            progressEntity.setId(null);
            progressEntity.setClient(user);
            progressEntity.setWeight(request.getWeight());
            progressEntity.setDate(request.getDate());
            progressEntity = progressRepository.saveAndFlush(progressEntity);
        }
        logService.info("New weight inserted! User: " + user.getUsername() + ".");
        var progress = progressRepository.getAllByClientId(id);
        return getValues(id,progress);
    }

    @Override
    public ResponseProgressChartDTO getChartValues(Long id, RequestChartDTO request, Authentication auth) {
        var user = clientRepository.findById(id).orElseThrow(NotFoundException::new);
        var jwtUser =(JwtUserDTO)auth.getPrincipal();
        if(!jwtUser.getId().equals(user.getId()))
        {
            logService.warning("Access to someone else's account attempted! User: " + jwtUser.getUsername() + ".");
            throw new UnauthorizedException();
        }
        List<ProgressEntity> progress = progressRepository.getAllByClientId(id);
        if(request.getStartDate() != null && request.getEndDate() != null){
            progress = progress.stream().filter(entity -> {
                        LocalDate localDate = entity.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        LocalDate startDate = request.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        LocalDate endDate = request.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        return (localDate.isAfter(startDate) || localDate.isEqual(startDate)) && (localDate.isBefore(endDate) || localDate.isEqual(endDate));
                    })
                    .collect(Collectors.toList());
        }
        return getValues(id,progress);
    }

    private ResponseProgressChartDTO getValues(Long id, List<ProgressEntity> progressList){

        List<Double> yAxis = new ArrayList<>();
        List<String> xAxis = new ArrayList<>();

        progressList.stream().sorted(Comparator.comparing(ProgressEntity::getDate)).forEach(el->{
            xAxis.add(new SimpleDateFormat("dd/MM/yyyy").format(el.getDate()));
            yAxis.add(el.getWeight());
        });
        ResponseProgressChartDTO response = new ResponseProgressChartDTO();
        response.setXAxis(xAxis);
        response.setYAxis(yAxis);
        return response;
    }

    private List<CardFitnessProgramDTO> getFinishedInProgress(Long id, Authentication auth, boolean check){
        var user = clientRepository.findById(id).orElseThrow(NotFoundException::new);
        var jwtUser =(JwtUserDTO)auth.getPrincipal();
        if(!jwtUser.getId().equals(user.getId()))
        {
            logService.warning("Access to someone else's account attempted! User: " + jwtUser.getUsername() + ".");
            throw new UnauthorizedException();
        }

        List<CardFitnessProgramDTO> programDTOS = new ArrayList<>();

        ClientEntity clientEntity = clientRepository.findById(id).orElseThrow(NotFoundException::new);
        List<ParticipateEntity> participation = clientEntity.getParticipation();

        participation.stream().forEach(part->{
            FitnessProgramEntity fitnessProgramEntity = part.getFitnessProgram();
            if(check){
                if(checkDate(fitnessProgramEntity,part)){
                    programDTOS.add(modelMapper.map(fitnessProgramEntity, CardFitnessProgramDTO.class));
                }
            }else {
                if(!checkDate(fitnessProgramEntity,part)){
                    programDTOS.add(modelMapper.map(fitnessProgramEntity, CardFitnessProgramDTO.class));
                }
            }
        });

        return programDTOS;
    }

    private boolean checkDate(FitnessProgramEntity fitnessProgramEntity ,ParticipateEntity part) {
        Date currentDate = new Date();
        long daysToAdd = fitnessProgramEntity.getDays();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(part.getStartDate());

        calendar.add(Calendar.DAY_OF_MONTH, (int)daysToAdd);

        Date programEndDate = calendar.getTime();
        System.out.println(currentDate);
        System.out.println(programEndDate);
        if(currentDate.after(programEndDate)){ // prosao program
            return true;
        }else //  nije prosao program
            return  false;
    }
}
