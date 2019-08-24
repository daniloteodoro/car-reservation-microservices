package com.reservation.unittest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.reservation.domain.model.reservation.City;
import com.reservation.domain.model.reservation.Country;
import com.reservation.domain.model.reservation.exceptions.CityFormatException;
import com.reservation.domain.model.reservation.exceptions.InvalidCityException;

public class CityTest {
	
	@Test
	public void testValidCityAndCountry() throws InvalidCityException {
		String validFormat = "rotterdam-nl";
		City validCity = City.parse(validFormat);
		assertThat(validCity.toString(), is(equalTo("rotterdam")));
		assertThat(validCity.getCountry(), is(equalTo(Country.NL)));
	}
	
	@Test(expected= InvalidCityException.class)
	public void testValidCityWithInvalidCountry() throws InvalidCityException {
		String validFormat = "rotterdam-ZZ";
		City.parse(validFormat);
	}
	
	@Test(expected=CityFormatException.class)
	public void testValidCityWithoutCountry() throws InvalidCityException {
		String validFormat = "rotterdam-";
		City.parse(validFormat);
	}
	
	@Test(expected=CityFormatException.class)
	public void testNonValidInput() throws InvalidCityException {
		String validFormat = "rotterdam";
		City.parse(validFormat);
	}
	
	// TODO: Cover the rest of the tests
	
}
