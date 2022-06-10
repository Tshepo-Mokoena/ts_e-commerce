package com.tshepo.exception;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
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
	private static final String FILE_UPLOAD_ERROR_MSG = "Only [ .png, .jpeg, .jpg, .gif ] are acceptable";
	private static final String ACCOUNT_LOCKED = "Your account has been locked. Please contact administration for help";
    private static final String INCORRECT_CREDENTIALS = "Email / password incorrect. Please try again";
    private static final String ACCOUNT_DISABLED = "Your account has been disabled. Please contact administration for help";
    private static final String NOT_ENOUGH_PERMISSION = "You do not have enough permission";
    private static final String TOKEN_EXPIRED_OR_INVALID = "Link Expired / not valid";
    public static final String ERROR_PATH = "/error";
    public static final String EMAIL_ALREADY_EXISTS = "Email already exists";
    public static final String EMAIL_NOT_FOUND = "Email not found";
    public static final String ACCOUNT_ENABLED = "Your account has been enabled.";
    	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<HttpResponse> handleAllException(Exception exception)
	{
    	log.error(exception.getMessage());
		return createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MSG);
	}
	
	@ExceptionHandler(FileUploadException.class)
	public ResponseEntity<HttpResponse> handleFileUploadException(FileUploadException exception)
	{
    	log.error(exception.getMessage());
		return createHttpResponse(HttpStatus.BAD_REQUEST, FILE_UPLOAD_ERROR_MSG);
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
    	    
    @ExceptionHandler(LinkExpiredOrNotValidException.class)
	public ResponseEntity<HttpResponse> handleLinkExpiredOrNotValidException(Exception exception)
	{
    	log.error(exception.getMessage());
		return createHttpResponse(HttpStatus.NOT_FOUND, TOKEN_EXPIRED_OR_INVALID);
	}
    
    @ExceptionHandler(DisabledException.class)
	public ResponseEntity<HttpResponse> handleDisabledException()
	{
		return createHttpResponse(HttpStatus.BAD_REQUEST, ACCOUNT_DISABLED);
	} 
    
    @ExceptionHandler(AccountEnabledException.class)
	public ResponseEntity<HttpResponse> handleAccountEnabledException()
	{
		return createHttpResponse(HttpStatus.BAD_REQUEST, ACCOUNT_ENABLED);
	}

    @ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<HttpResponse> handleBadCredentialsException()
	{
		return createHttpResponse(HttpStatus.BAD_REQUEST, INCORRECT_CREDENTIALS);
	} 
    
    @ExceptionHandler(InvalidEmailOrPasswordException.class)
	public ResponseEntity<HttpResponse> handleInvalidEmailOrPasswordException()
	{
		return createHttpResponse(HttpStatus.BAD_REQUEST, INCORRECT_CREDENTIALS);
	}
    
    @ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<HttpResponse> handleAccessDeniedException()
	{
		return createHttpResponse(HttpStatus.FORBIDDEN, NOT_ENOUGH_PERMISSION);
	}
    
    @ExceptionHandler(LockedException.class)
	public ResponseEntity<HttpResponse> handleLockedException()
	{
		return createHttpResponse(HttpStatus.UNAUTHORIZED, ACCOUNT_LOCKED);
	}
    
    @ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<HttpResponse> handleUnauthorizedException()
	{
		return createHttpResponse(HttpStatus.UNAUTHORIZED, NOT_ENOUGH_PERMISSION);
	}
    
    @ExceptionHandler(EmailExistException.class)
	public ResponseEntity<HttpResponse> handleEmailExistException(EmailExistException exception)
	{
    	log.error(exception.getMessage());
		return createHttpResponse(HttpStatus.CONFLICT, EMAIL_ALREADY_EXISTS);
	}
    
    
    @ExceptionHandler(EmailNotFoundException.class)
	public ResponseEntity<HttpResponse> handleEmailNotFoundException(EmailNotFoundException exception)
	{
    	log.error(exception.getMessage());
		return createHttpResponse(HttpStatus.NOT_FOUND, EMAIL_NOT_FOUND);
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
