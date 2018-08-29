package com.carrental.application;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

import com.carrental.application.dto.ReservationDto;
import com.carrental.domain.model.car.CarRepository;
import com.carrental.domain.model.car.Category;
import com.carrental.domain.model.car.CategoryFeaturingModel;
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
import com.carrental.domain.model.reservation.exceptions.ReservationNotFoundException;

@RestController
public class CarRentalController {
	
	private final CarRepository carRepository;
	private final ReservationRepository reservationRepository;
	private final CityRepository cityRepository;
	private final CategoryRepository categoryRepository;
	
	
	// TODO: Remove unused dependencies
	@Autowired
	public CarRentalController(CarRepository carRepository, ReservationRepository reservationRepository, CityRepository cityRepository, CategoryRepository categoryRepository) {
		super();
		this.carRepository = carRepository;
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
	public List<CategoryFeaturingModel> searchCars(
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
		
		List<CategoryFeaturingModel> availableCategories = 
				categoryRepository.availableOn(pickupLocation, start, dropoffLocation, finish);
		
		return availableCategories;
	}
	
	// TODO: Category instead of license plate?
	@PostMapping("/choose/{licenseplate}")
	public ResponseEntity<ReservationDto> choose(@PathVariable("licenseplate") String licensePlate, @RequestBody CategoryType category) throws URISyntaxException, CarUnavailableException, CityNotFoundException, CategoryNotFoundException {
		City rotterdam = City.parse("rotterdam-nl");
		LocalDateTime start = LocalDateTime.of(2018, 7, 3, 16, 30);
		LocalDateTime finish = LocalDateTime.of(2018, 7, 8, 16, 00);
		Customer visitor = new Visitor();
		Optional<Category> chosenCategory = categoryRepository.findById(category);
		
		if (!chosenCategory.isPresent()) {
			throw new CategoryNotFoundException();
		}
		
		// Start a new reservation based on the chosen car
		Reservation reservation = visitor.select(chosenCategory.get(), rotterdam, start.plusDays(1), rotterdam, finish.minusHours(2));
		
		reservationRepository.save(reservation);
		
		return ResponseEntity.created(new URI(String.format("/reservation/%s", reservation.getReservationNumber())))
							 .body(ReservationDto.basedOn(reservation));
	}
	
	// TODO: @GetMapping("/reservation/{reservationNumber}/extras")
	@PutMapping("/reservation/{reservationNumber}/extras/{extra}")
	public ResponseEntity<ReservationDto> addExtraToReservation(@PathVariable String reservationNumber, @PathVariable ExtraProduct extra) throws ReservationNotFoundException {
		
		Reservation original = reservationRepository.findByNumber(new ReservationNumber(reservationNumber))
													.orElseThrow(() -> new ReservationNotFoundException());
		
		original.addExtraProduct(extra);
		
		reservationRepository.save(original);
		
		return ResponseEntity.ok(ReservationDto.basedOn(original));
	}
	
	@DeleteMapping("/reservation/{reservationNumber}/extras/{extra}")
	public ResponseEntity<ReservationDto> removeExtraFromReservation(@PathVariable String reservationNumber, @PathVariable ExtraProduct extra) throws ReservationNotFoundException {
		
		Reservation original = reservationRepository.findByNumber(new ReservationNumber(reservationNumber))
													.orElseThrow(() -> new ReservationNotFoundException());
		
		original.removeExtraProduct(extra);
		
		reservationRepository.save(original);
		
		return ResponseEntity.ok(ReservationDto.basedOn(original));
	}
	
	@PutMapping("/reservation/{reservationNumber}/insurance/{insuranceType}")
	public ResponseEntity<ReservationDto> setReservationInsurance(@PathVariable String reservationNumber, @PathVariable InsuranceType insuranceType) throws ReservationNotFoundException {

		Reservation original = reservationRepository.findByNumber(new ReservationNumber(reservationNumber))
													.orElseThrow(() -> new ReservationNotFoundException());
		
		original.setInsurance(insuranceType);
		
		reservationRepository.save(original);
		
		return ResponseEntity.ok(ReservationDto.basedOn(original));
	}
	
	@PostMapping("/reservation/{reservationNumber}/personal-details")
	public ResponseEntity<ReservationDto> setReservationPersonalDetails(@PathVariable String reservationNumber /* Visitor data */) {
		
		
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/reservation/{reservationNumber}/confirm")
	public ResponseEntity<ReservationDto> confirmReservation(@PathVariable String reservationNumber) {
		
		
		return ResponseEntity.accepted().build();
	}
	
}











