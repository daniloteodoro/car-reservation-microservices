package com.reservation.domain.model.reservation;

import com.reservation.domain.model.car.ExtraProduct;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ExtraProductRepository extends CrudRepository<ExtraProduct, Long> {

	Optional<ExtraProduct> findByName(String name);
	
}
