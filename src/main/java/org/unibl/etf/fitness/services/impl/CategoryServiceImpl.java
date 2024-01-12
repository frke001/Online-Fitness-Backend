package org.unibl.etf.fitness.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.fitness.models.dto.CategoriesDTO;
import org.unibl.etf.fitness.models.dto.CategoryAttributeDTO;
import org.unibl.etf.fitness.models.dto.CategoryNameDTO;
import org.unibl.etf.fitness.repositories.CategoryAttributeRepository;
import org.unibl.etf.fitness.repositories.CategoryRepository;
import org.unibl.etf.fitness.services.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryAttributeRepository categoryAttributeRepository;
    private final ModelMapper modelMapper;


    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryAttributeRepository categoryAttributeRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryAttributeRepository = categoryAttributeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<CategoriesDTO> getAllCategories() {
        var categories = categoryRepository.getAllByDeleted(false)
                .stream()
                .map(category -> {
                    CategoriesDTO categoriesDTO = modelMapper.map(category, CategoriesDTO.class);
                    List<CategoryAttributeDTO> filteredAttributes = category.getAttributes().stream()
                            .filter(attr -> !attr.getDeleted())
                            .map(attr -> modelMapper.map(attr, CategoryAttributeDTO.class))
                            .collect(Collectors.toList());
                    categoriesDTO.setAttributes(filteredAttributes);
                    return categoriesDTO;
                })
                .collect(Collectors.toList());

        return categories;
    }

    @Override
    public List<CategoryNameDTO> getAllCategoryNames() {
        return categoryRepository.getAllByDeleted(false).stream().map((el)-> modelMapper.map(el,CategoryNameDTO.class)).collect(Collectors.toList());
    }
}
