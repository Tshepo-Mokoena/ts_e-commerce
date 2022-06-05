package com.tshepo.exception;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.tshepo.exception.entity.HttpResponse;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ExceptionHandling extends ResponseEntityExceptionHandler{
	
	private static final String INTERNAL_SERVER_ERROR_MSG = "An error occurred while processing the request";
	
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<HttpResponse> handleAllException(Exception exception)
	{
    	log.error(exception.getMessage());
		return createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MSG);
	}
		
	@ExceptionHandler(ItemExistException.class)
	public ResponseEntity<HttpResponse> handleHttpMediaTypeNotSupportedException(ItemExistException exception)
	{
    	log.error(exception.getMessage());
		return createHttpResponse(HttpStatus.CONFLICT, exception.getMessage());
	}
	
	@ExceptionHandler(ItemNotFoundException.class)
	public ResponseEntity<HttpResponse> handleNoProductsFoundException(ItemNotFoundException exception)
	{
    	log.error(exception.getMessage());
		return createHttpResponse(HttpStatus.NOT_FOUND, exception.getMessage());
	}
	
    @Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) 
	{
		HttpResponse httpResponse = 
				new HttpResponse(new Date(),status.value(),ex.getMessage(),ex.getBindingResult().toString());
		return new ResponseEntity<>(httpResponse, HttpStatus.BAD_REQUEST);
	}
	
	private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message) {
    	HttpResponse httpResponse = 
    			new HttpResponse(new Date(), httpStatus.value(), httpStatus.getReasonPhrase().toUpperCase(), message);
        return new ResponseEntity<>(httpResponse, httpStatus);
    }

}
