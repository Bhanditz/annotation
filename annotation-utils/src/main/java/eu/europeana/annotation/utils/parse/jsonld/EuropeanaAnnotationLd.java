package eu.europeana.annotation.utils.parse.jsonld;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.stanbol.commons.jsonld.JsonLd;
import org.apache.stanbol.commons.jsonld.JsonLdProperty;
import org.apache.stanbol.commons.jsonld.JsonLdPropertyValue;
import org.apache.stanbol.commons.jsonld.JsonLdResource;

import eu.europeana.annotation.definitions.exception.AnnotationAttributeInstantiationException;
import eu.europeana.annotation.definitions.model.Annotation;
import eu.europeana.annotation.definitions.model.WebAnnotationFields;
import eu.europeana.annotation.definitions.model.agent.Agent;
import eu.europeana.annotation.definitions.model.body.Body;
import eu.europeana.annotation.definitions.model.concept.Concept;
import eu.europeana.annotation.definitions.model.factory.impl.AgentObjectFactory;
import eu.europeana.annotation.definitions.model.factory.impl.AnnotationObjectFactory;
import eu.europeana.annotation.definitions.model.factory.impl.BodyObjectFactory;
import eu.europeana.annotation.definitions.model.factory.impl.ConceptObjectFactory;
import eu.europeana.annotation.definitions.model.factory.impl.SelectorObjectFactory;
import eu.europeana.annotation.definitions.model.factory.impl.StyleObjectFactory;
import eu.europeana.annotation.definitions.model.factory.impl.TargetObjectFactory;
import eu.europeana.annotation.definitions.model.impl.BaseObjectTag;
import eu.europeana.annotation.definitions.model.resource.InternetResource;
import eu.europeana.annotation.definitions.model.resource.impl.BaseInternetResource;
import eu.europeana.annotation.definitions.model.resource.selector.Selector;
import eu.europeana.annotation.definitions.model.resource.style.Style;
import eu.europeana.annotation.definitions.model.target.Target;
import eu.europeana.annotation.definitions.model.utils.TypeUtils;
import eu.europeana.annotation.definitions.model.vocabulary.AgentTypes;
import eu.europeana.annotation.definitions.model.vocabulary.AnnotationTypes;
import eu.europeana.annotation.definitions.model.vocabulary.ConceptTypes;
import eu.europeana.annotation.definitions.model.vocabulary.SelectorTypes;
import eu.europeana.annotation.utils.JsonUtils;

public class EuropeanaAnnotationLd extends JsonLd {

  //  private static final Logger logger = LoggerFactory.getLogger(AnnotationLd.class);
    
    TypeUtils typeHelper = new TypeUtils();
			
    public TypeUtils getTypeHelper() {
		return typeHelper;
	}

	/**
     * @param annotation
     */
    public EuropeanaAnnotationLd(Annotation annotation) {
    	setAnnotation(annotation);
    }
    
    /**
     * @param jsonLd
     */
    public EuropeanaAnnotationLd(JsonLd jsonLd) {
    	setJsonLd(jsonLd);
    }
    
