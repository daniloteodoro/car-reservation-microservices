package com.carrental.application;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.carrental.application.dto.CustomerDto;
import com.carrental.application.dto.ModelDto;
import com.carrental.application.dto.ReservationDto;
import com.carrental.domain.model.car.Category;
import com.carrental.domain.model.car.CategoryRepository;
import com.carrental.domain.model.car.CategoryType;
import com.carrental.domain.model.car.ExtraProduct;
import com.carrental.domain.model.car.InsuranceType;
import com.carrental.domain.model.car.exceptions.CarRentalRuntimeException;
import com.carrental.domain.model.car.exceptions.CategoryNotFoundException;
import com.carrental.domain.model.customer.Customer;
import com.carrental.domain.model.reservation.City;
import com.carrental.domain.model.reservation.CityRepository;
import com.carrental.domain.model.reservation.Reservation;
import com.carrental.domain.model.reservation.ReservationNumber;
import com.carrental.domain.model.reservation.ReservationRepository;
import com.carrental.domain.model.reservation.exceptions.CarUnavailableException;
import com.carrental.domain.model.reservation.exceptions.CityFormatException;
import com.carrental.domain.model.reservation.exceptions.CityNotFoundException;
import com.carrental.domain.model.reservation.exceptions.ReservationException;
import com.carrental.domain.model.reservation.exceptions.ReservationNotFoundException;
import com.carrental.domain.service.CarAuthService;
import com.carrental.domain.service.CarAuthService.CarLoginCredentials;
import com.carrental.util.JsonUtils;
import com.carrental.util.StringUtils;

@CrossOrigin
@RestController
@Api(value = "/reservations", tags = "Reservation", description = "Handles car searches and creates reservations.")
public class CarRentalController {
	
	private final ReservationRepository reservationRepository;
	private final CityRepository cityRepository;
	private final CategoryRepository categoryRepository;
	private final CarAuthService carAuthService;
	
	
	// TODO: Remove unused dependencies

	@Autowired
	public CarRentalController(ReservationRepository reservationRepository, CityRepository cityRepository, CategoryRepository categoryRepository, CarAuthService carAuthService) {
		super();
		this.reservationRepository = reservationRepository;
		this.cityRepository = cityRepository;
		this.categoryRepository = categoryRepository;
		this.carAuthService = carAuthService;
	}
	
	/***
	 * Handles calls using the following format: http://localhost:8081/search/from/<city_origin>/<start_datetime>/to/<city_destiny>/<end_datetime>
	 *   For example: http://localhost:8081/search/from/rotterdam-nl/2018-07-16 08:00/to/rotterdam-nl/2018-07-20 16:00
	 * @param origin Represents a city name followed by its country code
	 * @param start Represents the search's start date
	 * @param destiny Represents a city name followed by its country code
	 * @param finish Represents the search's end date
	 * @return A list of categories
	 * @throws CityNotFoundException In case the API could not find the city (404 status code)
	 */
	@ApiOperation("Returns available categories (model?) based on city and date/time requirements")
	@GetMapping("/search/from/{from}/{start}/to/{to}/{finish}")
	public List<ModelDto> searchAvailableCategories(
			@PathVariable("from") String origin, 
			@PathVariable("start") @DateTimeFormat(pattern="yyyy-MM-dd HH:mm") LocalDateTime start, 
			@PathVariable("to") String destiny, 
			@PathVariable("finish") @DateTimeFormat(pattern="yyyy-MM-dd HH:mm") LocalDateTime finish) throws CityNotFoundException {
		
		City pickupLocation = cityRepository.findByNameAndCountryCode(origin)
				.orElseThrow(() -> new CityNotFoundException(String.format("Origin city %s not found", origin)));
		City dropoffLocation = cityRepository.findByNameAndCountryCode(destiny)
				.orElseThrow(() -> new CityNotFoundException(String.format("Destiny city %s not found", destiny)));

		// TODO: Add timezone based on pick-up / drop-off location.

		List<ModelDto> availableCategories =
				categoryRepository.availableOn(pickupLocation, start, dropoffLocation, finish)
						.stream()
						.map( model -> ModelDto.basedOn(model) )
						.collect(Collectors.toList());
		
		return availableCategories;
	}
	
