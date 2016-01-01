package com.hlj.demo.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hlj.demo.dao.UserDao;
import com.hlj.demo.entity.WikiUser;

import io.swagger.annotations.ApiOperation;
@RequestMapping("/users")
@RestController
public class WikiUserController {
	@Autowired
	private UserDao userDao;
	@RequestMapping(method=RequestMethod.GET)
	@ApiOperation( value = "列出所有有效用户", response = WikiUser.class, responseContainer="List")
	public ResponseEntity<?> getWikiUsers(){
		List<WikiUser> findAll = userDao.findAll();
		return ResponseEntity.ok(findAll);
	}
}
