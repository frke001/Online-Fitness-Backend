package org.unibl.etf.fitness.services.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.unibl.etf.fitness.exceptions.AccountBlockedException;
import org.unibl.etf.fitness.exceptions.NotApprovedException;
import org.unibl.etf.fitness.exceptions.UnauthorizedException;
import org.unibl.etf.fitness.models.dto.*;
import org.unibl.etf.fitness.models.entities.ClientEntity;
import org.unibl.etf.fitness.models.entities.ImageEntity;
import org.unibl.etf.fitness.models.entities.ValidationTokenEntity;
import org.unibl.etf.fitness.models.enums.Role;
import org.unibl.etf.fitness.repositories.ClientRepository;
import org.unibl.etf.fitness.repositories.ValidationTokenRepository;
import org.unibl.etf.fitness.services.*;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {
    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final ValidationTokenService validationTokenService;
    private final EmailService emailService;

    private final ValidationTokenRepository validationTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final LogService logService;
    private final HttpServletRequest request;

    @PersistenceContext
    private EntityManager entityManager;

    public AuthServiceImpl(ClientRepository clientRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, ValidationTokenService validationTokenService, EmailService emailService, ValidationTokenRepository validationTokenRepository, AuthenticationManager authenticationManager, JwtService jwtService, LogService logService, HttpServletRequest request) {
        this.clientRepository = clientRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.validationTokenService = validationTokenService;
        this.emailService = emailService;
        this.validationTokenRepository = validationTokenRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.logService = logService;
        this.request = request;
    }

    @Override
    public void registerClient(RequestRegisterClientDTO request) {
        ClientEntity entity = modelMapper.map(request, ClientEntity.class);
        entity.setPassword(passwordEncoder.encode(request.getPassword()));
        entity.setAccountStatus(false);
        entity.setDeleted(false);
        entity.setRole(Role.REGISTERED_CLIENT);
        if(request.getProfileImageId() != null) {
            ImageEntity imageEntity = new ImageEntity();
            imageEntity.setId(request.getProfileImageId());
            entity.setProfileImage(imageEntity);
        }
        entity = clientRepository.saveAndFlush(entity);
        entityManager.refresh(entity);
        ValidationTokenEntity validationToken = validationTokenService.generateTokenForUser(entity);
        emailService.sendVerificationEmail(validationToken.getToken(), entity.getMail());
        logService.info("New account registered. User: " + request.getUsername() + ".");
    }

    @Override
    public boolean activateAccount(ValidationDTO validationDTO) {
        var token = validationTokenRepository.findByToken(validationDTO.getToken());
        if(token.isEmpty())
            return false;
        ClientEntity clientEntity = token.get().getClient();
        clientEntity.setAccountStatus(true);
        validationTokenRepository.delete(token.get());
        logService.info("New account activated. User: " + clientEntity.getUsername() + ".");
        return true;
    }

    @Override
    public void resendActivation(EmailDTO emailDTO) {
       ClientEntity clientEntity = clientRepository.findByUsername(emailDTO.getUsername()).get();
       ValidationTokenEntity validationTokenEntity = validationTokenRepository.findByClientId(clientEntity.getId()).get();
       emailService.sendVerificationEmail(validationTokenEntity.getToken(), clientEntity.getMail());
       logService.info("Verification mail resent. To: " + clientEntity.getMail() + ".");
    }

    @Override
    public boolean checkDetails(DetailsRequestDTO detailsRequestDTO) {
        if(detailsRequestDTO.getMail() != null){
            return clientRepository.existsByMail(detailsRequestDTO.getMail());
        }else if(detailsRequestDTO.getUsername() != null){
            return clientRepository.existsByUsername(detailsRequestDTO.getUsername());
        }
        //logService.info("User details checked. Address: " + request.getRemoteAddr() + ".");
        return false;
    }

    @Override
    public ClientDTO login(RequestLoginDTO requestLoginDTO) {
        Authentication auth = null;
        try {
            auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestLoginDTO.getUsername(), requestLoginDTO.getPassword()));
        }catch (Exception ex){
            throw new UnauthorizedException();
        }
            JwtUserDTO user = (JwtUserDTO) auth.getPrincipal();
            ClientDTO response = null;
            ClientEntity clientEntity = null;
            if (user.getRole() == Role.REGISTERED_CLIENT) {
                clientEntity = clientRepository.findById(user.getId()).get();
                response = modelMapper.map(clientEntity, ClientDTO.class);
            } else {
                throw new UnauthorizedException();
            }
            if (!clientEntity.getAccountStatus()) {
                logService.info("Account not activated occurred. User: " + user.getUsername() + ".");
                throw new NotApprovedException();
            }
            if (clientEntity.getDeleted()) {
                logService.info("Account blocked occurred. User: " + user.getUsername() + ".");
                throw new AccountBlockedException();
            }

            var token = jwtService.generateToken(user);
            response.setToken(token);
            logService.info("Successful login. User: " + user.getUsername() + ".");
            return response;
    }
}