	@PostMapping("/search/from/{from}/{start}/to/{to}/{finish}/{category}")
	public ResponseEntity<ReservationDto> chooseCategory(
			@PathVariable("from") String origin, 
			@PathVariable("start") @DateTimeFormat(pattern="yyyy-MM-dd HH:mm") LocalDateTime start, 
			@PathVariable("to") String destiny, 
			@PathVariable("finish") @DateTimeFormat(pattern="yyyy-MM-dd HH:mm") LocalDateTime finish,
			@PathVariable("category") String category
			) throws URISyntaxException, CityNotFoundException, CategoryNotFoundException, CarUnavailableException {
		
		Customer visitor = Customer.EMPTY;

		City pickupLocation;
		City dropoffLocation;
		try {
			pickupLocation = cityRepository.findByNameAndCountryCode(origin)
									 .orElseThrow(() -> new CityNotFoundException(String.format("Origin city %s not found", origin)));
			dropoffLocation = cityRepository.findByNameAndCountryCode(destiny)
					 				 .orElseThrow(() -> new CityNotFoundException(String.format("Destiny city %s not found", destiny)));
		} catch (CityFormatException e) {
			e.printStackTrace();
			throw e;
		} catch (CityNotFoundException e) {
			e.printStackTrace();
			throw e;
		}
		
		if (StringUtils.isEmpty(category) || CategoryType.valueOf(category.toUpperCase()) == null) {
			throw new CategoryNotFoundException();
		}
		Optional<Category> chosenCategory = categoryRepository.findById(CategoryType.valueOf(category));
		if (!chosenCategory.isPresent()) {
			throw new CategoryNotFoundException();
		}
		
		// Start a new reservation based on the chosen car
		Reservation reservation = visitor.select(chosenCategory.get(), pickupLocation, start, dropoffLocation, finish);
		
		reservationRepository.save(reservation);
		
		return ResponseEntity.created(new URI(String.format("/reservation/%s", reservation.getReservationNumber())))
							 .body(ReservationDto.basedOn(reservation));
	}
	
	@GetMapping("/reservation/{reservationNumber}")
	public ResponseEntity<ReservationDto> getReservation(@PathVariable("reservationNumber") String reservationNumber) throws ReservationNotFoundException {
		ReservationNumber id = ReservationNumber.of(reservationNumber);
		Reservation reservation = reservationRepository.findByNumber(id)
													   .orElseThrow(() -> new ReservationNotFoundException(id));
		
		return ResponseEntity.ok(ReservationDto.basedOn(reservation));
	}
	
	@PutMapping("/reservation/{reservationNumber}/extras")
	public ResponseEntity<ReservationDto> setReservationExtras(@PathVariable String reservationNumber, @RequestBody ExtraProduct[] extras) throws ReservationException {
		ReservationNumber id = ReservationNumber.of(reservationNumber);
		Reservation original = reservationRepository.findByNumber(id)
													.orElseThrow(() -> new ReservationNotFoundException(id));
		
		original.clearExtras();
		Arrays.stream(extras).forEach(
			(extraProduct) -> original.addExtraProduct(extraProduct)
		);
		
		reservationRepository.save(original);
		
		return ResponseEntity.ok(ReservationDto.basedOn(original));
	}
	
	@PutMapping("/reservation/{reservationNumber}/insurance/{insuranceType}")
	public ResponseEntity<ReservationDto> setReservationInsurance(@PathVariable String reservationNumber, @PathVariable InsuranceType insuranceType) throws ReservationNotFoundException {
		ReservationNumber id = ReservationNumber.of(reservationNumber);
		Reservation original = reservationRepository.findByNumber(id)
													.orElseThrow(() -> new ReservationNotFoundException(id));
		
		original.setInsurance(insuranceType);
		
		reservationRepository.save(original);
		
		return ResponseEntity.ok(ReservationDto.basedOn(original));
	}
	
