package com.reservation.domain.service;

import com.reservation.domain.model.car.Model;
import com.reservation.domain.model.reservation.City;

import java.time.LocalDateTime;
import java.util.List;

public interface ModelService {

    List<Model> modelsAvailableOn(City pickupLocation, LocalDateTime pickupDateTime, City dropOffLocation, LocalDateTime dropOffDateTime);

}
