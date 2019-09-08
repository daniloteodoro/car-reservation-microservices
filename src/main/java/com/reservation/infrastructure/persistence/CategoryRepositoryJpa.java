package com.reservation.infrastructure.persistence;

import com.reservation.domain.model.car.*;
import com.reservation.domain.model.reservation.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
	public List<CategoryAvailability> getCategoryAvailabilities(City pickupLocation, LocalDateTime pickupDateTime, City dropOffLocation, LocalDateTime dropOffDateTime) {

		List<CategoryWithReservationInfo> categoriesFromDb = new ArrayList<>();

		// Query query = entityManager.createNativeQuery(Category.GET_AVAILABLE_CATEGORY_BASED_ON_RESERVATION, Tuple.class);
        Query query = entityManager.createNativeQuery(CategoryWithReservationInfo.GET_AVAILABLE_CATEGORY_BASED_ON_RESERVATION, CategoryWithReservationInfo.CATEGORY_AVAILABILITY_MAPPING);
		query.setParameter("END_DATE", dropOffDateTime);
		query.setParameter("START_DATE", pickupDateTime);

		categoriesFromDb = query.getResultList();

		return categoriesFromDb.stream()
				.map(original -> new CategoryAvailability(original, pickupLocation, dropOffLocation, pickupDateTime, dropOffDateTime))
				.collect(Collectors.toList());
	}

	@Override
	public Optional<CategoryAvailability> getCategoryAvailabilityFor(CategoryType type, City pickupLocation, LocalDateTime pickupDateTime, City dropOffLocation, LocalDateTime dropOffDateTime) {
		List<CategoryAvailability> categories = getCategoryAvailabilities(pickupLocation, pickupDateTime, dropOffLocation, dropOffDateTime);
		return categories.stream()
				.filter(cat -> cat.getType() == type)
				.findFirst();
	}

}