	/**
     * Adds the given annotation to this JsonLd object using the resource's subject as key. If the key is NULL
     * and there does not exist a resource with an empty String as key the resource will be added using
     * an empty String ("") as key. 
     * 
     * @param annotation
     */
    public void setAnnotation(Annotation annotation) {
                
    	setUseTypeCoercion(false);
        setUseCuries(true);
//        addNamespacePrefix(WebAnnotationFields.OA_CONTEXT, WebAnnotationFields.OA);
        //TODO: verify if the following check is needed
        //if(isApplyNamespaces())
        	setUsedNamespaces(namespacePrefixMap);

        JsonLdResource jsonLdResource = new JsonLdResource();
        jsonLdResource.setSubject("");
    	jsonLdResource.putProperty(WebAnnotationFields.AT_CONTEXT, WebAnnotationFields.WA_CONTEXT);   
        if (!StringUtils.isBlank(annotation.getType())) {
        	jsonLdResource.addType(annotation.getType());
        } else {
        	jsonLdResource.addType(WebAnnotationFields.DEFAULT_ANNOTATION_TYPE);
        }
        
        if (annotation.getAnnotationId() != null){ 
        	jsonLdResource.putProperty(WebAnnotationFields.AT_ID, annotation.getAnnotationId().toHttpUrl()); 
        }
//        if (!StringUtils.isBlank(annotation.getType())) 
//        	jsonLdResource.putProperty(WebAnnotationFields.TYPE, annotation.getType());   
        
        if(annotation.getGenerator().getInputString() == null || isJsonObjectInput(annotation.getGenerator().getInputString())){
        	//user input string provided
        	//TODO: why reset?
        	annotation.getGenerator().setInputString(null);
            JsonLdProperty serializedByProperty = addSerializedByProperty(annotation);
            if (serializedByProperty != null)
            	jsonLdResource.putProperty(serializedByProperty);                
        } else {
        	//input string is a single value
        	jsonLdResource.putProperty(WebAnnotationFields.GENERATOR, annotation.getGenerator().getInputString());
        }
        
        
        
        if (annotation.getCreated() != null) 
        	jsonLdResource.putProperty(WebAnnotationFields.CREATED, TypeUtils.convertDateToStr(annotation.getCreated()));

        if (annotation.getCreator().getInputString() == null  || isJsonObjectInput(annotation.getCreator().getInputString())){
        	//TODO: why reset?
        	annotation.getCreator().setInputString(null);
            JsonLdProperty annotatedByProperty = addAnnotatedByProperty(annotation);
            if (annotatedByProperty != null)
            	jsonLdResource.putProperty(annotatedByProperty);                
        } else {
        	jsonLdResource.putProperty(WebAnnotationFields.CREATOR, annotation.getCreator().getInputString());
        }
        	
        if (annotation.getGenerated() != null) 
        	jsonLdResource.putProperty(WebAnnotationFields.GENERATED, TypeUtils.convertDateToStr(annotation.getGenerated()));
        if (!StringUtils.isBlank(annotation.getMotivation())) 
        	jsonLdResource.putProperty(WebAnnotationFields.MOTIVATION, annotation.getMotivation());
        JsonLdProperty styledByProperty = addStyledByProperty(annotation);
        if (styledByProperty != null)
        	jsonLdResource.putProperty(styledByProperty);                

        // TODO improve the following code 
        
        if (!annotation.getInternalType().equals(AnnotationTypes.OBJECT_LINKING.name())) {
	        //tag or comment, not linking
        	if (isJsonObjectInput(annotation.getBody().getInputString())){
	        	annotation.getBody().setInputString(null);
		        JsonLdProperty bodyProperty = addBodyProperty(annotation);
		        if (bodyProperty != null)
		        	jsonLdResource.putProperty(bodyProperty);
	        } else {
	        	if (annotation.getBody().getValues() != null && annotation.getBody().getValues().size() > 0) {
			        JsonLdProperty bodyProperty = addArrayProperty(WebAnnotationFields.BODY, annotation.getBody().getValues());
			        if (bodyProperty != null)
			        	jsonLdResource.putProperty(bodyProperty);
	        	}
	        	else
	        		jsonLdResource.putProperty(WebAnnotationFields.BODY, annotation.getBody().getInputString());
	        }
        }
        
        if (isJsonObjectInput(annotation.getTarget().getInputString())){
        	annotation.getTarget().setInputString(null);
	        JsonLdProperty targetProperty = addTargetProperty(annotation);
	        if (targetProperty != null)
	        	jsonLdResource.putProperty(targetProperty);
        } else {
            if (annotation.getInternalType().equals(AnnotationTypes.OBJECT_LINKING.name())) {
	            if (!StringUtils.isBlank(TypeUtils.getTypeListAsStr(annotation.getTarget().getValues())))  {
			        JsonLdProperty targetProperty = addArrayProperty(WebAnnotationFields.TARGET, annotation.getTarget().getValues());
			        if (targetProperty != null)
			        	jsonLdResource.putProperty(targetProperty);
	            }
            } else {
            	jsonLdResource.putProperty(WebAnnotationFields.TARGET, annotation.getTarget().getInputString());
            }
        }

	    if (annotation.getSameAs() != null) 
        	jsonLdResource.putProperty(WebAnnotationFields.SAME_AS, annotation.getSameAs());
        if (annotation.getEquivalentTo() != null) 
        	jsonLdResource.putProperty(WebAnnotationFields.EQUIVALENT_TO, annotation.getEquivalentTo());
        
        put(jsonLdResource);
    }

    /**
     * This method converts the jsonld representation (EuropeanaAnnotationLd) to model object (Annotation).
     * @return Annotation object
     * @throws RequestBodyValidationException 
     */
    @SuppressWarnings("rawtypes")
    public Annotation getAnnotation() {
		//TODO: must instantiate the correct class
		Annotation annotation = AnnotationObjectFactory.getInstance().createModelObjectInstance(
				AnnotationTypes.OBJECT_TAG.name());
    	
		Iterator<String> itrSubjects = this.getResourceSubjects().iterator();
    	JsonLdResource resource = getResource(itrSubjects.next()); //"");
    	Iterator<?> it = resource.getPropertyMap().entrySet().iterator();
		while (it.hasNext()) {
		    Map.Entry pairs = (Map.Entry)it.next();
		    String key = pairs.getKey().toString();
		    Object mapValue = pairs.getValue();
		    switch (key) {
		    case WebAnnotationFields.AT_ID:
		    	String annotationIdValue = getLiteralPropertyValue(mapValue);
				if (!StringUtils.isBlank(annotationIdValue)) 
					((BaseObjectTag) annotation).parse(annotationIdValue);
		    	break;
		    case WebAnnotationFields.TYPE:
		    	String typeValue = getLiteralPropertyValue(mapValue);
				if (!StringUtils.isBlank(typeValue)) 
					annotation.setType(typeValue);
		    	break;
		    case WebAnnotationFields.CREATED:
		    	String annotatedAtValue = getLiteralPropertyValue(mapValue);
				if (!StringUtils.isBlank(annotatedAtValue)) 
					annotation.setCreated(TypeUtils.convertStrToDate(annotatedAtValue));
		    	break;
		    case WebAnnotationFields.GENERATED:
		    	String serializedAtValue = getLiteralPropertyValue(mapValue);
				if (!StringUtils.isBlank(serializedAtValue)) 
					annotation.setGenerated(TypeUtils.convertStrToDate(serializedAtValue));
		    	break;
		    case WebAnnotationFields.MOTIVATION:
		    	String motivationValue = getLiteralPropertyValue(mapValue);
				if (!StringUtils.isBlank(motivationValue)) 
					annotation.setMotivation(motivationValue);
		    	break;
		    case WebAnnotationFields.BODY:
		    	Body body = getBody(mapValue);
			    annotation.setBody(body);
		    	break;
		    case WebAnnotationFields.TARGET:	    	
				Target target = getTarget(mapValue);						
			    annotation.setTarget(target);
		    	break;
		    case WebAnnotationFields.GENERATOR:
				Agent serializedBy = getSerializedBy(mapValue);
				annotation.setGenerator(serializedBy);
		    	break;
		    case WebAnnotationFields.CREATOR:
				Agent annotatedBy = getAnnotatedBy(mapValue);
				annotation.setCreator(annotatedBy);
		    	break;
		    case WebAnnotationFields.STYLED_BY:
		    	Style style = getStyledBy(mapValue);
				annotation.setStyledBy(style);
		    	break;
		    case WebAnnotationFields.SAME_AS:
		    	String sameAsValue = getLiteralPropertyValue(mapValue);
				if (!StringUtils.isBlank(sameAsValue)) 
					annotation.setSameAs(sameAsValue);
		    	break;
		    case WebAnnotationFields.EQUIVALENT_TO:
		    	String equivalentToValue = getLiteralPropertyValue(mapValue);
				if (!StringUtils.isBlank(equivalentToValue)) 
					annotation.setEquivalentTo(equivalentToValue);
		    	break;
		    default:
		    	break;
		    }		    
		}	

		return annotation;
    }

