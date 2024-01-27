package org.unibl.etf.fitness.models.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.unibl.etf.fitness.models.dto.FilterDTO;
import org.unibl.etf.fitness.models.entities.FitnessProgramEntity;
import org.unibl.etf.fitness.models.enums.DifficultyLevel;
import org.unibl.etf.fitness.models.enums.Location;

import java.util.ArrayList;
import java.util.List;

public class FitnessSpecification {

    public static Specification<FitnessProgramEntity> filters(List<FilterDTO> filtersList){
        return new Specification<FitnessProgramEntity>() {
            @Override
            public Predicate toPredicate(Root<FitnessProgramEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                filtersList.forEach(filter ->{
                    Predicate predicate;

                    if(filter.getColumnName().equals("name")){
                        predicate = criteriaBuilder.like(root.get("name"), "%" + filter.getColumnValue() + "%");
                    }else if(filter.getColumnName().equals("price")){
                        predicate = criteriaBuilder.equal(root.get("price"), filter.getColumnValue());
                    }else if(filter.getColumnName().equals("days")){
                        predicate = criteriaBuilder.equal(root.get("days"), filter.getColumnValue());
                    }
                    else if(filter.getColumnName().equals("category")) {
                        predicate = criteriaBuilder.equal(root.get("category").get("name"), filter.getColumnValue());
                    }else if(filter.getColumnName().equals("location")){
                        predicate = criteriaBuilder.equal(root.get("location"), Location.getByLocation((String)filter.getColumnValue()));
                    }else if(filter.getColumnName().equals("difficultyLevel")){
                        predicate = criteriaBuilder.equal(root.get("difficultyLevel"), DifficultyLevel.getByLevel((String)filter.getColumnValue()));
                    }else{
                        predicate = criteriaBuilder.equal(root.get(filter.getColumnName()), filter.getColumnValue());
                    }
                    predicates.add(predicate);
                });
                predicates.add(criteriaBuilder.equal(root.get("deleted"), false));
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
}
