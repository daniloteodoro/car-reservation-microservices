package com.carrental.infrastructure.persistence;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.carrental.domain.model.car.Category;
import com.carrental.domain.model.car.CategoryRepository;
import com.carrental.domain.model.car.CategoryType;
import com.carrental.domain.model.car.Model;
import com.carrental.domain.model.reservation.City;

@Repository
public class CategoryRepositoryJpa implements CategoryRepository {
	
	private final EntityManager entityManager;
	
	
	@Autowired
	public CategoryRepositoryJpa(final EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}
	
	@Override
	public List<Category> findAll() {
		TypedQuery<Category> query = entityManager.createNamedQuery(Category.GET_ALL_CATEGORIES, Category.class);
		List<Category> results = query.getResultList();
		return results;
	}
	
	@Override
	public Optional<Category> findById(CategoryType categoryType) {
		TypedQuery<Category> query = entityManager.createNamedQuery(Category.GET_CATEGORY_BY_TYPE, Category.class);
		query.setParameter("TYPE", categoryType);
		
		List<Category> results = query.getResultList();
		
		if (results.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(results.get(0));
		}
	}
	
	private Optional<Model> getExampleModelByCategory(Category category) {
		TypedQuery<Model> query = entityManager.createNamedQuery(Model.GET_EXAMPLE_MODEL_BY_CATEGORY, Model.class);
		query.setParameter("CATEGORY", category);
		
		List<Model> results = query.setMaxResults(1).getResultList();
		
		if (results.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(results.get(0));
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Model> availableOn(City pickupLocation, LocalDateTime pickupDateTime, City dropoffLocation, LocalDateTime dropoffDateTime) {
		List<Model> availableModel = new ArrayList<>();
		
		Query query = entityManager.createNativeQuery(Category.GET_AVAILABLE_CATEGORY_BASED_ON_RESERVATION, Category.class);
		query.setParameter("END_DATE", dropoffDateTime);
		query.setParameter("START_DATE", pickupDateTime);
		
		List<Category> categories = query.getResultList();
		
		Optional<Model> foundModel;
		for (Category current : categories) {
			foundModel = getExampleModelByCategory(current);
			if (foundModel.isPresent()) {
				availableModel.add(foundModel.get());
			}
		}
		
		return availableModel;
	}
	
	
	
}













