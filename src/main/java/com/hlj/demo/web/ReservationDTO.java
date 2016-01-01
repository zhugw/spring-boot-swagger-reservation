package com.hlj.demo.web;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReservationDTO {
	@NotBlank
	@ApiModelProperty(allowableValues = "花名")
	private String name;//花名
	@ApiModelProperty(allowableValues = "订餐备注")
	private String comment; //备注
	public ReservationDTO() {
	}
	public ReservationDTO(String name, String comment) {
		this.name = name;
		this.comment = comment;
	}
	
	
}