	@PutMapping("/reservation/{reservationNumber}/customer-details")
	public ResponseEntity<ReservationDto> setReservationCustomerDetails(@PathVariable String reservationNumber, @RequestBody CustomerDto customerData) throws CityNotFoundException, ReservationException {
		ReservationNumber id = ReservationNumber.of(reservationNumber);
		Reservation original = reservationRepository.findByNumber(id)
													.orElseThrow(() -> new ReservationNotFoundException(id));
		// TODO: Repository in memory for cities?
		City customerCity;
		try {
			Objects.requireNonNull(customerData.getCity(), "Customer's city is obligatory");
			customerCity = cityRepository.findByNameAndCountryCode(customerData.getCity().getName(), customerData.getCity().getCountry().toString())
					 				 	 .orElseThrow(() -> new CityNotFoundException(String.format("Customer city '%s' not found", customerData.getCity())));
		} catch (CityFormatException e) {
			e.printStackTrace();
			throw e;
		} catch (CityNotFoundException e) {
			e.printStackTrace();
			throw e;
		}
		
		// TODO: Validate customer information
		// TODO: Create factory to generate a consistent Customer
		Customer visitor = Customer.EMPTY;
		visitor.setFullName(customerData.getFullName());
		visitor.setEmail(customerData.getEmail());
		visitor.setPhoneNumber(customerData.getPhoneNumber());
		visitor.setAddress(customerData.getAddress());
		visitor.setCity(customerCity);
		
		original.setCustomer(visitor);
		
		reservationRepository.save(original);
		
		return ResponseEntity.ok(ReservationDto.basedOn(original));
	}
	
	// TODO: Review URLs
	@PostMapping("/reservation/{reservationNumber}/confirm")
	public ResponseEntity<String> confirmReservation(@PathVariable String reservationNumber) throws ReservationException {
		ReservationNumber id = ReservationNumber.of(reservationNumber);
		Reservation original = reservationRepository.findByNumber(id)
													.orElseThrow(() -> new ReservationNotFoundException(id));
		
		if (original.getCustomer() == null)
			throw new ReservationException("No customer associated to the reservation");
		if (!original.getCustomer().isValid())
			throw new ReservationException("Please fill in Customer details before confirming the reservation");
		
		System.out.println("\t\t\t * Placing order for reservation: " + id);
		
		Map<String, String> uriVariables = new HashMap<>();
		String json = "{\n" + 
				"	\"username\": \"GUEST\",\n" + 
				"	\"password\": \"guest\"\n" + 
				"}";
		
		// TODO: Where to place tokens, create object above?
		// Get token
		String response = "";
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> authRequest = new HttpEntity<>(json, headers);
		ResponseEntity<String> responseEntity;
		try {
			//responseEntity = new RestTemplate().postForEntity("http://localhost:8085/login", authRequest, String.class);
			//response = responseEntity.getBody();
			response = carAuthService.login(new CarLoginCredentials("guest", "guest"));
		} catch (Exception e) {
			throw new CarRentalRuntimeException("Fail to login to create order: "+e.getMessage());
		}
		
		if (response == null || !response.startsWith("Bearer "))
			throw new CarRentalRuntimeException("Fail to authenticate to complete order process - order was not generated");

		headers.set("Authorization", response);
		
		HttpEntity<String> orderRequest = new HttpEntity<>(JsonUtils.toJson(id), headers);
		
		try {
			responseEntity = 
					new RestTemplate().postForEntity("http://localhost:8086/orders", orderRequest, String.class, uriVariables);
		} catch (Exception e) {
			throw new CarRentalRuntimeException("Failure calling order service: "+e.getMessage());
		}
		response = responseEntity.getBody();
		
		System.out.println("Order has been placed!");
		
		// TODO: Mark current reservation as read-only  - Now we have a distributed transaction challenge :)
		
		return ResponseEntity.accepted().body(response);
	}
	
}