	private String getLiteralPropertyValue(Object mapValue) {
		String propValue = null;
		JsonLdProperty property = (JsonLdProperty) mapValue;
		if (property.getValues() != null && property.getValues().size() > 0) {
			JsonLdPropertyValue propertyValue = (JsonLdPropertyValue) property.getValues().get(0);					
			if (!StringUtils.isBlank(propertyValue.getLiteralValue())) {
				propValue = propertyValue.getLiteralValue();
			}	
		}
		return propValue;
	}

	/**
	 * This method retrieves Agent object for serializedBy field from AnnotationLd object.
	 * @param mapValue
	 * @return Agent object
	 * @throws RequestBodyValidationException 
	 */
	private Agent getSerializedBy(Object mapValue) {
		JsonLdProperty property = (JsonLdProperty) mapValue;
		return getAgentByProperty(property, AgentTypes.SOFTWARE);
	}

	private Agent getAgentByProperty(JsonLdProperty property, AgentTypes defaultAgentType){
		
		Agent agent = null;  
		
		if (property.getValues() != null && property.getValues().size() > 0) {
			JsonLdPropertyValue propertyValue = (JsonLdPropertyValue) property.getValues().get(0);
			
			AgentTypes agentType;
			
			String objectType = extractObjectType(propertyValue);
			//if not available in the input string use the default one
			if(objectType == null)
				agentType = defaultAgentType;
			else{
				agentType = AgentTypes.getByJsonValue(objectType);
				//wrong type
				if(agentType == null)
					throw new AnnotationAttributeInstantiationException(AnnotationAttributeInstantiationException.DEFAULT_MESSAGE, objectType);
				
			}
			
			agent = AgentObjectFactory.getInstance().createObjectInstance(agentType);
			if (hasValue(propertyValue, WebAnnotationFields.INPUT_STRING)) 
				agent.setInputString(propertyValue.getValues().get(WebAnnotationFields.INPUT_STRING));
			if (hasValue(propertyValue, WebAnnotationFields.AT_TYPE)) 
				agent.setAgentTypeAsString(propertyValue.getValues().get(WebAnnotationFields.AT_TYPE));			
			if (hasValue(propertyValue, WebAnnotationFields.AT_ID)) 
				agent.setOpenId(propertyValue.getValues().get(WebAnnotationFields.AT_ID));
			if (hasValue(propertyValue, WebAnnotationFields.NAME)) 
				agent.setName(propertyValue.getValues().get(WebAnnotationFields.NAME));
			if (hasValue(propertyValue, WebAnnotationFields.HOMEPAGE)) 
				agent.setHomepage(propertyValue.getValues().get(WebAnnotationFields.HOMEPAGE));
		}
		return agent;
	}

	protected boolean hasValue(JsonLdPropertyValue propertyValue, String fieldName) {
		return !StringUtils.isBlank(propertyValue.getValues().get(fieldName));
	}

	/**
	 * 	 
	 * @param propertyValue
	 * @return
	 */
	private String extractObjectType(JsonLdPropertyValue propertyValue) {
		return extractObjectType(propertyValue, WebAnnotationFields.AT_TYPE);		
	}

	private String extractObjectType(JsonLdPropertyValue propertyValue, String fieldName) {
		String typeArray = propertyValue.getValues().get(fieldName);
		return getTypeHelper().getInternalTypeFromTypeArray(typeArray);
	}

	/**
	 * This method retrieves Agent object for annotatedBy field from AnnotationLd object.
	 * @param mapValue
	 * @return Agent object
	 * @throws RequestBodyValidationException 
	 */
	private Agent getAnnotatedBy(Object mapValue){
		JsonLdProperty property = (JsonLdProperty) mapValue;
		return getAgentByProperty(property, AgentTypes.PERSON);
	}

