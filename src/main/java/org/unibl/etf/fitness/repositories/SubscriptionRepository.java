package org.unibl.etf.fitness.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibl.etf.fitness.models.entities.SubscriptionEntity;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {

    void deleteByCategoryIdAndClientId(Long categoryId, Long clientId);
    boolean existsByClientIdAndCategoryId(Long clientId, Long categoryId);
}
