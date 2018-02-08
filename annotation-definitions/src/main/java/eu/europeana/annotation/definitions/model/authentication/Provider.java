package eu.europeana.annotation.definitions.model.authentication;

import eu.europeana.annotation.definitions.model.agent.Agent;
import eu.europeana.annotation.definitions.model.vocabulary.IdGenerationTypes;

public interface Provider extends Agent{

	public void setApiKey(String apiKey);

	public String getApiKey();
	
	public void setIdGeneration(IdGenerationTypes idGeneration);
	
	public String getIdGeneration();
	
}