package eu.europeana.annotation.mongo.model.authentication.internal;

import org.bson.types.ObjectId;

import eu.europeana.annotation.definitions.model.authentication.Provider;
import eu.europeana.api.commons.nosql.entity.NoSqlEntity;

/**
 * This type is used as internal interface that binds the external interface (model.PersistentProvider) with the NoSql based implementation (NoSqlEntity)
 * and provides additional methods used internally by the service implementation
 */
public interface PersistentProvider extends Provider, NoSqlEntity {

	public final static String FIELD_NAME           = "name";
	public final static String FIELD_HTTP_URL       = "httpUrl";
//	public final static String FIELD_API_KEY        = "apiKey";
	public final static String FIELD_ID_GENERATION  = "idGeneration";

	/**
	 * 
	 * @return the generated mongo id
	 */
	public ObjectId getId();
	
	/**
	 * This method is necessary for the update
	 * @param id
	 */
	public void setId(ObjectId id);
	
}
