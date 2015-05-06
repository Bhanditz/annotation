package eu.europeana.annotation.web.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.stanbol.commons.exception.JsonParseException;
import org.apache.stanbol.commons.jsonld.JsonLd;
import org.apache.stanbol.commons.jsonld.JsonLdParser;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europeana.annotation.definitions.exception.AnnotationValidationException;
import eu.europeana.annotation.definitions.model.Annotation;
import eu.europeana.annotation.definitions.model.AnnotationId;
import eu.europeana.annotation.definitions.model.body.Body;
import eu.europeana.annotation.definitions.model.body.impl.PlainTagBody;
import eu.europeana.annotation.definitions.model.factory.impl.BodyObjectFactory;
import eu.europeana.annotation.definitions.model.utils.AnnotationBuilder;
import eu.europeana.annotation.definitions.model.utils.TypeUtils;
import eu.europeana.annotation.definitions.model.vocabulary.BodyTypes;
import eu.europeana.annotation.jsonld.AnnotationLd;
import eu.europeana.annotation.mongo.service.PersistentAnnotationService;
import eu.europeana.annotation.mongo.service.PersistentTagService;
import eu.europeana.annotation.solr.exceptions.AnnotationServiceException;
import eu.europeana.annotation.solr.exceptions.TagServiceException;
import eu.europeana.annotation.solr.model.internal.SolrAnnotation;
import eu.europeana.annotation.solr.model.internal.SolrAnnotationConst;
import eu.europeana.annotation.solr.model.internal.SolrAnnotationImpl;
import eu.europeana.annotation.solr.model.internal.SolrTag;
import eu.europeana.annotation.solr.model.internal.SolrTagImpl;
import eu.europeana.annotation.solr.service.SolrAnnotationService;
import eu.europeana.annotation.solr.service.SolrTagService;
import eu.europeana.annotation.utils.parse.AnnotationLdParser;
import eu.europeana.annotation.web.service.AnnotationConfiguration;
import eu.europeana.annotation.web.service.AnnotationService;

public class AnnotationServiceImpl implements AnnotationService {

	@Autowired
	AnnotationConfiguration configuration;
	
	@Autowired
	PersistentAnnotationService mongoPersistance;
	
	@Autowired
	PersistentTagService mongoTagPersistance;
	
	@Autowired
	SolrAnnotationService solrService;
	
	@Autowired
	SolrTagService solrTagService;
	
	AnnotationBuilder controllerHelper;
	
	public AnnotationBuilder getControllerHelper() {
		if(controllerHelper == null)
			controllerHelper = new AnnotationBuilder();
		return controllerHelper;
	}

	

	@Override
	public String getComponentName() {
		return configuration.getComponentName();
	}

	protected AnnotationConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(AnnotationConfiguration configuration) {
		this.configuration = configuration;
	}

	protected PersistentAnnotationService getMongoPersistence() {
		return mongoPersistance;
	}

	public void setMongoPersistance(PersistentAnnotationService mongoPersistance) {
		this.mongoPersistance = mongoPersistance;
	}

	public PersistentTagService getMongoTagPersistence() {
		return mongoTagPersistance;
	}

	public void setMongoTagPersistance(PersistentTagService mongoTagPersistance) {
		this.mongoTagPersistance = mongoTagPersistance;
	}

	@Override
	public List<? extends Annotation> getAnnotationList(String resourceId) {		
		return getMongoPersistence().getAnnotationList(resourceId);
	}
	
	@Override
	public List<? extends Annotation> getFilteredAnnotationList(
			String resourceId, String startOn, String limit, boolean isDisabled) {		
		return getMongoPersistence().getFilteredAnnotationList(resourceId, null, startOn, limit, isDisabled);
	}
	
	@Override
	public Annotation getAnnotationById(String provider, Long annotationNr) {
		return getMongoPersistence().find(provider, annotationNr);
		
	}

	@Override
	public Annotation getAnnotationById(String resourceId, String provider,
			Long annotationNr) {
		return getMongoPersistence().find(resourceId, provider, annotationNr);
		
	}

	@Override
	public List<? extends Annotation> searchAnnotations(String query) throws AnnotationServiceException {
		return getSolrService().search(query);
	}

	@Override
	public List<? extends Annotation> searchAnnotations(String query, String startOn, String limit) 
			throws AnnotationServiceException {
		return getSolrService().search(query, startOn, limit);
	}

