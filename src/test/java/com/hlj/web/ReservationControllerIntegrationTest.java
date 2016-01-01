package com.hlj.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hlj.demo.dao.ReservationDao;
import com.hlj.demo.entity.Reservation;
import com.hlj.demo.web.ReservationDTO;


public class ReservationControllerIntegrationTest extends BaseWebIntegrationTest{
	final String add_reservation_url_format = "http://localhost:%s/reservations";
	final String plus1_url_format = "http://localhost:%s/reservations/%s/plus1?comment=%s";
	final String stats_count_url_format = "http://localhost:%s/reservations/count";
	final String get_today_reservations_url_format = "http://localhost:%s/reservations";
	final String update_reservation_url_format = "http://localhost:%s/reservations/%d";
	final String delete_reservation_url_format = "http://localhost:%s/reservations/%d";
	final String delete_reservation_by_name_url_format = "http://localhost:%s/reservations/%s/minus1";
	@Autowired
	private ReservationDao dao;
	@Test
	public void test_add_reservation(){
		String add_reservation_url = String.format(add_reservation_url_format, port);
		ReservationDTO dto = new ReservationDTO();
		dto.setName("test_add_reservation");
		dto.setComment("不要辣");
		ResponseEntity<Reservation> response = template.postForEntity(add_reservation_url, dto , Reservation.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getId()).isNotNull();
	}
	@Test
	public void test_add_reservation_and_name_is_blank(){
		String add_reservation_url = String.format(add_reservation_url_format, port);
		ReservationDTO dto = new ReservationDTO();
		dto.setComment("不要辣");
		ResponseEntity<Reservation> response = template.postForEntity(add_reservation_url, dto , Reservation.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		
		dto.setName("");
		response = template.postForEntity(add_reservation_url, dto , Reservation.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		
		dto.setName("    ");
		response = template.postForEntity(add_reservation_url, dto , Reservation.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}
	@Test
	public void test_add_reservation_and_name_is_invalid(){
		String add_reservation_url = String.format(add_reservation_url_format, port);
		ReservationDTO dto = new ReservationDTO();
		dto.setName("non_exist_user");
		dto.setComment("foobar");
		ResponseEntity<String> response = template.postForEntity(add_reservation_url, dto , String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).isEqualTo("无效用户名");
		
	}
	@Test
	public void test_repeat_add_reservation(){
		String add_reservation_url = String.format(add_reservation_url_format, port);
		ReservationDTO dto = new ReservationDTO();
		dto.setName("test_repeat_add_reservation");
		dto.setComment("bar");
		template.postForEntity(add_reservation_url, dto , Reservation.class);
		ResponseEntity<String> response = template.postForEntity(add_reservation_url, dto , String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).isEqualTo(String.format("%s该日已加一", dto.getName(),LocalDate.now()));
	}
	@Test
	public void test_plus1(){
		String name = "test_plus1";
		String coment = "不要辣";
		String plus1_url = String.format(plus1_url_format, port,name,coment);
		ResponseEntity<Reservation> response =template.getForEntity(plus1_url, Reservation.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getId()).isNotNull();
	}
	@Test
	public void test_statsCount(){
		String statsCountUrl = String.format(stats_count_url_format, port);
		ResponseEntity<Long> response = template.getForEntity(statsCountUrl,  Long.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo(dao.countByCreatedDate(LocalDate.now()));
	}
	@Test
	public void test_getTodayReservations(){
		String get_today_reservations_url = String.format(get_today_reservations_url_format, port);
		ResponseEntity<List> response = template.getForEntity(get_today_reservations_url,  List.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().size()).isEqualTo((int)dao.countByCreatedDate(LocalDate.now()));
	}
	@Test
	public void test_updateReservation(){
		String add_reservation_url = String.format(add_reservation_url_format, port);
		ReservationDTO dto = new ReservationDTO();
		dto.setName("test_updateReservation");
		dto.setComment("foo");
		ResponseEntity<Reservation> response = template.postForEntity(add_reservation_url, dto , Reservation.class);
		long id = response.getBody().getId();
		
		String update_reservation_url = String.format(update_reservation_url_format, port,id);
		dto.setComment("bar");
		template.put(update_reservation_url, dto);
		
		Reservation reservation = dao.findOne(id);
		assertThat(reservation.getComment()).isEqualTo(dto.getComment());
	}
	
	@Test
	public void test_deleteReservation_by_name(){
		String add_reservation_url = String.format(add_reservation_url_format, port);
		ReservationDTO dto = new ReservationDTO();
		dto.setName("test_deleteReservation_by_name");
		dto.setComment("foo");
		ResponseEntity<Reservation> response = template.postForEntity(add_reservation_url, dto , Reservation.class);
		long id = response.getBody().getId();
		
		String delete_reservation_url = String.format(delete_reservation_by_name_url_format, port,dto.getName());
		template.delete(delete_reservation_url);
		
		Reservation reservation = dao.findOne(id);
		assertThat(reservation).isNull();
	}
}
