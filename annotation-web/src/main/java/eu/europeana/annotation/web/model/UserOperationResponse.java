package eu.europeana.annotation.web.model;

import eu.europeana.annotation.definitions.model.authentication.User;
import eu.europeana.api2.web.model.json.abstracts.ApiResponse;

public class UserOperationResponse extends ApiResponse{
	
	User user;

	public static String ERROR_NO_OBJECT_FOUND = "No Object Found!";
	public static String ERROR_ID_GENERATION_TYPE_DOES_NOT_MATCH = 
			"Passed 'idGeneration' type parameter does not match to the registered Id generation types given in the data model!";	
	
	public static String ERROR_PROVIDER_EXISTS_IN_DB = 
			"Cannot store object! An object with the given id already exists in the database: ";
	
	public UserOperationResponse(String apiKey, String action){
		super(apiKey, action);
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
