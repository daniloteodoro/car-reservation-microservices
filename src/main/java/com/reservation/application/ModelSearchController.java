package com.reservation.application;

import com.reservation.application.dto.ModelDto;
import com.reservation.application.exceptions.SearchCarException;
import com.reservation.domain.model.reservation.City;
import com.reservation.domain.model.reservation.CityRepository;
import com.reservation.domain.model.reservation.exceptions.CityNotFoundException;
import com.reservation.domain.model.reservation.exceptions.ReservationException;
import com.reservation.domain.service.ModelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@Api(value = "/models", tags = "Model", description = "Search for car models available to rent.")
public class ModelSearchController {

	private final CityRepository cityRepository;
	private final ModelService modelService;


	public ModelSearchController(final CityRepository cityRepository, final ModelService modelService) {
		super();
		this.cityRepository = cityRepository;
        this.modelService = modelService;
	}
	
	/**
	 * Return a list of models (a category, ultimately) available for rent given the parameters.
	 * This method handles calls using the following format: http://localhost:8081/search/from/(origin_city)/(start_datetime)/to/(destiny_city)/(end_datetime) <br>
	 *   For example: http://localhost:8081/search/from/rotterdam-nl/2018-07-16 08:00/to/rotterdam-nl/2018-07-20 16:00
	 * @param origin Represents a city name followed by its 2-digit country code
	 * @param start Represents the search's start date and time: yyyy-MM-dd HH:mm
	 * @param destiny Represents a city name followed by its 2-digit country code
	 * @param finish Represents the search's end date and time: yyyy-MM-dd HH:mm
	 * @return A list of available categories represented by a model inside this category
	 * @throws CityNotFoundException In case the API could not find the city
	 */
	@ApiOperation("Returns available categories, represented by one of their car models, based on city and date/time requirements")
	@GetMapping("/models/from/{from}/{start}/to/{to}/{finish}")
	public List<ModelDto> searchAvailableCategories(
			@PathVariable("from") String origin, 
			@PathVariable("start") @DateTimeFormat(pattern="yyyy-MM-dd HH:mm") LocalDateTime start, 
			@PathVariable("to") String destiny, 
			@PathVariable("finish") @DateTimeFormat(pattern="yyyy-MM-dd HH:mm") LocalDateTime finish) throws CityNotFoundException, ReservationException {

		if (start.isAfter(finish))
			throw new ReservationException("Pick-up date/time should be earlier than the Drop-off date/time");

		City pickupLocation = cityRepository.findByNameAndCountryCode(origin)
				.orElseThrow(() -> new CityNotFoundException(String.format("Origin city %s not found", origin)));
		City dropOffLocation = cityRepository.findByNameAndCountryCode(destiny)
				.orElseThrow(() -> new CityNotFoundException(String.format("Destiny city %s not found", destiny)));

		try {
			return modelService.modelsAvailableOn(pickupLocation, start, dropOffLocation, finish)
					.stream()
					.map(ModelDto::basedOn)
					.collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
			throw new SearchCarException("Failure searching models, please try again later.");
		}

	}

}