	@Override
	public Map<String, Integer> searchAnnotations(String [] qf, List<String> queries) throws AnnotationServiceException {
		return getSolrService().queryFacetSearch(SolrAnnotationConst.ALL_SOLR_ENTRIES, qf, queries);
	}

	@Override
	public List<? extends SolrTag> searchTags(
			String query) throws TagServiceException {
		return getSolrTagService().search(query);
	}

	@Override
	public List<? extends SolrTag> searchTags(
			String query, String startOn, String limit) throws TagServiceException {
		return getSolrTagService().search(query, startOn, limit);
	}

	@Override
	public Annotation parseAnnotation(String annotationJsonLdStr) {
	    
		/**
	     * parse JsonLd string using JsonLdParser.
	     * JsonLd string -> JsonLdParser -> JsonLd object
	     */
	    AnnotationLd parsedAnnotationLd = null;
	    JsonLd parsedJsonLd = null;
	    try {
	    	parsedJsonLd = JsonLdParser.parseExt(annotationJsonLdStr);
	    	
	    	/**
	    	 * convert JsonLd to AnnotationLd.
	    	 * JsonLd object -> AnnotationLd object
	    	 */
	    	parsedAnnotationLd = new AnnotationLd(parsedJsonLd);
		} catch (Exception e) {
			String errorMessage = "Cannot Parse JSON-LD input! ";
			Logger.getLogger(getClass().getName()).error(errorMessage, e);
		}
	    
	    /**
	     * AnnotationLd object -> Annotation object.
	     */
	    return parsedAnnotationLd.getAnnotation();
	}

	@Override
	public Annotation parseAnnotationLd(String annotationJsonLdStr) throws JsonParseException {
	    
		/**
	     * parse JsonLd string using JsonLdParser.
	     * JsonLd string -> JsonLdParser -> JsonLd object
	     */
		AnnotationLdParser europeanaParser = new AnnotationLdParser();
		return europeanaParser.parseAnnotation(annotationJsonLdStr);
		
//	    AnnotationLd parsedAnnotationLd = null;
//	    JsonLd parsedJsonLd = null;
//	    try {
//	    	parsedJsonLd = JsonLdParser.parseExt(annotationJsonLdStr);
//	    	
//	    	/**
//	    	 * convert JsonLd to AnnotationLd.
//	    	 * JsonLd object -> AnnotationLd object
//	    	 */
//	    	parsedAnnotationLd = new EuropeanaAnnotationLd(parsedJsonLd);
//		} catch (Exception e) {
//			String errorMessage = "Cannot Parse JSON-LD input! ";
//			Logger.getLogger(getClass().getName()).error(errorMessage, e);
//		}
//	    
//	    /**
//	     * AnnotationLd object -> Annotation object.
//	     */
//	    return parsedAnnotationLd.getAnnotation();
	}

	/* (non-Javadoc)
	 * @see eu.europeana.annotation.web.service.AnnotationService#storeAnnotation(eu.europeana.annotation.definitions.model.Annotation)
	 */
	@Override
	public Annotation storeAnnotation(Annotation newAnnotation) {
		return storeAnnotation(newAnnotation, true);
	}
		
	/* (non-Javadoc)
	 * @see eu.europeana.annotation.web.service.AnnotationService#storeAnnotation(eu.europeana.annotation.definitions.model.Annotation, boolean)
	 */
	@Override
	public Annotation storeAnnotation(Annotation newAnnotation, boolean indexing) {
		
		//must have annotaionId with resourceId and provider.
		validateAnnotationId(newAnnotation);
		
		// store in mongo database
		Annotation res =  getMongoPersistence().store(newAnnotation);
		
		if (indexing) {
			// add solr indexing here
	        try {
	       	    SolrAnnotation indexedAnnotation = copyIntoSolrAnnotation(res, true);
	       	    getSolrService().store(indexedAnnotation);
	        } catch (Exception e) {
	            Logger.getLogger(getClass().getName()).warn(
	        		   "The annotation was stored correctly into the Mongo, but it was not indexed yet. " + e);
	//    	    throw new RuntimeException(e);
	        }
	        
	        // check if the tag is already indexed 
	        try {
	       	    SolrTag indexedTag = copyIntoSolrTag(res.getBody());
	       	    getSolrTagService().findOrStore(indexedTag);
	        } catch (Exception e) {
	            Logger.getLogger(getClass().getName()).warn(
	        		   "The annotation was stored correctly into the Mongo, but the Body tag was not indexed yet. " + e);
	        }
			
	        // save the time of the last SOLR indexing
	        try {
	    	    getMongoPersistence().updateIndexingTime(res.getAnnotationId()); 	    
	        } catch (Exception e) {
	            Logger.getLogger(getClass().getName()).warn(
	         		   "The time of the last SOLR indexing could not be saved. " + e);
	        }
	   }
		
       return res;
	}

