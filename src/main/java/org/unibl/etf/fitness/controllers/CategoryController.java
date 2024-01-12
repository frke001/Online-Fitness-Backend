package org.unibl.etf.fitness.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.fitness.models.dto.CategoriesDTO;
import org.unibl.etf.fitness.models.dto.CategoryNameDTO;
import org.unibl.etf.fitness.services.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<CategoriesDTO> getAll(){
        return categoryService.getAllCategories();
    }

    @GetMapping("/names")
    public List<CategoryNameDTO> getAllNames(){
        return categoryService.getAllCategoryNames();
    }
}
