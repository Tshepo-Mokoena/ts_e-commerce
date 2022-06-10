package com.tshepo.exception;

import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@NoArgsConstructor
public class EmailExistException extends RuntimeException {
	public EmailExistException(String message) 
	{
		super(message);
	}
}
