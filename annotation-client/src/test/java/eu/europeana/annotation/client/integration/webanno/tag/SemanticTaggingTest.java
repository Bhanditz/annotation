package eu.europeana.annotation.client.integration.webanno.tag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.stanbol.commons.exception.JsonParseException;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import eu.europeana.annotation.definitions.model.Annotation;
import eu.europeana.annotation.definitions.model.vocabulary.WebAnnotationFields;

public class SemanticTaggingTest extends BaseTaggingTest {

	
	@Test
	public void createSemanticTagSimpleMinimal() throws IOException, JsonParseException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {

		Annotation anno = createAndValidateTag(SEMANTICTAG_SIMPLE_MINIMAL);
		System.out.println(anno.getBody().getInternalType());
	}

	@Test
	public void createSemanticTagSimpleStandard() throws IOException, JsonParseException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {

		Annotation anno = createAndValidateTag(SEMANTICTAG_SIMPLE_STANDARD);
		System.out.println(anno.getBody().getInternalType());

	}

	@Test
	public void createSemanticTagSpecificMinimal() throws IOException, JsonParseException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {

		Annotation anno = createAndValidateTag(SEMANTICTAG_SPECIFIC_MINIMAL);
		System.out.println(anno.getBody().getInternalType());

	}

	@Test
	public void createSemanticTagSpecificStandard() throws IOException, JsonParseException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {

		Annotation anno = createAndValidateTag(SEMANTICTAG_SPECIFIC_STANDARD);
		System.out.println(anno.getBody().getInternalType());

	}
}
