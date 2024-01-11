package org.unibl.etf.fitness.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibl.etf.fitness.models.entities.FitnessProgramCategoryAttributeEntity;

import java.util.List;

@Repository
public interface FitnessProgramCategoryAttributeRepository extends JpaRepository<FitnessProgramCategoryAttributeEntity, Long> {

    List<FitnessProgramCategoryAttributeEntity> getAllByFitnessProgramId(Long id);
}
