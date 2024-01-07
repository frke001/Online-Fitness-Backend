package org.unibl.etf.fitness.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibl.etf.fitness.models.entities.ClientEntity;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity,Long> {
    Optional<ClientEntity> findByUsername(String username);

    Optional<ClientEntity> findByMail(String mail);
    boolean existsByUsername(String username);
    boolean existsByMail(String email);

}
