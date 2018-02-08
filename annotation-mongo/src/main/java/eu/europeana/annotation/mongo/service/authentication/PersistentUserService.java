package eu.europeana.annotation.mongo.service.authentication;

import java.util.List;

import eu.europeana.annotation.definitions.exception.UserValidationException;
import eu.europeana.annotation.definitions.model.authentication.User;
import eu.europeana.annotation.mongo.model.authentication.internal.PersistentUser;
import eu.europeana.api.commons.nosql.service.AbstractNoSqlService;

public interface PersistentUserService extends AbstractNoSqlService<PersistentUser, String>{

	public abstract User store(User object) throws UserValidationException;

	/**
	 * This method performs update for the passed user object
	 * @param object
	 */
	public User update(User object) throws UserValidationException;
	
	/**
	 * @param name
	 * @return result of removal
	 */
	public int removeByName(String name);
	
	List<? extends PersistentUser> getAll();

	public PersistentUser findById(String httpUrl);
}

