package eu.europeana.annotation.client.connection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.stanbol.commons.exception.JsonParseException;
import org.apache.stanbol.commons.jsonld.JsonLdParser;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.http.ResponseEntity;

import eu.europeana.annotation.client.config.ClientConfiguration;
import eu.europeana.annotation.client.model.result.AnnotationOperationResponse;
import eu.europeana.annotation.client.model.result.AnnotationSearchResults;
import eu.europeana.annotation.client.model.result.ConceptOperationResponse;
import eu.europeana.annotation.client.model.result.TagSearchResults;
import eu.europeana.annotation.client.model.result.WhitelistOperationResponse;
import eu.europeana.annotation.definitions.model.Annotation;
import eu.europeana.annotation.definitions.model.resource.impl.BaseTagResource;
import eu.europeana.annotation.definitions.model.resource.impl.TagResource;
import eu.europeana.annotation.definitions.model.search.SearchProfiles;
import eu.europeana.annotation.definitions.model.search.result.AnnotationPage;
import eu.europeana.annotation.definitions.model.search.result.impl.AnnotationPageImpl;
import eu.europeana.annotation.definitions.model.utils.AnnotationIdHelper;
import eu.europeana.annotation.definitions.model.utils.ModelConst;
import eu.europeana.annotation.definitions.model.vocabulary.WebAnnotationFields;
import eu.europeana.annotation.definitions.model.whitelist.WhitelistEntry;
import eu.europeana.annotation.utils.JsonUtils;
import eu.europeana.annotation.utils.parse.AnnotationPageParser;
import eu.europeana.annotation.utils.parse.WhiteListParser;

/**
 * @author GrafR
 *
 */
public class AnnotationApiConnection extends BaseApiConnection {

	/**
	 * Create a new connection to the Annotation Service (REST API).
	 * 
	 * @param apiKey
	 *            API Key required to access the API
	 */
	public AnnotationApiConnection(String annotationServiceUri, String apiKey) {
		super(annotationServiceUri, apiKey);
	}

	public AnnotationApiConnection() {
		this(ClientConfiguration.getInstance().getServiceUri(),
				ClientConfiguration.getInstance().getApiKey());
	}
	
	/**
	 * @param collectionId
	 * @param objectHash
	 * @return
	 * @throws IOException
	 */
	public AnnotationSearchResults getAnnotationsForObject(String collectionId,
			String objectHash) throws IOException {
		String url = getAnnotationServiceUri();
		url += WebAnnotationFields.SLASH + collectionId 
				+ WebAnnotationFields.SLASH + objectHash 
				+ ".json";
		url += "?wsKey=" + getApiKey() + "&profile=annotation";

		// Execute Europeana API request
		String json = getJSONResult(url);
		
		return getAnnotationSearchResults(json);
//
//		Gson gson = getAnnotationGson();
//
//		return (AnnotationSearchResults) gson.fromJson(json,
//				AnnotationSearchResults.class);
	}
	
	/**
	 * The HTTP request sample:
	 *     http://localhost:8081/annotation-web/annotations/{collection}/{object}.json?collection=15502&object=GG_8285&provider=webanno
	 * @param collectionId
	 * @param objectHash
	 * @param provider
	 * @return
	 * @throws IOException
	 */
	public AnnotationSearchResults getAnnotationsForObject(String collectionId,
			String objectHash, String provider) throws IOException {
		String url = getAnnotationServiceUri();
		url += WebAnnotationFields.SLASH + collectionId 
				+ WebAnnotationFields.SLASH + objectHash; 
//				+ WebAnnotationFields.SLASH + provider 
//				+ ".json";
		url += WebAnnotationFields.JSON_REST + WebAnnotationFields.PAR_CHAR;
//		url += WebAnnotationFields.WSKEY + WebAnnotationFields.EQUALS + "ws" + WebAnnotationFields.AND;
		url += WebAnnotationFields.COLLECTION + WebAnnotationFields.EQUALS + collectionId + WebAnnotationFields.AND;
		url += WebAnnotationFields.OBJECT + WebAnnotationFields.EQUALS + objectHash + WebAnnotationFields.AND;
		if (StringUtils.isNotEmpty(provider))
			url += WebAnnotationFields.PROVIDER + WebAnnotationFields.EQUALS + provider;// + WebAnnotationFields.AND;
//		url += "?wsKey=" + getApiKey() + "&profile=annotation";

		// Execute Europeana API request
		String json = getJSONResult(url);
		
//		return getAnnotationSearchResults(json);
		AnnotationSearchResults asr = new AnnotationSearchResults();
		asr.setJson(json);
		return asr;

//
//		Gson gson = getAnnotationGson();
//
//		return (AnnotationSearchResults) gson.fromJson(json,
//				AnnotationSearchResults.class);
	}
	
	/**
	 * This method retrieves the Europeana AnnotationLd object by provider and annotationNr.
	 * The HTTP request sample is:
	 *     http://localhost:8081/annotation-web/annotation.jsonld?provider=webanno&annotationNr=111
	 * @param provider
	 * @param annotationNr
	 * @return
	 */
	public AnnotationSearchResults getAnnotationLd(String provider, Long annotationNr) throws IOException {
		String url = getAnnotationServiceUri(); // current annotation service uri is .../annotation-web/annotations
		url += WebAnnotationFields.ANNOTATION_JSON_LD_REST + WebAnnotationFields.PAR_CHAR;
		url += WebAnnotationFields.PARAM_WSKEY + WebAnnotationFields.EQUALS + "ws" + WebAnnotationFields.AND;
		if (StringUtils.isNotEmpty(provider))
			url += WebAnnotationFields.PROVIDER + WebAnnotationFields.EQUALS + provider + WebAnnotationFields.AND;
		if (annotationNr != null)
			url += WebAnnotationFields.IDENTIFIER + WebAnnotationFields.EQUALS + annotationNr;

		/**
		 * Execute Europeana API request
		 */
		String json = getJSONResult(url);
		AnnotationSearchResults asr = new AnnotationSearchResults();
		asr.setJson(json);
		return asr;
	}
	
