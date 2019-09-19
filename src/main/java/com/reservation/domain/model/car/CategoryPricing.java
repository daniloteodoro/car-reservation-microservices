package com.reservation.domain.model.car;

import com.reservation.domain.model.car.exceptions.CarRentalRuntimeException;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity(name = "CATEGORY_PRICING")
@NamedQueries({
		@NamedQuery(name=CategoryPricing.GET_ALL_CATEGORIES, query="from CATEGORY_PRICING c order by c.type"),
		@NamedQuery(name=CategoryPricing.GET_CATEGORY_BY_TYPE, query="from CATEGORY_PRICING c where c.type = :TYPE"),
})
@SqlResultSetMapping(
		name = CategoryWithReservationInfo.CATEGORY_AVAILABILITY_MAPPING,
		classes = {
				@ConstructorResult(
						targetClass = CategoryWithReservationInfo.class,
						columns = {
								@ColumnResult(name = "category_type", type = String.class),
								@ColumnResult(name = "price_per_day", type = Double.class),
								@ColumnResult(name = "standard_insurance", type = Double.class),
								@ColumnResult(name = "full_insurance", type = Double.class),
								@ColumnResult(name = "total", type = Integer.class),
								@ColumnResult(name = "total_reserved", type = Integer.class)
						}
				)
		})
/**
 * Represents the prices a category has associated to it.
 */
public class CategoryPricing implements com.reservation.domain.model.shared.Entity {

	private static final long serialVersionUID = -3207483314736276184L;

	public static final String GET_ALL_CATEGORIES = "getAllCategories";
	public static final String GET_CATEGORY_BY_TYPE = "getCategoryByType";

	@Id
	@Enumerated(EnumType.STRING)
	@Column(name="CATEGORY_TYPE", nullable = false)
	private final CategoryType type;

	@NotNull
	@Embedded
	@AttributeOverride(name="value", column=@Column(name="PRICE_PER_DAY", nullable = false))
	private final CarPrice pricePerDay;

	@NotNull
	@Embedded
	@AttributeOverride(name="value", column=@Column(name="STANDARD_INSURANCE", nullable = false))
	private final StandardInsurancePrice standardInsurance;

	@NotNull
	@Embedded
	@AttributeOverride(name="value", column=@Column(name="FULL_INSURANCE", nullable = false))
	private final FullInsurancePrice fullInsurance;


	public CategoryPricing(final CategoryType type, final CarPrice pricePerDay, final StandardInsurancePrice standardInsurance, final FullInsurancePrice fullInsurance) {
		super();
		this.type = Objects.requireNonNull(type, "Category type must not be null.");
		this.pricePerDay = Objects.requireNonNull(pricePerDay, "Category's price per day must not be null.");
		this.standardInsurance = Objects.requireNonNull(standardInsurance, "Category's standard insurance must not be null.");
		this.fullInsurance = Objects.requireNonNull(fullInsurance, "Category's full insurance must not be null.");
	}
	
	// Simple constructor for ORM and serializers
	protected CategoryPricing() {
		super();
		this.type = CategoryType.VAN;
		this.pricePerDay = new CarPrice(1_000_000.00);
		this.standardInsurance = StandardInsurancePrice.ZERO;
		this.fullInsurance = FullInsurancePrice.ZERO;
	}
	
	public Price getInsurancePriceFor(InsuranceType insuranceType) {
		if (insuranceType == null) {
			throw new CarRentalRuntimeException("Insurance type must not be null");
		}
		switch (insuranceType) {
		case FULL_INSURANCE: return getFullInsurance().toPrice();
		case STANDARD_INSURANCE: return getStandardInsurance().toPrice();
		default:
			throw new CarRentalRuntimeException("Unknown insurance type: " + insuranceType.toString());
		}
	}
	
	public CategoryType getType() { return type; }
	public CarPrice getPricePerDay() { return pricePerDay; }
	public StandardInsurancePrice getStandardInsurance() { return standardInsurance; }
	public FullInsurancePrice getFullInsurance() { return fullInsurance;	}

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
		
		CategoryPricing other = (CategoryPricing) obj;
		return type.equals(other.type);
	}
	
}
