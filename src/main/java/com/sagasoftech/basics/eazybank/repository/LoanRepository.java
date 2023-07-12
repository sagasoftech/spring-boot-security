package com.sagasoftech.basics.eazybank.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import com.sagasoftech.basics.eazybank.model.Loans;

@Repository
public interface LoanRepository extends CrudRepository<Loans, Long> {
	
	/*
	 * Method level authorization
	 * 
	 * Only users who have role as USER can access this method
	 */
	@PreAuthorize("hasRole('USER')")
	List<Loans> findByCustomerIdOrderByStartDtDesc(int customerId);

}
