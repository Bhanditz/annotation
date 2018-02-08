package eu.europeana.annotation.definitions.model.authentication.impl;

import eu.europeana.annotation.definitions.model.agent.impl.BaseAgent;
import eu.europeana.annotation.definitions.model.authentication.Provider;
import eu.europeana.annotation.definitions.model.vocabulary.IdGenerationTypes;


public class BaseProvider extends BaseAgent implements Provider {

	private String apiKey;
	private IdGenerationTypes idGeneration;

	
	public BaseProvider(){
		super();
	}
	
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getApiKey() {
		return this.apiKey;
	}
	
	public String getIdGeneration() {
		return idGeneration.getIdType();
	}

	public void setIdGeneration(IdGenerationTypes idGeneration) {
		this.idGeneration = idGeneration;
	}

	@Override
	public boolean equals(Object other) {
	    if (!(other instanceof Provider)) {
	        return false;
	    }

	    Provider that = (Provider) other;

	    boolean res = true;
	    
	    /**
	     * equality check for all relevant fields.
	     */
	    if ((this.getName() != null) && (that.getName() != null) &&
	    		(!this.getName().equals(that.getName()))) {
	    	System.out.println("Provider objects have different 'name' fields.");
	    	res = false;
	    }
	    
	    if ((this.getHttpUrl() != null) && (that.getHttpUrl() != null) &&
	    		(!this.getHttpUrl().equals(that.getHttpUrl()))) {
	    	System.out.println("Provider objects have different 'httpUrls' fields.");
	    	res = false;
	    }
	    
	    if ((this.getIdGeneration() != null) && (that.getIdGeneration() != null) &&
	    		(!this.getIdGeneration().equals(that.getIdGeneration()))) {
	    	System.out.println("Provider objects have different 'IdGeneration' fields.");
	    	res = false;
	    }
	    
	    return res;
	}
	
	@Override
	public String toString() {
		String res = "### Provider ###\n";
		if (getName() != null) 
			res = res + "\t" + "name:" + getName() + "\n";
		if (getHttpUrl() != null) 
			res = res + "\t" + "httpUrl:" + getHttpUrl() + "\n";
		if (idGeneration != null) 
			res = res + "\t" + "idGeneration:" + idGeneration + "\n";
		return res;
	}	
	
}
