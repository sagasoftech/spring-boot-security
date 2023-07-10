package com.sagasoftech.basics.eazybank.config;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.sagasoftech.basics.eazybank.filter.AuthoritiesLoggingAfterFilter;
import com.sagasoftech.basics.eazybank.filter.AuthoritiesLoggingAtFilter;
import com.sagasoftech.basics.eazybank.filter.CsrfCookieFilter;
import com.sagasoftech.basics.eazybank.filter.RequestValidationBeforeFilter;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class ProjectSecurityConfig {

	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		
		//http.authorizeHttpRequests((requests) -> requests.anyRequest().denyAll());
		//http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());
		
		/*
		 * Get CSRF token value as an Header and Cookies to the UI application  
		 */
		CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
		requestHandler.setCsrfRequestAttributeName("_csrf");
			/*
			 * Request to Spring Security to always create JSESSIONID after initial login is completed.
			 * Without this, we need to share credentials every time we try to access the secured API. 
			 */
		http/*.securityContext().requireExplicitSave(false)
			.and().sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))*/
			/* To avoid generating JSESSIONID or any HTTP Sessions*/
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.cors().configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                config.setAllowedMethods(Collections.singletonList("*"));
                config.setAllowCredentials(true);
                config.setAllowedHeaders(Collections.singletonList("*"));
                /*Expose Header from backend application to UI application */
                config.setExposedHeaders(Arrays.asList("Authorization"));
                config.setMaxAge(3600L);
                return config;
            }}).and().csrf((csrf)-> csrf.csrfTokenRequestHandler(requestHandler).ignoringRequestMatchers("/contact", "/register")
            		.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
					/*
					 * CookieCsrfTokenRepository - persists the CSRF token in a cookie named"XSRF-TOKEN" 
					 * and reads from the header "X-XSRF-TOKEN"
					 * 
					 * HttpOnlyFalse so that JavaScript code deployed in angular application can read the cookie
					 * 
					 */
				/*
				 * Filter to send cookies and header value to the UI application after initial login
				 */
				.addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
				.addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
				.addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class)
				.addFilterAt(new AuthoritiesLoggingAtFilter(), BasicAuthenticationFilter.class)
				.authorizeHttpRequests((requests) -> requests
				/*
				.requestMatchers("/myAccount").hasAuthority("VIEWACCOUNT")
                .requestMatchers("/myBalance").hasAnyAuthority("VIEWACCOUNT","VIEWBALANCE")
                .requestMatchers("/myLoans").hasAuthority("VIEWLOANS")
                .requestMatchers("/myCards").hasAuthority("VIEWCARDS")
                */
                .requestMatchers("/myAccount").hasRole("USER")
                .requestMatchers("/myBalance").hasAnyRole("USER","ADMIN")
                .requestMatchers("/myLoans").hasRole("USER")
                .requestMatchers("/myCards").hasRole("USER")
				.requestMatchers("/user").authenticated()
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
