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
import org.unibl.etf.fitness.repositories.ClientRepository;
import org.unibl.etf.fitness.repositories.FitnessProgramCategoryAttributeRepository;
import org.unibl.etf.fitness.repositories.FitnessProgramRepository;
import org.unibl.etf.fitness.services.ClientService;
import org.unibl.etf.fitness.services.ImageService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;
    private final ImageService imageService;
    private final PasswordEncoder passwordEncoder;
    private final FitnessProgramRepository fitnessProgramRepository;

    private final FitnessProgramCategoryAttributeRepository fitnessProgramCategoryAttributeRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public ClientServiceImpl(ClientRepository clientRepository, ModelMapper modelMapper, ImageService imageService, PasswordEncoder passwordEncoder, FitnessProgramRepository fitnessProgramRepository, FitnessProgramCategoryAttributeRepository fitnessProgramCategoryAttributeRepository) {
        this.clientRepository = clientRepository;
        this.modelMapper = modelMapper;
        this.imageService = imageService;
        this.passwordEncoder = passwordEncoder;
        this.fitnessProgramRepository = fitnessProgramRepository;
        this.fitnessProgramCategoryAttributeRepository = fitnessProgramCategoryAttributeRepository;
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
}
