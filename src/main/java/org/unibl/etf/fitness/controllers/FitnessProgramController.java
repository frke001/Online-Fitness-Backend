package org.unibl.etf.fitness.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.fitness.models.dto.CardFitnessProgramDTO;
import org.unibl.etf.fitness.models.dto.FilterDTO;
import org.unibl.etf.fitness.models.dto.FitnessProgramDTO;
import org.unibl.etf.fitness.services.FitnessProgramService;

import java.util.List;


@RestController
@RequestMapping("api/v1/fitness-programs")
public class FitnessProgramController {

    private final FitnessProgramService fitnessProgramService;

    public FitnessProgramController(FitnessProgramService fitnessProgramService) {
        this.fitnessProgramService = fitnessProgramService;
    }
    @GetMapping("/{id}")
    public FitnessProgramDTO getFitnessProgram(@PathVariable Long id){
        return this.fitnessProgramService.getFitnessProgram(id);
    }

    @PostMapping()
    public Page<CardFitnessProgramDTO> getAllFitnessPrograms(Pageable pageable, @RequestBody List<FilterDTO> filters){
        return fitnessProgramService.findAllFitnessPrograms(pageable,filters);
    }
}
