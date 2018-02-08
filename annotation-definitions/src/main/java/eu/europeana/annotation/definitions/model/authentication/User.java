package eu.europeana.annotation.definitions.model.authentication;

import eu.europeana.annotation.definitions.model.agent.Agent;

public interface User extends Agent {

	public void setUserToken(String userToken);

	public String getUserToken();
	
}
