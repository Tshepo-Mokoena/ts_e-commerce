package com.tshepo.exception;

import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@NoArgsConstructor
public class LinkExpiredOrNotValidException extends RuntimeException {	
	public LinkExpiredOrNotValidException(String message) {
		super(message);
	}
}
