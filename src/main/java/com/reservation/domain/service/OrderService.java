package com.reservation.domain.service;

import com.reservation.domain.model.reservation.ReservationNumber;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="car-rental-order", url="localhost:8086")
public interface OrderService {

	// TODO: Add all reservation fields here
	class OrderDetailsDto {

		private ReservationNumber reservationNumber;

		protected OrderDetailsDto() {
			super();
			this.reservationNumber = ReservationNumber.of("");
		}

		public OrderDetailsDto(ReservationNumber reservationNumber) {
			super();
			this.reservationNumber = reservationNumber;
		}

		public ReservationNumber getReservationNumber() {
			return reservationNumber;
		}

		@Override
		public String toString() {
			return reservationNumber.toString();
		}

	}

	@PostMapping("/orders")
	String submit(@RequestHeader("Authorization") String authorization,
				  @RequestBody OrderDetailsDto credentials);

}