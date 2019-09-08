package com.reservation.domain.model.car;


import com.reservation.domain.model.customer.Customer;
import com.reservation.domain.model.reservation.Reservation;

/***
 * One projection of com.reservation.domain.model.car.Category containing information about total vehicles reserved and total vehicles available
 */
public class CategoryWithReservationInfo {

    public static final String CATEGORY_AVAILABILITY_MAPPING = "CategoryAvailabilityMapping";
    public static final String GET_AVAILABLE_CATEGORY_BASED_ON_RESERVATION =
            "select c.category_type, c.full_insurance, c.price, c.standard_insurance, coalesce(available.total, 0) as total, coalesce(reservations.amount, 0) as total_reserved \n" +
                    "from category c\n" +
                    "left join (select m.category_id, count(*) as total\n" +
                    "from car c\n" +
                    "inner join model m on m.id = c.model_id\n" +
                    "group by m.category_id) available on available.category_id = c.category_type\n" +
                    "left join (select r.category_category_type, count(*) as amount\n" +
                    "from reservation r\n" +
                    "where ((:START_DATE between r.pickupdatetime and r.dropoffdatetime) or\n" +
                    "(:END_DATE between r.pickupdatetime and r.dropoffdatetime) or\n" +
                    "(:START_DATE <= r.pickupdatetime and :END_DATE >= r.dropoffdatetime))\n" +
                    "group by r.category_category_type) reservations\n" +
                    "on reservations.category_category_type = c.category_type\n"+
                    "order by c.category_type";

    private final Category category;
    private final Integer total;
    private final Integer totalReserved;


    public CategoryWithReservationInfo(String type, Double pricePerDay, Double standardInsurance, Double fullInsurance, Integer total, Integer totalReserved) {
        this.category = new Category(CategoryType.valueOf(type), new CarPrice(pricePerDay), new Price(standardInsurance), new Price(fullInsurance));
        this.total = total;
        this.totalReserved = totalReserved;
    }

    public Category getCategory() {
        return category;
    }

    public Integer getTotal() {
        return total;
    }

    public Integer getTotalReserved() {
        return totalReserved;
    }

    public Reservation reserve(Customer visitor) {
        return null;
    }
}
