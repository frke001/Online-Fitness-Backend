package org.unibl.etf.fitness.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.unibl.etf.fitness.models.dto.FitnessProgramDTO;
import org.unibl.etf.fitness.models.entities.FitnessProgramEntity;

import java.util.List;

@Repository
public interface FitnessProgramRepository extends JpaRepository<FitnessProgramEntity, Long>, JpaSpecificationExecutor<FitnessProgramEntity> {

    List<FitnessProgramEntity> getAllByClientIdAndDeleted(Long id, Boolean deleted);

}
