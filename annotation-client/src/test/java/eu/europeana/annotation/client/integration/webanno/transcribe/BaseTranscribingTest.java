package eu.europeana.annotation.client.integration.webanno.transcribe;

import org.apache.stanbol.commons.exception.JsonParseException;

import eu.europeana.annotation.client.integration.webanno.BaseWebAnnotationProtocolTest;
import eu.europeana.annotation.definitions.model.Annotation;
import eu.europeana.annotation.definitions.model.vocabulary.MotivationTypes;

public class BaseTranscribingTest extends BaseWebAnnotationProtocolTest {

	public static final String TRANSCRIPTION_MINIMAL = "/transcription/minimal.json";

	
	protected Annotation parseTranscription(String jsonString) throws JsonParseException {
		MotivationTypes motivationType = MotivationTypes.TRANSCRIBING;
		return parseAnnotation(jsonString, motivationType);		
	}

}
