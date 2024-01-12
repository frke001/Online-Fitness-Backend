package org.unibl.etf.fitness.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibl.etf.fitness.models.entities.ParticipateEntity;

import java.util.Optional;

@Repository
public interface ParticipateRepository extends JpaRepository<ParticipateEntity, Long> {

    ParticipateEntity findByClientIdAndFitnessProgramId(Long clientId, Long programId);
    boolean existsByClientIdAndFitnessProgramId(Long clientId, Long programId);
}
