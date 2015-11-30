package eu.europeana.annotation.client.integration.webanno;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;

import org.apache.stanbol.commons.exception.JsonParseException;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import eu.europeana.annotation.definitions.model.Annotation;
import eu.europeana.annotation.definitions.model.WebAnnotationFields;


/**
 * This class aims at testing of the annotation methods.
 * @author GrafR
 */
public class WebAnnotationProtocolTest extends BaseWebAnnotationProtocolTest { 

		
	@Test
	public void createWebannoAnnotationTag() throws JsonParseException {
		
		ResponseEntity<String> response = storeTestAnnotation();

		validateResponse(response);
		
	}


	protected void validateResponse(ResponseEntity<String> response) throws JsonParseException {
		validateResponse(response, HttpStatus.CREATED);
	}
	
	protected void validateResponse(ResponseEntity<String> response, HttpStatus status) throws JsonParseException {
		assertNotNull(response.getBody());
		assertEquals(response.getStatusCode(), status);
		
		Annotation storedAnno = getApiClient().parseResponseBody(response);
		assertNotNull(storedAnno.getAnnotationId());
		assertTrue(storedAnno.getAnnotationId().toHttpUrl().startsWith("http://"));
	}
	
	@Test
	public void createWebannoAnnotationLink() throws JsonParseException {
		
		ResponseEntity<String> response = getApiClient().createAnnotation(
				getApiKey()
				, WebAnnotationFields.PROVIDER_WEBANNO
				, null
				, false
				, LINK_JSON
				, TEST_USER_TOKEN
				, null
				);
		
		validateResponse(response);
	}
		

	@Test
	public void createWebannoAnnotationTagByTypeJsonld() throws JsonParseException {
		
		ResponseEntity<String> response = getApiClient().createAnnotation(
				getApiKey()
				, WebAnnotationFields.PROVIDER_WEBANNO
				, null
				, false
				, TAG_JSON_BY_TYPE_JSONLD
				, TEST_USER_TOKEN
				, WebAnnotationFields.TAG
				);
		
		validateResponse(response);
	}
	
	
	@Test
	public void createWebannoAnnotationLinkByTypeJsonld() throws JsonParseException {
		
		ResponseEntity<String> response = getApiClient().createAnnotation(
				getApiKey()
				, WebAnnotationFields.PROVIDER_WEBANNO
				, null
				, false
				, LINK_JSON_BY_TYPE_JSONLD
				, TEST_USER_TOKEN
				, WebAnnotationFields.LINK
				);
		
		validateResponse(response);
	}
		

	@Test
	public void getAnnotation() throws JsonParseException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		ResponseEntity<String> createResponse = storeTestAnnotation(); 
		Annotation annotation = parseAndVerifyTestAnnotation(createResponse);
		/**
		 * get annotation by provider and identifier
		 */
		ResponseEntity<String> response = getApiClient().getAnnotation(
				getApiKey()
				, WebAnnotationFields.PROVIDER_WEBANNO
				, annotation.getAnnotationId().getIdentifier()
				);
		
		//validateResponse(response, HttpStatus.OK);
		Annotation storedAnno = parseAndVerifyTestAnnotation(response, HttpStatus.OK);
		validateOutputAgainstInput(storedAnno, annotation);
	}
	
					
	@Test
	public void updateAnnotation() throws JsonParseException {
				
		/**
		 * store annotation
		 */
		Annotation anno = createTestAnnotation();
		/**
		 * update annotation by identifier URL
		 */
		ResponseEntity<String> response = getApiClient().updateAnnotation(
				getApiKey()
				, anno.getAnnotationId().getProvider()
				, anno.getAnnotationId().getIdentifier()
				, UPDATE_JSON
				, TEST_USER_TOKEN
				);
		
		assertNotNull(response.getBody());
		assertTrue(response.getBody().contains(UPDATE_BODY));
		assertTrue(response.getBody().contains(UPDATE_TARGET));
		assertEquals( HttpStatus.OK, response.getStatusCode());
	}
			
				
	@Test
	public void deleteAnnotation() throws JsonParseException {
				
		/**
		 * store annotation and retrieve its identifier URL
		 */
		Annotation anno = createTestAnnotation();
		
		/**
		 * delete annotation by identifier URL
		 */
		ResponseEntity<String> response = getApiClient().deleteAnnotation(
				getApiKey(), anno.getAnnotationId().getProvider(),
				anno.getAnnotationId().getIdentifier(), TEST_USER_TOKEN, null
				);
		
	//	assertTrue(response.getBody().equals(""));
	//	System.out.println(response.getBody());
		log.debug("Response body: " + response.getBody());
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}
			
				
}