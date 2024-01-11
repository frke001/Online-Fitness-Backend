package org.unibl.etf.fitness.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.fitness.models.dto.FitnessProgramDTO;
import org.unibl.etf.fitness.services.FitnessProgramService;



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
}
