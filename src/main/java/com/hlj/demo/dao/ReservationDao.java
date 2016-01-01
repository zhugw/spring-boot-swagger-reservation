package com.hlj.demo.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.hlj.demo.entity.Reservation;

public interface ReservationDao extends CrudRepository<Reservation, Long> {

	Reservation findByNameAndCreatedDate(String name, LocalDate now);

	long countByCreatedDate(LocalDate now);

	List<Reservation> findByCreatedDate(LocalDate now);

}
