package org.unibl.etf.fitness.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibl.etf.fitness.models.entities.ValidationTokenEntity;

import java.util.Optional;

@Repository
public interface ValidationTokenRepository extends JpaRepository<ValidationTokenEntity,Long> {

    Optional<ValidationTokenEntity> findByToken(String token);
    Optional<ValidationTokenEntity> findByClientId(Long id);
}
