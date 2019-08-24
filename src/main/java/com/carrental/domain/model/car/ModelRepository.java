package com.carrental.domain.model.car;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModelRepository extends JpaRepository<Model, Integer> {

	Optional<Model> findFirstModelByCategory(Category category);

}
