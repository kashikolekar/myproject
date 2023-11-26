package com.spring.smart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class MyConfig  {

	@Bean
	public UserDetailsService getUserDetilsService()
	{
		return new UserDetilsServiceImpl(); 
		
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider()
	{
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();	
		daoAuthenticationProvider.setUserDetailsService(this.getUserDetilsService());
		daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder());
	
		return daoAuthenticationProvider;
	}
	
	
	
	@Bean
	public SecurityFilterChain chain(HttpSecurity http , AuthenticationManagerBuilder auth) throws Exception
	{
		/*
		 * http .authorizeRequests(authorizeRequests -> authorizeRequests
		 * .requestMatchers("/admin").hasRole("ADMIN")
		 * .requestMatchers("/user").hasRole("USER") .requestMatchers("/**").permitAll()
		 * ) .formLogin(Customizer.withDefaults());
		 * 
		 * // Disable CSRF for simplicity http.csrf().disable();
		 */
          auth.authenticationProvider(authenticationProvider());
	//	http.csrf().disable().authorizeRequests().requestMatchers("/admin/**").hasRole("ADMIN"). requestMatchers("/User/**").hasRole("USER").requestMatchers("/**").permitAll().and().formLogin();
	
	//System.out.println(http.authorizeRequests().requestMatchers("/user/**").hasRole("USER"));
	http.authorizeRequests().requestMatchers("/user/**").hasRole("USER").requestMatchers("/admin/**").hasRole("ADMIN").requestMatchers("/**").permitAll().and().formLogin()
	.loginPage("/singin")
	.loginProcessingUrl("/dologin")
	.defaultSuccessUrl("/user/index")
	.failureUrl("/login_fail")
	.and().csrf().disable();
	
	//sop("h1"+)
          return http.build();
		
	}
	

}

