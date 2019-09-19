package com.reservation.application;

import com.reservation.application.dto.CreateReservationDto;
import com.reservation.application.dto.CustomerDto;
import com.reservation.application.dto.ReservationDto;
import com.reservation.domain.model.car.*;
import com.reservation.domain.model.car.exceptions.CarRentalException;
import com.reservation.domain.model.car.exceptions.CategoryNotFoundException;
import com.reservation.domain.model.car.exceptions.CategoryTypeNotFoundException;
import com.reservation.domain.model.car.exceptions.CustomerNotFoundException;
import com.reservation.domain.model.reservation.*;
import com.reservation.domain.model.reservation.exceptions.*;
import com.reservation.domain.service.ReservationService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@CrossOrigin
@RestController
@Api(value = "/reservations", tags = "Reservations", description = "Allow starting, updating and confirming car reservations")
public class ReservationController {

	private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

	private final ReservationRepository reservationRepository;
	private final CityRepository cityRepository;
	private final ReservationService reservationService;

	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


	@Autowired
	public ReservationController(ReservationRepository reservationRepository, CityRepository cityRepository, ReservationService reservationService) {
		super();
		this.reservationRepository = reservationRepository;
		this.cityRepository = cityRepository;
		this.reservationService = reservationService;
	}

	/**
	 * Starts a new reservation, for an anonymous user, based on the origin and period of time.
	 * @param payload	A dto containing origin, destiny, datetime of start and finish, and the category to be reserved.
	 * @return			A new reservation with a unique id, a few options to be changed, and that can be optionally confirmed to become an actual car rental.
	 * @throws CityNotFoundException                Informed origin or destiny city was not found or is not available
	 * @throws CategoryTypeNotFoundException	Category type was not found
	 * @throws CategoryNotFoundException		Category was not found
	 * @throws CategoryUnavailableException		There are no available models to be reserved given the current parameters
	 * @throws CustomerNotFoundException		Cannot find the anonymous customer
	 */
	@PostMapping("/reservations")
	public ResponseEntity<ReservationDto> startReservation(@RequestBody CreateReservationDto payload) throws CityNotFoundException, CategoryTypeNotFoundException, ReservationException, CustomerNotFoundException, CategoryNotFoundException {

		City pickupLocation = cityRepository.findByNameAndCountryCode(payload.getOrigin())
								 .orElseThrow(() -> new CityNotFoundException(String.format("Origin city %s not found", payload.getOrigin())));
		City dropOffLocation = cityRepository.findByNameAndCountryCode(payload.getDestiny())
								 .orElseThrow(() -> new CityNotFoundException(String.format("Destiny city %s not found", payload.getDestiny())));

		CategoryType type = CategoryType.findByName(payload.getCategoryType())
				.orElseThrow(CategoryTypeNotFoundException::new);

		LocalDateTime start = LocalDateTime.parse(payload.getStart(), dateTimeFormatter);
		LocalDateTime finish = LocalDateTime.parse(payload.getFinish(), dateTimeFormatter);

		try {

			// Start a new reservation based on the chosen category
			Reservation reservation = reservationService.startReservationAnonymously(type, pickupLocation, start, dropOffLocation, finish);

			return ResponseEntity.created(new URI(String.format("/reservations/%s", reservation.getReservationNumber())))
					.body(ReservationDto.basedOn(reservation));

		} catch (CarRentalException e) {
			throw e;
		} catch (Exception e) {
			// Logs detailed, technical information
			e.printStackTrace();
			// Returns only message to the api client
			throw new ReservationException("Failure starting reservation. Please try again");
		}
	}
	
	@GetMapping("/reservations/{reservationNumber}")
	public ResponseEntity<ReservationDto> getReservation(@PathVariable("reservationNumber") String reservationNumber) throws ReservationNotFoundException {
		ReservationNumber id = ReservationNumber.of(reservationNumber);
		Reservation reservation = reservationRepository.findById(id)
				.orElseThrow(() -> new ReservationNotFoundException(id));
		
		return ResponseEntity.ok(ReservationDto.basedOn(reservation));
	}