	/**
	 * This method retrieves Style object for styledBy field from AnnotationLd object.
	 * @param mapValue
	 * @return Style object
	 */
	private Style getStyledBy(Object mapValue) {
		JsonLdProperty property = (JsonLdProperty) mapValue;
		Style style = null;
		if (property.getValues() != null && property.getValues().size() > 0) {
			JsonLdPropertyValue propertyValue = (JsonLdPropertyValue) property.getValues().get(0);
			
			String objectType = "";
			if (!StringUtils.isBlank(propertyValue.getType())) 
				objectType = getTypeHelper().getInternalTypeFromTypeArray(propertyValue.getType());
			if (StringUtils.isBlank(objectType) && hasValue(propertyValue, WebAnnotationFields.AT_TYPE)) 
				objectType = getTypeHelper().getInternalTypeFromTypeArray(propertyValue.getValues().get(WebAnnotationFields.AT_TYPE));
			//if not set 
			if (StringUtils.isBlank(objectType))
				throw new AnnotationAttributeInstantiationException(objectType);
			
			style = StyleObjectFactory.getInstance().createModelObjectInstance(objectType);
			
			if (!StringUtils.isBlank(propertyValue.getType())) 
				style.setHttpUri(propertyValue.getType());
			if (StringUtils.isBlank(propertyValue.getType()) && hasValue(propertyValue, WebAnnotationFields.AT_TYPE)) 
				style.setHttpUri(propertyValue.getValues().get(WebAnnotationFields.AT_TYPE));
			if (hasValue(propertyValue, WebAnnotationFields.STYLE_CLASS)) 
				style.setContentType(propertyValue.getValues().get(WebAnnotationFields.STYLE_CLASS));
			if (hasValue(propertyValue, WebAnnotationFields.SOURCE)) 
				style.setValue(propertyValue.getValues().get(WebAnnotationFields.SOURCE));
		}
		return style;
	}

	/**
	 * This method retrieves Target object from AnnotationLd object.
	 * @param mapValue
	 * @return Target object
	 */
	private Target getTarget(Object mapValue) {
		Target target = null;
		JsonLdProperty property = (JsonLdProperty) mapValue;
		if (property.getValues() != null && property.getValues().size() > 0) {
			JsonLdPropertyValue propertyValue = (JsonLdPropertyValue) property.getValues().get(0);
			if (hasValue(propertyValue, WebAnnotationFields.TYPE)) {
				String euType = extractObjectType(propertyValue, WebAnnotationFields.TYPE);			
				//if not set 
				if (StringUtils.isBlank(euType))
					throw new AnnotationAttributeInstantiationException(euType);
				
				target = TargetObjectFactory.getInstance().createModelObjectInstance(euType);
				target.setType(propertyValue.getValues().get(WebAnnotationFields.TYPE));
				if (hasValue(propertyValue, WebAnnotationFields.INPUT_STRING)) 
					target.setInputString(propertyValue.getValues().get(WebAnnotationFields.INPUT_STRING));
				if (hasValue(propertyValue, WebAnnotationFields.CONTENT_TYPE)) 
					target.setContentType(propertyValue.getValues().get(WebAnnotationFields.CONTENT_TYPE));
				if (hasValue(propertyValue, WebAnnotationFields.HTTP_URI)) 
					target.setHttpUri(propertyValue.getValues().get(WebAnnotationFields.HTTP_URI));
				
				JsonLdProperty sourceProperty = propertyValue.getProperty(WebAnnotationFields.SOURCE);
				JsonLdPropertyValue propertyValue2 = (JsonLdPropertyValue) sourceProperty.getValues().get(0);
				InternetResource source = new BaseInternetResource();
				if (hasValue(propertyValue2, WebAnnotationFields.CONTENT_TYPE)) 
					source.setContentType(propertyValue2.getValues().get(WebAnnotationFields.CONTENT_TYPE));
				if (hasValue(propertyValue2, WebAnnotationFields.AT_ID)) {
					source.setHttpUri(propertyValue2.getValues().get(WebAnnotationFields.AT_ID));
				}
				if (hasValue(propertyValue2, WebAnnotationFields.FORMAT)) 
					source.setMediaType(propertyValue2.getValues().get(WebAnnotationFields.FORMAT));
				
				target.setSourceResource(source);
				
				JsonLdProperty selectorProperty = propertyValue.getProperty(WebAnnotationFields.SELECTOR);
				if (selectorProperty != null) {
					JsonLdPropertyValue propertyValue3 = (JsonLdPropertyValue) selectorProperty.getValues().get(0);
					Selector selector = SelectorObjectFactory.getInstance().createModelObjectInstance(
							SelectorTypes.SVG_RECTANGLE_SELECTOR.name());
					if (hasValue(propertyValue3, WebAnnotationFields.AT_TYPE)) 
						selector.setSelectorType(propertyValue3.getValues().get(WebAnnotationFields.AT_TYPE));
					if (hasValue(propertyValue3, WebAnnotationFields.DIMENSION_MAP)) 
						selector.setDimensionMap(
								JsonUtils.stringToMapExt(propertyValue3.getValues().get(WebAnnotationFields.DIMENSION_MAP)));
					target.setSelector((Selector)selector);
				}
			}
		}
		return target;
	}

