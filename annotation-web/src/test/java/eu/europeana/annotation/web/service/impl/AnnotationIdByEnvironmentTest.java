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

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.europeana.annotation.config.AnnotationConfigurationImpl;
import eu.europeana.annotation.definitions.model.Annotation;
import eu.europeana.annotation.definitions.model.AnnotationId;
import eu.europeana.annotation.definitions.model.impl.BaseAnnotationId;
import eu.europeana.annotation.definitions.model.util.AnnotationTestObjectBuilder;
import eu.europeana.annotation.definitions.model.vocabulary.AnnotationTypes;
import eu.europeana.annotation.definitions.model.vocabulary.WebAnnotationFields;
import eu.europeana.annotation.solr.exceptions.AnnotationServiceException;
import eu.europeana.annotation.web.service.AnnotationService;


/**
 * Unit test for the Web Annotation service
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/annotation-web-context.xml", "/annotation-mongo-test.xml"
	//, "/annotation-solr-test.xml" 
	})
public class AnnotationIdByEnvironmentTest extends AnnotationTestObjectBuilder{

	@Resource 
	AnnotationService webAnnotationService;
	
	
	@Test
	public void testStoreInDevelopmentEnvironment() 
			throws MalformedURLException, IOException, AnnotationServiceException {
		
		String environment = AnnotationConfigurationImpl.VALUE_ENVIRONMENT_DEVELOPMENT;
		createTestAnnoInEnvironment(environment);	
	}

	@Test
	public void testStoreInTestEnvironment() 
			throws MalformedURLException, IOException, AnnotationServiceException {
		
		String environment = AnnotationConfigurationImpl.VALUE_ENVIRONMENT_TEST;
		createTestAnnoInEnvironment(environment);
	}

	@Test
	public void testStoreInProductionEnvironment() 
			throws MalformedURLException, IOException, AnnotationServiceException {
		
		String environment = AnnotationConfigurationImpl.VALUE_ENVIRONMENT_PRODUCTION;
		createTestAnnoInEnvironment(environment);
	}

	protected void createTestAnnoInEnvironment(String environment)
			throws MalformedURLException, IOException, AnnotationServiceException {
		
		AnnotationConfigurationImpl config = (AnnotationConfigurationImpl) ((BaseAnnotationServiceImpl)webAnnotationService).getConfiguration();
		config.getAnnotationProperties().put(AnnotationConfigurationImpl.ANNOTATION_ENVIRONMENT, environment);
		
		Annotation anno = testCreateAnnotationWebanno();
		
		System.out.println("@Id in "+ environment + " environment: " + anno.getAnnotationId().toHttpUrl());
		assertTrue(anno.getAnnotationId().toHttpUrl().startsWith(config.getAnnotationBaseUrl()));
	}
	
	
	//@Test
	public Annotation testCreateAnnotationWebanno() 
			throws MalformedURLException, IOException, AnnotationServiceException {
		
		Annotation testAnnotation = createTestAnnotation();		

		/**
		 * Store Annotation in database.
		 */
		Annotation webAnnotation = webAnnotationService.storeAnnotation(testAnnotation);
		
		if (StringUtils.isBlank(webAnnotation.getType())) {
			webAnnotation.setType(AnnotationTypes.OBJECT_TAG.name());
		}
		
//		System.out.println("testAnnotation: " + testAnnotation.toString());
//		System.out.println("webAnnotation: " + webAnnotation.toString());
		
		return webAnnotation;
	}

	/**
	 * Create a test annotation object.
	 * @return Annotation
	 */
	Annotation createTestAnnotation() {
		Annotation testAnnotation = createBaseObjectTagInstance();
		AnnotationId annoId = new BaseAnnotationId(null, WebAnnotationFields.PROVIDER_WEBANNO, null);
				
		testAnnotation.setAnnotationId(annoId);					
		return testAnnotation;
	}

	
	
	
		
}
