package eu.europeana.annotation.definitions.model.authentication.impl;

import eu.europeana.annotation.definitions.model.agent.impl.BaseAgent;
import eu.europeana.annotation.definitions.model.authentication.User;
import eu.europeana.annotation.definitions.model.vocabulary.IdGenerationTypes;

public class BaseUser extends BaseAgent implements User {
	
	private String password;	
	private String userToken;	
	private String salt;	

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public BaseUser(){
		super();
//		setAgentTypeEnum(AgentTypes.PERSON);
	}
}
