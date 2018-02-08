package eu.europeana.annotation.web.exception;

import org.springframework.http.HttpStatus;

import eu.europeana.api.commons.web.exception.HttpException;

public class UserOperationException extends HttpException{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserOperationException(String message, String i18nKey, String[] i18nParams){
		this(message, i18nKey, i18nParams, HttpStatus.METHOD_NOT_ALLOWED, null);
	}
	
	public UserOperationException(String message, String i18nKey, String[] i18nParams, Throwable th){
		this(message, i18nKey, i18nParams, HttpStatus.METHOD_NOT_ALLOWED, th);
	}
	
	public UserOperationException(String message, String i18nKey, String[] i18nParams, HttpStatus status){
		this(message, i18nKey, i18nParams, status, null);
	}

	public UserOperationException(String message, String i18nKey, String[] i18nParams, HttpStatus status, Throwable th){
		super(message, i18nKey, i18nParams, status, th);
	}


}
