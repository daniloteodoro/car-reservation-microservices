package com.reservation.domain.model.customer;

import com.reservation.domain.model.car.exceptions.CustomerNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    default Customer getAnonymousCustomer() throws CustomerNotFoundException {
        return findById(Customer.ANONYMOUS.getId())
                .orElseThrow(CustomerNotFoundException::new);
    }
	
}