	/**
	 * This method validates AnnotationId object.
	 * @param newAnnotation
	 */
	private void validateAnnotationId(Annotation newAnnotation) {
		
		if (newAnnotation.getAnnotationId() == null)
			throw new AnnotationValidationException(
					"Annotaion.AnnotationId must not be null!");
			
//		if (newAnnotation.getAnnotationId().getResourceId() == null)
//			throw new AnnotationValidationException(
//					"Annotaion.AnnotationId.resourceId must not be null!");
			
		if (newAnnotation.getAnnotationId().getProvider() == null)
			throw new AnnotationValidationException(
					"Annotaion.AnnotationId.provider must not be null!");
	}

	private SolrAnnotation copyIntoSolrAnnotation(Annotation annotation) {
		
		return copyIntoSolrAnnotation(annotation, false);
	}

	private SolrAnnotation copyIntoSolrAnnotation(Annotation annotation, boolean withMultilingual) {
		
		SolrAnnotation res = null;
		
  		SolrAnnotationImpl solrAnnotationImpl = new SolrAnnotationImpl();
//  		solrAnnotationImpl.setType(annotation.getType()); 
  		//TODO: update this to store the internal type instead of the oa type in the index
  		//TODO: add the internal type solr configs
		solrAnnotationImpl.setAnnotationType(annotation.getInternalType()); 
//  		solrAnnotationImpl.setInternalType(annotation.getInternalType());
  		solrAnnotationImpl.setAnnotatedBy(annotation.getAnnotatedBy());
  		Body body = annotation.getBody();
  		if (withMultilingual) 
  			body = convertToSolrMultilingual(body);
		solrAnnotationImpl.setBody(body);
  		solrAnnotationImpl.setAnnotatedAt(annotation.getAnnotatedAt());
  		solrAnnotationImpl.setAnnotatedByString(annotation.getAnnotatedBy().getName());
  		solrAnnotationImpl.setTarget(annotation.getTarget());
  		solrAnnotationImpl.setAnnotationId(annotation.getAnnotationId());
  		solrAnnotationImpl.setLabel(body.getValue());
  		solrAnnotationImpl.setLanguage(body.getLanguage());
  		solrAnnotationImpl.setMotivatedBy(annotation.getMotivatedBy());
  		solrAnnotationImpl.setSerializedAt(annotation.getSerializedAt());
  		solrAnnotationImpl.setSerializedBy(annotation.getSerializedBy());
  		solrAnnotationImpl.setStyledBy(annotation.getStyledBy());
  		solrAnnotationImpl.setAnnotationIdString(annotation.getAnnotationId().toUri());
  		
  		solrAnnotationImpl.setTagId(solrAnnotationImpl.getTagId());
		solrAnnotationImpl.setSameAs(solrAnnotationImpl.getSameAs());
		//TODO: add the equivalent to solr configs
		solrAnnotationImpl.setSameAs(solrAnnotationImpl.getEquivalentTo());
		
        res = solrAnnotationImpl;

        return res;
	}

	/**
     * This method converts a multilingual part of the Annotation Body
     * in a multilingual value that is conform for Solr. E.g. 'en' in 'EN_multilingual'
	 * @param body
	 * @return converted body
	 */
	private Body convertToSolrMultilingual(Body body) {
		Body bodyRes = BodyObjectFactory.getInstance().createModelObjectInstance(BodyTypes.SEMANTIC_TAG.name());
  		Map<String, String> multilingualMap = body.getMultilingual();
  		Map<String, String> solrMultilingualMap = new HashMap<String, String>();
		for (Map.Entry<String, String> entry : multilingualMap.entrySet()) {
		    String key = entry.getKey();
		    if (!key.contains(SolrAnnotationConst.UNDERSCORE + SolrAnnotationConst.MULTILINGUAL)) {
		    	key = key.toUpperCase() + SolrAnnotationConst.UNDERSCORE + SolrAnnotationConst.MULTILINGUAL;
		    }
			solrMultilingualMap.put(key, entry.getValue());
		}
		if (solrMultilingualMap.size() > 0)
			bodyRes.setMultilingual(solrMultilingualMap);
//		if (StringUtils.isNotEmpty(body.getType()))
		if (body.getType() != null)
			bodyRes.setType(body.getType());
		if (StringUtils.isNotEmpty(body.getContentType()))
			bodyRes.setContentType(body.getContentType());
		if (StringUtils.isNotEmpty(body.getHttpUri()))
			bodyRes.setHttpUri(body.getHttpUri());
		if (StringUtils.isNotEmpty(body.getLanguage()))
			bodyRes.setLanguage(body.getLanguage());
		if (StringUtils.isNotEmpty(body.getMediaType()))
			bodyRes.setMediaType(body.getMediaType());
		if (StringUtils.isNotEmpty(body.getValue()))
			bodyRes.setValue(body.getValue());
		if (StringUtils.isNotBlank(((PlainTagBody) body).getTagId())) {
			((PlainTagBody) bodyRes).setTagId(((PlainTagBody) body).getTagId());
		}
		return bodyRes;
	}

