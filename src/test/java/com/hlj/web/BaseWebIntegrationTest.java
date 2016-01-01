package com.hlj.web;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hlj.ReservationApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ReservationApplication.class)
@WebIntegrationTest(randomPort = true)	
@ActiveProfiles("localtest")
@Sql({"/test-schema.sql","/test-user-data.sql"})
public abstract class BaseWebIntegrationTest {
	@Value("${local.server.port}")
    protected int port;
	protected TestRestTemplate template = new TestRestTemplate();
}
