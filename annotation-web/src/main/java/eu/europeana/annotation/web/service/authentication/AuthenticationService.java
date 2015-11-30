package eu.europeana.annotation.web.service.authentication;

import eu.europeana.annotation.definitions.model.agent.Agent;
import eu.europeana.annotation.web.exception.authentication.ApplicationAuthenticationException;
import eu.europeana.annotation.web.exception.authorization.UserAuthorizationException;
import eu.europeana.annotation.web.service.authentication.model.Application;

public interface AuthenticationService {

	public Application findByApiKey(String apiKey) throws ApplicationAuthenticationException;
	
	public Agent getUserByToken(String apiKey, String userToken) throws UserAuthorizationException;

	public Application getByApiKey(String apiKey) throws ApplicationAuthenticationException;
}