package org.unibl.etf.fitness.services.impl;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.unibl.etf.fitness.exceptions.NotFoundException;
import org.unibl.etf.fitness.exceptions.UnauthorizedException;
import org.unibl.etf.fitness.models.dto.*;
import org.unibl.etf.fitness.models.entities.ImageEntity;
import org.unibl.etf.fitness.repositories.ClientRepository;
import org.unibl.etf.fitness.services.ClientService;
import org.unibl.etf.fitness.services.ImageService;

import java.io.IOException;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;
    private final ImageService imageService;
    private final PasswordEncoder passwordEncoder;

    public ClientServiceImpl(ClientRepository clientRepository, ModelMapper modelMapper, ImageService imageService, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.modelMapper = modelMapper;
        this.imageService = imageService;
        this.passwordEncoder = passwordEncoder;
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
//        var jwtUser=(JwtUserDTO) auth.getPrincipal();
//        if(!jwtUser.getId().equals(id))
//            throw new UnauthorizedException();
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
}
