package org.unibl.etf.fitness.services;

import org.unibl.etf.fitness.models.entities.ClientEntity;
import org.unibl.etf.fitness.models.entities.ValidationTokenEntity;

public interface ValidationTokenService {
    String generateToken();
    ValidationTokenEntity generateTokenForUser(ClientEntity client);
}
