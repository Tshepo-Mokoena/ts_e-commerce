package com.tshepo.exception.entity;

import java.util.Date;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HttpResponse {
	
	private Date timeStamp;
	
    private int httpStatusCode;
        
    private String reason;
    
    private String message;

}