	/**
	 * This method searches for Europeana AnnotationLd objects by target or by resourceId.
	 * The HTTP request sample for target is:
	 *     http://localhost:8081/annotation-web/annotations/search.jsonld?target=http%3A%2F%2Fdata.europeana.eu%2Fitem%2F123%2Fxyz&
	 * The HTTP request sample for resourceId is:
	 *     http://localhost:8081/annotation-web/annotations/search.jsonld?resourceId=%2F123%2Fxyz
	 * @param target 
	 * @param resourceId
	 * @return
	 */
	public AnnotationSearchResults searchAnnotationLd(String target, String resourceId) throws IOException {
		String url = getAnnotationServiceUri() + WebAnnotationFields.SLASH; 
		url += WebAnnotationFields.SEARCH_JSON_LD_REST + WebAnnotationFields.PAR_CHAR;
		url += WebAnnotationFields.PARAM_WSKEY + WebAnnotationFields.EQUALS + "ws" + WebAnnotationFields.AND;
		if (StringUtils.isNotEmpty(target))
			url += WebAnnotationFields.TARGET + WebAnnotationFields.EQUALS + target;
		if (StringUtils.isNotEmpty(resourceId))
			url += WebAnnotationFields.RESOURCE_ID + WebAnnotationFields.EQUALS + resourceId;

		/**
		 * Execute Europeana API request
		 */
		String json = getJSONResult(url);
		AnnotationSearchResults asr = new AnnotationSearchResults();
		asr.setJson(json);
		return asr;
	}
	
//	public AnnotationSearchResults convertAnnotationToAnnotationLdString(Annotation annotation) throws IOException {
//		String url = getAnnotationServiceUri();
//		url += "/" + collectionId + "/" + objectHash + ".json";
//		url += "?wsKey=" + getApiKey() + "&profile=annotation";
//
//		// Execute Europeana API request
//		String json = getJSONResult(url);
//		
//		return getAnnotationSearchResults(json);
//	}
	
	public AnnotationOperationResponse createAnnotation(Annotation annotation) throws IOException {
		String url = getAnnotationServiceUri();
        String resourceId = (new AnnotationIdHelper()).extractResourceId(annotation);
		url += resourceId;
		url += ModelConst.JSON_REST;
		url += "?collection=" + (new AnnotationIdHelper()).extractCollectionFromResourceId(resourceId) 
				+ "&object=" + (new AnnotationIdHelper()).extractObjectFromResourceId(resourceId) 
				+ "&provider=" + WebAnnotationFields.PROVIDER_WEBANNO;
		// Execute Europeana API request
		String jsonPost = getAnnotationGson().toJson(annotation);
//		String json = getJSONResult(url, ModelConst.ANNOTATION, jsonPost);
		String json = getJSONResultWithBody(url, jsonPost);
		
		//JSONObject jsonObj = (JSONObject) new JSONParser().parse(json);
//		AnnotationOperationResponse aor = new ObjectMapper().readValue(json, AnnotationOperationResponse.class);
		AnnotationOperationResponse aor = new AnnotationOperationResponse();
		aor.setSuccess("true");
		aor.setAction("create:/annotations/collection/object.json");
		String annotationJsonString = JsonUtils.extractAnnotationStringFromJsonString(json);
//		annotationJsonString = annotationJsonString
//				.replace("\"\\\"", "").replace("\\\"\"","").replace(":[",":\"[").replace("],","]\",").replace("]", "]\"");
//		aor.setAnnotation(JsonUtils.toAnnotationObject(annotationJsonString));
		aor.setJson(annotationJsonString);
		return aor;

//		return (AnnotationOperationResponse) getAnnotationGson().fromJson(json,
//				AnnotationOperationResponse.class);
	}

	
	/**
	 * Sample HTTP request
	 * 	   http://localhost:8081/annotation-web/concepts/{uri}.json?uri=myuri.com
	 * @param conceptJson
	 * @return
	 * @throws IOException
	 */
	public ConceptOperationResponse createConcept(String conceptJson) throws IOException {
		String url = getConceptsBaseUri() + WebAnnotationFields.SLASH; // current annotation service uri is .../annotation-web/annotations
		url += WebAnnotationFields.CONCEPT_JSON_REST + WebAnnotationFields.PAR_CHAR;		
		url += "uri=" + JsonUtils.extractUriFromConceptJson(conceptJson);
		// Execute Concept API request
		String json = getJSONResultWithBody(url, conceptJson);
		
		ConceptOperationResponse aor = new ConceptOperationResponse();
		aor.setSuccess("true");
		aor.setAction("create:/concepts/uri.json");
		String conceptJsonString = JsonUtils.extractAnnotationStringFromJsonString(json);
		aor.setJson(conceptJsonString);
		return aor;
	}

	protected String getConceptsBaseUri() {
		return getAnnotationServiceUri().replace("annotations","concepts");
	}

	/**
	 * This method creates AnnotationLd object from JsonLd string.
	 * The HTTP request sample is:
	 *     http://localhost:8081/annotation-web/annotationld.jsonld?provider=historypin&annotationNr=1111&indexing=true
	 * @param provider
	 * @param annotationNr
	 * @param annotationJsonLdStr The Annotation
	 * @return annotation operation response
	 * @throws IOException
	 */
	public AnnotationOperationResponse createAnnotationLd(
			String provider, Long annotationNr, String annotationJsonLdStr) throws IOException {
		
		String url = getAnnotationServiceUri(); // current annotation service uri is .../annotation-web/annotations
		url += WebAnnotationFields.ANNOTATION_LD_JSON_LD_REST + WebAnnotationFields.PAR_CHAR;
		url += WebAnnotationFields.PARAM_WSKEY + WebAnnotationFields.EQUALS + "ws" + WebAnnotationFields.AND;
		url += WebAnnotationFields.PROVIDER + WebAnnotationFields.EQUALS + provider + WebAnnotationFields.AND;
		if (annotationNr != null)
			url += WebAnnotationFields.IDENTIFIER + WebAnnotationFields.EQUALS + annotationNr + WebAnnotationFields.AND;
		url += WebAnnotationFields.INDEXING + WebAnnotationFields.EQUALS + "true";

		/**
		 * Execute Europeana API request
		 */
		String json = getJSONResultWithBody(url, annotationJsonLdStr);
		
		AnnotationOperationResponse aor = new AnnotationOperationResponse();
		aor.setSuccess("true");
		aor.setAction("create:/annotationld.jsonld");
		aor.setJson(json);
		return aor;
	}

