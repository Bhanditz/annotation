package eu.europeana.annotation.client.integration.webanno.tag;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.stanbol.commons.exception.JsonParseException;

import eu.europeana.annotation.client.integration.webanno.BaseWebAnnotationProtocolTest;
import eu.europeana.annotation.definitions.model.Annotation;
import eu.europeana.annotation.definitions.model.vocabulary.MotivationTypes;

public class BaseTaggingTest extends BaseWebAnnotationProtocolTest {

	public static final String TAG_MINIMAL = "/tag/minimal.json";
	public static final String TAG_BODY_TEXT = "/tag/bodyText.json";
	public static final String TAG_MINIMAL_WRONG = "/tag/wrong/minimal_wrong.json";
	public static final String TAG_GEO_WRONG_LAT = "/tag/wrong/geotag_wrong_lat.json";
	public static final String TAG_GEO_WRONG_LONG = "/tag/wrong/geotag_wrong_long.json";
	public static final String TAG_STANDARD = "/tag/standard.json";
	public static final String TAG_GEOTAG = "/tag/geotag.json";
	public static final String SEMANTICTAG_SIMPLE_MINIMAL = "/semantictag/simple_minimal.json";
	public static final String SEMANTICTAG_SIMPLE_STANDARD = "/semantictag/simple_standard.json";
	public static final String SEMANTICTAG_SPECIFIC_MINIMAL = "/semantictag/specific_minimal.json";
	public static final String SEMANTICTAG_SPECIFIC_STANDARD = "/semantictag/specific_standard.json";
	public static final String TAG_CANONICAL = "/tag/canonical.json";
	public static final String TAG_VIA_STRING = "/tag/via_string.json";
	public static final String TAG_VIA_ARRAY = "/tag/via_array.json";

	protected Annotation createAndValidateTag(String inputFile) throws IOException, JsonParseException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		System.out.println("Input File: " + inputFile);

		String requestBody = getJsonStringInput(inputFile);

		Annotation storedAnno = createTag(requestBody);

		Annotation inputAnno = parseTag(requestBody);

		// validate the reflection of input in output!
		validateOutputAgainstInput(storedAnno, inputAnno);

		return storedAnno;
	}

	protected Annotation parseTag(String jsonString) throws JsonParseException {
		MotivationTypes motivationType = MotivationTypes.TAGGING;
		return parseAnnotation(jsonString, motivationType);
	}

	// protected Annotation createTag(String requestBody) throws
	// JsonParseException {
	// ResponseEntity<String> response = getApiClient().createTag(
	// WebAnnotationFields.PROVIDER_WEBANNO, null, false, requestBody,
	// TEST_USER_TOKEN);
	//
	// assertNotNull(response.getBody());
	// assertEquals(response.getStatusCode(), HttpStatus.CREATED);
	//
	// Annotation storedAnno = getApiClient().parseResponseBody(response);
	// assertNotNull(storedAnno.getCreator());
	// assertNotNull(storedAnno.getGenerator());
	// return storedAnno;
	// }

	// protected Annotation createTagWithProviderAndUserToken(
	// String requestBody, String provider, String userToken) throws
	// JsonParseException {
	// ResponseEntity<String> response = getApiClient().createTag(
	// provider, null, false, requestBody, userToken);
	//
	// assertNotNull(response.getBody());
	// assertEquals(response.getStatusCode(), HttpStatus.CREATED);
	//
	// Annotation storedAnno = getApiClient().parseResponseBody(response);
	// assertNotNull(storedAnno.getCreator());
	// assertNotNull(storedAnno.getGenerator());
	// return storedAnno;
	// }
}
