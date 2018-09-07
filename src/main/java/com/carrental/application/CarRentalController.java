package com.carrental.application;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.carrental.application.dto.ModelDto;
import com.carrental.application.dto.ReservationDto;
import com.carrental.domain.model.car.Category;
import com.carrental.domain.model.car.CategoryRepository;
import com.carrental.domain.model.car.CategoryType;
import com.carrental.domain.model.car.ExtraProduct;
import com.carrental.domain.model.car.InsuranceType;
import com.carrental.domain.model.car.exceptions.CategoryNotFoundException;
import com.carrental.domain.model.customer.Customer;
import com.carrental.domain.model.customer.Visitor;
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
import com.carrental.util.StringUtils;

@RestController
public class CarRentalController {
	
	private final ReservationRepository reservationRepository;
	private final CityRepository cityRepository;
	private final CategoryRepository categoryRepository;
	
	
	// TODO: Remove unused dependencies
	@Autowired
	public CarRentalController(ReservationRepository reservationRepository, CityRepository cityRepository, CategoryRepository categoryRepository) {
		super();
		this.reservationRepository = reservationRepository;
		this.cityRepository = cityRepository;
		this.categoryRepository = categoryRepository;
	}
	
	// TODO: HATEOAS
	/***
	 * Handles calls using the following example: http://localhost:8081/search/from/rotterdam-nl/2018-07-16 08:00/to/rotterdam-nl/2018-07-20 16:00
	 * @param origin Represents a city name followed by its country code
	 * @param start Represents the search's start date
	 * @param destiny Represents a city name followed by its country code
	 * @param finish Represents the search's end date
	 * @return <s> A list of cars</s> A list of categories
	 * @throws CityNotFoundException In case the API could not find the city (404 status code)
	 */
	@GetMapping("/search/from/{from}/{start}/to/{to}/{finish}")
	public List<ModelDto> searchCars(
			@PathVariable("from") String origin, 
			@PathVariable("start") @DateTimeFormat(pattern="yyyy-MM-dd HH:mm") LocalDateTime start, 
			@PathVariable("to") String destiny, 
			@PathVariable("finish") @DateTimeFormat(pattern="yyyy-MM-dd HH:mm") LocalDateTime finish) throws CityNotFoundException {
		
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
		
		// TODO: Add timezone based on pick-up / drop-off location.
		
		List<ModelDto> availableCategories = 
				categoryRepository.availableOn(pickupLocation, start, dropoffLocation, finish).stream()
				.map((model) -> ModelDto.basedOn(model))
				.collect(Collectors.toList());
		
		return availableCategories;
	}
	
	@PostMapping("/search/from/{from}/{start}/to/{to}/{finish}/{category}")
	public ResponseEntity<ReservationDto> choose(
			@PathVariable("from") String origin, 
			@PathVariable("start") @DateTimeFormat(pattern="yyyy-MM-dd HH:mm") LocalDateTime start, 
			@PathVariable("to") String destiny, 
			@PathVariable("finish") @DateTimeFormat(pattern="yyyy-MM-dd HH:mm") LocalDateTime finish,
			@PathVariable("category") String category
			) throws URISyntaxException, CityNotFoundException, CategoryNotFoundException, CarUnavailableException {
		
		Customer visitor = new Visitor();
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
	public ResponseEntity<ReservationDto> addExtraToReservation(@PathVariable String reservationNumber, @RequestBody ExtraProduct[] extras) throws ReservationException {
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
	
	@DeleteMapping("/reservation/{reservationNumber}/extras/{extra}")
	public ResponseEntity<ReservationDto> removeExtraFromReservation(@PathVariable String reservationNumber, @PathVariable ExtraProduct extra) throws ReservationNotFoundException {
		ReservationNumber id = ReservationNumber.of(reservationNumber);
		Reservation original = reservationRepository.findByNumber(id)
													.orElseThrow(() -> new ReservationNotFoundException(id));
		
		original.removeExtraProduct(extra);
		
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
	
	@PostMapping("/reservation/{reservationNumber}/personal-details")
	public ResponseEntity<ReservationDto> setReservationPersonalDetails(@PathVariable String reservationNumber /* Visitor data */) throws ReservationNotFoundException {
		ReservationNumber id = ReservationNumber.of(reservationNumber);
		Reservation original = reservationRepository.findByNumber(id)
													.orElseThrow(() -> new ReservationNotFoundException(id));
		
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/reservation/{reservationNumber}/confirm")
	public ResponseEntity<ReservationDto> confirmReservation(@PathVariable String reservationNumber) throws ReservationNotFoundException {
		ReservationNumber id = ReservationNumber.of(reservationNumber);
		Reservation original = reservationRepository.findByNumber(id)
													.orElseThrow(() -> new ReservationNotFoundException(id));
		
		return ResponseEntity.accepted().build();
	}
	
}











