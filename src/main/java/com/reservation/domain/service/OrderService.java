package com.reservation.domain.service;

import com.reservation.domain.model.reservation.OrderId;
import com.reservation.domain.service.dto.CarReservationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="car-rental-order", url="localhost:8086")
public interface OrderService {

	@PostMapping("/orders")
	OrderId submit(@RequestHeader("Authorization") String authorization,
				   @RequestBody CarReservationDto reservation);

}
