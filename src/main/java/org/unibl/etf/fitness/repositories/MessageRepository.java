package org.unibl.etf.fitness.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibl.etf.fitness.models.dto.MessageDTO;
import org.unibl.etf.fitness.models.entities.MessageEntity;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    List<MessageEntity> findAllByClientReceiverId(Long id);
    List<MessageEntity> findAllByClientSenderId(Long id);
}
