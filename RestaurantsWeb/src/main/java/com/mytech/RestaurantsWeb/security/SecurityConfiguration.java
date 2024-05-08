

package com.mytech.RestaurantsWeb.security;

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

import com.mytech.RestaurantsWeb.security.handlers.OnAuthenticationFailedHandler;
import com.mytech.RestaurantsWeb.security.handlers.OnAuthenticationSuccess;
import com.mytech.RestaurantsWeb.security.handlers.OnAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity	//Fixed here
@EnableMethodSecurity(jsr250Enabled = true)
public class SecurityConfiguration {

	@Autowired
	private CustomerOAuth2UserService userService;

	@Autowired
	private AppUserDetailsService userDetailsService;

	@Autowired
	private OnAuthenticationSuccess onAuthenticationSuccess;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	

	@Bean
	AuthenticationManager authenticationManager() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return new ProviderManager(authProvider);
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		System.out.println("SecurityFilterChain--------------");
		http.authorizeHttpRequests((authorize) -> authorize
				.requestMatchers("/profile/**", "/checkout/**", "orders/**").authenticated()
				.requestMatchers("table/**","menu/**").hasAuthority("STAFF")
				.requestMatchers("/apis/v1/posts/**").permitAll()
				.anyRequest().permitAll())
				.formLogin(login -> login.permitAll().loginPage("/login").usernameParameter("email")
						.passwordParameter("password").loginProcessingUrl("/dologin")
						.successHandler(new OnAuthenticationSuccessHandler())
						.failureHandler(new OnAuthenticationFailedHandler()))
				.logout(logout -> logout.permitAll()).exceptionHandling(handling -> handling.accessDeniedPage("/403"))
				.oauth2Login(login -> login.permitAll().loginPage("/login")
						.userInfoEndpoint(endpoint -> endpoint.userService(userService))
						.successHandler(onAuthenticationSuccess))
				.rememberMe(config -> config.key("asdfghjklmnbvcxz").tokenValiditySeconds(7 * 24 * 60 * 60))
				
				;
		;

		return http.build();
	}


	@Bean
	WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/assets/**");    
	          
	           
	           
	           
	           
	}
}

