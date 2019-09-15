package com.reservation.domain.model.reservation;

import com.reservation.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class City implements com.reservation.domain.model.shared.Entity {

    private static final long serialVersionUID = 84786101485256347L;
    private static final long UNKNOWN_CITY_ID = -1;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private final Long id;

    @NotNull
    @Column(nullable = false)
    private final String name;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private final Country country;


    public City(final Long id, final String name, final Country country) {
        super();
        this.id = Objects.requireNonNull(id, "City id must not be null");
        this.name = StringUtils.requireNonEmpty(name, "City name must not be null");
        this.country = Objects.requireNonNull(country, "City Country must not be null");
    }

    // Simple constructor for ORM and serializers
    protected City() {
        super();
        this.id = UNKNOWN_CITY_ID;
        this.name = "";
        this.country = Country.UNKNOWN;
    }

    public static City UNKNOWN = new City(UNKNOWN_CITY_ID, "Unknown", Country.UNKNOWN);

    public boolean isUnknown() {
        return this == UNKNOWN;
    }

    public Long getId() {
        return id;
    }
    public String getName() {
        return this.name;
    }
    public Country getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return String.format("%s-%s", this.name, this.country);
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

        City other = (City) obj;
        return this.id.equals(other.id);
    }

}
