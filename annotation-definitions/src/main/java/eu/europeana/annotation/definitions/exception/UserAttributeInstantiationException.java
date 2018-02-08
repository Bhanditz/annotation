package eu.europeana.annotation.definitions.exception;

/**
 * This class is used represent validation errors for the provider attributes instantiation
 */
public class UserAttributeInstantiationException extends RuntimeException{

	private static final long serialVersionUID = -7419759006948411637L;
	public static final String BASE_MESSAGE = "Cannot instantiate user attribute. ";
	public static final String DEFAULT_MESSAGE = "Cannot instantiate user attribute for type: ";
	public static final String MESSAGE_UNKNOWN_TYPE = "Cannot instantiate user attribute for type: ";
	
	public UserAttributeInstantiationException(String attributeType){
		this(attributeType, DEFAULT_MESSAGE);
	}
	
	public UserAttributeInstantiationException(String attributeType, String message){
		this(attributeType, message, null);
	}
	
	public UserAttributeInstantiationException(String attributeType, String message, Throwable th){
		super(message + attributeType, th);
	}
	
}
