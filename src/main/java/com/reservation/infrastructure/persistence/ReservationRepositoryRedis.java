package com.reservation.infrastructure.persistence;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import com.reservation.domain.model.reservation.Reservation;
import com.reservation.domain.model.reservation.ReservationNumber;
import com.reservation.domain.model.reservation.ReservationRepository;

// TODO: Change to JpaRepository
@Repository
public class ReservationRepositoryRedis implements ReservationRepository {
	
	private final ValueOperations<String, Reservation> redisCommand;
	
	
	public ReservationRepositoryRedis(@Autowired final RedisTemplate<String, Reservation> redisTemplate) {
		super();
		this.redisCommand = redisTemplate.opsForValue();
	}
	
	@Override
	public Optional<Reservation> findByNumber(ReservationNumber number) {
		return Optional.ofNullable(redisCommand.get(getReservationKey(number)));
	}
	
	@Override
	public void save(Reservation reservation) {
		redisCommand.set(getReservationKey(reservation.getReservationNumber()), reservation);
	}
	
	private String getReservationKey(ReservationNumber number) {
		return String.format("Reservation:%s", number);
	}
	
}







