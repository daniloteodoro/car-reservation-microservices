package com.carrental.domain.service;

import com.carrental.domain.model.car.Model;
import com.carrental.domain.model.reservation.City;

import java.time.LocalDateTime;
import java.util.List;

public interface ModelService {

    List<Model> availableOn(City pickupLocation, LocalDateTime pickupDateTime, City dropOffLocation, LocalDateTime dropOffDateTime);

}
