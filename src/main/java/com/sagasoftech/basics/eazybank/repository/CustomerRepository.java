package com.sagasoftech.basics.eazybank.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sagasoftech.basics.eazybank.model.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long>{

	List<Customer> findByEmail(String email);
	
	
}
