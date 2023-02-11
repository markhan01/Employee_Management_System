package org.markhan.springboot.thymeleafdemo.config;

import org.markhan.springboot.thymeleafdemo.service.SecurityUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private SecurityUserDetailsService userDetailsService;
	
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
		.antMatchers("/employees/showForm*").hasAnyRole("MANAGER", "ADMIN")
		.antMatchers("/employees/save*").hasAnyRole("MANAGER", "ADMIN")
		.antMatchers("/employees/delete").hasRole("ADMIN")
		.antMatchers("/employees/**").hasAnyRole("EMPLOYEE", "MANAGER", "ADMIN")
		.antMatchers("/resources/**").permitAll()
				.and()
				.formLogin()
					.loginPage("/login")
					.defaultSuccessUrl("/employees/list")
					.permitAll()
				.and()
				.logout().invalidateHttpSession(true)
				.clearAuthentication(true).permitAll()
				.and()
				.exceptionHandling().accessDeniedPage("/access-denied");
	}
	
	//bcrypt bean definition
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	//authenticationProvider bean definition
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userDetailsService); //set the custom user details service
		auth.setPasswordEncoder(passwordEncoder()); //set the password encoder - bcrypt
		return auth;
	}

}
