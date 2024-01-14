package org.unibl.etf.fitness.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.unibl.etf.fitness.models.dto.*;

import java.util.List;

public interface FitnessProgramService {

    FitnessProgramDTO getFitnessProgram(Long id);

    Page<CardFitnessProgramDTO> findAllFitnessPrograms(Pageable page, List<FilterDTO> filters);

    ResponseQuestionDTO askQuestion(Long id, RequestQuestionDTO request, Authentication auth);
}
