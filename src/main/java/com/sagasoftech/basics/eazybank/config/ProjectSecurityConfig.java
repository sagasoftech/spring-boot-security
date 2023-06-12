package com.sagasoftech.basics.eazybank.config;

import static org.springframework.security.config.Customizer.withDefaults;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ProjectSecurityConfig {

	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		
		//http.authorizeHttpRequests((requests) -> requests.anyRequest().denyAll());
		//http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());
			
		http.csrf().disable()
			.authorizeHttpRequests((requests) -> requests
			.requestMatchers("/myAccount", "/myBalance","/myLoans","/myCards").authenticated()
			.requestMatchers("/notices","/contact", "/register").permitAll());
		http.formLogin(withDefaults());
		http.httpBasic(withDefaults());
		
		return http.build();
	}
	
//	@Bean
//	public InMemoryUserDetailsManager userDetailsService() {
//		
//		/*
//		 * Approach 1 where we use withDefaultPasswordEncoder() method 
//		 * while creating the user details
//		 */
//		/*UserDetails admin = User.withDefaultPasswordEncoder()
//				.username("admin")
//				.password("123456")
//				.authorities("admin")
//				.build();
//
//		UserDetails user = User.withDefaultPasswordEncoder()
//				.username("user")
//				.password("123456")
//				.authorities("read")
//				.build();
//		
//		return new InMemoryUserDetailsManager(admin, user);*/
//		
//		/*
//		 * Approach 2 where we use NoOpPasswordEncoder 
//		 * while creating the user details
//		 */
//		UserDetails admin = User.withUsername("admin")
//				.password("123456")
//				.authorities("admin")
//				.build();
//
//		UserDetails user = User.withUsername("user")
//				.password("123456")
//				.authorities("read")
//				.build();
//		
//		return new InMemoryUserDetailsManager(admin, user);
//	}
	
	/*
	@Bean
	public UserDetailsService userDetailsService(DataSource dataSource) {
		return new JdbcUserDetailsManager(dataSource);
	}
	*/
	
	/*
	 * This will return PasswordEncoder that will get used for application
	 * If not added, it will throw below error, because our customized InMemoryUserDetailsManager does not have encoder now
	 * 'There is no PasswordEncoder mapped'
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
