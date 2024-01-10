package org.unibl.etf.fitness.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibl.etf.fitness.models.entities.FitnessProgramEntity;

@Repository
public interface FitnessProgramRepository extends JpaRepository<FitnessProgramEntity, Long> {
}
