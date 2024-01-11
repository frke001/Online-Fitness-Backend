package org.unibl.etf.fitness.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.fitness.exceptions.NotApprovedException;
import org.unibl.etf.fitness.models.dto.FitnessProgramDTO;
import org.unibl.etf.fitness.models.dto.ResponseCategoryAttributeValueDTO;
import org.unibl.etf.fitness.models.entities.FitnessProgramEntity;
import org.unibl.etf.fitness.repositories.CategoryAttributeRepository;
import org.unibl.etf.fitness.repositories.FitnessProgramCategoryAttributeRepository;
import org.unibl.etf.fitness.repositories.FitnessProgramRepository;
import org.unibl.etf.fitness.services.FitnessProgramService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FitnessProgramServiceImpl implements FitnessProgramService {

    private final FitnessProgramRepository fitnessProgramRepository;
    private final FitnessProgramCategoryAttributeRepository fitnessProgramCategoryAttributeRepository;
    private final CategoryAttributeRepository categoryAttributeRepository;
    private final ModelMapper modelMapper;

    public FitnessProgramServiceImpl(FitnessProgramRepository fitnessProgramRepository, ModelMapper modelMapper, FitnessProgramCategoryAttributeRepository fitnessProgramCategoryAttributeRepository, CategoryAttributeRepository categoryAttributeRepository) {
        this.fitnessProgramRepository = fitnessProgramRepository;
        this.modelMapper = modelMapper;
        this.fitnessProgramCategoryAttributeRepository = fitnessProgramCategoryAttributeRepository;
        this.categoryAttributeRepository = categoryAttributeRepository;
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

        return result;
    }
}
