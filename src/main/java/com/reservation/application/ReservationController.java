package com.reservation.application;

import com.reservation.application.dto.CreateReservationDto;
import com.reservation.application.dto.CustomerDto;
import com.reservation.application.dto.ReservationDto;
import com.reservation.domain.model.car.*;
import com.reservation.domain.model.car.exceptions.CategoryNotFoundException;
import com.reservation.domain.model.car.exceptions.CategoryTypeNotFoundException;
import com.reservation.domain.model.car.exceptions.CustomerNotFoundException;
import com.reservation.domain.model.customer.Customer;
import com.reservation.domain.model.customer.CustomerRepository;
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
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@CrossOrigin
@RestController
@Api(value = "/reservations", tags = "Reservations", description = "Allow starting, updating and confirming car reservations")
public class ReservationController {

	private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

	private final ReservationRepository reservationRepository;
	private final CityRepository cityRepository;
	private final CategoryRepository categoryRepository;
	private final CustomerRepository customerRepository;
	private final ReservationService reservationService;

	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


	@Autowired
	public ReservationController(ReservationRepository reservationRepository, CityRepository cityRepository, CategoryRepository categoryRepository, CustomerRepository customerRepository, ReservationService reservationService) {
		super();
		this.reservationRepository = reservationRepository;
		this.cityRepository = cityRepository;
		this.categoryRepository = categoryRepository;
		this.customerRepository = customerRepository;
		this.reservationService = reservationService;
	}

	/**
	 * Starts a new reservation, for an anonymous user, based on the origin and period of time.
	 * @param payload	A dto containing origin, destiny, datetime of start and finish, and the category to be reserved.
	 * @return			A new reservation with a unique id, a few options to be changed, and that can be optionally confirmed to become an actual car rental.
	 * @throws InvalidCityException				Informed origin or destiny city was not found or is not available
	 * @throws CategoryTypeNotFoundException	Category type was not found
	 * @throws CategoryNotFoundException		Category was not found
	 * @throws CategoryUnavailableException		There are no available models to be reserved given the current parameters
	 * @throws CustomerNotFoundException		Cannot find the anonymous customer
	 */
	@PostMapping("/reservations")
	public ResponseEntity<ReservationDto> startReservation(@RequestBody CreateReservationDto payload) throws URISyntaxException, InvalidCityException, CategoryNotFoundException, CategoryTypeNotFoundException, CategoryUnavailableException, CustomerNotFoundException {

		City pickupLocation = cityRepository.findByNameAndCountryCode(payload.getOrigin())
								 .orElseThrow(() -> new InvalidCityException(String.format("Origin city %s not found", payload.getOrigin())));
		City dropOffLocation = cityRepository.findByNameAndCountryCode(payload.getDestiny())
								 .orElseThrow(() -> new InvalidCityException(String.format("Destiny city %s not found", payload.getDestiny())));
		// TODO: Add timezone based on pick-up / drop-off location.
		// TODO: Validate start and end dates

		CategoryType type = CategoryType.findByName(payload.getCategoryType())
				.orElseThrow(CategoryTypeNotFoundException::new);

		LocalDateTime start = LocalDateTime.parse(payload.getStart(), dateTimeFormatter);
		LocalDateTime finish = LocalDateTime.parse(payload.getFinish(), dateTimeFormatter);

		CategoryAvailability chosenCategory = categoryRepository.getCategoryAvailabilityFor(type, pickupLocation, start, dropOffLocation, finish)
				.orElseThrow(CategoryNotFoundException::new);

		// TODO: Support logged in user?
		Customer visitor = customerRepository.findById(Customer.ANONYMOUS.getId())
				.orElseThrow(CustomerNotFoundException::new);

		// Start a new reservation based on the chosen category
		Reservation reservation = chosenCategory.reserve(visitor);

		reservationRepository.save(reservation);
		
		return ResponseEntity.created(new URI(String.format("/reservations/%s", reservation.getReservationNumber())))
							 .body(ReservationDto.basedOn(reservation));
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
		Reservation original = reservationRepository.findById(id)
				.orElseThrow(() -> new ReservationNotFoundException(id));

		// TODO: Put this inside the domain object
		original.clearExtras();
		Arrays.stream(extras).forEach(original::addExtraProduct);
		
		reservationRepository.save(original);
		
		return ResponseEntity.ok(ReservationDto.basedOn(original));
	}
	
	@PutMapping("/reservations/{reservationNumber}/insurance/{insuranceType}")
	public ResponseEntity<ReservationDto> setReservationInsurance(@PathVariable String reservationNumber, @PathVariable InsuranceType insuranceType) throws ReservationNotFoundException {
		ReservationNumber id = ReservationNumber.of(reservationNumber);
		Reservation original = reservationRepository.findById(id)
				.orElseThrow(() -> new ReservationNotFoundException(id));
		
		original.setInsurance(insuranceType);
		
		reservationRepository.save(original);
		
		return ResponseEntity.ok(ReservationDto.basedOn(original));
	}
	
	@PutMapping("/reservations/{reservationNumber}/customer-details")
	public ResponseEntity<ReservationDto> setReservationCustomerDetails(@PathVariable String reservationNumber, @RequestBody CustomerDto customerData) throws InvalidCityException, ReservationException, CustomerException {
		ReservationNumber id = ReservationNumber.of(reservationNumber);
		Reservation original = reservationRepository.findById(id)
				.orElseThrow(() -> new ReservationNotFoundException(id));

		if (customerData.getCity() == null) {
			throw new CustomerException("Customer's city is required");
		}
		City customerCity = cityRepository.findByNameAndCountryCode(customerData.getCity().getName(), customerData.getCity().getCountry().toString())
				.orElseThrow(() -> new InvalidCityException(String.format("Customer city '%s' not found", customerData.getCity())));

		if (original.getCustomer().isAnonymous()) {
			original.setCustomer(new Customer.Builder()
					.withFullName(customerData.getFullName())
					.withEmail(customerData.getEmail())
					.withPhoneNumber(customerData.getPhoneNumber())
					.withAddress(customerData.getAddress())
					.withCity(customerCity)
					.build());
		} else {
			original.getCustomer().setFullName(customerData.getFullName());
			original.getCustomer().setEmail(customerData.getEmail());
			original.getCustomer().setPhoneNumber(customerData.getPhoneNumber());
			original.getCustomer().setAddress(customerData.getAddress());
			original.getCustomer().setCity(customerCity);
		}

		reservationRepository.save(original);
		
		return ResponseEntity.ok(ReservationDto.basedOn(original));
	}

	@PutMapping("/reservations/{reservationNumber}/confirm")
	public ResponseEntity<OrderId> confirmReservation(@PathVariable String reservationNumber) throws ReservationException {
		ReservationNumber id = ReservationNumber.of(reservationNumber);
		Reservation original = reservationRepository.findById(id)
				.orElseThrow(() -> new ReservationNotFoundException(id));
		try {

			OrderId orderId = original.confirm(reservationService.confirmReservation());

			reservationRepository.save(original);

			return ResponseEntity.accepted().body(orderId);

		} catch (Exception e) {
			// Logs detailed, technical information
			e.printStackTrace();
			// Returns only message to the api client
			throw new ReservationException("Failed confirming reservation please try again");
		}
	}
	
}



