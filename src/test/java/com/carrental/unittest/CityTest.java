package com.carrental.unittest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.carrental.domain.model.reservation.City;
import com.carrental.domain.model.reservation.Country;
import com.carrental.domain.model.reservation.exceptions.CityFormatException;
import com.carrental.domain.model.reservation.exceptions.CityNotFoundException;

public class CityTest {
	
	@Test
	public void testValidCityAndCountry() throws CityNotFoundException {
		String validFormat = "rotterdam-nl";
		City validCity = City.parse(validFormat);
		assertThat(validCity.toString(), is(equalTo("rotterdam")));
		assertThat(validCity.getCountry(), is(equalTo(Country.NL)));
	}
	
	@Test(expected=CityNotFoundException.class)
	public void testValidCityWithInvalidCountry() throws CityNotFoundException {
		String validFormat = "rotterdam-ZZ";
		City.parse(validFormat);
	}
	
	@Test(expected=CityFormatException.class)
	public void testValidCityWithoutCountry() throws CityNotFoundException {
		String validFormat = "rotterdam-";
		City.parse(validFormat);
	}
	
	@Test(expected=CityFormatException.class)
	public void testNonValidInput() throws CityNotFoundException {
		String validFormat = "rotterdam";
		City.parse(validFormat);
	}
	
	// TODO: Cover the rest of the tests
	
}
