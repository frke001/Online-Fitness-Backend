package org.unibl.etf.fitness.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibl.etf.fitness.models.entities.AdvisorQuestionEntity;

@Repository
public interface AdvisorQuestionRepository extends JpaRepository<AdvisorQuestionEntity, Long> {
}
