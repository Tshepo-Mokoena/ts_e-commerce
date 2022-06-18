package com.tshepo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.tshepo.persistence.auth.Role;
import com.tshepo.security.jwt.JwtAuthorizationFilter;
import com.tshepo.service.impl.AccountSecurityService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Value("${authentication.internal-api-key}")
	private String internalApiKey;
	
	@Autowired
	private AccountSecurityService accountSecurityService;
	
	private PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception 
	{
		auth.userDetailsService(accountSecurityService).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception 
	{	
		http.cors();
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.authorizeRequests()
			.antMatchers("/api/ts-ecommerce/authentication/**").permitAll()
			.antMatchers(HttpMethod.GET, "/api/ts-ecommerce/products/**").permitAll()
			.antMatchers(HttpMethod.POST,"/api/ts-ecommerce/products/**").hasRole(Role.ADMIN.name())
			.antMatchers(HttpMethod.GET,"/api/ts-ecommerce/products/all").hasRole(Role.ADMIN.name())
			.antMatchers(HttpMethod.PUT,"/api/ts-ecommerce/manager-operations/**").hasRole(Role.SYSTEM_MANAGER.name())
			.anyRequest().authenticated();		
	
		http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(managersApiAuthenticationFilter(), JwtAuthorizationFilter.class);
		
	}	
	
	@Bean
	public ManagersApiAuthenticationFilter managersApiAuthenticationFilter()
	{		
		return new ManagersApiAuthenticationFilter(internalApiKey);
	}
	
	@Bean
	public JwtAuthorizationFilter jwtAuthorizationFilter()
	{
		return new JwtAuthorizationFilter();
	}

	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception 
	{
		return super.authenticationManagerBean();
	}	
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer()
				{
					@Override
					public void addCorsMappings(CorsRegistry registry) 
					{
						registry.addMapping("/**").allowedOrigins("*").allowedMethods("*");
					}
			
				};		
	}
	

}