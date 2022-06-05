package com.tshepo.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Utilities {
	
	@Bean
	public static String generateUniqueNumericUUId()
	{
		return RandomStringUtils.randomNumeric(12);
	}
	
	@Bean
	public static String generateUniqueAlphaNumericUUId()
	{
		return RandomStringUtils.randomAlphanumeric(12);
	}

}
