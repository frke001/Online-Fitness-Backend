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
    @PersistenceContext
    private EntityManager entityManager;

    public ClientServiceImpl(ClientRepository clientRepository, ModelMapper modelMapper, ImageService imageService, PasswordEncoder passwordEncoder, FitnessProgramRepository fitnessProgramRepository, FitnessProgramCategoryAttributeRepository fitnessProgramCategoryAttributeRepository, ParticipateRepository participateRepository, MessageRepository messageRepository) {
        this.clientRepository = clientRepository;
        this.modelMapper = modelMapper;
        this.imageService = imageService;
        this.passwordEncoder = passwordEncoder;
        this.fitnessProgramRepository = fitnessProgramRepository;
        this.fitnessProgramCategoryAttributeRepository = fitnessProgramCategoryAttributeRepository;
        this.participateRepository = participateRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public UpdateClientResponseDTO getDetails(Long id, Authentication auth) {
        var jwtUser=(JwtUserDTO) auth.getPrincipal();
        if(!jwtUser.getId().equals(id))
            throw new UnauthorizedException();
        var entity=clientRepository.findById(id).orElseThrow(NotFoundException::new);
        return modelMapper.map(entity,UpdateClientResponseDTO.class);
    }

    @Override
    public UpdateClientResponseDTO updateProfile(Long id, UpdateClientRequestDTO request, Authentication auth) {
        var jwtUser=(JwtUserDTO) auth.getPrincipal();
        if(!jwtUser.getId().equals(id))
            throw new UnauthorizedException();
        var entity=clientRepository.findById(id).orElseThrow(NotFoundException::new);
        if(request.getCity() != null)
            entity.setCity(request.getCity());
        if(request.getMail() != null)
            entity.setMail(request.getMail());
        if(request.getSurname() != null)
            entity.setSurname(request.getSurname());
        if(request.getName() != null)
            entity.setName(request.getName());
        return modelMapper.map(entity,UpdateClientResponseDTO.class);
    }

    @Override
    public boolean updateProfilePicture(Long id, UpdatePictureDTO request, Authentication auth) {
        var jwtUser=(JwtUserDTO) auth.getPrincipal();
        if(!jwtUser.getId().equals(id))
            throw new UnauthorizedException();
        var entity=clientRepository.findById(id).orElseThrow(NotFoundException::new);
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setId(request.getProfilePictureId());
        if(entity.getProfileImage() != null) {
            try {
                imageService.deleteImage(entity.getProfileImage());
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return false;
            }
        }
        entity.setProfileImage(imageEntity);
        return true;
    }

    @Override
    public Long getImageId(Long id, Authentication auth) {
        var jwtUser=(JwtUserDTO) auth.getPrincipal();
        if(!jwtUser.getId().equals(id))
            throw new UnauthorizedException();
        var entity=clientRepository.findById(id).orElseThrow(NotFoundException::new);

        return entity.getProfileImage() != null? entity.getProfileImage().getId() : null;
    }

    @Override
    public boolean changePassword(Long id, ChangePasswordDTO request, Authentication auth) {
        var user = clientRepository.findById(id).orElseThrow(NotFoundException::new);
        var jwtUser =(JwtUserDTO)auth.getPrincipal();
        if(!jwtUser.getId().equals(user.getId()))
            throw new UnauthorizedException();

        if(!passwordEncoder.matches(request.getOldPassword(), user.getPassword()))
            return false;
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        return true;
    }

    @Override
    public ResponseFitnessProgramDTO insertFitnessProgram(Long id, RequestFitnessProgramDTO request, Authentication auth) {
        var user = clientRepository.findById(id).orElseThrow(NotFoundException::new);
        var jwtUser =(JwtUserDTO)auth.getPrincipal();
        if(!jwtUser.getId().equals(user.getId()))
            throw new UnauthorizedException();

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
        return modelMapper.map(entity, ResponseFitnessProgramDTO.class);
    }

    @Override
    public List<CardFitnessProgramDTO> getAllProgramsForClient(Long id, Authentication auth) {
        var user = clientRepository.findById(id).orElseThrow(NotFoundException::new);
        var jwtUser =(JwtUserDTO)auth.getPrincipal();
        if(!jwtUser.getId().equals(user.getId()))
            throw new UnauthorizedException();
        return fitnessProgramRepository.getAllByClientIdAndDeleted(id,false).stream()
                .map(el -> modelMapper.map(el,CardFitnessProgramDTO.class)).collect(Collectors.toList());
    }

    @Override
    public boolean deleteFitnessProgram(Long clientId, Long programId, Authentication auth) {
        var user = clientRepository.findById(clientId).orElseThrow(NotFoundException::new);
        var jwtUser =(JwtUserDTO)auth.getPrincipal();
        if(!jwtUser.getId().equals(user.getId()))
            throw new UnauthorizedException();
        if(fitnessProgramRepository.existsById(programId)){
            var entity = fitnessProgramRepository.findById(programId).get();
            entity.setDeleted(true);
            return true;
        }
        return false;
    }

    @Override
    public ResponseParticipateEntityDTO participateInProgram(Long clientId, Long programId, Authentication auth) {
        var user = clientRepository.findById(clientId).orElseThrow(NotFoundException::new);
        var jwtUser =(JwtUserDTO)auth.getPrincipal();
        if(!jwtUser.getId().equals(user.getId()))
            throw new UnauthorizedException();
        if(!fitnessProgramRepository.existsById(programId))
            throw new NotFoundException();
        ParticipateEntity participateEntity = new ParticipateEntity();
        participateEntity.setId(null);
        FitnessProgramEntity fitnessProgramEntity = new FitnessProgramEntity();
        fitnessProgramEntity.setId(programId);
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(clientId);
        participateEntity.setFitnessProgram(fitnessProgramEntity);
        participateEntity.setClient(clientEntity);
        participateEntity.setStartDate(new Date());
        participateEntity = participateRepository.saveAndFlush(participateEntity);
        entityManager.refresh(participateEntity);

        return modelMapper.map(participateEntity,ResponseParticipateEntityDTO.class);
    }

    @Override
    public boolean isParticipating(Long clientId, Long programId, Authentication auth) {
        var user = clientRepository.findById(clientId).orElseThrow(NotFoundException::new);
        var jwtUser =(JwtUserDTO)auth.getPrincipal();
        if(!jwtUser.getId().equals(user.getId()))
            throw new UnauthorizedException();
        return participateRepository.existsByClientIdAndFitnessProgramId(clientId, programId);
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
            throw new UnauthorizedException();
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
            throw new UnauthorizedException();
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
        return result;
    }

    @Override
    public MessageDTO updateMessage(Long clientId, Long messageId, Authentication auth) {
        var user = clientRepository.findById(clientId).orElseThrow(NotFoundException::new);
        var jwtUser =(JwtUserDTO)auth.getPrincipal();
        if(!jwtUser.getId().equals(user.getId()))
            throw new UnauthorizedException();
        MessageEntity messageEntity = messageRepository.findById(messageId).orElseThrow(NotFoundException::new);
        messageEntity.setIsRead(true);
        messageEntity = messageRepository.saveAndFlush(messageEntity);
        entityManager.refresh(messageEntity);
        var result = modelMapper.map(messageEntity,MessageDTO.class);
        result.setCreationDate(new SimpleDateFormat("dd MMM, yyyy HH:mm").format(messageEntity.getCreationDate()));
        return result;
    }

    private List<CardFitnessProgramDTO> getFinishedInProgress(Long id, Authentication auth, boolean check){
        var user = clientRepository.findById(id).orElseThrow(NotFoundException::new);
        var jwtUser =(JwtUserDTO)auth.getPrincipal();
        if(!jwtUser.getId().equals(user.getId()))
            throw new UnauthorizedException();

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
