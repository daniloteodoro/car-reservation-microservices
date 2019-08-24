package com.carrental.infrastructure;

import com.carrental.domain.model.car.CategoryAvailability;
import com.carrental.domain.model.car.CategoryRepository;
import com.carrental.domain.model.car.Model;
import com.carrental.domain.model.car.ModelRepository;
import com.carrental.domain.model.reservation.City;
import com.carrental.domain.service.ModelService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ModelServiceJpa implements ModelService {

    private final CategoryRepository categoryRepository;
    private final ModelRepository modelRepository;

    public ModelServiceJpa(CategoryRepository categoryRepository, ModelRepository modelRepository) {
        this.categoryRepository = categoryRepository;
        this.modelRepository = modelRepository;
    }

    @Override
    public List<Model> availableOn(City pickupLocation, LocalDateTime pickupDateTime, City dropOffLocation, LocalDateTime dropOffDateTime) {
        List<Model> availableModels = new ArrayList<>();

        List<CategoryAvailability> availableCategories =
                categoryRepository.getCategoryAvailability(pickupLocation, pickupDateTime, dropOffLocation, dropOffDateTime);

        for (CategoryAvailability current : availableCategories) {
            if (current.isAvailable()) {
                modelRepository.findFirstModelByCategory(current.getCategory())
                        .ifPresent(availableModels::add);
            }
        }

        return availableModels;
    }

}