	@PutMapping("/reservations/{reservationNumber}/extras")
	public ResponseEntity<ReservationDto> setReservationExtras(@PathVariable String reservationNumber, @RequestBody ExtraProduct[] extras) throws ReservationException {
		ReservationNumber id = ReservationNumber.of(reservationNumber);
		Reservation reservation = reservationRepository.findById(id)
				.orElseThrow(() -> new ReservationNotFoundException(id));
		try {

			reservation.replaceExtraProductsWith(extras);

			reservationRepository.save(reservation);

			return ResponseEntity.ok(ReservationDto.basedOn(reservation));

		} catch (Exception e) {
			// Logs detailed, technical information
			e.printStackTrace();
			// Returns only message to the api client
			throw new ReservationException("Failure setting reservation's extra products. Please try again");
		}
	}
	
	@PutMapping("/reservations/{reservationNumber}/insurance/{insuranceType}")
	public ResponseEntity<ReservationDto> setReservationInsurance(@PathVariable String reservationNumber, @PathVariable InsuranceType insuranceType) throws ReservationException {
		ReservationNumber id = ReservationNumber.of(reservationNumber);
		Reservation original = reservationRepository.findById(id)
				.orElseThrow(() -> new ReservationNotFoundException(id));
		try {

			original.setInsuranceType(insuranceType);

			reservationRepository.save(original);

			return ResponseEntity.ok(ReservationDto.basedOn(original));

		} catch (Exception e) {
			// Logs detailed, technical information
			e.printStackTrace();
			// Returns only message to the api client
			throw new ReservationException("Failure setting reservation's insurance type. Please try again");
		}
	}
	
	@PutMapping("/reservations/{reservationNumber}/customer-details")
	public ResponseEntity<ReservationDto> setReservationCustomerDetails(@PathVariable String reservationNumber, @RequestBody CustomerDto customerData) throws CityNotFoundException, ReservationException, CustomerException {
		ReservationNumber id = ReservationNumber.of(reservationNumber);
		Reservation reservation = reservationRepository.findById(id)
				.orElseThrow(() -> new ReservationNotFoundException(id));

		if (customerData.getCity() == null) {
			throw new CustomerException("Customer's city is required");
		}
		City customerCity = cityRepository.findByNameAndCountryCode(customerData.getCity().toString())
				.orElseThrow(() -> new CityNotFoundException(String.format("Customer city '%s' not found", customerData.getCity())));

		try {

			reservationService.setCustomer(reservation, customerData.getFullName(), customerData.getEmail(), customerData.getPhoneNumber(), customerData.getAddress(), customerCity);

			return ResponseEntity.ok(ReservationDto.basedOn(reservation));

		} catch (CarRentalException e) {
			throw e;
		} catch (Exception e) {
			// Logs detailed, technical information
			e.printStackTrace();
			// Returns only message to the api client
			throw new ReservationException("Failure starting reservation. Please try again");
		}
	}

	@PutMapping("/reservations/{reservationNumber}/confirm")
	public ResponseEntity<OrderId> confirmReservation(@PathVariable String reservationNumber) throws ReservationException {
		ReservationNumber id = ReservationNumber.of(reservationNumber);
		Reservation reservation = reservationRepository.findById(id)
				.orElseThrow(() -> new ReservationNotFoundException(id));
		try {

			OrderId orderId = reservationService.confirm(reservation);

			return ResponseEntity.accepted().body(orderId);

		} catch (CarRentalException e) {
			throw e;
		} catch (Exception e) {
			// Logs detailed, technical information
			e.printStackTrace();
			// Returns only message to the api client
			throw new ReservationException("Failed confirming reservation. Please try again");
		}
	}
	
}



