package eu.europeana.annotation.client.admin;

import org.springframework.http.ResponseEntity;

public interface WebAnnotationAdminApi {
	
	public ResponseEntity<String> deleteAnnotation(String provider, String identifier);

	public ResponseEntity<String> reindexOutdated();

}
