package com.reservation.domain.model.car;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.reservation.domain.model.shared.ValueObject;

// Design decision to have this object as a read-only list with some convenient	methods
public class AvailableCarList extends AbstractList<Car> implements ValueObject, List<Car> {
	
	private static final long serialVersionUID = 5645588000036085269L;
	
	private final List<Car> availableCars;
	
	
	public AvailableCarList(List<Car> availableCars) {
		super();
		this.availableCars = new ArrayList<>(availableCars);
	}

	@Override
	public Car get(int index) {
		return availableCars.get(index);
	}
	
	@Override
	public int size() {
		return availableCars.size();
	}
	
	public Optional<Car> findByModel(Model model) {
		Optional<Car> foundCar = availableCars.stream()
				 .filter((car) -> car.is(model))
				 .findFirst();
		return foundCar;
	}

}
