package com.tshepo.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConstants {
		
	@Value("${app.home}")
	private String homePage;
	
	@Value("${app.confirmation}")
	private String confirmation;
	
	@Value("${app.domain}")
	private String appDomain;
	
	public String appConfirmUrl() 
	{
		return appDomain + confirmation;
	}
	
	public String appUrl() 
	{
		return appDomain;
	}
	
	public String appHomePageUrl() 
	{
		return appDomain + homePage;
	}

}
