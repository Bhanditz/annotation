/*
 * Copyright 2007-2012 The Europeana Foundation
 *
 *  Licenced under the EUPL, Version 1.1 (the "Licence") and subsequent versions as approved
 *  by the European Commission;
 *  You may not use this work except in compliance with the Licence.
 * 
 *  You may obtain a copy of the Licence at:
 *  http://joinup.ec.europa.eu/software/page/eupl
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under
 *  the Licence is distributed on an "AS IS" basis, without warranties or conditions of
 *  any kind, either express or implied.
 *  See the Licence for the specific language governing permissions and limitations under
 *  the Licence.
 */
package eu.europeana.annotation.web.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.stanbol.commons.exception.JsonParseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.europeana.annotation.definitions.model.agent.Agent;
import eu.europeana.annotation.definitions.model.authentication.Provider;
import eu.europeana.annotation.definitions.model.authentication.impl.BaseProvider;
import eu.europeana.annotation.definitions.model.vocabulary.AgentTypes;
import eu.europeana.annotation.definitions.model.vocabulary.IdGenerationTypes;
import eu.europeana.annotation.definitions.model.vocabulary.WebAnnotationFields;
import eu.europeana.annotation.solr.exceptions.AnnotationServiceException;
import eu.europeana.annotation.web.exception.InternalServerException;
import eu.europeana.annotation.web.exception.ProviderOperationException;
import eu.europeana.annotation.web.model.ProviderOperationResponse;
import eu.europeana.annotation.web.service.AnnotationService;
import eu.europeana.annotation.web.service.authorization.AuthorizationService;
import eu.europeana.api.common.config.I18nConstants;
import eu.europeana.api.commons.web.exception.HttpException;


/**
 * Unit test for the provider management service
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/annotation-web-context.xml", "/annotation-mongo-test.xml"
	})
public class ProviderManagementServiceTest {

	public static String TEST_WS_KEY = "apidemo";
	public static String TEST_USER_TOKEN = "PrivateKey1";
	public static String TEST_NAME = "Europeana.eu";
	public static String TEST_PROVIDER_TYPE = AgentTypes.ORGANIZATION.name();
	
	public final static String TEST_PROVIDER_ID = "100";
	
	/**
	 * Create a test JsonLd provider object string.
	 */
    public static String providerJsonLdObjectString = 
    	"{\"@context\": \"http://www.w3.org/ns/anno.jsonld\","
    	+ "\"id\": \"http://data.europeana.eu/annotation/provider/" + TEST_PROVIDER_ID + "\","
    	+ "\"type\": \"" + TEST_PROVIDER_TYPE + "\","
    	+ "\"name\": \"" + TEST_NAME + "\"}";

	
	@Resource 
	AnnotationService webAnnotationService;
	
	@Resource
	AuthorizationService authorizationService;
	
	public AnnotationService getWebAnnotationService() {
		return webAnnotationService;
	}

	public void setWebAnnotationService(AnnotationService webAnnotationService) {
		this.webAnnotationService = webAnnotationService;
	}			
	
	public AuthorizationService getAuthorizationService() {
		return authorizationService;
	}

	public void setAuthorizationService(AuthorizationService authorizationService) {
		this.authorizationService = authorizationService;
	}

	@Test
	public void testStoreProviderInDbAndDelete() 
			throws MalformedURLException, IOException, AnnotationServiceException, JsonParseException, HttpException {
		
		String name = TEST_NAME;
		String wsKey = TEST_WS_KEY;
		
//		try {
//			getAuthorizationService().authorizeUser(TEST_USER_TOKEN, wsKey, Operations.PROVIDER_CREATE);
//		} catch (Exception e) {
//			fail("Authorization of provider failed!");
//		}
		
		AgentTypes defaultAgentType = AgentTypes.ORGANIZATION;
		Agent webAgent = null;
		try {
			webAgent = getWebAnnotationService().parseAgentLd(defaultAgentType, providerJsonLdObjectString);
		} catch (JsonParseException e) {
			throw new ProviderOperationException(I18nConstants.PROVIDER_CANT_PARSE_BODY, 
					I18nConstants.PROVIDER_CANT_PARSE_BODY, 
					new String[]{WebAnnotationFields.PROVIDER, providerJsonLdObjectString});
		}
		
		/** validate if given id already exists in database */
		if (getWebAnnotationService().existsProviderIdInDb(TEST_PROVIDER_ID)) 
			throw new ProviderOperationException(I18nConstants.PROVIDER_ID_EXISTS, 
					I18nConstants.PROVIDER_ID_EXISTS, 
					new String[]{WebAnnotationFields.PROVIDER, TEST_PROVIDER_ID});
		
		try {
			/** build provider */
			Provider provider = new BaseProvider();
			IdGenerationTypes idGeneration = IdGenerationTypes.getValueByType(
					IdGenerationTypes.GENERATED_BY_PROVIDER.getIdType());
			if (!IdGenerationTypes.isRegistered(idGeneration.name())) {
				fail(ProviderOperationResponse.ERROR_ID_GENERATION_TYPE_DOES_NOT_MATCH 
						+ IdGenerationTypes.printTypes());
			}
			String providerIdUrl = WebAnnotationFields.PROVIDER_ID_PREFIX + TEST_PROVIDER_ID;	

			provider.setApiKey(wsKey);
			provider.setName(webAgent.getName());
			provider.setInternalType(webAgent.getInternalType());
			provider.setType(webAgent.getType());
			provider.setHttpUrl(providerIdUrl);
			provider.setIdGeneration(idGeneration);
			
			/** store */
			Agent storedProvider = getWebAnnotationService().storeProvider(provider);
			assertEquals(storedProvider.getName(), name);

			/** delete */
			int numDeletedProviderEntries = getWebAnnotationService().deleteProviderEntry(providerIdUrl);	
			assertTrue(numDeletedProviderEntries == 1);
			
		} catch (Exception e) {
			throw new InternalServerException(e);
		}		
	}
		
