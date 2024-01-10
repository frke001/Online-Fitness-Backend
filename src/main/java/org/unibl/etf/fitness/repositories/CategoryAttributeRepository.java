package org.unibl.etf.fitness.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibl.etf.fitness.models.entities.CategoryAttributeEntity;

import java.util.List;

@Repository
public interface CategoryAttributeRepository extends JpaRepository<CategoryAttributeEntity, Long> {

    List<CategoryAttributeEntity> getAllByCategoryIdAndDeleted(Long id, Boolean deleted);
}
