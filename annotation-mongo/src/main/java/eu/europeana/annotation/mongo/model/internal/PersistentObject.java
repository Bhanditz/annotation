package eu.europeana.annotation.mongo.model.internal;

import java.util.Date;

public interface PersistentObject{

	//public void copyFrom(Object volatileObject);
	public Date getCreated();

	public void setCreated(Date creationDate);

	public Date getLastUpdate();

	public void setLastUpdate(Date lastUpdate);

	

}
