package org.unibl.etf.fitness.services;


import org.unibl.etf.fitness.models.dto.CategoriesDTO;

import java.util.List;

public interface CategoryService {

    List<CategoriesDTO> getAllCategories();
}
