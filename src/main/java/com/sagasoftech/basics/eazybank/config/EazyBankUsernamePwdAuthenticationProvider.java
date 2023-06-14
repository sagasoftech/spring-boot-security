package com.sagasoftech.basics.eazybank.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.sagasoftech.basics.eazybank.model.Customer;
import com.sagasoftech.basics.eazybank.repository.CustomerRepository;

@Component
public class EazyBankUsernamePwdAuthenticationProvider implements AuthenticationProvider{

	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String userName = authentication.getName();
		String pwd = authentication.getCredentials().toString();
		List<Customer> customer = customerRepository.findByEmail(userName);
		if(customer.size() > 0) {
			if(passwordEncoder.matches(pwd, customer.get(0).getPwd())) {
				List<GrantedAuthority> authorities = new ArrayList<>();
				authorities.add(new SimpleGrantedAuthority(customer.get(0).getRole()));
				return new UsernamePasswordAuthenticationToken(customer, pwd, authorities);
			}else {
				throw new BadCredentialsException("Invalid Passworrd!");
			}
		}else {
			throw new BadCredentialsException("No user registered with this details!");
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}

}
