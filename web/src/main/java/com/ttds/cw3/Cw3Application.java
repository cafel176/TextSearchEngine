package com.ttds.cw3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Cw3Application
{
	public static void main(String[] args)
	{
		SpringApplication.run(Cw3Application.class, args);
	}
}
