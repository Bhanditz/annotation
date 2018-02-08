package eu.europeana.annotation.mongo.model.authentication;

import java.util.Date;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexed;

import eu.europeana.annotation.definitions.model.agent.impl.BaseAgent;
import eu.europeana.annotation.definitions.model.vocabulary.IdGenerationTypes;
import eu.europeana.annotation.mongo.model.authentication.internal.PersistentUser;
import eu.europeana.annotation.mongo.model.internal.PersistentObject;

@Entity("user")
public class PersistentUserImpl  extends BaseAgent implements PersistentUser, PersistentObject {

	@Id
	private ObjectId id;
	
	private String userToken;
	
//	@Indexed(options = @IndexOptions(unique = true))
//	private String name;
	private String idGeneration;
	private Date created;
	private Date lastUpdated;

	
	//getters and setters
	
	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	
	public String toString() {
		return "PersistentUser [userToken:" + getUserToken() + ", name:" + getName() +" ]";
	}

	@Override
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	@Override
	public String getUserToken() {
		return this.userToken;
	}

	@Override
	public Date getCreated() {
		return this.created;
	}

	@Override
	public void setCreated(Date creationDate) {
		this.created = creationDate;
	}

	@Override
	public Date getLastUpdate() {
		return this.lastUpdated;
	}

	@Override
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdated = lastUpdate;
	}
			
}