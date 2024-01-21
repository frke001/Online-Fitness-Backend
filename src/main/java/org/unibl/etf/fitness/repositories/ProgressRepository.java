package org.unibl.etf.fitness.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibl.etf.fitness.models.entities.ProgressEntity;

import java.util.Date;
import java.util.List;

public interface ProgressRepository extends JpaRepository<ProgressEntity, Long> {

    List<ProgressEntity> getAllByClientId(Long id);
    List<ProgressEntity> getByClientIdAndDateBetween(Long id,Date startDate, Date endDate);
    boolean existsByDateAndClientId(Date date, Long id);
    ProgressEntity getByDate(Date date);
}
