package eu.europeana.annotation.mongo.service.authentication;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryResults;
import org.springframework.stereotype.Component;

import com.mongodb.WriteResult;

import eu.europeana.annotation.definitions.exception.UserAttributeInstantiationException;
import eu.europeana.annotation.definitions.exception.UserValidationException;
import eu.europeana.annotation.definitions.model.authentication.User;
import eu.europeana.annotation.definitions.model.vocabulary.IdGenerationTypes;
import eu.europeana.annotation.mongo.model.authentication.PersistentUserImpl;
import eu.europeana.annotation.mongo.model.authentication.internal.PersistentUser;
import eu.europeana.api.commons.nosql.service.impl.AbstractNoSqlServiceImpl;

@Component
public class PersistentUserServiceImpl extends
		AbstractNoSqlServiceImpl<PersistentUser, String> implements
		PersistentUserService {

	/**
	 * This method creates a User object in database.
	 * 
	 * @throws UserValidationException
	 */
	@Override
	public PersistentUser store(PersistentUser object) {
		validatePersistentUser(object);		
		return super.store(object);
	}

	/**
	 * Check user fields and compliance to the registered id generation types.
	 * @param object
	 */
	private void validatePersistentUser(PersistentUser object) {

		if (object.getId() != null)
			throw new UserValidationException(
					UserValidationException.ERROR_NULL_OBJECT_ID);
		
		// check userToken
//		if (object.getUserToken() == null)
//			throw new UserValidationException(
//					UserValidationException.ERROR_NOT_NULL_USER_TOKEN);

		// check name
		if (object.getName() == null)
			throw new UserValidationException(
					UserValidationException.ERROR_NOT_NULL_NAME);
		
		// check HTTP URL
		if (object.getHttpUrl() == null)
			throw new UserValidationException(
					UserValidationException.ERROR_NOT_NULL_HTTP_URL);

	}
	
	@Override
	public User store(User object) {
		User res = null;
		if(object instanceof PersistentUser)
			res = this.store((PersistentUser) object);
		else{
			PersistentUser persistentObject = copyIntoPersistentUser(object);
			return this.store(persistentObject); 
		}
		return res;
	}

	public PersistentUser findById(String httpUrl) {
		Query<PersistentUser> query = getDao().createQuery();
		query.filter(PersistentUser.FIELD_HTTP_URL, httpUrl);

		return getDao().findOne(query);
	}
	
	@Override
	public PersistentUser findByID(String id) {
		return getDao().findOne("_id", new ObjectId(id));
	}

	@Override
	public void remove(String id) {
		PersistentUser user = findByID(id);
		getDao().delete(user);

	}

//	@Override
//	public void remove(String name, String idGeneration) {
//		Query<PersistentUser> query = getDao().createQuery();
//		query.filter(PersistentUser.FIELD_NAME, name);
//		query.filter(PersistentUser.FIELD_ID_GENERATION, idGeneration);
//
//		getDao().deleteByQuery(query);
//	}

	@Override
	public User update(User object) {

		User res = null;

		PersistentUser persistentUser = (PersistentUser) object;

		if (persistentUser != null 
//				&& StringUtils.isNotEmpty(persistentUser.getUserToken()) 
				&& StringUtils.isNotEmpty(persistentUser.getName()) 
				) {
			remove(persistentUser.getHttpUrl());
			persistentUser.setId(null);
			res = store(persistentUser);
		} else {
			throw new UserValidationException(
					UserValidationException.ERROR_MISSING_NAME_OR_ID_GENERATION);
		}

		return res;
	}

	public PersistentUser copyIntoPersistentUser(User user) {

		PersistentUserImpl persistentUser = new PersistentUserImpl();
		persistentUser.setUserToken(user.getUserToken());
		persistentUser.setHttpUrl(user.getHttpUrl());
		persistentUser.setName(user.getName());
		persistentUser.setInternalType(user.getInternalType());
		persistentUser.setType(user.getType());		
		return persistentUser;
	}
	
	@Override
	public int removeByName(String name) {		
		Query<PersistentUser> query = getDao().createQuery();
		query.filter(PersistentUser.FIELD_NAME, name);
		WriteResult writeResult = getDao().deleteByQuery(query);
		return writeResult.getN();
	}
		
	@Override
	public List<? extends PersistentUser> getAll() {
		return (List<? extends PersistentUser>) findAll();
	}
		
}
