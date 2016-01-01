package com.hlj.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hlj.ReservationApplication;
import com.hlj.demo.entity.Reservation;
import com.hlj.demo.web.ReservationController;
import com.hlj.demo.web.ReservationDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ReservationApplication.class)
@ActiveProfiles("localtest")
@Sql({"/test-schema.sql","/test-user-data.sql"})
public class ReservationControllerTest {
	@Autowired
	private ReservationController controller;
	@Test
	public void test_add_reservation(){
		ReservationDTO dto = new ReservationDTO();
		dto.setName("test_add_reservation");
		dto.setComment("不要辣");
		ResponseEntity<Reservation> result = (ResponseEntity<Reservation>) controller.addReservation(dto);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody().getId()).isNotNull();
	}
	@Test
	public void test_repeat_add_reservation(){
		ReservationDTO dto = new ReservationDTO();
		dto.setName("test_repeat_add_reservation");
		dto.setComment("bar");
		controller.addReservation(dto);
		
		ResponseEntity<String> result = (ResponseEntity<String>) controller.addReservation(dto);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(result.getBody()).isEqualTo(String.format("%s该日已加一", dto.getName(),LocalDate.now()));
		
		
	}
}
