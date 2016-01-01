package com.hlj.demo.web;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hlj.demo.dao.ReservationDao;
import com.hlj.demo.entity.Reservation;
import com.hlj.demo.service.ReservationService;
import com.hlj.utils.BeanMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


/**
 * 订餐
 * @author zhuguowei
 *
 */
@RestController
@RequestMapping("/reservations")
@Api(value = "/reservations")
public class ReservationController {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private ReservationDao reservationDao;
	@Autowired
	private ReservationService service;
	/**
	 * 添加一个订餐信息 即加1
	 * @param dto
	 */
	/**
	 * @param dto
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST)
	@ApiOperation( value = "订餐加一", notes = "先判断是否为有效用户，再判断该日该用户是否已加一了", response = Reservation.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "无效用户名或已加一")} )
	public ResponseEntity<?> addReservation(@Valid @RequestBody ReservationDTO dto){
		logger.info("{} 加一", dto.getName());
		return service.addReservation(dto);
	}
	
	@RequestMapping(value="/{name:\\w+}/plus1",method=RequestMethod.GET)
	@ApiOperation( value = "订餐加一", notes = "先判断是否为有效用户，再判断该日该用户是否已加一了", response = Reservation.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "无效用户名或已加一")} )
	public ResponseEntity<?> plus1(@ApiParam(value="花名",required=true) @PathVariable String name,@ApiParam(value="备注") @RequestParam(required=false) String comment){
		logger.info("{} 加一", name);
		return service.addReservation(new ReservationDTO(name,comment));
	}
	/**
	 * 统计今日订餐人数
	 * @return
	 */
	@RequestMapping(value="/count",method=RequestMethod.GET)
	@ApiOperation( value = "统计今日订餐人数", response = Long.class)
	public ResponseEntity<?> statsCount(){
		long count = reservationDao.countByCreatedDate(LocalDate.now());
		return ResponseEntity.ok(count);
	}
	/**
	 * 列出今日加一清单
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET)
	@ApiOperation( value = "列出今日订餐人员清单", response = Reservation.class, responseContainer="List")
	public ResponseEntity<?> getTodayReservations(){
		List<Reservation> list = reservationDao.findByCreatedDate(LocalDate.now());
		return ResponseEntity.ok(list);
	}
	@RequestMapping(value="/{id:\\d+}",method=RequestMethod.PUT)
	@ApiOperation( value = "修改备注信息")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "Not Found"),@ApiResponse(code = 204, message = "No Content")} )
	public ResponseEntity<?> updateReservation(@PathVariable long id, @Valid @RequestBody ReservationDTO dto){
		
		Reservation result = reservationDao.findOne(id);
		if(result==null)
			return ResponseEntity.notFound().build();
		BeanMapper.copy(dto, result);
		reservationDao.save(result);
		return ResponseEntity.noContent().build();
	}
	@RequestMapping(value="/{name:\\w+}/minus1",method=RequestMethod.DELETE)
	@ApiOperation( value = "减一")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "Not Found"),@ApiResponse(code = 204, message = "No Content")} )
	public ResponseEntity<?> deleteReservationByName(@ApiParam(value="花名",required=true) @PathVariable String name){
		logger.info("{} 减一", name);		
		Reservation result = reservationDao.findByNameAndCreatedDate(name, LocalDate.now());
		if(result==null)
			return ResponseEntity.notFound().build();
		reservationDao.delete(result.getId());
		return ResponseEntity.noContent().build();
	}
}
