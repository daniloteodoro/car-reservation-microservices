package com.reservation.domain.model.car;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ModelRepository extends JpaRepository<Model, Integer> {

	@Query("from Model m where m.categoryPricing.type = :type")
	List<Model> findModelsByCategoryType(CategoryType type);

}