	/**
	 * This method converts Body object in SolrTag object.
	 * @param tag The body object
	 * @return the SolrTag object
	 */
	private SolrTag copyIntoSolrTag(Body tag) {
		
		SolrTag res = null;
		
  		tag = convertToSolrMultilingual(tag);
  		SolrTagImpl solrTagImpl = new SolrTagImpl();
		if (StringUtils.isNotBlank(((PlainTagBody) tag).getTagId())) {
			solrTagImpl.setId(((PlainTagBody) tag).getTagId());
		}
//  		solrTagImpl.setTagType(tag.getType());
  		solrTagImpl.setTagType(TypeUtils.getTypeListAsStr(tag.getType()));
  		solrTagImpl.setValue(tag.getValue());
  		solrTagImpl.setLanguage(tag.getLanguage());
  		solrTagImpl.setContentType(tag.getContentType());
  		solrTagImpl.setHttpUri(tag.getHttpUri());
  		solrTagImpl.setMultilingual(tag.getMultilingual());

        res = solrTagImpl;

        return res;
	}

	/**
	 * This method converts Body object in SolrTag object.
	 * @param tag The body object
	 * @return the SolrTag object
	 */
//	private SolrTag copyPersistentTagIntoSolrTag(PersistentTag tag) {
//		
//		SolrTag res = null;
//		
//  		SolrTagImpl solrTagImpl = new SolrTagImpl();
//		if (StringUtils.isNotBlank(((PlainTagBody) tag).getTagId())) {
//			solrTagImpl.setId(((PlainTagBody) tag).getTagId());
//		}
//  		solrTagImpl.setTagType(tag.getTagType());
//  		solrTagImpl.setValue(tag.getValue());
//  		solrTagImpl.setLanguage(tag.getLanguage());
//  		solrTagImpl.setContentType(tag.getContentType());
//  		solrTagImpl.setHttpUri(tag.getHttpUri());
//  		solrTagImpl.setMultilingual(tag.getMultilingual());
//
//        res = solrTagImpl;
//
//        return res;
//	}

	@Override
	public Annotation updateAnnotation(Annotation annotation) {
		
		Annotation res = getMongoPersistence().update(annotation);

		try {
    	    SolrAnnotation indexedAnnotation = copyIntoSolrAnnotation(annotation);
    	    getSolrService().update(indexedAnnotation);
        } catch (Exception e) {
        	throw new RuntimeException(e);
        }
		
		return res;
	}

	@Override
//	public void deleteAnnotation(String resourceId, String provider, Long annotationNr) {
	public void deleteAnnotation(String provider, Long annotationNr) {
        try {
//    		Annotation res =  getMongoPersistance().findByID(String.valueOf(annotationNr));
//    		Annotation res =  getMongoPersistence().find(resourceId, provider, annotationNr);
    		Annotation res =  getMongoPersistence().find(provider, annotationNr);
    	    SolrAnnotation indexedAnnotation = copyIntoSolrAnnotation(res);
    	    getSolrService().delete(indexedAnnotation);
        } catch (Exception e) {
        	throw new RuntimeException(e);
        }
//		getMongoPersistence().remove(resourceId, provider, annotationNr);
		getMongoPersistence().remove(provider, annotationNr);
	}

	@Override
//	public void indexAnnotation(String resourceId, String provider, Long annotationNr) {
	public void indexAnnotation(String provider, Long annotationNr) {
        try {
//    		Annotation res =  getMongoPersistence().find(resourceId, provider, annotationNr);
    		Annotation res =  getMongoPersistence().find(provider, annotationNr);
       	    SolrAnnotation indexedAnnotation = copyIntoSolrAnnotation(res, true);
    	    getSolrService().delete(indexedAnnotation);
    	    getSolrService().store(indexedAnnotation);
        } catch (Exception e) {
        	throw new RuntimeException(e);
        }
	}