	/**
	 * This method retrieves Body object from AnnotationLd object.
	 * @param mapValue
	 * @return Body object
	 */
	private Body getBody(Object mapValue) {
		Body body = null;
		JsonLdProperty property = (JsonLdProperty) mapValue;
		if (property.getValues() != null && property.getValues().size() > 0) {
			JsonLdPropertyValue propertyValue = (JsonLdPropertyValue) property.getValues().get(0);
			
			if (hasValue(propertyValue, WebAnnotationFields.AT_TYPE)) {
				String objectType = extractObjectType(propertyValue);

				//if not set 
				if (StringUtils.isBlank(objectType))
					throw new AnnotationAttributeInstantiationException(objectType);
				
				body = BodyObjectFactory.getInstance().createModelObjectInstance(objectType);
				
				if (hasValue(propertyValue, WebAnnotationFields.AT_TYPE)) 
					body.setType(TypeUtils.convertStringToList(propertyValue.getValues().get(WebAnnotationFields.AT_TYPE)));			

				if (hasValue(propertyValue, WebAnnotationFields.INPUT_STRING)) 
					body.setInputString(propertyValue.getValues().get(WebAnnotationFields.INPUT_STRING));
				if (hasValue(propertyValue, WebAnnotationFields.CHARS)) 
					body.setValue(propertyValue.getValues().get(WebAnnotationFields.CHARS));
				if (hasValue(propertyValue, WebAnnotationFields.DC_LANGUAGE)) 
					body.setLanguage(propertyValue.getValues().get(WebAnnotationFields.DC_LANGUAGE));
				if (hasValue(propertyValue, WebAnnotationFields.FORMAT)) 
					body.setContentType(propertyValue.getValues().get(WebAnnotationFields.FORMAT));
				if (hasValue(propertyValue, WebAnnotationFields.PAGE)) 
					body.setHttpUri(propertyValue.getValues().get(WebAnnotationFields.PAGE));
				if (hasValue(propertyValue, WebAnnotationFields.MULTILINGUAL)) 
					body.setMultilingual(JsonUtils.stringToMap(propertyValue.getValues().get(WebAnnotationFields.MULTILINGUAL)));
				if (hasValue(propertyValue, WebAnnotationFields.AT_ID)) 
					body.setInternalId(propertyValue.getValues().get(WebAnnotationFields.AT_ID));
				if (hasValue(propertyValue, WebAnnotationFields.SOURCE)) 
					body.setSource(propertyValue.getValues().get(WebAnnotationFields.SOURCE));
				if (hasValue(propertyValue, WebAnnotationFields.PURPOSE)) 
					body.setPurpose(propertyValue.getValues().get(WebAnnotationFields.PURPOSE));
			}
			
			Concept concept = getConcept(propertyValue); 
			if (concept != null)
				body.setConcept(concept);
		}
		return body;
	}
    
	/**
	 * This method parses string list given in JSON-LD format.
	 * @param propertyValue
	 * @param fieldName
	 * @return string list
	 */
	private List<String> parseList(JsonLdPropertyValue propertyValue, String fieldName) {
		List<String> res = null;
		if (hasValue(propertyValue, fieldName)) {
			String fieldContent = "[" + propertyValue.getValues().get(fieldName) + "]";
			res = JsonUtils.stringToList(fieldContent);
		}
		return res;
	}
	
	/**
	 * This method parses string map given in JSON-LD format.
	 * @param propertyValue
	 * @param fieldName
	 * @return string map
	 */
	private Map<String, String> parseMap(JsonLdPropertyValue propertyValue, String fieldName) {
		Map<String, String> res = null;
		String fieldNameId = fieldName + ":@" + WebAnnotationFields.ID;
		String fieldNameContainer = "@" + WebAnnotationFields.CONTAINER;
		if (hasValue(propertyValue, fieldNameId)) {
			res = new HashMap<String, String>();
			res.put("@" + WebAnnotationFields.ID, propertyValue.getValues().get(fieldNameId));
			res.put("@" + WebAnnotationFields.CONTAINER, propertyValue.getValues().get(fieldNameContainer));
		}
		return res;
	}
	
	/**
	 * This method parses a Concept object from JSON-LD string.
	 * @param mapValue
	 * @return Concept object
	 */
	private Concept getConcept(Object mapValue) {
		Concept concept = null;
		JsonLdProperty property = ((JsonLdPropertyValue) mapValue).getProperty(WebAnnotationFields.CONCEPT);
		if (property != null && property.getValues() != null && property.getValues().size() > 0) {
			JsonLdPropertyValue propertyValue = (JsonLdPropertyValue) property.getValues().get(0);
			
			concept = ConceptObjectFactory.getInstance().createModelObjectInstance(
					ConceptTypes.BASE_CONCEPT.name());
			concept.setNotation(parseList(propertyValue, WebAnnotationFields.NOTATION));
			concept.setRelated(parseList(propertyValue, WebAnnotationFields.RELATED));
			concept.setNarrower(parseList(propertyValue, WebAnnotationFields.NARROWER));
			concept.setBroader(parseList(propertyValue, WebAnnotationFields.BROADER));
			concept.setPrefLabel(parseMap(propertyValue, WebAnnotationFields.PREF_LABEL));
			concept.setHiddenLabel(parseMap(propertyValue, WebAnnotationFields.HIDDEN_LABEL));
			concept.setAltLabel(parseMap(propertyValue, WebAnnotationFields.ALT_LABEL));
		}
		
		return concept;
	}
    
