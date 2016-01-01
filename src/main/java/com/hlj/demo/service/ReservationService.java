package com.hlj.demo.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hlj.demo.dao.ReservationDao;
import com.hlj.demo.dao.UserDao;
import com.hlj.demo.entity.Reservation;
import com.hlj.demo.entity.WikiUser;
import com.hlj.demo.web.ReservationDTO;
import com.hlj.utils.BeanMapper;
@Service
public class ReservationService {
	@Autowired
	private ReservationDao reservationDao;
	@Autowired
	private UserDao userDao;
	
	public ResponseEntity<?> addReservation(ReservationDTO dto) {
		//判断是否为有效用户
		WikiUser user = userDao.findByName(dto.getName());
		if(user==null)
			return ResponseEntity.badRequest().body("无效用户名");
		//判断该用户该日是否已加一了
		Reservation reservation = reservationDao.findByNameAndCreatedDate(dto.getName(),LocalDate.now());
		if(reservation != null){
			return ResponseEntity.badRequest().body(String.format("%s该日已加一", dto.getName()));
		}
		//若该日未加一 加一
		Reservation entity = BeanMapper.map(dto, Reservation.class);
		entity.setCreatedDate(LocalDate.now());
		reservationDao.save(entity);
		
		return ResponseEntity.ok(entity);
	}
}
