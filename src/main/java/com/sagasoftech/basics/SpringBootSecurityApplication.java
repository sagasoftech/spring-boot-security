package com.sagasoftech.basics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
/*
 * This is optional for this project as the package to scan in inside our main package 
 * where spring boot application main class is present
 * However, it required if controller class are outside of the package of spring boot main class
 */
@ComponentScan("com.sagasoftech.basics.controller")
public class SpringBootSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootSecurityApplication.class, args);
	}

}
