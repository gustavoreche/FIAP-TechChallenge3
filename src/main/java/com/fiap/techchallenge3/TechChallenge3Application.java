package com.fiap.techchallenge3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class TechChallenge3Application {

	public static void main(String[] args) {
		SpringApplication.run(TechChallenge3Application.class, args);
	}

}
