package com.mytech.restaurantportal.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.mytech.restaurantportal.filters.AuthTokenFilter;
import com.mytech.restaurantportal.helpers.AppConstant;
import com.mytech.restaurantportal.security.handlers.OnAuthenticationFailedHandler;
import com.mytech.restaurantportal.security.handlers.OnAuthenticationSuccessHandler;
import com.mytech.restaurantportal.security.jwt.AuthEntryPointJwt;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
//@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Autowired
	private AppUserDetailsService userDetailsService;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((authorize) -> authorize
				.requestMatchers("/apis/v1/ingredients/search").permitAll()
			    .requestMatchers("/apis/v1/chef/dishes").hasAnyAuthority(AppConstant.ADMIN, AppConstant.CHEF)  // Cho phép ADMIN và CHEF xem
			    .requestMatchers("/apis/v1/chef/updateStatus").hasAuthority(AppConstant.CHEF)  // Chỉ cho CHEF cập nhật
			    .requestMatchers("/chef/**").hasAuthority(AppConstant.CHEF)  // Các trang quản lý của CHEF
			    .requestMatchers("/apis/v1/signin", "/apis/test/**", "/files/**", "/forgot_password", "/reset_password", "/apis/v1/users",
			            "/apis/v1/ingredients", "/apis/v1/ingredients/**","/apis/v1/workschedules/**",
			            "/apis/v1/posts/search", "/apis/v1/posts/**", "/apis/v1/contacts/**","/apis/v1/bookingtable/**","/apis/v1/tables","/apis/v1/order/**","/apis/v1/staffchedules/**").permitAll()
			    .anyRequest().authenticated())
			    .formLogin(login -> login.permitAll().loginPage("/login").usernameParameter("email")
			            .passwordParameter("password").loginProcessingUrl("/dologin")
			            .successHandler(new OnAuthenticationSuccessHandler())
			            .failureHandler(new OnAuthenticationFailedHandler()))
			    .logout(logout -> logout.permitAll()).exceptionHandling(handling -> handling.accessDeniedPage("/403"))
			    .rememberMe(config -> config.key("asdfghjklmnbvcxz").tokenValiditySeconds(7 * 24 * 60 * 60))
			    .exceptionHandling(exception -> {
			        exception.authenticationEntryPoint(unauthorizedHandler);
			    }).csrf(csrf -> csrf.disable());


	    http.authenticationProvider(authenticationProvider());
	    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

	    return http.build();
	}


	@Bean
	AuthenticationManager authenticationManager() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return new ProviderManager(authProvider);
	}

//	@Bean
//	UserDetailsService userDetailsService() {
//		return new AppUserDetailsService();
//	}

	@Bean
	WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/assets/**","/files/**");     
	          
	           
	           
	           
	           
	}
}