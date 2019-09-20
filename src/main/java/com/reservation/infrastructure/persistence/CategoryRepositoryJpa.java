package com.reservation.infrastructure.persistence;

import com.reservation.domain.model.car.*;
import com.reservation.domain.model.reservation.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
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
	public List<CategoryPricing> findAll() {
		TypedQuery<CategoryPricing> query = entityManager.createNamedQuery(CategoryPricing.GET_ALL_CATEGORIES, CategoryPricing.class);
		List<CategoryPricing> results = query.getResultList();
		return results;
	}
	
	@Override
	public Optional<CategoryPricing> findById(CategoryType categoryType) {
		TypedQuery<CategoryPricing> query = entityManager.createNamedQuery(CategoryPricing.GET_CATEGORY_BY_TYPE, CategoryPricing.class);
		query.setParameter("TYPE", categoryType);
		
		List<CategoryPricing> results = query.getResultList();
		
		if (results.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(results.get(0));
		}
	}

	@Override
	public List<Category> getCategoryAvailabilities(City pickupLocation, LocalDateTime pickupDateTime, City dropOffLocation, LocalDateTime dropOffDateTime) {

		List<CategoryWithReservationInfo> categoriesWithTotals;

        Query query = entityManager.createNativeQuery(CategoryWithReservationInfo.GET_AVAILABLE_CATEGORY_BASED_ON_RESERVATION, CategoryWithReservationInfo.CATEGORY_AVAILABILITY_MAPPING);
		query.setParameter("END_DATE", dropOffDateTime);
		query.setParameter("START_DATE", pickupDateTime);

		categoriesWithTotals = query.getResultList();

		return categoriesWithTotals.stream()
				.map(original -> new Category(original.getCategoryPricing().getType(), original.getCategoryPricing(), pickupLocation, dropOffLocation, pickupDateTime, dropOffDateTime, original.getTotal(), original.getTotalReserved()))
				.collect(Collectors.toList());
	}

	@Override
	public Optional<Category> getCategoryAvailabilityFor(CategoryType type, City pickupLocation, LocalDateTime pickupDateTime, City dropOffLocation, LocalDateTime dropOffDateTime) {
		if (pickupDateTime.isAfter(dropOffDateTime)) {
			throw new RuntimeException("Pick-up date/time should be earlier than the Drop-off date/time");
		}
		return getCategoryAvailabilities(pickupLocation, pickupDateTime, dropOffLocation, dropOffDateTime)
				.stream()
				.filter(cat -> cat.getPricingInformation().getType() == type)
				.findFirst();
	}

}
