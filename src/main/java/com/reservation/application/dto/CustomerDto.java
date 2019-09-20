package com.reservation.application.dto;

import com.reservation.domain.model.customer.Customer;

public class CustomerDto {
	
	private String fullName;
	private String email;
	private String phoneNumber;
	private String address;
	private CityDto city;
	
	
	public CustomerDto(String fullName, String email, String phoneNumber, String address, CityDto city) {
		super();
		this.fullName = fullName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.city = city;
	}
	
	public static CustomerDto basedOn(Customer customer) {
		if (customer == null) {
			return null;
		}
		return new CustomerDto(customer.getFullName(), customer.getEmail(), customer.getPhoneNumber(), customer.getAddress(), CityDto.basedOn(customer.getCity()));
	}

	public String getFullName() {
		return fullName;
	}

	public String getEmail() {
		return email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public CityDto getCity() {
		return city;
	}

}
