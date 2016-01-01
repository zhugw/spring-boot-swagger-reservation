package com.hlj.demo.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
@Entity
@Data
public class WikiUser {
	@Id
	private String name; //wiki登录名
	
	private String email;
}
