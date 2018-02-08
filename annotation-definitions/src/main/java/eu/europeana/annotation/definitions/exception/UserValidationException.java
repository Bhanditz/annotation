package eu.europeana.annotation.definitions.exception;

/**
 * This class is used to represent validation errors for the user class 
 */
public class UserValidationException extends RuntimeException{

	private static final long serialVersionUID = -5337162016088196364L;
	public static final String ERROR_NULL_OBJECT_ID = "Object ID must be null";
	public static final String ERROR_NOT_NULL_HTTP_URL = "User HTTP URL must not be null";
	public static final String ERROR_NOT_NULL_NAME = "User name must not be null";
	public static final String ERROR_NOT_NULL_USER_TOKEN = "User token must not be null";
	public static final String ERROR_NOT_NULL_ID_GENERATION = "User ID generation must not be null";
	public static final String ERROR_MISSING_NAME_OR_ID_GENERATION = "User name and ID generation must not be null";
	public static final String ERROR_NOT_STANDARDIZED_ID_GENERATION = "ID generation field must be compliant to the defined types";
	public static final String ERROR_NO_ENTRIES_FOUND_TO_DELETE = "No user entry found for delete. ";

	public UserValidationException(String message){
		super(message);
	}
	
	public UserValidationException(String message, Throwable th){
		super(message, th);
	}
	
	
}
