package com.reservation.domain.model.car;

import java.util.Objects;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.reservation.util.StringUtils;

@Entity
@NamedQueries({
	@NamedQuery(name=Model.GET_EXAMPLE_MODEL_BY_CATEGORY, query="from Model m where m.categoryPricing = :CATEGORY")
})
public class Model implements com.reservation.domain.model.shared.Entity {
	
	private static final long serialVersionUID = 5896117340555165412L;
	public static final String GET_EXAMPLE_MODEL_BY_CATEGORY = "getExampleModelByCategory";
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private final Integer id;

	@NotNull
	@Embedded
	@Column(nullable = false)
	private final Brand brand;

	@NotEmpty
	@Column(nullable = false)
	private final String description;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="CATEGORY_ID", nullable=false)
	private final CategoryPricing categoryPricing;
	
	
	public Model(Integer id, Brand brand, String modelDescription, CategoryPricing categoryPricing) {
		super();
		this.id = Objects.requireNonNull(id, "Id must not be null");
		this.brand = Objects.requireNonNull(brand, "Brand must not be null");
		this.description = StringUtils.requireNonEmpty(modelDescription, "Model description must not be null");
		this.categoryPricing = Objects.requireNonNull(categoryPricing, "Category must not be null");
	}
	
	public Model(Brand brand, String modelDescription, CategoryPricing categoryPricing) {
		super();
		this.id = -1;
		this.brand = Objects.requireNonNull(brand, "Brand must not be null");
		this.description = StringUtils.requireNonEmpty(modelDescription, "Model description must not be null");
		this.categoryPricing = Objects.requireNonNull(categoryPricing, "Category must not be null");
	}
	
	// Simple constructor for persistence and serializers
	protected Model() {
		super();
		this.id = null;
		this.brand = null;
		this.description = "";
		this.categoryPricing = null;
	}
	
	public Brand getBrand() {
		return brand;
	}
	public CategoryPricing getCategoryPricing() {
		return categoryPricing;
	}
	public String getDescription() {
		return description;
	}
	public Integer getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return String.format("%s %s", this.brand, this.description);
	}
	
	@Override
	public int hashCode() {
		return this.id.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		Model other = (Model) obj;
		return this.id.equals(other.id);
	}

}
