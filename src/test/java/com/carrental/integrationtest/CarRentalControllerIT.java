package com.carrental.integrationtest;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.carrental.App;
import com.carrental.application.dto.CarDto;
import com.carrental.domain.model.car.Brand;
import com.carrental.domain.model.car.Car;
import com.carrental.domain.model.car.Category;
import com.carrental.domain.model.car.LicensePlate;
import com.carrental.domain.model.car.Model;
import com.carrental.domain.model.reservation.City;
import com.carrental.domain.model.reservation.Country;
import com.carrental.unittest.util.GsonLocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.MOCK, classes=App.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@ActiveProfiles("test")
public class CarRentalControllerIT {
	
	// Simulates a servlet to handle API calls
	@Autowired
    private MockMvc mvc;
	
	
	@Test
	public void givenTheCityOfRotterdam_whenSearchingForCars_thenOneCarIsReturned() throws Exception {
		
		// http://localhost:8081/search/from/rotterdam-nl/2018-07-16 08:00/to/rotterdam-nl/2018-07-20 16:00
		
	    mvc.perform(get("/search/from/rotterdam-nl/2018-07-16 08:00/to/rotterdam-nl/2018-07-20 16:00")
	      .contentType(MediaType.APPLICATION_JSON))
	      .andExpect(status().isOk())
	      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		      .andExpect(jsonPath("$[0].car.licensePlate.data", is("AB-1234")));
	}
	
	@Test
	public void givenASearchResult_whenChoosingVWGolf_thenANewReservationIsReturned() throws Exception {
		City rotterdam = new City(1, "rotterdam", Country.NL);
		LicensePlate plate = new LicensePlate("AB-1234");
		Car golf = new Car.Builder()
					.withModel(new Model(new Brand("VW"), "Golf", Category.MEDIUMSIZED))
					.withPickupLocation(rotterdam)
					.withPickupDateTime(LocalDateTime.of(2018, 8, 1, 10, 00))
					.withDropoffLocation(rotterdam)
					.withDropoffDateTime(LocalDateTime.of(2018, 8, 31, 10, 00))
					.withPricePerDay(40.0)
					.withStandardInsurance(0.0)
					.withFullInsuranceInsurance(18.0)
					.withLicensePlate(plate)
					.build();
		
		Gson gson = new GsonBuilder().setPrettyPrinting()
									 .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
									 .create();
		String payload = gson.toJson(CarDto.basedOn(golf));
		
	    mvc.perform(post("/choose/AB-1234")
	      .contentType(MediaType.APPLICATION_JSON).content(payload))
	      .andExpect(status().isCreated())
	      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	      .andExpect(jsonPath("$.reservationNumber.data").isNotEmpty());
	}

}









