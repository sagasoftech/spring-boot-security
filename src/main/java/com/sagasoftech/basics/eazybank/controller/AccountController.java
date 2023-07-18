package com.sagasoftech.basics.eazybank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sagasoftech.basics.eazybank.model.Accounts;
import com.sagasoftech.basics.eazybank.model.Customer;
import com.sagasoftech.basics.eazybank.repository.AccountsRepository;
import com.sagasoftech.basics.eazybank.repository.CustomerRepository;

@RestController
public class AccountController {

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private CustomerRepository customerRepository;
    
    public Accounts getAccountDetails(@RequestParam String email) {
        List<Customer> customers = customerRepository.findByEmail(email);
        if (customers != null && !customers.isEmpty()) {
            Accounts accounts = accountsRepository.findByCustomerId(customers.get(0).getId());
            if (accounts != null) {
                return accounts;
            }
        }
        return null;
    }

}
