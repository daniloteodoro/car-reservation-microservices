package com.carrental.infrastructure.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import com.carrental.domain.model.reservation.Reservation;
import com.carrental.domain.model.reservation.ReservationNumber;
import com.carrental.domain.model.reservation.ReservationRepository;

@Repository
public class ReservationRepositoryRedis implements ReservationRepository {
	
	private final ValueOperations<String, Reservation> redisCommand;
	
	
	public ReservationRepositoryRedis(@Autowired final RedisTemplate<String, Reservation> redisTemplate) {
		super();
		this.redisCommand = redisTemplate.opsForValue();
	}
	
	@Override
	public Reservation findByNumber(ReservationNumber number) {
		return redisCommand.get(getReservationKey(number));
	}
	
	@Override
	public void save(Reservation reservation) {
		redisCommand.set(getReservationKey(reservation.getReservationNumber()), reservation);
	}
	
	private String getReservationKey(ReservationNumber number) {
		return String.format("Reservation:%s", number);
	}
	

}







