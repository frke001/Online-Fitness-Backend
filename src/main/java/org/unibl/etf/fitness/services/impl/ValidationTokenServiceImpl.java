package org.unibl.etf.fitness.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.fitness.models.entities.ClientEntity;
import org.unibl.etf.fitness.models.entities.ValidationTokenEntity;
import org.unibl.etf.fitness.repositories.ValidationTokenRepository;
import org.unibl.etf.fitness.services.ValidationTokenService;

import java.util.UUID;

@Service
public class ValidationTokenServiceImpl implements ValidationTokenService {

    private final ValidationTokenRepository validationTokenRepository;

    public ValidationTokenServiceImpl(ValidationTokenRepository validationTokenRepository) {
        this.validationTokenRepository = validationTokenRepository;
    }

    @Override
    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public ValidationTokenEntity generateTokenForUser(ClientEntity client) {
        String token = this.generateToken();
        ValidationTokenEntity entity = new ValidationTokenEntity();
        entity.setToken(token);
        entity.setClient(client);
        this.validationTokenRepository.saveAndFlush(entity);
        return entity;
    }
}