//	@Test
	public void testCheckProviderAlreadyRegisteredByStoring() 
			throws MalformedURLException, IOException, AnnotationServiceException, JsonParseException, HttpException {
		
		String name = TEST_NAME;
		String wsKey = TEST_WS_KEY;
		
//		try {
//			getAuthorizationService().authorizeUser(TEST_USER_TOKEN, wsKey, Operations.PROVIDER_CREATE);
//		} catch (Exception e) {
//			fail("Authorization of provider failed!");
//		}
		
		AgentTypes defaultAgentType = AgentTypes.ORGANIZATION;
		Agent webAgent = null;
		try {
			webAgent = getWebAnnotationService().parseAgentLd(defaultAgentType, providerJsonLdObjectString);
		} catch (JsonParseException e) {
			throw new ProviderOperationException(I18nConstants.PROVIDER_CANT_PARSE_BODY, 
					I18nConstants.PROVIDER_CANT_PARSE_BODY, 
					new String[]{WebAnnotationFields.PROVIDER, providerJsonLdObjectString});
		}
		
		try {
			/** build provider */
			Provider provider = new BaseProvider();
			IdGenerationTypes idGeneration = IdGenerationTypes.getValueByType(
					IdGenerationTypes.GENERATED_BY_PROVIDER.getIdType());
			if (!IdGenerationTypes.isRegistered(idGeneration.name())) {
				fail(ProviderOperationResponse.ERROR_ID_GENERATION_TYPE_DOES_NOT_MATCH 
						+ IdGenerationTypes.printTypes());
			}
			String providerIdUrl = WebAnnotationFields.PROVIDER_ID_PREFIX + TEST_PROVIDER_ID;	

			provider.setApiKey(wsKey);
			provider.setName(webAgent.getName());
			provider.setInternalType(webAgent.getInternalType());
			provider.setType(webAgent.getType());
			provider.setHttpUrl(providerIdUrl);
			provider.setIdGeneration(idGeneration);
			
			/** store */
			Agent storedProvider = getWebAnnotationService().storeProvider(provider);
			assertEquals(storedProvider.getName(), name);

			/** validate if given id already exists in database */
			if (getWebAnnotationService().existsProviderIdInDb(providerIdUrl)) 
				throw new ProviderOperationException(I18nConstants.PROVIDER_ID_EXISTS, 
						I18nConstants.PROVIDER_ID_EXISTS, 
						new String[]{WebAnnotationFields.PROVIDER, providerIdUrl});
			fail("Exception about already registered provider not throuwn!");

		} catch (Exception e) {
			assertTrue(e.getMessage().equals(I18nConstants.PROVIDER_ID_EXISTS));
		}		
	}
		
}