	/**
	 * This method creates Europeana Annotation object from JsonLd string.
	 * The HTTP request sample is:
	 *     http://localhost:8081/annotation-web/annotation.jsonld?wskey=ws&provider=historypin&annotationNr=161&indexing=true
	 *     http://localhost:8081/annotation-web/annotation/oa%3Atagging.jsonld?provider=webanno&&indexing=true
	 * @param motivation
	 * @param provider
	 * @param annotationNr
	 * @param europeanaLdStr The Annotation
	 * @return annotation operation response
	 * @throws IOException
	 */
	public AnnotationOperationResponse createEuropeanaAnnotationLd(
			String motivation, String provider, Long annotationNr, String europeanaLdStr) throws IOException {
		
		String url = getAnnotationServiceUri(); // current annotation service uri is .../annotation-web/annotations
//		url += WebAnnotationFields.ANNOTATION_JSON_LD_REST + WebAnnotationFields.PAR_CHAR;
		url += "annotation/" + motivation + ".jsonld" + WebAnnotationFields.PAR_CHAR;
		url += WebAnnotationFields.PARAM_WSKEY + WebAnnotationFields.EQUALS + "ws" + WebAnnotationFields.AND;
		url += WebAnnotationFields.PROVIDER + WebAnnotationFields.EQUALS + provider + WebAnnotationFields.AND;
		if (annotationNr != null)
			url += WebAnnotationFields.IDENTIFIER + WebAnnotationFields.EQUALS + annotationNr + WebAnnotationFields.AND;
		url += WebAnnotationFields.INDEXING + WebAnnotationFields.EQUALS + "true";

		/**
		 * Execute Europeana API request
		 */
		String json = getJSONResultWithBody(url, europeanaLdStr);
		
		AnnotationOperationResponse aor = new AnnotationOperationResponse();
		aor.setSuccess("true");
		aor.setAction("create:/annotation.jsonld");
		
		
//		String annotationJsonString = JsonUtils.extractAnnotationStringFromJsonString(json);
//		aor.setAnnotation(JsonUtils.toAnnotationObject(annotationJsonString));
//		aor.setAnnotation(JsonUtils.toAnnotationObject(json));
		aor.setJson(json);
		return aor;
		
//		return (AnnotationOperationResponse) getAnnotationGson().fromJson(json,
//				AnnotationOperationResponse.class);
//		int jj = 4;
//		AnnotationSearchResults asr = getAnnotationSearchResults(json);
//		return null;
	}

	
	/**
	 * This method creates Annotation object from JsonLd string.
	 * Example HTTP request for tag object: 
	 *      http://localhost:8080/annotation/?wskey=apidemo&provider=webanno&&indexOnCreate=false
	 * and for tag object with type jsonld
	 *     http://localhost:8080/annotation/tag.jsonld?wskey=apidemo&provider=webanno&&indexOnCreate=false
	 * @param wskey
	 * @param provider
	 * @param identifier
	 * @param indexOnCreate
	 * @param annotation The Annotation body
	 * @param userToken
	 * @param annoType
	 * @return response entity that comprises response body, headers and status code.
	 * @throws IOException
	 */
	public ResponseEntity<String> createAnnotation(
			String wskey, String provider, String identifier, Boolean indexOnCreate, 
			String annotation, String userToken, String annoType) throws IOException {
		
		String url = getAnnotationServiceUri();
		if(!url.endsWith(WebAnnotationFields.SLASH))
			url +=  WebAnnotationFields.SLASH;
		if (annoType != null)
			url += annoType + WebAnnotationFields.JSON_LD_REST;
		url += WebAnnotationFields.PAR_CHAR;
		url += WebAnnotationFields.PARAM_WSKEY + WebAnnotationFields.EQUALS + wskey + WebAnnotationFields.AND;
		url += WebAnnotationFields.PROVIDER + WebAnnotationFields.EQUALS + provider + WebAnnotationFields.AND;
		if (identifier != null)
			url += WebAnnotationFields.IDENTIFIER + WebAnnotationFields.EQUALS + identifier + WebAnnotationFields.AND;
		url += WebAnnotationFields.USER_TOKEN + WebAnnotationFields.EQUALS + userToken + WebAnnotationFields.AND;
		if(indexOnCreate != null )
			url += WebAnnotationFields.INDEX_ON_CREATE + WebAnnotationFields.EQUALS + indexOnCreate;		
		
		logger.trace("Ivoking create annotation: " + url);
		/**
		 * Execute Europeana API request
		 */
		return postURL(url, annotation);		
	}

	
	/**
	 * This method creates Annotation report object.
	 * Example HTTP request for tag object: 
	 *      http://localhost:8080/annotation/webanno/1222/report?wskey=apiadmin&userToken=tester1
	 * @param wskey
	 * @param provider
	 * @param identifier
	 * @param userToken
	 * @return response entity that comprises response body, headers and status code.
	 * @throws IOException
	 */
	public ResponseEntity<String> createAnnotationReport(
			String wskey, String provider, String identifier, String userToken) throws IOException {
		
		String url = getAnnotationServiceUri();
		if(!url.endsWith(WebAnnotationFields.SLASH))
			url +=  WebAnnotationFields.SLASH;
		url += provider + WebAnnotationFields.SLASH;
		url += identifier + WebAnnotationFields.SLASH;
		url += WebAnnotationFields.PATH_FIELD_REPORT;
		url += WebAnnotationFields.PAR_CHAR;
		url += WebAnnotationFields.PARAM_WSKEY + WebAnnotationFields.EQUALS + wskey + WebAnnotationFields.AND;
		url += WebAnnotationFields.USER_TOKEN + WebAnnotationFields.EQUALS + userToken;
		
		logger.trace("Ivoking create annotation report: " + url);
		/**
		 * Execute Europeana API request
		 */
		return postURL(url, "");		
	}

	
	/**
	 * This method retrieves Annotation object.
	 * Example HTTP request for tag object: 
	 *      http://localhost:8080/annotation/webanno/497?wskey=apidemo
	 * and for tag object with type jsonld
	 *     http://localhost:8080/annotation/webanno/497.jsonld?wskey=apidemo
	 * @param wskey
	 * @param provider
	 * @param identifier
	 * @return response entity that comprises response body, headers and status code.
	 * @throws IOException
	 */
	public ResponseEntity<String> getAnnotation(
			String wskey, String provider, String identifier, boolean isTypeJsonld) throws IOException {
		
		String url = getAnnotationServiceUri() + WebAnnotationFields.SLASH;
		url += provider + WebAnnotationFields.SLASH;
    	url += identifier;
		if (isTypeJsonld)
			url += WebAnnotationFields.JSON_LD_REST;
		url += WebAnnotationFields.PAR_CHAR;
		url += WebAnnotationFields.PARAM_WSKEY + WebAnnotationFields.EQUALS + wskey + WebAnnotationFields.AND;
		
		/**
		 * Execute Europeana API request
		 */
		return getURL(url);		
	}


	/**
	 * This method retrieves summary of the Annotation moderation report object.
	 * Example HTTP request for tag object: 
	 *      http://localhost:8080/annotation/webanno/1202/moderationsummary?wskey=apiadmin&userToken=tester1
	 * @param wskey
	 * @param provider
	 * @param identifier
	 * @param userToken
	 * @return response entity that comprises response body, headers and status code.
	 * @throws IOException
	 */
	public ResponseEntity<String> getModerationReport(
			String wskey, String provider, String identifier, String userToken) throws IOException {
		
		String url = getAnnotationServiceUri();
		if(!url.endsWith(WebAnnotationFields.SLASH))
			url +=  WebAnnotationFields.SLASH;
		url += provider + WebAnnotationFields.SLASH;
		url += identifier + WebAnnotationFields.SLASH;
		url += WebAnnotationFields.PATH_FIELD_MODERATION_SUMMARY;
		url += WebAnnotationFields.PAR_CHAR;
		url += WebAnnotationFields.PARAM_WSKEY + WebAnnotationFields.EQUALS + wskey + WebAnnotationFields.AND;
		url += WebAnnotationFields.USER_TOKEN + WebAnnotationFields.EQUALS + userToken;
		
		logger.trace("Ivoking get annotation moderation report: " + url);
		
		/**
		 * Execute Europeana API request
		 */
		return getURL(url);		
	}