	/**
     * Adds the values from the passed JsonLd object. 
     * 
     * @param jsonLd
     */
	public void setJsonLd(JsonLd jsonLd) {
             
    	setUseTypeCoercion(false);
        setUseCuries(true);
        
        if (jsonLd != null) {
    		if (jsonLd.getNamespacePrefixMap() != null) {
    	    	setNamespacePrefixMap(jsonLd.getNamespacePrefixMap());
    		}
    		if (jsonLd.getUsedNamespaces() != null) {
    			setUsedNamespaces(jsonLd.getUsedNamespaces());
    		}
    		if (jsonLd.getResourceSubjects() != null) {
    			Iterator<String> itr = jsonLd.getResourceSubjects().iterator();
    			while (itr.hasNext()) {
    				String resourceName = itr.next();
    				if (resourceName != null) {
    					put(jsonLd.getResource(resourceName));
    				}
    			}
    		}
   			setUseJointGraphs(jsonLd.isUseJointGraphs());
   			setUseTypeCoercion(jsonLd.isUseTypeCoercion());
   			setUseCuries(jsonLd.isUseCuries());
   			setApplyNamespaces(jsonLd.isApplyNamespaces());
    	}    	
    }
    
    /**
     * This method takes passed serialised AnnotationLd GSON string and
     * passes it to the parser. 
     * @param serializedAnnotationLd
     * @return Annotation object
     */
//    public static Annotation deserialise(String serialisedAnnotationLd) {
//    	Annotation res = null;
//        Gson gson = new Gson();
//        AnnotationLd annotationLdDeserialisedObject = gson.fromJson(serialisedAnnotationLd, AnnotationLd.class);
//        String annotationLdDeserialisedString = annotationLdDeserialisedObject.toString();
//        AnnotationLd.toConsole("deserialise: ", annotationLdDeserialisedString);
//        try {
//			JsonLd deserialisedJsonLd = JsonLdParser.parseExt(annotationLdDeserialisedString);
//			res = AnnotationLd.getAnnotationFromJsonLd(deserialisedJsonLd);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//        return res;
//    }
    
//    /**
//     * This method converts deserialised JsonLd to Annotation object.
//     * @param deserialisedJsonLd
//     * @return Annotation object
//     */
//    public static Annotation getAnnotationFromJsonLd(JsonLd deserialisedJsonLd) {
//    	Annotation res = null;
//    	AnnotationLd annotationLd = new AnnotationLd(deserialisedJsonLd);
//    	res = annotationLd.getAnnotation();
//    	return res;
//    }
    
	private JsonLdProperty addTargetProperty(Annotation annotation) {
		JsonLdProperty targetProperty = new JsonLdProperty(WebAnnotationFields.TARGET);
        JsonLdPropertyValue propertyValue = new JsonLdPropertyValue();
        
        if (annotation != null && annotation.getTarget() != null) {
            if (!StringUtils.isBlank(annotation.getTarget().getInputString())) 
            	propertyValue.getValues().put(WebAnnotationFields.INPUT_STRING, annotation.getTarget().getInputString());
        	if (!StringUtils.isBlank(annotation.getTarget().getType())) 
    			propertyValue.addType(annotation.getTarget().getType().replace("[", "").replace("]", ""));

            if (!StringUtils.isBlank(annotation.getTarget().getType()))         	
            	propertyValue.getValues().put(WebAnnotationFields.TYPE, annotation.getTarget().getType());
            if (!StringUtils.isBlank(annotation.getTarget().getContentType()))         	
            	propertyValue.getValues().put(WebAnnotationFields.CONTENT_TYPE, annotation.getTarget().getContentType());
            if (!StringUtils.isBlank(annotation.getTarget().getHttpUri()))         	
            	propertyValue.getValues().put(WebAnnotationFields.HTTP_URI, annotation.getTarget().getHttpUri());
        	
	        JsonLdProperty sourceProperty = new JsonLdProperty(WebAnnotationFields.SOURCE);
	        JsonLdPropertyValue propertyValue2 = new JsonLdPropertyValue();
	        
        	if (annotation.getTarget().getSourceResource() != null) { 
            	if (!StringUtils.isBlank(annotation.getTarget().getSourceResource().getContentType())) 
            		propertyValue2.getValues().put(WebAnnotationFields.CONTENT_TYPE, annotation.getTarget().getSourceResource().getContentType());
            	if (!StringUtils.isBlank(annotation.getTarget().getSourceResource().getHttpUri())) 
            		propertyValue2.getValues().put(WebAnnotationFields.AT_ID, annotation.getTarget().getSourceResource().getHttpUri());
            	if (!StringUtils.isBlank(annotation.getTarget().getSourceResource().getMediaType())) 
            		propertyValue2.getValues().put(WebAnnotationFields.FORMAT, annotation.getTarget().getSourceResource().getMediaType());
                if (propertyValue2.getValues().size() != 0) {
	            	sourceProperty.addValue(propertyValue2);        
			        propertyValue.putProperty(sourceProperty);
                }
        	}
	        
	        JsonLdProperty selectorProperty = new JsonLdProperty(WebAnnotationFields.SELECTOR);
	        JsonLdPropertyValue propertyValue3 = new JsonLdPropertyValue();

            if (annotation.getTarget().getSelector() != null 
            		&& !StringUtils.isBlank(annotation.getTarget().getSelector().getSelectorType()))         	
            	propertyValue3.getValues().put(WebAnnotationFields.AT_TYPE
            			, annotation.getTarget().getSelector().getSelectorType());
            else
            	propertyValue3.setType(""); // if property is empty - set empty type

            if (annotation.getTarget().getSelector() != null 
            		&& !StringUtils.isBlank(annotation.getTarget().getSelector().getDimensionMap().toString()))         	
            	propertyValue3.getValues().put(
            			WebAnnotationFields.DIMENSION_MAP
            			, JsonUtils.mapToStringExt(annotation.getTarget().getSelector().getDimensionMap()));

            if (propertyValue3.getValues().size() != 0) {
		        selectorProperty.addValue(propertyValue3);        
		        propertyValue.putProperty(selectorProperty);
            }
	        
	        if (propertyValue.getValues().size() == 0)
	        	return null;
	        targetProperty.addValue(propertyValue);
        } else {
        	return null;
        }
		return targetProperty;
	}
    
