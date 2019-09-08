package com.reservation.domain.model.customer;

import com.reservation.domain.model.reservation.City;
import com.reservation.domain.model.reservation.exceptions.CustomerException;
import com.reservation.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/***
 * Represents a person booking a reservation.
 * @author Danilo Teodoro
 *
 */
@Entity
public class Customer implements com.reservation.domain.model.shared.Entity {

	private static final long serialVersionUID = -1315287830188832753L;
	private static final long UNKNOWN_CLIENT_ID = -1;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotEmpty
	@Column(nullable = false)
	private String fullName;

	@NotEmpty
	@Column(nullable = false)
	private String email;

	private String phoneNumber;

	@NotEmpty
	@Column(nullable = false)
	private String address;

	@NotNull
	@ManyToOne(cascade = CascadeType.ALL)
	private City city = City.EMPTY;

	public static final Customer ANONYMOUS = buildAnonymousCustomer();


	public boolean isAnonymous() {
		return this.equals(ANONYMOUS);
	}

	private static Customer buildAnonymousCustomer() {
		Customer c = new Customer();
		c.id = UNKNOWN_CLIENT_ID;
		return c;
	}

	// Used internally or by ORM/Serializers
	protected Customer() {
		super();
	}

	public Long getId() {
		return id;
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
	public City getCity() {
		return city;
	}
	public void setFullName(String fullName) throws CustomerException {
		this.fullName = StringUtils.requireNonEmpty(fullName, () -> new CustomerException("Full name is required"));;
	}
	public void setEmail(String email) throws CustomerException {
		this.email = StringUtils.requireNonEmpty(email, () -> new CustomerException("E-mail is required"));;
	}
	public void setPhoneNumber(String phoneNumber) throws CustomerException {
		this.phoneNumber = StringUtils.requireNonEmpty(phoneNumber, () -> new CustomerException("Phone Number is required"));
	}
	public void setAddress(String address) throws CustomerException {
		this.address = StringUtils.requireNonEmpty(address, () -> new CustomerException("Address is required"));;
	}
	public void setCity(City city) throws CustomerException {
		if (city == null) {
			throw new CustomerException("City is required");
		}
		this.city = city;
	}

	@Override
	public String toString() {
		return String.format("%d: %s", this.id, this.fullName);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		Customer other = (Customer) obj;
		return id.equals(other.id);
	}

	public static class Builder {
		private String fullName;
		private String email;
		private String phoneNumber;
		private String address;
		private City city = City.EMPTY;

		public Builder() {
			super();
		}

		public Builder withFullName(String name) {
			this.fullName = name;
			return this;
		}

		public Builder withEmail(String email) {
			this.email = email;
			return this;
		}

		public Builder withPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
			return this;
		}

		public Builder withAddress(String address) {
			this.address = address;
			return this;
		}

		public Builder withCity(City city) {
			this.city = city;
			return this;
		}

		public Customer build() throws CustomerException {
			Customer customer = new Customer();
			customer.fullName = StringUtils.requireNonEmpty(fullName, () -> new CustomerException("Full name is required"));
			customer.email = StringUtils.requireNonEmpty(email, () -> new CustomerException("E-mail is required"));
			customer.phoneNumber = StringUtils.requireNonEmpty(phoneNumber, () -> new CustomerException("Phone Number is required"));
			customer.address = StringUtils.requireNonEmpty(address, () -> new CustomerException("Address is required"));
			if (city == null) {
				throw new CustomerException("City is required");
			}
			customer.city = this.city;
			return customer;
		}

	}

}
