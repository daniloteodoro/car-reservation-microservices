package com.reservation.application.dto;


public class CreateReservationDto {

	private final String origin;
	private final String start;
	private final String destiny;
	private final String finish;
	private final String categoryType;


	public CreateReservationDto(String origin, String start, String destiny, String finish, String categoryType) {
		this.origin = origin;
		this.start = start;
		this.destiny = destiny;
		this.finish = finish;
		this.categoryType = categoryType;
	}

	public String getOrigin() {
		return origin;
	}

	public String getStart() {
		return start;
	}

	public String getDestiny() {
		return destiny;
	}

	public String getFinish() {
		return finish;
	}

	public String getCategoryType() {
		return categoryType;
	}
}