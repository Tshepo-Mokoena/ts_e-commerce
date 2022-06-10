package com.tshepo.util;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class Utilities {
	
	public static String generateUniqueNumericUUId()
	{
		return RandomStringUtils.randomNumeric(12);
	}
	
	public static String generateUniqueAlphaNumericUUId()
	{
		return RandomStringUtils.randomAlphanumeric(12);
	}
	
	public static String getExtension(String originalFileName) {
        return StringUtils.getFilenameExtension(originalFileName);
    }
	
	public static boolean fileExtensionValidator(String originalFileName) 
	{
		String getFileExtension = getExtension(originalFileName);
		List<String> extensionList = List.of("png" , "jpg" , "jpeg", "gif", "svg");
		for (String extensionString : extensionList)
		{
			if (getFileExtension.contains(extensionString))
				return true;				
		}
		return false;
	}	
	
	public static boolean isValidPassword(String password)
	{
		if(password.length() < 8) return false;
		boolean hasDigit = false;
		boolean hasUpperCase = false;
		boolean hasLowerCase = false;
		char c;
		for(int i = 0; i < password.length(); i++ )
		{
			 c = password.charAt(i);
			if(Character.isDigit(c))
				hasDigit = true;
			else if (Character.isUpperCase(c))
				hasUpperCase = true;
			else if(Character.isLowerCase(c))
				hasLowerCase = true;
			if(hasDigit && hasLowerCase && hasUpperCase)
				return true;
		}
		return false;
	}
	
}
