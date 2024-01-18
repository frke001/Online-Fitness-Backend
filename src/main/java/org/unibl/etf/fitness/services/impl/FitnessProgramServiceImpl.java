package org.unibl.etf.fitness.services.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.unibl.etf.fitness.exceptions.NotApprovedException;
import org.unibl.etf.fitness.exceptions.NotFoundException;
import org.unibl.etf.fitness.exceptions.UnauthorizedException;
import org.unibl.etf.fitness.models.dto.*;
import org.unibl.etf.fitness.models.entities.FitnessProgramEntity;
import org.unibl.etf.fitness.models.entities.QuestionEntity;
import org.unibl.etf.fitness.models.specification.FitnessSpecification;
import org.unibl.etf.fitness.repositories.*;
import org.unibl.etf.fitness.services.FitnessProgramService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FitnessProgramServiceImpl implements FitnessProgramService {

    private final FitnessProgramRepository fitnessProgramRepository;
    private final FitnessProgramCategoryAttributeRepository fitnessProgramCategoryAttributeRepository;
    private final CategoryAttributeRepository categoryAttributeRepository;
    private final QuestionRepository questionRepository;

    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;
    @PersistenceContext
    private EntityManager entityManager;

    public FitnessProgramServiceImpl(FitnessProgramRepository fitnessProgramRepository, ModelMapper modelMapper, FitnessProgramCategoryAttributeRepository fitnessProgramCategoryAttributeRepository, CategoryAttributeRepository categoryAttributeRepository, QuestionRepository questionRepository, ClientRepository clientRepository) {
        this.fitnessProgramRepository = fitnessProgramRepository;
        this.modelMapper = modelMapper;
        this.fitnessProgramCategoryAttributeRepository = fitnessProgramCategoryAttributeRepository;
        this.categoryAttributeRepository = categoryAttributeRepository;
        this.questionRepository = questionRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public FitnessProgramDTO getFitnessProgram(Long id) {
        FitnessProgramEntity fitnessProgram = fitnessProgramRepository.findById(id).orElseThrow(NotApprovedException::new);

        FitnessProgramDTO result = modelMapper.map(fitnessProgram, FitnessProgramDTO.class);

        List<ResponseCategoryAttributeValueDTO> attributes = fitnessProgram.getCategoryAttributeValues().stream()
                .map(categoryAttribute -> {
                    ResponseCategoryAttributeValueDTO attrValue = new ResponseCategoryAttributeValueDTO();
                    attrValue.setId(categoryAttribute.getId());
                    attrValue.setName(categoryAttribute.getCategoryAttribute().getName());
                    attrValue.setValue(categoryAttribute.getValue());
                    return attrValue;
                })
                .collect(Collectors.toList());

        result.setCategoryAttributeValues(attributes);

        var questions = fitnessProgram.getQuestions().stream().map(el->modelMapper.map(el,ResponseQuestionDTO.class)).collect(Collectors.toList());
        result.setQuestions(questions);

        return result;
    }

    @Override
    public Page<CardFitnessProgramDTO> findAllFitnessPrograms(Pageable pageable, List<FilterDTO> filters) {

        Specification<FitnessProgramEntity> specification = FitnessSpecification.filters(filters);
        return fitnessProgramRepository.findAll(specification, pageable).map(el-> modelMapper.map(el,CardFitnessProgramDTO.class));
    }

    @Override
    public ResponseQuestionDTO askQuestion(Long id, RequestQuestionDTO request, Authentication auth) {
        var user = clientRepository.findById(request.getClientSenderId()).orElseThrow(NotFoundException::new);
        var jwtUser =(JwtUserDTO)auth.getPrincipal();
        if(!jwtUser.getId().equals(user.getId()))
            throw new UnauthorizedException();
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setId(null);
        questionEntity.setQuestion(request.getQuestion());
        questionEntity.setClientSender(user);
        FitnessProgramEntity fitnessProgramEntity = new FitnessProgramEntity();
        fitnessProgramEntity.setId(id);
        questionEntity.setFitnessProgram(fitnessProgramEntity);
        questionEntity = questionRepository.saveAndFlush(questionEntity);
        entityManager.refresh(questionEntity);
        return modelMapper.map(questionEntity,ResponseQuestionDTO.class);
    }

    @Override
    public List<QuestionFitnessProgramDTO> getAll(IdRequestDTO request, Authentication auth) {
        var user = clientRepository.findById(request.getId()).orElseThrow(NotFoundException::new);
        var jwtUser =(JwtUserDTO)auth.getPrincipal();
        if(!jwtUser.getId().equals(user.getId()))
            throw new UnauthorizedException();
        return fitnessProgramRepository.findAll().stream()
                .map(el->modelMapper.map(el, QuestionFitnessProgramDTO.class)).collect(Collectors.toList());
    }
}
