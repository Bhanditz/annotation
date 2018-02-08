package eu.europeana.annotation.mongo.service.authentication;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryResults;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.mongodb.WriteResult;

import eu.europeana.annotation.definitions.exception.ProviderAttributeInstantiationException;
import eu.europeana.annotation.definitions.exception.ProviderValidationException;
import eu.europeana.annotation.definitions.model.authentication.Provider;
import eu.europeana.annotation.definitions.model.vocabulary.IdGenerationTypes;
import eu.europeana.annotation.mongo.model.authentication.PersistentProviderImpl;
import eu.europeana.annotation.mongo.model.authentication.internal.PersistentProvider;
import eu.europeana.api.commons.nosql.service.impl.AbstractNoSqlServiceImpl;

@Component
@Configuration
public class PersistentProviderServiceImpl extends
		AbstractNoSqlServiceImpl<PersistentProvider, String> implements
		PersistentProviderService {

	/**
	 * This method creates a Provider object in database.
	 */
	@Override
	public PersistentProvider store(PersistentProvider object) { 
		return super.store(object);
	}

	/**
	 * Check provider fields and compliance to the registered id generation types.
	 * @param object
	 * @throws ProviderValidationException 
	 */
	private void validatePersistentProvider(PersistentProvider object) throws ProviderValidationException {

		if (object.getId() != null)
			throw new ProviderValidationException(
					ProviderValidationException.ERROR_NULL_OBJECT_ID);
		
		// check apiKey
		if (object.getApiKey() == null)
			throw new ProviderValidationException(
					ProviderValidationException.ERROR_NOT_NULL_NAME);

		// check id generation
		if (object.getIdGeneration() == null)
			throw new ProviderValidationException(
					ProviderValidationException.ERROR_NOT_NULL_ID_GENERATION);

		if (StringUtils.isEmpty(IdGenerationTypes.isRegisteredAs(object.getIdGeneration())))
			throw new ProviderValidationException(
					ProviderValidationException.ERROR_NOT_STANDARDIZED_ID_GENERATION);
	}
	
//	MongoAnnotationId copyInto(AnnotationId annoId) {
//		MongoAnnotationId embeddedId = new MongoAnnotationId();
//		embeddedId.copyFrom(annoId);
//		return embeddedId;
//	}

//	public static String extractResoureIdFromHttpUri(String httpUri) {
//		String res = "";
//		if (StringUtils.isNotEmpty(httpUri)) {
//			String[] arrValue = httpUri.split(WebAnnotationFields.SLASH);
//			// computed from the end of the url
//			int collectionPosition = arrValue.length - 2;
//			int objectPosition = arrValue.length - 1;
//
//			String collection = arrValue[collectionPosition];
//			String object = arrValue[objectPosition].replace(".html", "");
//			if (StringUtils.isNotEmpty(collection)
//					&& StringUtils.isNotEmpty(object))
//				res = WebAnnotationFields.SLASH + collection
//						+ WebAnnotationFields.SLASH + object;
//		}
//		return res;
//	}

	@Override
	public Provider store(Provider object) throws ProviderValidationException {
		Provider res = null;
		if(object instanceof PersistentProvider) {
			PersistentProvider persistentProvider = (PersistentProvider) object;
			validatePersistentProvider(persistentProvider);
			res = this.store(persistentProvider);
		}
		else{
			PersistentProvider persistentObject = copyIntoPersistentProvider(object);
			return this.store(persistentObject); 
		}
		return res;
	}

//	@Override
//	public List<? extends Provider> getProviderList(String idGeneration) {
//		return getFilteredProviderList(idGeneration, null, null);
//	}

	/**
	 * This method filters providers by id generation type. 
	 * @param idGeneration type
	 * @return evaluated list
	 */
//	public List<? extends Provider> filterProviderListByIdGenerationType (
//			String idGeneration) {
//		
//		Query<PersistentProvider> query = getDao().createQuery();
//		if (StringUtils.isNotEmpty(idGeneration)) {
//			query.disableValidation().filter(PersistentProvider.FIELD_ID_GENERATION, idGeneration); 
//		}
//		QueryResults<? extends PersistentProvider> results = getDao().find(query);
//		return results.asList();
//	}

//	@Override
//	public List<? extends Provider> getFilteredProviderList(
//			String idGeneration, String startOn, String limit
//			) {
//		Query<PersistentProvider> query = getDao().createQuery();
//		if (StringUtils.isNotEmpty(idGeneration))
//			query.filter(PersistentProvider.FIELD_ID_GENERATION, idGeneration);
//		try {
//			if (StringUtils.isNotEmpty(startOn))
//				query.offset(Integer.parseInt(startOn));
//			if (StringUtils.isNotEmpty(limit))
//				query.limit(Integer.parseInt(limit));
//		} catch (Exception e) {
//			throw new ProviderAttributeInstantiationException(
//					"Unexpected exception occured when searching providers. "
//							+ ProviderAttributeInstantiationException.BASE_MESSAGE,
//					"startOn: " + startOn + ", limit: " + limit + ". ", e);
//		}
//
//		QueryResults<? extends PersistentProvider> results = getDao()
//				.find(query);
//		return results.asList();
//	}

//	@Override
//	public PersistentProvider find(String name, String idGeneration) {
//		Query<PersistentProvider> query = getDao().createQuery();
//		query.filter(PersistentProvider.FIELD_NAME, name);
////		query.filter(PersistentProvider.FIELD_API_KEY, name);
//		query.filter(PersistentProvider.FIELD_ID_GENERATION, idGeneration);
//
//		return getDao().findOne(query);
//	}

	public PersistentProvider findById(String httpUrl) {
		Query<PersistentProvider> query = getDao().createQuery();
		query.filter(PersistentProvider.FIELD_HTTP_URL, httpUrl);

		return getDao().findOne(query);
	}

	@Override
	public PersistentProvider findByID(String id) {
		return getDao().findOne("_id", new ObjectId(id));
	}

	@Override
	public void remove(String id) {
		PersistentProvider provider = findByID(id);
		getDao().delete(provider);

	}

	@Override
	public void remove(String name, String idGeneration) {
		Query<PersistentProvider> query = getDao().createQuery();
		query.filter(PersistentProvider.FIELD_NAME, name);
		query.filter(PersistentProvider.FIELD_ID_GENERATION, idGeneration);

		getDao().deleteByQuery(query);
	}

	@Override
	public Provider update(Provider object) throws ProviderValidationException {

		Provider res = null;

		PersistentProvider persistentProvider = (PersistentProvider) object;

		if (persistentProvider != null 
//				&& StringUtils.isNotEmpty(persistentProvider.getApiKey()) 
				&& StringUtils.isNotEmpty(persistentProvider.getName()) 
				&& StringUtils.isNotEmpty(persistentProvider.getIdGeneration())
				) {
			remove(persistentProvider.getName()
					, persistentProvider.getIdGeneration());
			persistentProvider.setId(null);
			res = store(persistentProvider);
		} else {
			throw new ProviderValidationException(
					ProviderValidationException.ERROR_MISSING_NAME_OR_ID_GENERATION);
		}

		return res;
	}

	public PersistentProvider copyIntoPersistentProvider(Provider provider) {

		PersistentProviderImpl persistentProvider = new PersistentProviderImpl();
		persistentProvider.setApiKey(provider.getApiKey());
		persistentProvider.setName(provider.getName());
		persistentProvider.setInternalType(provider.getInternalType());
		persistentProvider.setType(provider.getType());		
		persistentProvider.setHttpUrl(provider.getHttpUrl());
		persistentProvider.setIdGeneration(IdGenerationTypes.getValueByType(provider.getIdGeneration()));
		return persistentProvider;
	}
		
//	@Override
//	public int removeAll() {
//		int totalNumDeletedProviderEntries = 0;
//		Iterator<PersistentProvider> itr = findAll().iterator();
//		while (itr.hasNext()) {
//			PersistentProvider providerObj = itr.next();
//			int numDeletedProviderEntries = removeByName(providerObj.getName());
//			totalNumDeletedProviderEntries = totalNumDeletedProviderEntries + numDeletedProviderEntries;
//		}
//		return totalNumDeletedProviderEntries;
//	}
		
	@Override
	public int removeById(String id) {		
		Query<PersistentProvider> query = getDao().createQuery();
		query.filter(PersistentProvider.FIELD_HTTP_URL, id);
		WriteResult writeResult = getDao().deleteByQuery(query);
		return writeResult.getN();
	}
		
	@Override
	public int removeByName(String name) {		
		Query<PersistentProvider> query = getDao().createQuery();
		query.filter(PersistentProvider.FIELD_NAME, name);
		WriteResult writeResult = getDao().deleteByQuery(query);
		return writeResult.getN();
	}
		
	@Override
	public List<? extends PersistentProvider> getAll() {
		return (List<? extends PersistentProvider>) findAll();
	}
	
	
}
