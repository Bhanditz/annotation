package eu.europeana.annotation.mongo.model.authentication;

import java.util.Date;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import eu.europeana.annotation.definitions.model.agent.impl.BaseAgent;
import eu.europeana.annotation.definitions.model.vocabulary.IdGenerationTypes;
import eu.europeana.annotation.mongo.model.authentication.internal.PersistentProvider;
import eu.europeana.annotation.mongo.model.internal.PersistentObject;

@Entity("provider")
public class PersistentProviderImpl extends BaseAgent implements PersistentProvider, PersistentObject {

	@Id
	private ObjectId id;
	
	private String apiKey;
	
//	@Indexed(options = @IndexOptions(unique = true))
//	private String name;
	private String idGeneration;
	private Date created;
	private Date lastUpdate;

	
	
	//getters and setters
	
	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

  
	public String toString() {
		return "PersistentProvider [apiKey:" + getApiKey() + ", name:" + getName() +
				", idGeneration:" + getIdGeneration() + ", last update: " + getLastUpdate() + "]";
	}

	@Override
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	@Override
	public String getApiKey() {
		return this.apiKey;
	}

	@Override
	public void setIdGeneration(IdGenerationTypes idGeneration) {
		this.idGeneration = idGeneration.getIdType();
	}

	@Override
	public String getIdGeneration() {
		return this.idGeneration;
	}

	
	@Override
	public boolean equalsContent(Object other) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date creationDate) {
		this.created = creationDate;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	
			
}