	/**
	 * This method updates Annotation object by the passed JsonLd update string.
	 * Example HTTP request: 
	 *      http://localhost:8080/annotation/{identifier}.jsonld?wskey=apidemo&identifier=http%3A%2F%2Fdata.europeana.eu%2Fannotation%2Fwebanno%2F496
	 * where identifier is:
	 *     http://data.europeana.eu/annotation/webanno/496
	 * and the update JSON string is:
	 *     { "body": "Buccin Trombone","target": "http://data.europeana.eu/item/09102/_UEDIN_2214" }
	 * @param wskey
	 * @param identifier The identifier URL that comprise annotation provider and ID
	 * @param updateAnnotation The update Annotation body in JSON format
	 * @param userToken
	 * @return response entity that comprises response body, headers and status code.
	 * @throws IOException
	 */
	public ResponseEntity<String> updateAnnotation(
			String wskey, String identifier, String updateAnnotation, String userToken) throws IOException {
		
		String url = getAnnotationServiceUri() + WebAnnotationFields.SLASH;
		url += encodeUrl("{") + WebAnnotationFields.IDENTIFIER + encodeUrl("}") + WebAnnotationFields.JSON_LD_REST;
		url += WebAnnotationFields.PAR_CHAR;
		url += WebAnnotationFields.PARAM_WSKEY + WebAnnotationFields.EQUALS + wskey + WebAnnotationFields.AND;
		if (identifier != null)
			url += WebAnnotationFields.IDENTIFIER + WebAnnotationFields.EQUALS + encodeUrl(identifier) + WebAnnotationFields.AND;
		url += WebAnnotationFields.USER_TOKEN + WebAnnotationFields.EQUALS + userToken;
		
		/**
		 * Execute Europeana API request
		 */
		return putURL(url, updateAnnotation);		
	}

	/**
	 * This method updates Annotation object by the passed JsonLd update string.
	 * Example HTTP request: 
	 *      http://localhost:8080/annotation/{provider}/{identifier}?wskey=apidemo
	 * where identifier is:
	 *     http://data.europeana.eu/annotation/webanno/496
	 * and the update JSON string is:
	 *     { "body": "Buccin Trombone","target": "http://data.europeana.eu/item/09102/_UEDIN_2214" }
	 * @param wskey
	 * @param provider
	 * @param identifier The identifier URL that comprise annotation provider and ID
	 * @param updateAnnotation The update Annotation body in JSON format
	 * @param userToken
	 * @return response entity that comprises response body, headers and status code.
	 * @throws IOException
	 */
	public ResponseEntity<String> updateAnnotation(
			String wskey, String provider, String identifier, String updateAnnotation, String userToken, String format) throws IOException {
		
		//TODO:refactor to use an abstract implementation for building client urls 
		String url = getAnnotationServiceUri();
		url += WebAnnotationFields.SLASH + provider;
		url += WebAnnotationFields.SLASH + identifier;
		if(format != null)
			url+="."+format;	
		
		url += WebAnnotationFields.PAR_CHAR;
		url += WebAnnotationFields.PARAM_WSKEY + WebAnnotationFields.EQUALS + wskey + WebAnnotationFields.AND;
		url += WebAnnotationFields.USER_TOKEN + WebAnnotationFields.EQUALS + userToken;
		
		
		/**
		 * Execute Europeana API request
		 */
		return putURL(url, updateAnnotation);		
	}
	/**
	 * @Deprecated see new specifications for WebAnnotationProtocol
	 * This method deletes Annotation object by the passed identifier.
	 * Example HTTP request: 
	 *      http://localhost:8080/annotation/{identifier}.jsonld?wskey=apidemo&identifier=http%3A%2F%2Fdata.europeana.eu%2Fannotation%2Fwebanno%2F494
	 * where identifier is:
	 *     http://data.europeana.eu/annotation/webanno/494
	 * @param wskey
	 * @param identifier The identifier URL that comprise annotation provider and ID
	 * @param userToken
	 * @param format 
	 * @return response entity that comprises response headers and status code.
	 * @throws IOException
	 * @deprecated TODO: remove deprecation or method when the json API will be enabled
	 */
	public ResponseEntity<String> deleteAnnotation(
			String wskey, String identifier, String userToken) throws IOException {
		
		String url = getAnnotationServiceUri() + WebAnnotationFields.SLASH;
		url += encodeUrl("{") + WebAnnotationFields.IDENTIFIER + encodeUrl("}") + WebAnnotationFields.JSON_LD_REST;
		url += WebAnnotationFields.PAR_CHAR;
		url += WebAnnotationFields.PARAM_WSKEY + WebAnnotationFields.EQUALS + wskey + WebAnnotationFields.AND;
		if (identifier != null)
			url += WebAnnotationFields.IDENTIFIER + WebAnnotationFields.EQUALS + encodeUrl(identifier) + WebAnnotationFields.AND;
		url += WebAnnotationFields.USER_TOKEN + WebAnnotationFields.EQUALS + userToken;	
		
		/**
		 * Execute Europeana API request
		 */
		return deleteURL(url);		
	}

	/**
	 * This method deletes Annotation object by the passed identifier.
	 * Example HTTP request: 
	 *      http://localhost:8080/annotation/provider/identifier{.jsonld}?wskey=apidemo
	 * where identifier is:
	 *     http://data.europeana.eu/annotation/webanno/494
	 * @param wskey
	 * @param identifier The identifier URL that comprise annotation provider and ID
	 * @param userToken
	 * @param format 
	 * @return response entity that comprises response headers and status code.
	 * @throws IOException
	 */
	public ResponseEntity<String> deleteAnnotation(
			String wskey, String provider, String identifier, String userToken, String format) throws IOException {
		
		String url = getAnnotationServiceUri();
		url += WebAnnotationFields.SLASH + provider;
		url += WebAnnotationFields.SLASH + identifier;
		if(format != null)
			url+="."+format;	
		
		url += WebAnnotationFields.PAR_CHAR;
		url += WebAnnotationFields.PARAM_WSKEY + WebAnnotationFields.EQUALS + wskey;
//		if (identifier != null)
//			url += WebAnnotationFields.IDENTIFIER + WebAnnotationFields.EQUALS + encodeUrl(identifier);
		
		if(userToken != null)
			url +=  WebAnnotationFields.AND + WebAnnotationFields.USER_TOKEN + WebAnnotationFields.EQUALS + userToken;
		
		/**
		 * Execute Europeana API request
		 */
		return deleteURL(url);		
	}

	
	/**
	 * This method creates Annotation object from JsonLd string.
	 * @param annotationJsonLdStr The Annotation
	 * @return annotation operation response
	 * @throws IOException
	 */
	public AnnotationOperationResponse createAnnotation(String annotationJsonLdStr) throws IOException {
//		String url = getAnnotationServiceUri();
//		url += JsonUtils.extractEuropeanaIdFromJsonLdStr(annotationJsonLdStr);
//		url += ModelConst.JSON_REST;

		String url = getAnnotationServiceUri(); // current annotation service uri is .../annotation-web/annotations
		url += WebAnnotationFields.ANNOTATION_JSON_LD_REST + WebAnnotationFields.PAR_CHAR;
		url += WebAnnotationFields.PARAM_WSKEY + WebAnnotationFields.EQUALS + "ws" + WebAnnotationFields.AND;
//		url += WebAnnotationFields.PROVIDER + WebAnnotationFields.EQUALS + provider + WebAnnotationFields.AND;
//		if (annotationNr != null)
//			url += WebAnnotationFields.ANNOTATION_NR + WebAnnotationFields.EQUALS + annotationNr + WebAnnotationFields.AND;
//		url += WebAnnotationFields.INDEXING + WebAnnotationFields.EQUALS + "true";
		
		
		/**
		 * Execute Europeana API request
		 */
		String json = getJSONResultWithBody(url, annotationJsonLdStr);		
//		String json = getJSONResult(url, ModelConst.ANNOTATION, annotationJsonLdStr);
		
		AnnotationOperationResponse aor = new AnnotationOperationResponse();
		aor.setSuccess("true");
		aor.setAction("create:/annotations/collection/object.json");
		String annotationJsonString = JsonUtils.extractAnnotationStringFromJsonString(json);
		aor.setAnnotation(JsonUtils.toAnnotationObject(annotationJsonString));
		return aor;
	}

