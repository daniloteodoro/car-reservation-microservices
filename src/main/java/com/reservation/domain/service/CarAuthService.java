package com.reservation.domain.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="car-rental-auth", url="localhost:8085")
public interface CarAuthService {
	
	class CarLoginCredentialsDto {
		private String username;
		private String password;
		
		
		public CarLoginCredentialsDto(String username, String password) {
			super();
			this.username = username;
			this.password = password;
		}

		public String getUsername() { return username; }
		public String getPassword() { return password; }

		@Override
		public String toString() {
			return username;
		}
	}
	
	@PostMapping("/login")
	String login(@RequestBody CarLoginCredentialsDto credentials);

}