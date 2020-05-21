package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class KtbackendApplication {

	private static ApplicationContext ctx;

	public static void main(String[] args) {
		ctx =  SpringApplication.run(KtbackendApplication.class, args);
	}
}