	/**
	 * This method returns a list of Annotation objects for the passed query.
     * E.g. /annotation-web/annotations/search?
     *     wskey=key&profile=webtest&value=vlad&field=all&language=en&startOn=0&limit=&search=search	 
     * @param query The query string 
     * @param page Start page
     * @param pageSize Page size
	 * @return annotation operation response
	 * @throws IOException
	 * @throws JsonParseException 
	 */
	public AnnotationPage search(String query, String page, String pageSize) 
			throws IOException, JsonParseException {
		
		return search(query, page, pageSize, SearchProfiles.STANDARD); 
		
	}
	

	/**
	 * This method returns a list of Annotation objects for the passed query according to the given search profile.
     * E.g. /annotation-web/annotations/search?wskey=key&profile=webtest&value=vlad&field=all&language=en&startOn=0&limit=&search=search	 
     * @param query The query string 
	 * @return AnnotationPage object
	 * @throws IOException
	 * @throws JsonParseException 
	 */
	public AnnotationPage search(String query, String page, String pageSize, SearchProfiles searchProfile) 
			throws IOException, JsonParseException {
		String url = buildUrl(query, page, pageSize, searchProfile.toString()); 
		
		// Execute Europeana API request
		String json = getJSONResult(url);
		
		return getAnnotationPage(json);
	}


	/**
	 * This method converts json response in AnnotationSearchResults.
	 * @param json
	 * @return AnnotationSearchResults
	 * @throws JsonParseException 
	 */
	public AnnotationPage getAnnotationPage(String json) throws JsonParseException {
		AnnotationPageParser apParser = new AnnotationPageParser();
		AnnotationPage ap = apParser.parseAnnotationPage(json);
		return ap;
	}

	/**
	 * This method converts json response in AnnotationSearchResults.
	 * @param json
	 * @return AnnotationSearchResults
	 */
	public AnnotationSearchResults getAnnotationSearchResults(String json) {
		AnnotationSearchResults asr = new AnnotationSearchResults();
		asr.setSuccess("true");
		asr.setAction("create:/annotations/search");
		String annotationListJsonString = JsonUtils.extractAnnotationListStringFromJsonString(json, "\":(.*?)}]");
		if (StringUtils.isNotEmpty(annotationListJsonString)) {
	        if (!annotationListJsonString.isEmpty()) {
	        	annotationListJsonString = 
	        			annotationListJsonString.substring(1, annotationListJsonString.length() - 1); // remove braces
		        String[] arrValue = JsonLdParser.splitAnnotationListStringToArray(annotationListJsonString);
		        List<Annotation> annotationList = new ArrayList<Annotation>();
		        for (String annotationJsonString : arrValue) {
		        	if (!annotationJsonString.startsWith("{"))
		        		annotationJsonString = "{" + annotationJsonString;
		    		Annotation annotationObject = JsonUtils.toAnnotationObject(annotationJsonString + "}}");
					annotationList.add(annotationObject);
		    	}
		        asr.setItems(annotationList);
	        }
		}
		return asr;
	}

	/**
	 * This method constructs the url dependent on search parameters.
	 * @param query Query
	 * @param page Start page
	 * @param pageSize Page size
	 * @param profile Query profile
	 * @return query Query URL
	 */
	private String buildUrl(String query, String page, String pageSize, String profile)  throws IOException {
		String url = getAnnotationServiceUri();
		url += "/search?wskey=" + getApiKey() + "&profile=" + profile;
		if (StringUtils.isNotEmpty(query)) {
			url += "&query=" + encodeUrl(query);
		}
		if (StringUtils.isNotEmpty(page))
			url += "&page=" + page;
		else
			url += "&page=0";
		if (StringUtils.isNotEmpty(pageSize))
			url += "&pageSize=" + pageSize;
		else
			url += "&pageSize=10";
		return url;
	}

	public AnnotationPage search(String query) throws IOException, JsonParseException {
		return search(query, null, null);
	}

	/**
	 * This method returns a list of Tag objects for the passed query.
     * Sample url: "http://localhost:8081/annotation-web/tags/search?value=Vlad&field=multilingual&startOn=0&limit=10&language=en";
	 * @param query The query string
	 * @return tag search results
	 * @throws IOException
	 */
	public TagSearchResults searchTags(String query, String startOn, String limit) throws IOException {
		
		String url = buildUrl(query, startOn, limit, ModelConst.TAG);
		url = url.replace("annotations", "tags");
		
		/**
		 * Execute Europeana API request
		 */
		String json = getJSONResult(url);
		
		TagSearchResults tsr = new TagSearchResults();
		tsr.setSuccess("true");
		tsr.setAction("create:/tags/search");
		String tagListJsonString = JsonUtils.extractAnnotationListStringFromJsonString(json, "\":(.*?)}]");
		if (StringUtils.isNotEmpty(tagListJsonString)) {
	        if (!tagListJsonString.isEmpty()) {
	        	tagListJsonString = 
	        			tagListJsonString.substring(1, tagListJsonString.length() - 2); // remove braces
		        String[] arrValue = JsonLdParser.splitAnnotationListStringToArray(tagListJsonString);
		        List<TagResource> tagList = new ArrayList<TagResource>();
		        for (String tagJsonStringElem : arrValue) {
		        	if (!tagJsonStringElem.startsWith("{"))
		        		tagJsonStringElem = "{" + tagJsonStringElem;
		        	if (!tagJsonStringElem.endsWith("}"))
		        		tagJsonStringElem = tagJsonStringElem + "}";
		        	TagResource tagObject = toTagObject(tagJsonStringElem);
					tagList.add(tagObject);
		    	}
		        tsr.setItems(tagList);
	        }
		}
		return tsr;
	}

