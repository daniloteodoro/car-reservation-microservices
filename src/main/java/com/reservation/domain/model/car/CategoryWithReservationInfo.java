package com.reservation.domain.model.car;


/***
 * One projection of com.reservation.domain.model.car.Category containing information about total vehicles reserved and total vehicles available
 */
public class CategoryWithReservationInfo {

    public static final String CATEGORY_AVAILABILITY_MAPPING = "CategoryAvailabilityMapping";
    public static final String GET_AVAILABLE_CATEGORY_BASED_ON_RESERVATION =
            "select c.category_type, c.full_insurance, c.price_per_day, c.standard_insurance, coalesce(available.total, 0) as total, coalesce(reservations.amount, 0) as total_reserved \n" +
            "  from category_pricing c \n" +
            "  left join (select m.category_id, count(*) as total \n" +
            "               from car c \n" +
            "               inner join model m on m.id = c.model_id \n" +
            "               group by m.category_id) available on available.category_id = c.category_type \n" +
            "  left join (select r.category_type, count(*) as amount \n" +
            "               from reservation r \n" +
            "              where ((:START_DATE between r.pickupdatetime and r.dropoffdatetime) or \n" +
            "                     (:END_DATE between r.pickupdatetime and r.dropoffdatetime) or \n" +
            "                     (:START_DATE <= r.pickupdatetime and :END_DATE >= r.dropoffdatetime)) \n" +
            "              group by r.category_type) reservations on reservations.category_type = c.category_type \n"+
            " order by c.category_type ";

    private final CategoryPricing categoryPricing;
    private final Integer total;
    private final Integer totalReserved;


    public CategoryWithReservationInfo(String type, Double pricePerDay, Double standardInsurance, Double fullInsurance, Integer total, Integer totalReserved) {
        this.categoryPricing = new CategoryPricing(CategoryType.valueOf(type), new CarPrice(pricePerDay), new StandardInsurancePrice(standardInsurance), new FullInsurancePrice(fullInsurance));
        this.total = total;
        this.totalReserved = totalReserved;
    }

    public CategoryPricing getCategoryPricing() {
        return categoryPricing;
    }
    public Integer getTotal() {
        return total;
    }
    public Integer getTotalReserved() {
        return totalReserved;
    }

}
