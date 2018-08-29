package com.carrental.domain.model.car;

import java.util.Objects;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/***
 * Represents a unique car available to be picked up in a given location and to be dropped off in another location, during a period of time.
 * Its uniqueness comes from the License plate, otherwise it would be just a car model like VW Golf.
 * In this way, we can control when a specific car is available or not.
 * @author Danilo Teodoro
 *
 */
@Entity
public class Car implements com.carrental.shared.Entity {
	
	private static final long serialVersionUID = 4404795652514822852L;
	
	@EmbeddedId
	private LicensePlate licensePlate;
	
	@ManyToOne
	@JoinColumn(name="MODEL_ID", nullable=false, updatable=false)
	private Model model;
	
	
	public Car(LicensePlate licensePlate, Model model) {
		super();
		this.model = Objects.requireNonNull(model, "Model must not be null");
		this.licensePlate = Objects.requireNonNull(licensePlate, "License plate must not be null");
	}
	
	// Simple constructor for ORM and serializers
	protected Car() {
		super();
	}
	
	public boolean is(Model model) {
		return this.getModel().equals(model);
	}
	
	public Model getModel() {
		return model;
	}
	
	public LicensePlate getLicensePlate() {
		return licensePlate;
	}
	
	@Override
	public String toString() {
		return String.format("%s: %s", this.licensePlate.toString(), this.model.toString());
	}
	
	@Override
	public int hashCode() {
		return licensePlate.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Car other = (Car) obj;
		return this.licensePlate.equals(other.licensePlate);
	}
	


}







