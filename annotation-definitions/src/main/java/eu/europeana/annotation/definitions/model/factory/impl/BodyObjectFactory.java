package eu.europeana.annotation.definitions.model.factory.impl;

import eu.europeana.annotation.definitions.exception.AnnotationInstantiationException;
import eu.europeana.annotation.definitions.model.body.Body;
import eu.europeana.annotation.definitions.model.body.impl.PlainTagBody;
import eu.europeana.annotation.definitions.model.body.impl.SemanticLinkBody;
import eu.europeana.annotation.definitions.model.body.impl.SemanticTagBody;
import eu.europeana.annotation.definitions.model.body.impl.TextBody;
import eu.europeana.annotation.definitions.model.factory.AbstractModelObjectFactory;
import eu.europeana.annotation.definitions.model.vocabulary.BodyTypes;

public class BodyObjectFactory extends
		AbstractModelObjectFactory<Body, BodyTypes> {

	private static BodyObjectFactory singleton;

	// force singleton usage
	private BodyObjectFactory() {
	};

	public static BodyObjectFactory getInstance() {

		if (singleton == null) {
			synchronized (BodyObjectFactory.class) {
				singleton = new BodyObjectFactory();
			}
		}

		return singleton;

	}

	@Override
	public Class<? extends Body> getClassForType(Enum<BodyTypes> modelType) {
		// TEXT, TAG, SEMANTIC_TAG, SEMANTIC_LINK;
		Class<? extends Body> returnType = null;
		BodyTypes bodyType = BodyTypes.valueOf(modelType.name());
		switch (bodyType) {
		case SEMANTIC_LINK:
			returnType = SemanticLinkBody.class;
			break;
		case SEMANTIC_TAG:
			returnType = SemanticTagBody.class;
			break;
		case TAG:
			returnType = PlainTagBody.class;
			break;
		case TEXT:
			returnType = TextBody.class; 
			break;
		default:
			throw new AnnotationInstantiationException("unsupported body type: " + modelType);

		}

		return returnType;
	}

	@Override
	public Class<BodyTypes> getEnumClass() {
		return BodyTypes.class;
	}

}
