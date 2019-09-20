package com.reservation.infrastructure.services;

import com.reservation.domain.model.car.Category;
import com.reservation.domain.model.car.CategoryRepository;
import com.reservation.domain.model.car.Model;
import com.reservation.domain.model.car.ModelRepository;
import com.reservation.domain.model.reservation.City;
import com.reservation.domain.service.ModelService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ModelServiceImpl implements ModelService {

    private final CategoryRepository categoryRepository;
    private final ModelRepository modelRepository;

    public ModelServiceImpl(CategoryRepository categoryRepository, ModelRepository modelRepository) {
        this.categoryRepository = categoryRepository;
        this.modelRepository = modelRepository;
    }

    @Override
    public List<Model> modelsAvailableOn(City pickupLocation, LocalDateTime pickupDateTime, City dropOffLocation, LocalDateTime dropOffDateTime) {
        List<Model> availableModels = new ArrayList<>();

        List<Category> availableCategories =
                categoryRepository.getCategoryAvailabilities(pickupLocation, pickupDateTime, dropOffLocation, dropOffDateTime);

        for (Category current : availableCategories) {
            if (current.isAvailable()) {
                modelRepository.findModelsByCategoryType(current.getType())
                        .stream()
                        .findFirst()
                        .ifPresent(availableModels::add);
            }
        }

        return availableModels;
    }

}