	/**
	 * This method converts json string into TagResource object.
	 * @param json
	 * @return tag object
	 */
	public TagResource toTagObject(String json) {
		BaseTagResource tag = new BaseTagResource();
		String id = JsonUtils.extractValueFromJsonString(WebAnnotationFields.ID, json);
		if (StringUtils.isNotEmpty(id))
			tag.setId(id);
		String label = JsonUtils.extractValueFromJsonString(WebAnnotationFields.LABEL, json);
		if (StringUtils.isNotEmpty(label))
			tag.setLabel(label);
		String httpUri = JsonUtils.extractValueFromJsonString(WebAnnotationFields.HTTP_URI, json);
		if (StringUtils.isNotEmpty(httpUri))
			tag.setHttpUri(httpUri);
		String value = JsonUtils.extractValueFromJsonString(WebAnnotationFields.VALUE, json);
		if (StringUtils.isNotEmpty(value))
			tag.setValue(value);
		String language = JsonUtils.extractValueFromJsonString(WebAnnotationFields.LANGUAGE, json);
		if (StringUtils.isNotEmpty(language))
			tag.setLanguage(language);
		return tag;
	}
	
	public TagSearchResults searchTags(String query) throws IOException {
	    return searchTags(query, null, null);
	}	
		
	public AnnotationOperationResponse getAnnotation(
			String europeanaId, String provider, String identifier) throws IOException {
		
		String url = getAnnotationServiceUri();
		if(!europeanaId.startsWith(WebAnnotationFields.SLASH))
			url += WebAnnotationFields.SLASH ;
		
		url += europeanaId;
		url += WebAnnotationFields.SLASH + provider;
		url += WebAnnotationFields.SLASH + identifier;
		
		url += "?wsKey=" + getApiKey() + "&profile=annotation";

		// Execute Europeana API request
		String json = getJSONResult(url);
		
		return (AnnotationOperationResponse) getAnnotationGson().fromJson(json,
				AnnotationOperationResponse.class);

	}

	public ConceptOperationResponse getConcept(
			String uri) throws IOException {
		
		String url = getConceptsBaseUri() + WebAnnotationFields.SLASH; // current annotation service uri is .../annotation-web/annotations
		url += WebAnnotationFields.CONCEPT_JSON_REST + WebAnnotationFields.PAR_CHAR;		
		url += "uri=" + uri;

		// Execute Europeana API request
		String json = getJSONResult(url);
		ConceptOperationResponse cor = new ConceptOperationResponse();
		if (json != null)
			cor.setSuccess("true");
		cor.setJson(json);
		return cor;
//		return (ConceptOperationResponse) getAnnotationGson().fromJson(json,
//				ConceptOperationResponse.class);
	}


	/**
	 * Sample HTTP request:
	 *     //http://localhost:8081/annotation-web/annotations/set/status/{provider}/{annotationNr}.json?provider=webanno&annotationNr=214&status=public
	 *     http://localhost:8081/annotation-web/admin/set/status/{provider}/{annotationNr}.json?provider=webanno&annotationNr=294&status=public
	 * @param provider
	 * @param identifier
	 * @param status
	 * @return
	 * @throws IOException
	 */
	public AnnotationOperationResponse setAnnotationStatus(String provider, String identifier, String status) throws IOException {
		String url = getAnnotationServiceUri().replace("annotations","admin"); // current annotation service uri is .../annotation-web/annotations
//		String url = getAnnotationServiceUri(); 
//		url += "/set/status/{provider}/{annotationNr}.json" + WebAnnotationFields.PAR_CHAR;
//		url += "/set/status/provider/annotationNr.json" + WebAnnotationFields.PAR_CHAR;
		url += "/set/status/" + provider + "/" + identifier + ".json" + WebAnnotationFields.PAR_CHAR;
//		url += "/set/status.json" + WebAnnotationFields.PAR_CHAR;
//		url += WebAnnotationFields.WSKEY + WebAnnotationFields.EQUALS + "ws" + WebAnnotationFields.AND;
		url += WebAnnotationFields.PROVIDER + WebAnnotationFields.EQUALS + provider + WebAnnotationFields.AND;
		if (identifier != null)
			url += WebAnnotationFields.IDENTIFIER + WebAnnotationFields.EQUALS + identifier + WebAnnotationFields.AND;
		url += WebAnnotationFields.STATUS + WebAnnotationFields.EQUALS + status;

//		String json = getJSONResult(url);
		String json = getJSONResultWithBody(url, status);		
		
		AnnotationOperationResponse aor = new AnnotationOperationResponse();
		aor.setSuccess("true");
		aor.setAction("set:/annotations/set/status/object.json");
		aor.setJson(json);
		return aor;
	}

	/**
	 * Sample HTTP request:
	 *     http://localhost:8081/annotation-web/statuslogs/search?status=private&startOn=0&limit=10
	 * @param status
	 * @param startOn
	 * @param limit
	 * @return
	 * @throws IOException
	 */
	public AnnotationOperationResponse searchAnnotationStatusLogs(String status, String startOn, String limit) throws IOException {
		String url = getAnnotationServiceUri(); // current annotation service uri is .../annotation-web/annotations
		url += "/statuslogs/search" + WebAnnotationFields.PAR_CHAR;
		url += WebAnnotationFields.STATUS + WebAnnotationFields.EQUALS + status + WebAnnotationFields.AND;
		url += WebAnnotationFields.START_ON + WebAnnotationFields.EQUALS + startOn + WebAnnotationFields.AND;
		url += WebAnnotationFields.LIMIT + WebAnnotationFields.EQUALS + limit;

		String json = getJSONResult(url);
		
		AnnotationOperationResponse aor = new AnnotationOperationResponse();
		aor.setSuccess("true");
		aor.setAction("search:/statuslogs.json");
		aor.setJson(json);
		return aor;
	}

