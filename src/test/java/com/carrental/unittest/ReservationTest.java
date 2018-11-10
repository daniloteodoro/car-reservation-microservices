package com.carrental.unittest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.carrental.domain.model.car.ExtraProduct;
import com.carrental.domain.model.car.InsuranceType;
import com.carrental.domain.model.car.LicensePlate;
import com.carrental.domain.model.customer.Visitor;
import com.carrental.domain.model.reservation.City;
import com.carrental.domain.model.reservation.Reservation;
import com.carrental.domain.model.reservation.exceptions.CarUnavailableException;
import com.carrental.domain.model.reservation.exceptions.ReservationException;
import com.carrental.shared.SampleCategories;

@RunWith(MockitoJUnitRunner.class)
public class ReservationTest {

	@Mock
	private City someCity;
	
	@Mock
	private LicensePlate someLicensePlate;
	
	
	@Test(expected=CarUnavailableException.class)
	public void testBookingUnavailableCar() throws CarUnavailableException {
		LocalDateTime start = LocalDateTime.of(2018, 8, 10, 10, 00);
		LocalDateTime finish = LocalDateTime.of(2018, 9, 2, 10, 00);
		
		// Date of the reservation does not fit into the period the car will be available.
		new Reservation(new Visitor(), SampleCategories.MEDIUMSIZED_CATEGORY, someCity, start, someCity, finish);
	}
	
	@Test
	public void testTotalFor5DaysReservation() throws CarUnavailableException {
		LocalDateTime start = LocalDateTime.of(2018, 8, 10, 10, 00);
		LocalDateTime finish = LocalDateTime.of(2018, 8, 15, 10, 00);
		
		Reservation reservation = new Reservation(new Visitor(), SampleCategories.MEDIUMSIZED_CATEGORY, someCity, start, someCity, finish);
		
		assertThat(reservation.calculateTotal(), is(equalTo(200.0)));
	}
	
	@Test
	public void testTotalFor5DaysReservationWithAdditionalDriver() throws ReservationException {
		LocalDateTime start = LocalDateTime.of(2018, 8, 10, 10, 00);
		LocalDateTime finish = LocalDateTime.of(2018, 8, 15, 10, 00);

		Reservation reservation = new Reservation(new Visitor(), SampleCategories.MEDIUMSIZED_CATEGORY, someCity, start, someCity, finish);
		reservation.addExtraProduct(ExtraProduct.ADDITIONAL_DRIVER);

		assertThat(reservation.calculateTotal(), is(equalTo(260.0)));
	}
	
	@Test
	public void testTotalFor5DaysReservationWithAdditionalDriverAndFullInsurance() throws ReservationException {
		LocalDateTime start = LocalDateTime.of(2018, 8, 10, 10, 00);
		LocalDateTime finish = LocalDateTime.of(2018, 8, 15, 10, 00);
		
		Reservation reservation = new Reservation(new Visitor(), SampleCategories.MEDIUMSIZED_CATEGORY, someCity, start, someCity, finish);
		reservation.addExtraProduct(ExtraProduct.ADDITIONAL_DRIVER);
		reservation.setInsurance(InsuranceType.FULL_INSURANCE);
		
		assertThat(reservation.calculateTotal(), is(equalTo(350.0)));
	}
	
	@Test
	public void testReservationConstructor() throws CarUnavailableException {
		try {
			new Reservation(null, null, null, null, null, null);
			Assert.fail("NullPointerException was expected");
		} catch (NullPointerException e) {
			assertThat("Incorrect message", e.getMessage().length(), is(greaterThan(0)));
		}
	}

	@Test
	public void testForEquality() throws CarUnavailableException {
		LocalDateTime start = LocalDateTime.of(2018, 8, 10, 10, 00);
		LocalDateTime finish = LocalDateTime.of(2018, 8, 15, 10, 00);
		
		Reservation r1 = new Reservation(new Visitor(), SampleCategories.MEDIUMSIZED_CATEGORY, someCity, start, someCity, finish);
		Reservation r2 = new Reservation(new Visitor(), SampleCategories.MEDIUMSIZED_CATEGORY, someCity, start, someCity, finish);
		
		assertThat(r1, is(equalTo(r1)));
		assertThat(r1, is(not(equalTo(r2))));
	}

}











