package com.sagasoftech.basics.eazybank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sagasoftech.basics.eazybank.model.Customer;
import com.sagasoftech.basics.eazybank.repository.CustomerRepository;

@RestController
public class LoginController {
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	/*
	 * Sample JSON
	 * 
	   {
	    "email": "sam@example.com",
	    "pwd": "123456",
	    "role": "user"
	   }
	 */
	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody Customer customer){
		Customer savedCustomer = null;
		ResponseEntity<String> response = null;
		try {
			String hashPwd = passwordEncoder.encode(customer.getPwd());
			customer.setPwd(hashPwd);
			
			savedCustomer = customerRepository.save(customer);
			if(savedCustomer.getId() > 0) {
				response = ResponseEntity
						.status(HttpStatus.CREATED)
						.body("Given user details are successfully registered");
			}
		}catch(Exception ex) {
			response = ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An exception occured due to " + ex.getMessage());
		}
		return response;
	}

}
