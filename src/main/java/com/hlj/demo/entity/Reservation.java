package com.hlj.demo.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import lombok.Data;

@Data
@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"name","createdDate"})})
public class Reservation {
	@Id
	@GeneratedValue
	private Long id;
	
	private String name;//花名
	private String comment; //备注
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDate createdDate;
}
