package com.carrental.application;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.carrental.application.dto.CarDto;
import com.carrental.application.dto.ReservationDto;
import com.carrental.domain.model.car.Car;
import com.carrental.domain.model.car.CarRepository;
import com.carrental.domain.model.car.ExtraProduct;
import com.carrental.domain.model.car.InsuranceType;
import com.carrental.domain.model.customer.Customer;
import com.carrental.domain.model.customer.Visitor;
import com.carrental.domain.model.reservation.City;
import com.carrental.domain.model.reservation.Reservation;
import com.carrental.domain.model.reservation.ReservationNumber;
import com.carrental.domain.model.reservation.ReservationRepository;
import com.carrental.domain.model.reservation.exceptions.ReservationNotFoundException;

@RestController
public class CarRentalController {
	
	private final CarRepository carRepository;
	private final ReservationRepository reservationRepository;
	
	@Autowired
	public CarRentalController(CarRepository carRepository, ReservationRepository reservationRepository) {
		super();
		this.carRepository = carRepository;
		this.reservationRepository = reservationRepository;
	}
	
	// TODO: HATEOAS
	@GetMapping("/search/{city}")
	public List<CarDto> searchCars(@PathVariable String city) {
		City cityFrom = City.parse(city);
		LocalDateTime start = LocalDateTime.of(2018, 7, 3, 16, 30);
		LocalDateTime finish = LocalDateTime.of(2018, 7, 8, 16, 00);
		
		List<CarDto> cars = 
				carRepository.basedOn(cityFrom, start, cityFrom, finish)
					.stream()
					.map((car) -> new CarDto(car.getLicensePlate(), car.getModel(), car.getPickupLocation(), car.getPickupDateTime(), car.getDropoffLocation(), car.getDropoffDateTime(), car.getPricePerDay(), car.getStore()))
					.collect(Collectors.toList());
		
		return cars;
	}
	
	// TODO: Category instead of license plate?
	@PostMapping("/choose/{licenseplate}")
	public ResponseEntity<ReservationDto> choose(@PathVariable("licenseplate") String licensePlate, @RequestBody CarDto car) throws URISyntaxException {
		City rotterdam = City.parse("rotterdam-nl");
		LocalDateTime start = LocalDateTime.of(2018, 7, 3, 16, 30);
		LocalDateTime finish = LocalDateTime.of(2018, 7, 8, 16, 00);
		Customer visitor = new Visitor();
		Car chosenCar = new Car(car.getLicensePlate(), car.getModel(), rotterdam, start, rotterdam, finish, car.getPricePerDay());
		
		// Start a new reservation based on the chosen car
		Reservation reservation = visitor.select(chosenCar, rotterdam, start, rotterdam, finish);
		
		reservationRepository.save(reservation);
		
		return ResponseEntity.created(new URI(String.format("/reservation/%s", reservation.getReservationNumber())))
							 .body(ReservationDto.basedOn(reservation));
	}
	
	// TODO: @GetMapping("/reservation/{reservationNumber}/extras")
	@PutMapping("/reservation/{reservationNumber}/extras/{extra}")
	public ResponseEntity<ReservationDto> addExtraToReservation(@PathVariable String reservationNumber, @PathVariable ExtraProduct extra) throws ReservationNotFoundException {
		
		Reservation original = reservationRepository.findByNumber(new ReservationNumber(reservationNumber));
		
		original.addExtraProduct(extra);
		
		reservationRepository.save(original);
		
		return ResponseEntity.ok(ReservationDto.basedOn(original));
	}
	
	@DeleteMapping("/reservation/{reservationNumber}/extras/{extra}")
	public ResponseEntity<ReservationDto> removeExtraFromReservation(@PathVariable String reservationNumber, @PathVariable ExtraProduct extra) throws ReservationNotFoundException {

		Reservation original = reservationRepository.findByNumber(new ReservationNumber(reservationNumber));
		
		original.removeExtraProduct(extra);
		
		reservationRepository.save(original);
		
		return ResponseEntity.ok(ReservationDto.basedOn(original));
	}
	
	@PutMapping("/reservation/{reservationNumber}/insurance/{insuranceType}")
	public ResponseEntity<ReservationDto> setReservationInsurance(@PathVariable String reservationNumber, @PathVariable InsuranceType insuranceType) throws ReservationNotFoundException {

		Reservation original = reservationRepository.findByNumber(new ReservationNumber(reservationNumber));
		
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











