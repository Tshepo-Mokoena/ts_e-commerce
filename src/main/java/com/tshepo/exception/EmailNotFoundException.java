package com.tshepo.exception;

import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@NoArgsConstructor
public class EmailNotFoundException extends RuntimeException {
	public EmailNotFoundException(String message) 
	{
		super(message);
	}
}
