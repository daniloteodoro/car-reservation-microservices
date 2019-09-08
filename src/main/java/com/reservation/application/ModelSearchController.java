package com.reservation.application;

import com.reservation.application.dto.ModelDto;
import com.reservation.domain.model.reservation.*;
import com.reservation.domain.model.reservation.exceptions.*;
import com.reservation.domain.service.ModelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
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
	 * Handles calls using the following format: http://localhost:8081/search/from/(origin_city)/(start_datetime)/to/(destiny_city)/(end_datetime) <br>
	 *   For example: http://localhost:8081/search/from/rotterdam-nl/2018-07-16 08:00/to/rotterdam-nl/2018-07-20 16:00
	 * @param origin Represents a city name followed by its 2-digit country code
	 * @param start Represents the search's start date
	 * @param destiny Represents a city name followed by its 2-digit country code
	 * @param finish Represents the search's end date
	 * @return A list of available categories represented by a model inside this category
	 * @throws InvalidCityException In case the API could not find the city
	 */
	@ApiOperation("Returns available categories, represented by one of their car models, based on city and date/time requirements")
	@GetMapping("/models/from/{from}/{start}/to/{to}/{finish}")
	public List<ModelDto> searchAvailableCategories(
			@PathVariable("from") String origin, 
			@PathVariable("start") @DateTimeFormat(pattern="yyyy-MM-dd HH:mm") LocalDateTime start, 
			@PathVariable("to") String destiny, 
			@PathVariable("finish") @DateTimeFormat(pattern="yyyy-MM-dd HH:mm") LocalDateTime finish) throws InvalidCityException {
		
		City pickupLocation = cityRepository.findByNameAndCountryCode(origin)
				.orElseThrow(() -> new InvalidCityException(String.format("Origin city %s not found", origin)));
		City dropOffLocation = cityRepository.findByNameAndCountryCode(destiny)
				.orElseThrow(() -> new InvalidCityException(String.format("Destiny city %s not found", destiny)));

		// TODO: Add timezone based on pick-up / drop-off location.
		// TODO: Validate start and end dates

		List<ModelDto> modelsFromAvailableCategories =
                modelService.availableOn(pickupLocation, start, dropOffLocation, finish)
						.stream()
						.map( model -> ModelDto.basedOn(model) )
						.collect(Collectors.toList());

		return modelsFromAvailableCategories;
	}

}










