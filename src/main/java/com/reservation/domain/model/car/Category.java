package com.reservation.domain.model.car;

import com.reservation.domain.model.car.exceptions.CarRentalRuntimeException;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NamedQueries({
		@NamedQuery(name=Category.GET_ALL_CATEGORIES, query="select c from Category c order by c.type"),
		@NamedQuery(name=Category.GET_CATEGORY_BY_TYPE, query="select c from Category c where c.type = :TYPE"),
})
@SqlResultSetMapping(
		name = CategoryAvailability.CATEGORY_AVAILABILITY_MAPPING,
		classes = {
				@ConstructorResult(
						targetClass = CategoryAvailability.class,
						columns = {
								@ColumnResult(name = "category_type", type = String.class),
								@ColumnResult(name = "price", type = Double.class),
								@ColumnResult(name = "standard_insurance", type = Double.class),
								@ColumnResult(name = "full_insurance", type = Double.class),
								@ColumnResult(name = "total", type = Integer.class),
								@ColumnResult(name = "total_reserved", type = Integer.class)
						}
				)
		})
public class Category implements com.reservation.shared.Entity {

	private static final long serialVersionUID = -6083285778312724846L;
	public static final String GET_ALL_CATEGORIES = "getAllCategories";
	public static final String GET_CATEGORY_BY_TYPE = "getCategoryByType";

	@Id
	@Enumerated(EnumType.STRING)
	@Column(name="CATEGORY_TYPE")
	private final CategoryType type;
	
	@Embedded
	@AttributeOverride(name="value", column=@Column(name="PRICE"))
	private final CarPrice pricePerDay;
	
	@Embedded
	@AttributeOverride(name="value", column=@Column(name="STANDARD_INSURANCE"))
	private final Price standardInsurance;
	
	@Embedded
	@AttributeOverride(name="value", column=@Column(name="FULL_INSURANCE"))
	private final Price fullInsurance;


	public Category(final CategoryType type, final CarPrice pricePerDay, final Price standardInsurance, final Price fullInsurance) {
		super();
		this.type = Objects.requireNonNull(type, "Category type must not be null.");
		this.pricePerDay = pricePerDay;
		this.standardInsurance = standardInsurance;
		this.fullInsurance = fullInsurance;
	}
	
	// Simple constructor for ORM and serializers
	protected Category() {
		super();
		this.type = CategoryType.VAN;
		this.pricePerDay = new CarPrice(1_000_000.00);
		this.standardInsurance = Price.ZERO;
		this.fullInsurance = Price.ZERO;
	}
	
	public Price getInsurancePriceFor(InsuranceType insuranceType) {
		if (insuranceType == null) {
			throw new CarRentalRuntimeException("Insurance type must not be null");
		}
		switch (insuranceType) {
		case FULL_INSURANCE: return getFullInsurance();
		case STANDARD_INSURANCE: return getStandardInsurance();
		default:
			throw new CarRentalRuntimeException("Unknown insurance type: " + insuranceType.toString());
		}
	}
	
	public CategoryType getType() {
		return type;
	}
	
	public CarPrice getPricePerDay() {
		return pricePerDay;
	}
	
	public Price getStandardInsurance() {
		return standardInsurance;
	}
	
	public Price getFullInsurance() {
		return fullInsurance;
	}

	@Override
	public String toString() {
		return type.toString();
	}
	
	@Override
	public int hashCode() {
		return type.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		Category other = (Category) obj;
		return type.equals(other.type);
	}
	
}

