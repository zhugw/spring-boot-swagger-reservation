package com.hlj.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hlj.demo.dao.UserDao;

public class WikiUserControllerIntegerationTest extends BaseWebIntegrationTest{
	
	@Autowired
	private UserDao dao;
	
	@Test
	public void test_getWikiUsers(){
		String url = String.format("http://localhost:%d/users", port);
		ResponseEntity<List> response = template.getForEntity(url,  List.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().size()).isEqualTo((int)dao.count());
	}
}
