package eu.europeana.annotation.web.exception;

import org.springframework.http.HttpStatus;

import eu.europeana.api.commons.web.exception.HttpException;

public class ProviderOperationException extends HttpException{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProviderOperationException(String message, String i18nKey, String[] i18nParams){
		this(message, i18nKey, i18nParams, HttpStatus.METHOD_NOT_ALLOWED, null);
	}
	
	public ProviderOperationException(String message, String i18nKey, String[] i18nParams, Throwable th){
		this(message, i18nKey, i18nParams, HttpStatus.METHOD_NOT_ALLOWED, th);
	}
	
	public ProviderOperationException(String message, String i18nKey, String[] i18nParams, HttpStatus status){
		this(message, i18nKey, i18nParams, status, null);
	}

	public ProviderOperationException(String message, String i18nKey, String[] i18nParams, HttpStatus status, Throwable th){
		super(message, i18nKey, i18nParams, status, th);
	}


}
