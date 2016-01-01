package com.hlj.demo.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.hlj.demo.entity.WikiUser;

public interface UserDao extends CrudRepository<WikiUser, Integer> {
	public List<WikiUser> findAll();

	public WikiUser findByName(String name);
}
