package com.reservation.domain.model.reservation;

import com.fasterxml.jackson.annotation.JsonValue;
import com.reservation.domain.model.shared.ValueObject;
import com.reservation.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OrderId implements ValueObject {

    private static final long serialVersionUID = 7241509359785710628L;

    @Column(name="order_id")
    private final String value;


    private OrderId(final String data) {
        super();
        this.value = StringUtils.requireNonEmpty(data, "Order id must not be null");
    }

    public static OrderId of(final String data) {
        return new OrderId(data);
    }

    // Simple constructor for persistence and serializers
    protected OrderId() {
        super();
        this.value = "";
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        OrderId other = (OrderId) obj;
        return value.equalsIgnoreCase(other.value);
    }

}
