package com.carrental.infrastructure.persistence;

import com.carrental.domain.model.car.Category;
import com.carrental.domain.model.car.CategoryAvailability;
import com.carrental.domain.model.car.CategoryRepository;
import com.carrental.domain.model.car.CategoryType;
import com.carrental.domain.model.reservation.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

	@Override
	public List<CategoryAvailability> getCategoryAvailability(City pickupLocation, LocalDateTime pickupDateTime, City dropOffLocation, LocalDateTime dropOffDateTime) {

		List<CategoryAvailability> result = new ArrayList<>();

		// Query query = entityManager.createNativeQuery(Category.GET_AVAILABLE_CATEGORY_BASED_ON_RESERVATION, Tuple.class);
        Query query = entityManager.createNativeQuery(CategoryAvailability.GET_AVAILABLE_CATEGORY_BASED_ON_RESERVATION, CategoryAvailability.CATEGORY_AVAILABILITY_MAPPING);
		query.setParameter("END_DATE", dropOffDateTime);
		query.setParameter("START_DATE", pickupDateTime);

		return query.getResultList();
	}
	
}
