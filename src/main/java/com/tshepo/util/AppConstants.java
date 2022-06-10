package com.tshepo.util;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class AppConstants {
		
	private Environment environment;
	
	public String appConfirmUrl() 
	{
		return environment.getProperty("app.confirm.url");
	}
	
	public String appUrl() 
	{
		return environment.getProperty("app.home.url");
	}
	
	public String appEditAccountUrl() 
	{
		return environment.getProperty("app.edit-account.url");
	}

}