	/**
	 * Sample HTTP request:
	 *     http://localhost:8081/annotation-web/annotations/get/status/{provider}/{annotationNr}.json?provider=webanno&annotationNr=214
	 * @param provider
	 * @param identifier
	 * @param status
	 * @return
	 * @throws IOException
	 */
	public AnnotationOperationResponse getAnnotationStatus(String provider, String identifier) throws IOException {
		String url = getAnnotationServiceUri(); 
		url += "/get/status/" + provider + "/" + identifier + ".json" + WebAnnotationFields.PAR_CHAR;
		url += WebAnnotationFields.PROVIDER + WebAnnotationFields.EQUALS + provider + WebAnnotationFields.AND;
		if (identifier != null)
			url += WebAnnotationFields.IDENTIFIER + WebAnnotationFields.EQUALS + identifier;

		String json = getJSONResult(url);
		
		AnnotationOperationResponse aor = new AnnotationOperationResponse();
		aor.setSuccess("true");
		aor.setAction("get:/annotations/get/status/object.json");
		aor.setJson(json);
		return aor;
	}

		
	/**
	 * Sample HTTP request:
	 *     http://localhost:8081/annotation-web/admin/annotation/disable/webanno/441.json
	 * @param provider
	 * @param identifier
	 * @return
	 * @throws IOException
	 */
	public AnnotationOperationResponse disableAnnotation(String provider, String identifier) throws IOException {
		String url = getAnnotationServiceUri(); // current annotation service uri is .../annotation-web/annotations 
		url += "/admin/annotation/disable/" + provider + "/" + identifier + ".json";
//		url += "/admin/annotation/disable/" + provider + "/" + annotationNr + ".json" + WebAnnotationFields.PAR_CHAR;
//		url += "/admin/annotation/disable" + WebAnnotationFields.PAR_CHAR;
//		url += WebAnnotationFields.PROVIDER + WebAnnotationFields.EQUALS + provider + WebAnnotationFields.AND;
//		if (annotationNr != null)
//			url += WebAnnotationFields.ANNOTATION_NR + WebAnnotationFields.EQUALS + annotationNr;

//		String json = getJSONResult(url);
		String json = getJSONResultWithBody(url, "");		

		
		AnnotationOperationResponse aor = new AnnotationOperationResponse();
		aor.setSuccess("true");
		aor.setAction("put:/admin/annotation/disable/object.json");
		aor.setJson(json);
		return aor;
	}

		
	/**
	 * Sample HTTP request:
	 *     http://localhost:8081/annotation-web/annotations/check/visibility/{provider}/{annotationNr}/{user}.json?provider=webanno&annotationNr=407&
	 * @param provider
	 * @param annotationNr
	 * @return
	 * @throws IOException
	 */
	public AnnotationOperationResponse checkVisibility(Annotation annotation, String user) throws IOException {
//		public AnnotationOperationResponse checkVisibility(String provider, Long annotationNr) throws IOException {
		String url = getAnnotationServiceUri();
		String provider = annotation.getAnnotationId().getProvider();
		String identifier = annotation.getAnnotationId().getIdentifier();
		url += "/check/visibility/" + provider + "/" + identifier + "/" + user + ".json" + WebAnnotationFields.PAR_CHAR;
		url += WebAnnotationFields.PROVIDER + WebAnnotationFields.EQUALS + provider + WebAnnotationFields.AND;
		if (identifier != null)
			url += WebAnnotationFields.IDENTIFIER + WebAnnotationFields.EQUALS + identifier + WebAnnotationFields.AND;
		url += WebAnnotationFields.USER + WebAnnotationFields.EQUALS + user;

		String json = getJSONResult(url);
		
		AnnotationOperationResponse aor = new AnnotationOperationResponse();
		aor.setSuccess("true");
		aor.setAction("get:/annotations/check/visibility/object.json");
		aor.setJson(json);
		return aor;
	}

		
	/**
	 * Sample HTTP request
	 *     http://localhost:8080/whitelist/create?apiKey=apidemo
	 * @param whitelistEntryJson
	 * @return WhitelistOperationResponse
	 * @throws IOException
	 */
	public WhitelistOperationResponse createWhitelistEntry(String whitelistEntryJson) throws IOException {
		String action = "create";
		String url = getWhitelistServiceUrl(action);
		
		// Execute Whitelist API request
		String json = getJSONResultWithBody(url, whitelistEntryJson);
		
		WhitelistOperationResponse aor = new WhitelistOperationResponse();
		aor.setSuccess("true");
		aor.setAction("create:/whitelist");
		String whitelistJsonString = "";
		try {
			JSONObject mainObject = new JSONObject(json);
			JSONObject whitelistEntry = mainObject.getJSONObject("whitelistEntry");
			whitelistJsonString = whitelistEntry.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		aor.setWhitelistEntry(WhiteListParser.toWhitelistEntry(whitelistJsonString));
		aor.setJson(json);
		return aor;
	}

	protected String getWhitelistServiceBaseUri() {
		return getAnnotationServiceUri().replace("annotation","whitelist") + "/";
	}

	
	/**
	 * Sample HTTP request
	 *     http://localhost:8080/whitelist/load?apiKey=apidemo
	 * @return WhitelistOperationResponse
	 * @throws IOException
	 */
	public WhitelistOperationResponse loadWhitelist() throws IOException {
		String action = "load"; 
		String url = getWhitelistServiceUrl(action);	

		
		// Execute Whitelist API request
		String json = getJSONResult(url);
		
		WhitelistOperationResponse aor = new WhitelistOperationResponse();
		aor.setSuccess("true");
		aor.setAction("load:/whitelist");
		List<WhitelistEntry> resList = new ArrayList<WhitelistEntry>();
		try {
			JSONObject mainObject = new JSONObject(json);
			JSONArray whitelistEntries = mainObject.getJSONArray("items");
			for (int i=0; i < whitelistEntries.length(); i++) {
			    JSONObject entry = (JSONObject) whitelistEntries.get(0);
			    WhitelistEntry whitelistEntry = WhiteListParser.toWhitelistEntry(entry.toString());
			    resList.add(whitelistEntry);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		aor.setWhitelistEntries(resList);
		aor.setJson(json);
		return aor;
	}

	protected String getWhitelistServiceUrl(String action) {
		String url = getWhitelistServiceBaseUri()
				+ action; 
		url += WebAnnotationFields.PAR_CHAR + WebAnnotationFields.PARAM_WSKEY + "=" 
				+ getAdminApiKey();
		//TODO: add admin user tocken to properties
		url += WebAnnotationFields.AND + WebAnnotationFields.USER_TOKEN +"=admin";
		return url;
	}

	
	/**
	 * Sample HTTP request
	 *     http://localhost:8080/whitelist/search?apiKey=apidemo&url=http%3A%2F%2Ftest.data.europeana.eu
	 * @param httpUrl
	 * @return WhitelistOperationResponse
	 * @throws IOException
	 */
	public WhitelistOperationResponse getWhitelistEntry(
			String httpUrl) throws IOException {
		
		String action = "search";
		String url = getWhitelistServiceUrl(action);	

		url += WebAnnotationFields.AND + "url=" + httpUrl;
		// Execute Whitelist API request
		String json = getJSONResult(url);
		
		WhitelistOperationResponse aor = new WhitelistOperationResponse();
		aor.setSuccess("true");
		aor.setAction("search:/whitelist");
		String whitelistJsonString = "";
		try {
			JSONObject mainObject = new JSONObject(json);
			JSONObject whitelistEntry = mainObject.getJSONObject("whitelistEntry");
			whitelistJsonString = whitelistEntry.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		aor.setWhitelistEntry(WhiteListParser.toWhitelistEntry(whitelistJsonString));
		aor.setJson(json);
		return aor;
	}

	
	/**
	 * Sample HTTP request
	 *     http://localhost:8080/whitelist/view?apiKey=apidemo
	 * @return WhitelistOperationResponse
	 * @throws IOException
	 */
	public WhitelistOperationResponse getWhitelist() throws IOException {
		
		String action = "view";
		String url = getWhitelistServiceUrl(action);	
	
		
		// Execute Whitelist API request
		String json = getJSONResult(url);
		
		WhitelistOperationResponse aor = new WhitelistOperationResponse();
		aor.setSuccess("true");
		aor.setAction("view:/whitelist");
		List<WhitelistEntry> resList = new ArrayList<WhitelistEntry>();
		try {
			JSONObject mainObject = new JSONObject(json);
			JSONArray whitelistEntries = mainObject.getJSONArray("items");
			for (int i=0; i < whitelistEntries.length(); i++) {
			    JSONObject entry = (JSONObject) whitelistEntries.get(0);
			    WhitelistEntry whitelistEntry = WhiteListParser.toWhitelistEntry(entry.toString());
			    resList.add(whitelistEntry);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		aor.setWhitelistEntries(resList);
		aor.setJson(json);
		return aor;
	}

	
	/**
	 * Sample HTTP request
	 * 	   http://localhost:8080/whitelist/delete?apiKey=apidemo&url=http%3A%2F%2Ftest.data.europeana.eu
	 * @param httpUrl
	 * @return ResponseEntity<String>
	 * @throws IOException
	 */
	public ResponseEntity<String> deleteWhitelistEntry(
			String httpUrl) throws IOException {
		
		String action = "delete";
		String url = getWhitelistServiceUrl(action);	
	
				
		url += WebAnnotationFields.AND + "url=" + httpUrl;
		// Execute Whitelist API request
		return deleteURL(url);
	}

	
	/**
	 * Sample HTTP request
	 * 	   http://localhost:8080/whitelist/deleteall?apiKey=apidemo
	 * @return ResponseEntity<String>
	 * @throws IOException
	 */
	public ResponseEntity<String> deleteWholeWhitelist() throws IOException {
		
		String action = "deleteall";
		String url = getWhitelistServiceUrl(action);	

		
		// Execute Whitelist API request
		return deleteURL(url);
	}
	
	
	/**
	 * Sample HTTP request
	 * 	   curl -X DELETE --header 'Accept: application/ld+json' 'http://localhost:8080/admin/annotation/delete?wskey=apiadmin&provider=webanno&identifier=19&userToken=admin'
	 * @return ResponseEntity<String>
	 * @throws IOException
	 */
	public ResponseEntity<String> deleteAnnotation(String provider, String identifier) throws IOException {
		
		String action = "delete";
		
		logger.debug("Annotation service URI: " +getAnnotationServiceUri());	
		String adminAnnotationServiceUri = getAnnotationServiceUri().replace("annotation", "admin/annotation");
		logger.trace("Admin annotation service URI: " +adminAnnotationServiceUri);	
		
		String prov = (provider != null)? provider: "webanno";
		
		String url = adminAnnotationServiceUri+ WebAnnotationFields.SLASH + action ; 	
		url += WebAnnotationFields.PAR_CHAR + WebAnnotationFields.PARAM_WSKEY + "=" + getAdminApiKey();
		url += WebAnnotationFields.AND + WebAnnotationFields.PROVIDER +"=" +  prov;
		url += WebAnnotationFields.AND + WebAnnotationFields.IDENTIFIER +"="+identifier;
		url += WebAnnotationFields.AND + WebAnnotationFields.USER_TOKEN +"=admin";
		
		logger.trace("Delete Annotation request URL: " + url);
		// Execute Annotation delete request
		ResponseEntity<String> re = deleteURL(url);
		logger.trace(re.toString());

		return re;
	}
	
	/**
	 * Sample HTTP request
	 *     curl -X GET --header 'Accept: application/ld+json' 'http://localhost:8080/admin/annotation/reindexoutdated?wskey=apidemo&userToken=admin'
	 * @return ResponseEntity<String>
	 * @throws IOException
	 */
	public ResponseEntity<String> reindexOutdated() throws IOException {
		
		String action = "reindexoutdated";
		
		logger.debug("Annotation service URI: " +getAnnotationServiceUri());	
		String adminAnnotationServiceUri = getAnnotationServiceUri().replace("annotation", "admin/annotation");
		logger.trace("Admin annotation service URI: " +adminAnnotationServiceUri);	
		
		String url = adminAnnotationServiceUri+ WebAnnotationFields.SLASH + action ; 	
		url += WebAnnotationFields.PAR_CHAR + WebAnnotationFields.PARAM_WSKEY + "=" + getAdminApiKey();
		url += WebAnnotationFields.AND + WebAnnotationFields.USER_TOKEN +"=admin";
		
		logger.trace("(Re)index outdated annotations request URL: " + url);
		ResponseEntity<String> res = getHttpConnection().getURL(url);
		logger.trace("(Re)index outdated annotations HTTP status: " + res.getStatusCode().toString());
		
//		AnnotationOperationResponse aor = new AnnotationOperationResponse();
//		aor.setSuccess("true");
//		aor.setAction("get:/annotations/check/visibility/object.json");
//		aor.setJson(json);
		
		return res;
	}
	
	
	
	/**
	 * This method uploads annotations passed as annotations page json.
	 * Example HTTP request for tag object: 
	 *      http://localhost:8080/annotations/?wskey=apidemo&userToken=tester1
	 * @param wskey
	 * @param userToken
	 * @return response entity that comprises response body, headers and status code.
	 * @throws IOException
	 */
	public ResponseEntity<String> uploadAnnotations(
			String wskey, String userToken, String tag, Boolean indexOnCreate, String provider) throws IOException {
		String url = getAnnotationServiceUri()+"s";
		if(!url.endsWith(WebAnnotationFields.SLASH))
			url +=  WebAnnotationFields.SLASH;
		url += WebAnnotationFields.PAR_CHAR + WebAnnotationFields.PARAM_WSKEY + WebAnnotationFields.EQUALS + wskey + WebAnnotationFields.AND;
		url += WebAnnotationFields.USER_TOKEN + WebAnnotationFields.EQUALS + userToken + WebAnnotationFields.AND;
		url += WebAnnotationFields.PROVIDER + WebAnnotationFields.EQUALS + provider + WebAnnotationFields.AND;
		url += WebAnnotationFields.INDEX_ON_CREATE + WebAnnotationFields.EQUALS + indexOnCreate;
		
		logger.debug("Upload annotations request URL: " + url);
		
		/**
		 * Execute Europeana API request
		 */
		return postURL(url, tag);		
	}

}