	private JsonLdProperty addConceptProperty(Concept concept) {
        JsonLdProperty conceptProperty = new JsonLdProperty(WebAnnotationFields.CONCEPT);
        JsonLdPropertyValue propertyValue = new JsonLdPropertyValue();                
        
    	if (concept != null) { 
            addListToProperty(concept.getNotation(), propertyValue, WebAnnotationFields.NOTATION);
            addListToProperty(concept.getNarrower(), propertyValue, WebAnnotationFields.NARROWER);
            addListToProperty(concept.getBroader(), propertyValue, WebAnnotationFields.BROADER);
            addListToProperty(concept.getRelated(), propertyValue, WebAnnotationFields.RELATED);
            
            addMapToProperty(concept.getPrefLabel(), propertyValue, WebAnnotationFields.PREF_LABEL);
            addMapToProperty(concept.getHiddenLabel(), propertyValue, WebAnnotationFields.HIDDEN_LABEL);
            addMapToProperty(concept.getAltLabel(), propertyValue, WebAnnotationFields.ALT_LABEL);
            if (propertyValue.getValues().size() != 0) {
            	conceptProperty.addValue(propertyValue);        
            }
    	}	
    	return conceptProperty;
	}

	/**
	 * @param map
	 * @param propertyValue
	 * @param field
	 */
	private void addMapToProperty(Map<String, String> map, JsonLdPropertyValue propertyValue, String field) {
        JsonLdProperty fieldProperty = new JsonLdProperty(field);
        JsonLdPropertyValue fieldPropertyValue = new JsonLdPropertyValue();
        
	    Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
	        String curValue = pairs.getValue();
        	if (!StringUtils.isBlank(curValue)) 
        		fieldPropertyValue.getValues().put(pairs.getKey(), pairs.getValue());
	        it.remove(); // avoids a ConcurrentModificationException
	    }
        if (fieldPropertyValue.getValues().size() != 0) {
         	fieldProperty.addValue(fieldPropertyValue);        
         	propertyValue.putProperty(fieldProperty);
    	}
	}
	
	/**
	 * @param list
	 * @param propertyValue
	 * @param field
	 */
	private void addListToProperty(List<String> list, JsonLdPropertyValue propertyValue, String field) {
		String listString = TypeUtils.getTypeListAsStr(list);
		if (!StringUtils.isBlank(listString)) 
			propertyValue.getValues().put(field, listString);
	}
	
	/**
	 * @param annotation
	 * @return
	 */
	private JsonLdProperty addArrayProperty(String propertyName, List<String> valueList) {
		JsonLdProperty arrProperty = new JsonLdProperty(propertyName);
        if (valueList != null) {
        	Iterator<String> itr = valueList.iterator();
        	while (itr.hasNext()) {
        		String value = itr.next();
        		JsonLdPropertyValue propertyValue = new JsonLdPropertyValue();
        		propertyValue.setValue(value);
    	        arrProperty.addValue(propertyValue);        
        	}
        } else {
        	return null;
        }
		return arrProperty;
	}

	/**
	 * @param annotation
	 * @return
	 */
	private JsonLdProperty addBodyProperty(Annotation annotation) {
		JsonLdProperty bodyProperty = new JsonLdProperty(WebAnnotationFields.BODY);
		JsonLdPropertyValue propertyValue = new JsonLdPropertyValue();
        
        if (annotation != null && annotation.getBody() != null) {	
            if (!StringUtils.isBlank(annotation.getBody().getInputString())) 
            	propertyValue.getValues().put(WebAnnotationFields.INPUT_STRING, annotation.getBody().getInputString());
            if (!StringUtils.isBlank(TypeUtils.getTypeListAsStr(annotation.getBody().getType()))) 
        	    propertyValue.getValues().put(WebAnnotationFields.AT_TYPE, TypeUtils.getTypeListAsStr(annotation.getBody().getType()));            
            if (!StringUtils.isBlank(annotation.getBody().getValue()))         	
            	propertyValue.getValues().put(WebAnnotationFields.CHARS, annotation.getBody().getValue());
            if (!StringUtils.isBlank(annotation.getBody().getLanguage()))         	
            	propertyValue.getValues().put(WebAnnotationFields.DC_LANGUAGE, annotation.getBody().getLanguage());
            if (!StringUtils.isBlank(annotation.getBody().getContentType()))         	
            	propertyValue.getValues().put(WebAnnotationFields.FORMAT, annotation.getBody().getContentType());	
            if (!StringUtils.isBlank(annotation.getBody().getHttpUri()))         	
            	propertyValue.getValues().put(WebAnnotationFields.AT_ID, annotation.getBody().getHttpUri());
            if (annotation.getBody().getMultilingual() != null && !annotation.getBody().getMultilingual().isEmpty())         	
            	propertyValue.getValues().put(WebAnnotationFields.MULTILINGUAL, JsonUtils.mapToString(annotation.getBody().getMultilingual()));
            if (annotation.getBody().getConcept() != null)         	
            	propertyValue.putProperty(addConceptProperty(annotation.getBody().getConcept()));
//            if (!StringUtils.isBlank(annotation.getBody().getInternalId()))         	
//            	propertyValue.getValues().put(WebAnnotationFields.AT_ID, annotation.getBody().getInternalId());
            if (!StringUtils.isBlank(annotation.getBody().getSource()))         	
            	propertyValue.getValues().put(WebAnnotationFields.SOURCE, annotation.getBody().getSource());
            if (annotation.getBody().getSourceResource() != null)         	
            	;//TODO add serialization of resource
            if (!StringUtils.isBlank(annotation.getBody().getPurpose()))         	
            	propertyValue.getValues().put(WebAnnotationFields.PURPOSE, annotation.getBody().getPurpose());
            if (propertyValue.getValues().size() == 0)
            	return null;
	        bodyProperty.addValue(propertyValue);        
        } else {
        	return null;
        }
		return bodyProperty;
	}

	private JsonLdProperty addSerializedByProperty(Annotation annotation) {
		JsonLdProperty serializedByProperty = new JsonLdProperty(WebAnnotationFields.GENERATOR);
        JsonLdPropertyValue propertyValue = new JsonLdPropertyValue();
        Agent agent = annotation.getGenerator();        
        addAgentByProperty(propertyValue, agent);
        if (propertyValue.getValues().size() == 0)
        	return null;
        serializedByProperty.addValue(propertyValue);
		return serializedByProperty;
	}

	private void addAgentByProperty(JsonLdPropertyValue propertyValue,
			Agent agent) {
       	if(agent.getOpenId() != null)
       		propertyValue.getValues().put(WebAnnotationFields.AT_ID, agent.getOpenId());
        if (!StringUtils.isBlank(agent.getType())) //convert internal type to json value
        	propertyValue.getValues().put(WebAnnotationFields.AT_TYPE, AgentTypes.valueOf(agent.getInternalType()).getJsonValue());
        if (!StringUtils.isBlank(agent.getName())) 
        	propertyValue.getValues().put(WebAnnotationFields.NAME, agent.getName());
        if (!StringUtils.isBlank(agent.getHomepage())) 
        	propertyValue.getValues().put(WebAnnotationFields.HOMEPAGE, agent.getHomepage());
	}

	private JsonLdProperty addAnnotatedByProperty(Annotation annotation) {
		JsonLdProperty annotatedByProperty = new JsonLdProperty(WebAnnotationFields.CREATOR);
        JsonLdPropertyValue propertyValue = new JsonLdPropertyValue();
        Agent agent = annotation.getCreator();  
        addAgentByProperty(propertyValue, agent);
        if (propertyValue.getValues().size() == 0)
        	return null;
        annotatedByProperty.addValue(propertyValue);
		return annotatedByProperty;
	}

	private boolean isJsonObjectInput(String inputString) {
		return inputString!= null && inputString.contains("{");
	}

	private JsonLdProperty addStyledByProperty(Annotation annotation) {
		JsonLdProperty styledByProperty = new JsonLdProperty(WebAnnotationFields.STYLED_BY);
        JsonLdPropertyValue propertyValue = new JsonLdPropertyValue();
        Style style = annotation.getStyledBy();        
        if (style != null && !StringUtils.isBlank(style.getHttpUri())) 
        	propertyValue.setType(style.getHttpUri());
        if (style != null && !StringUtils.isBlank(style.getContentType())) 
        	propertyValue.getValues().put(WebAnnotationFields.STYLE_CLASS, style.getContentType());
        if (style != null && !StringUtils.isBlank(style.getValue())) 
        	propertyValue.getValues().put(WebAnnotationFields.SOURCE, style.getValue());
        if (propertyValue.getValues().size() == 0)
        	return null;
        styledByProperty.addValue(propertyValue);
		return styledByProperty;
	}
    
//TODO: remove dead code	
//    public static void toConsole(String description, String actual) {
//    	logger.info(description);
//    	logger.info(actual);
//        String s = actual;
//        s = s.replaceAll("\\\\", "\\\\\\\\");
//        s = s.replace("\"", "\\\"");
//        s = s.replace("\n", "\\n");
//        logger.info(s);
//    }
}