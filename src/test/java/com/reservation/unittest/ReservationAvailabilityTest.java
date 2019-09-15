package com.reservation.unittest;

import static org.mockito.Mockito.mock;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.reservation.domain.model.car.Category;
import com.reservation.domain.model.customer.Customer;
import com.reservation.domain.model.reservation.City;
import com.reservation.domain.model.reservation.Reservation;

public class ReservationAvailabilityTest {
	
	
//	private Reservation generateReservation(LocalDateTime start, LocalDateTime finish) {
//		Customer someCustomer = mock(Customer.class);
//		Category someCategory = mock(Category.class);
//		City someCity = mock(City.class);
//		return new Reservation(someCustomer, someCategory, someCity, start, someCity, finish);
//	}
	
//	@Test
//	public void testFreeSpot() {
//		Reservation r1 = generateReservation(LocalDateTime.of(2018, 07, 1, 8, 00), LocalDateTime.of(2018, 7, 03, 16, 00));
//		Reservation r2 = generateReservation(LocalDateTime.of(2018, 07, 7, 8, 00), LocalDateTime.of(2018, 7, 14, 16, 00));
//		Reservation r3 = generateReservation(LocalDateTime.of(2018, 07, 15, 8, 00), LocalDateTime.of(2018, 7, 15, 18, 00));
//		Reservation r4 = generateReservation(LocalDateTime.of(2018, 07, 19, 8, 00), LocalDateTime.of(2018, 7, 21, 16, 00));
//		Reservation r5 = generateReservation(LocalDateTime.of(2018, 07, 29, 8, 00), LocalDateTime.of(2018, 8, 02, 16, 00));
//
//		ReservationAvailability ra = new ReservationAvailability(Arrays.asList(r1, r2, r3, r4, r5));
//
//		Assert.assertFalse(ra.isAvailable(LocalDateTime.of(2018, 07, 2, 8, 00), LocalDateTime.of(2018, 07, 4, 8, 00)));
//		Assert.assertTrue(ra.isAvailable(LocalDateTime.of(2018, 07, 4, 8, 00), LocalDateTime.of(2018, 07, 5, 8, 00)));
//		Assert.assertFalse(ra.isAvailable(LocalDateTime.of(2018, 07, 16, 8, 00), LocalDateTime.of(2018, 07, 20, 8, 00)));
//		Assert.assertFalse(ra.isAvailable(LocalDateTime.of(2018, 07, 29, 8, 00), LocalDateTime.of(2018, 8, 30, 8, 00)));
//	}

}
