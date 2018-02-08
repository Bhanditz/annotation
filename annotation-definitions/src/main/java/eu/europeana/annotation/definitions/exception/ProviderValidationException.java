package eu.europeana.annotation.definitions.exception;

/**
 * This class is used represent validation errors for the provider class 
 */
public class ProviderValidationException extends Exception{

	private static final long serialVersionUID = -2337162016088196364L;
	public static final String ERROR_NULL_OBJECT_ID = "Object ID must be null";
	public static final String ERROR_NOT_NULL_NAME = "Provider name must not be null";
	public static final String ERROR_NOT_NULL_URI = "Provider uri must not be null";
	public static final String ERROR_NOT_NULL_ID_GENERATION = "Provider ID generation must not be null";
	public static final String ERROR_MISSING_NAME_OR_ID_GENERATION = "Provider name and ID generation must not be null";
	public static final String ERROR_NOT_STANDARDIZED_ID_GENERATION = "ID generation field must be compliant to the defined types";
	public static final String ERROR_NO_ENTRIES_FOUND_TO_DELETE = "No provider entry found for delete. ";

	public ProviderValidationException(String message){
		super(message);
	}
	
	public ProviderValidationException(String message, Throwable th){
		super(message, th);
	}
	
	
}