	@Override
//	public Annotation disableAnnotation(String resourceId, String provider, Long annotationNr) {
	public Annotation disableAnnotation(String provider, Long annotationNr) {
        try {
//    		Annotation res = getMongoPersistence().find(resourceId, provider, annotationNr);
    		Annotation res = getMongoPersistence().find(provider, annotationNr);
    	    SolrAnnotation indexedAnnotation = copyIntoSolrAnnotation(res);
    	    indexedAnnotation.setAnnotationId(res.getAnnotationId());
    	    getSolrService().delete(indexedAnnotation);
    	    res.setDisabled(true);
    		return getMongoPersistence().update(res);
        } catch (Exception e) {
        	throw new RuntimeException(e);
        }
	}

	public SolrAnnotationService getSolrService() {
		return solrService;
	}

	public void setSolrService(SolrAnnotationService solrService) {
		this.solrService = solrService;
	}
	
	public SolrTagService getSolrTagService() {
		return solrTagService;
	}

	public void setSolrTagService(SolrTagService solrTagService) {
		this.solrTagService = solrTagService;
	}

	@Override
	public void deleteTag(String tagId) {
        try {
    		Annotation res = getMongoPersistence().findByID(tagId);
    		if (res == null) {
//    			PersistentTag persistentTag = getMongoTagPersistence().findByID(tagId);
//	    	    SolrTag indexedTag = copyPersistentTagIntoSolrTag(persistentTag);
	    	    SolrTag solrTag = getSolrTagService().search(tagId).get(0);
	    	    getSolrTagService().delete(solrTag);
	    		getMongoTagPersistence().remove(tagId);
    		} else {
    			throw new TagServiceException("Tag with ID: '" + tagId + 
    					"' can't be removed since it is referenced by annotation '" + 
    					res.getAnnotationId().toString() + "'.");
    		}
        } catch (Exception e) {
        	throw new RuntimeException(e);
        }
	}

	@Override
	public List<? extends Annotation> getAnnotationListByProvider(
			String resourceId, String provider) {
		return getMongoPersistence().getAnnotationListByProvider(resourceId, provider);
	}
	
	/* (non-Javadoc)
	 * @see eu.europeana.annotation.web.service.AnnotationService#existsInDb(eu.europeana.annotation.definitions.model.AnnotationId)
	 */
	public boolean existsInDb(AnnotationId annoId) {
		boolean res = false;
        try {
    		Annotation dbRes =  getMongoPersistence().find(annoId.getProvider(), annoId.getAnnotationNr());
    		if (dbRes != null)
    			res = true;
        } catch (Exception e) {
        	throw new RuntimeException(e);
        }
		return res;
	}
	
	/**
	 * This method initializes AnnotationId dependent on provider.
	 * @param newAnnotation
	 * @return Annotation object initialized with AnnotationId
	 */
//	public AnnotationId initializeAnnotationId(Annotation newAnnotation) {
//		if (StringUtils.isNotEmpty(newAnnotation.getSameAs()) 
//			&& newAnnotation.getSameAs().contains(WebAnnotationFields.HISTORY_PIN)) {
//			MongoAnnotationId annotationId = new MongoAnnotationId();
//	        String[] arrValue = newAnnotation.getSameAs().split(WebAnnotationFields.SLASH);
//	        if (arrValue.length >= WebAnnotationFields.MIN_HISTORY_PIN_COMPONENT_COUNT) {
//				String resourceId = new AnnotationControllerHelper().extractResourceId(newAnnotation);
//				if (StringUtils.isNotEmpty(resourceId))
//					annotationId.setResourceId(resourceId);
//	        	annotationId.setProvider(WebAnnotationFields.HISTORY_PIN);
//				//the external id of the annotation is found in the last element of the url
//	        	String annotationNrStr = arrValue[arrValue.length - 1];
//				if (StringUtils.isNotEmpty(annotationNrStr))
//					annotationId.setAnnotationNr(Integer.parseInt(annotationNrStr));
//	        }
//	    }
//		
//		// set default provider if sameAs field is empty
//		if (StringUtils.isEmpty(newAnnotation.getSameAs())
//				|| StringUtils.isEmpty(res.getAnnotationId().getProvider())) { 
//			String provider = WebAnnotationFields.WEB_ANNO;
//			res.getAnnotationId().setProvider(provider);
//		}
//		
//		
//		return newAnnotation;
//	}
}

