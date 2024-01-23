package org.unibl.etf.fitness.services.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.fitness.models.entities.LogEntity;
import org.unibl.etf.fitness.models.enums.LogLevel;
import org.unibl.etf.fitness.repositories.LogRepository;
import org.unibl.etf.fitness.services.LogService;

import java.util.Date;

@Service
@Transactional
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public LogServiceImpl(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Override
    public void info(String message) {
        LogEntity logEntity = log(message);
        logEntity.setLevel(LogLevel.INFO.getLevel());
        logEntity = logRepository.saveAndFlush(logEntity);
        entityManager.refresh(logEntity);
    }

    @Override
    public void warning(String message) {
        LogEntity logEntity = log(message);
        logEntity.setLevel(LogLevel.WARNING.getLevel());
        logEntity = logRepository.saveAndFlush(logEntity);
        entityManager.refresh(logEntity);
    }

    @Override
    public void error(String message) {
        LogEntity logEntity = log(message);
        logEntity.setLevel(LogLevel.ERROR.getLevel());
        logEntity = logRepository.saveAndFlush(logEntity);
        entityManager.refresh(logEntity);
    }

    private LogEntity log(String message){
        LogEntity logEntity = new LogEntity();
        logEntity.setId(null);
        logEntity.setMessage(message);
        logEntity.setCreationDate(new Date());
        return logEntity;
    }

}
