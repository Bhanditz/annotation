package eu.europeana.annotation.web.service.controller.admin;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.stanbol.commons.exception.JsonParseException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.europeana.annotation.definitions.exception.UserValidationException;
import eu.europeana.annotation.definitions.model.agent.Agent;
import eu.europeana.annotation.definitions.model.authentication.impl.BaseUser;
import eu.europeana.annotation.definitions.model.vocabulary.AgentTypes;
import eu.europeana.annotation.definitions.model.vocabulary.IdGenerationTypes;
import eu.europeana.annotation.definitions.model.vocabulary.WebAnnotationFields;
import eu.europeana.annotation.web.exception.ProviderOperationException;
import eu.europeana.annotation.web.exception.UserOperationException;
import eu.europeana.annotation.web.exception.request.ParamValidationException;
import eu.europeana.annotation.web.http.SwaggerConstants;
import eu.europeana.annotation.web.model.ProviderOperationResponse;
import eu.europeana.annotation.web.model.vocabulary.Operations;
import eu.europeana.annotation.web.service.controller.jsonld.BaseJsonldRest;
import eu.europeana.api.common.config.I18nConstants;
import eu.europeana.api.common.config.swagger.SwaggerSelect;
import eu.europeana.api.commons.web.exception.HttpException;
import eu.europeana.api2.utils.JsonWebUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Controller
@SwaggerSelect
@Api(tags = "Provider Management Rest Service", description=" ", hidden=true)
public class ProviderManagementRest extends BaseJsonldRest {

	protected final Logger logger = getLogger();
	
	@GET
	@RequestMapping(value = "/admin/provider/component", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	@ResponseBody
	public String getComponentName() {
		return getConfiguration().getComponentName() + "-admin/provider";
	}

	@DELETE
	@RequestMapping(value = "/provider/delete", method = RequestMethod.DELETE
					, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Delete a provider from the provider list", nickname = "deleteProviderEntry", response = java.lang.Void.class)
	public ResponseEntity<String> deleteProviderEntry(
		@RequestParam(value = WebAnnotationFields.PARAM_WSKEY, required = true) String wsKey,
		@RequestParam(value = WebAnnotationFields.USER_TOKEN, required = false, defaultValue = WebAnnotationFields.USER_ANONYMOUNS) String userToken,
		@RequestParam(value = WebAnnotationFields.ID, required = false) String id,
		@RequestParam(value = WebAnnotationFields.NAME, required = false) String name,
		HttpServletRequest request
		) throws HttpException { 

		validateApiKey(wsKey, WebAnnotationFields.DELETE_METHOD);
		userToken = getUserToken(userToken, request);
		getAuthorizationService().authorizeUser(userToken, wsKey, Operations.PROVIDER_DELETE);
		
		ProviderOperationResponse response;
		response = new ProviderOperationResponse(
				wsKey, "/provider/delete");
			
		String query = "";
		
		try{
			if (StringUtils.isNotEmpty(id)) {
				String providerIdUrl = WebAnnotationFields.PROVIDER_ID_PREFIX + id;	
				query = providerIdUrl;
				getAnnotationService().deleteProviderEntry(providerIdUrl);
				response.success = true;
			} else {
				if (StringUtils.isNotEmpty(name)) {
					query = name;
					getAnnotationService().deleteProviderEntryByName(name);
					response.success = true;
				} else {
					throw new ProviderOperationException(I18nConstants.PROVIDER_INVALID_ID, 
							I18nConstants.PROVIDER_INVALID_ID, 
							new String[]{WebAnnotationFields.PROVIDER, query});
				}
			}
		} catch (Exception e) {
			throw new ProviderOperationException(e.getMessage(), 
				e.getMessage(), 
				new String[]{WebAnnotationFields.PROVIDER, query});
		}

		String jsonStr = JsonWebUtils.toJson(response, null);
		return buildResponseEntityForJsonString(jsonStr);		
	}
	
	
/*	
	@DELETE
	@RequestMapping(value = "/admin/delete", method = RequestMethod.DELETE
					, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Delete a user from the user list", nickname = "deleteUserEntry", response = java.lang.Void.class)
	public ResponseEntity<String> deleteUserEntry(
		@RequestParam(value = WebAnnotationFields.PARAM_WSKEY, required = true) String wsKey,
		@RequestParam(value = WebAnnotationFields.USER_TOKEN, required = false, defaultValue = WebAnnotationFields.USER_ANONYMOUNS) String userToken,
		@RequestParam(value = "name", required = true) String name,
		HttpServletRequest request
		) throws ApplicationAuthenticationException, UserAuthorizationException, OperationAuthorizationException {

		validateApiKey(wsKey, WebAnnotationFields.DELETE_METHOD);
		userToken = getUserToken(userToken, request);
		getAuthorizationService().authorizeUser(userToken, wsKey, Operations.USER_DELETE);
		
		UserOperationResponse response;
		response = new UserOperationResponse(
				wsKey, "/admin/delete");
			
		try{
			int numDeletedUserEntries = getAnnotationService().deleteUserEntry(name);
			response.success = true;
			response.error = "number of deleted user entries: " + Integer.toString(numDeletedUserEntries);
		} catch (Exception e){
			Logger.getLogger(SolrSyntaxConstants.ROOT).error(e);
			response.success = false;
			response.error = e.getMessage();
		}

		String jsonStr = JsonWebUtils.toJson(response, null);
		return buildResponseEntityForJsonString(jsonStr);		
	}	
*/
	
	@RequestMapping(value = "/provider/{providerId}.json", method = RequestMethod.POST
			, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(notes = SwaggerConstants.SAMPLES_PROVIDER_API, value = "Register a new annotation provider", nickname = "createAnnotationProvider", response = java.lang.Void.class)
	public ResponseEntity<String> createAnnotationProvider (
		@RequestParam(value = WebAnnotationFields.PARAM_WSKEY, required = true) String wsKey,
		@RequestParam(value = WebAnnotationFields.PARAM_CLIENT_SECRET, required = false) String clientSecret,
		@RequestParam(value = WebAnnotationFields.PARAM_USERNAME, required = false) String userName,
		@RequestParam(value = WebAnnotationFields.PARAM_PASSWORD, required = false) String password,
		@RequestParam(value = WebAnnotationFields.USER_TOKEN, required = false, defaultValue = WebAnnotationFields.USER_ANONYMOUNS) String userToken,
		@PathVariable(value = WebAnnotationFields.PATH_PARAM_PROVIDER_ID) String providerId,
		@RequestBody String providerBody,
		HttpServletRequest request) throws HttpException {

		validateApiKey(wsKey, WebAnnotationFields.WRITE_METHOD);
		userToken = getUserToken(userToken, request);
		getAuthorizationService().authorizeUser(userToken, wsKey, Operations.PROVIDER_CREATE);
		
		validatePrivateKey(wsKey, clientSecret);
		
		AgentTypes defaultAgentType = AgentTypes.ORGANIZATION;
		Agent webAgent = null;
		try {
			webAgent = getAnnotationService().parseAgentLd(defaultAgentType, providerBody);
		} catch (JsonParseException e) {
			throw new ProviderOperationException(I18nConstants.PROVIDER_CANT_PARSE_BODY, 
					I18nConstants.PROVIDER_CANT_PARSE_BODY, 
					new String[]{WebAnnotationFields.PROVIDER, providerBody});
		}

		String providerIdUrl = WebAnnotationFields.PROVIDER_ID_PREFIX + providerId;	
		validateProvider(providerIdUrl, webAgent);

		return storeProvider(wsKey, defaultAgentType, webAgent, userToken, providerIdUrl); 
	}

	/**
	 * This method validates that given id and template id are equal if template id exists and
	 * that given id not already exists in database.
	 * @param providerIdUrl The given ID
	 * @param webAgent Agent object containing ID field
	 * @throws ParamValidationException
	 * @throws ProviderOperationException
	 */
	private void validateProvider(String providerIdUrl, Agent webAgent) 
			throws ParamValidationException, ProviderOperationException {
		
		/** validate that given id and template id are equal if template id exists */
		if(StringUtils.isNotEmpty(webAgent.getHttpUrl()) && !webAgent.getHttpUrl().equals(providerIdUrl))
			throw new ProviderOperationException(
					I18nConstants.PROVIDER_MESSAGE_IDENTIFIER_WRONG
					, I18nConstants.PROVIDER_MESSAGE_IDENTIFIER_WRONG
					, new String[]{providerIdUrl, webAgent.getHttpUrl()});
					
		/** validate if given id already exists in database */
		if (getAnnotationService().existsProviderIdInDb(providerIdUrl)) 
			throw new ProviderOperationException(I18nConstants.PROVIDER_ID_EXISTS, 
					I18nConstants.PROVIDER_ID_EXISTS, 
					new String[]{WebAnnotationFields.PROVIDER, providerIdUrl});

		/** check for mandatory fields  */
		if(StringUtils.isEmpty(webAgent.getName()))
			throw new ProviderOperationException(I18nConstants.PROVIDER_MISSING_MANDATORY_FIELD, 
					I18nConstants.PROVIDER_MISSING_MANDATORY_FIELD, 
					new String[]{WebAnnotationFields.PROVIDER, providerIdUrl});
	}
	
	@RequestMapping(value = "/admin/user", method = RequestMethod.POST
			, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Register a new user", nickname = "registerUser", response = java.lang.Void.class)
	public ResponseEntity<String> createUser (
		@RequestParam(value = WebAnnotationFields.PARAM_WSKEY, required = true) String wsKey,
		@RequestParam(value = WebAnnotationFields.PARAM_USERNAME, required = false) String userName,
		@RequestParam(value = WebAnnotationFields.PARAM_TYPE, required = false) String type,
		@RequestParam(value = WebAnnotationFields.PATH_PARAM_PROVIDER_ID, required = false) String providerId,
		@RequestParam(value = WebAnnotationFields.USER_TOKEN, required = false, defaultValue = WebAnnotationFields.USER_ANONYMOUNS) String userToken,
		@RequestParam(value = WebAnnotationFields.PATH_PARAM_USER_ID, required = true) String userId,
		HttpServletRequest request) throws HttpException {
	
		validateApiKey(wsKey, WebAnnotationFields.WRITE_METHOD);
		userToken = getUserToken(userToken, request);
		getAuthorizationService().authorizeUser(userToken, wsKey, Operations.USER_CREATE);

		AgentTypes agentType = AgentTypes.PERSON;
		if (StringUtils.isNotEmpty(type))
			agentType = AgentTypes.getByJsonValue(type);

		/** create user ID URL using prefix */
		String userIdUrl = WebAnnotationFields.USER_ID_PREFIX + userId;	
		
		/** build user */		
        BaseUser user = new BaseUser();

        IdGenerationTypes idGeneration = IdGenerationTypes.getValueByType(
				IdGenerationTypes.GENERATED_BY_PROVIDER.getIdType());
		if (!IdGenerationTypes.isRegistered(idGeneration.name())) {
			throw new UserOperationException(I18nConstants.USER_ID_GENERATION_TYPE_DOES_NOT_MATCH, 
					I18nConstants.USER_ID_GENERATION_TYPE_DOES_NOT_MATCH + IdGenerationTypes.printTypes(), 
					new String[]{WebAnnotationFields.USER, userId});
		}
		user.setName(userName);
		user.setInternalType(agentType.name());
		user.setType(agentType.getJsonValue());
		user.setHttpUrl(userIdUrl);
		user.setIdGeneration(idGeneration);
		
		validateUser(userIdUrl, user);

		return storeUser(wsKey, agentType, user); 
	}
	
	/**
	 * This method validates that given user id and template id are equal if template id exists and
	 * that given id not already exists in database.
	 * @param userId The given ID
	 * @param webAgent Agent object containing ID field
	 * @throws UserValidationException
	 * @throws UserOperationException
	 */
	private void validateUser(String userId, Agent webAgent) 
			throws ParamValidationException, UserOperationException {
					
		/** validate if given id already exists in database */
		if (getAnnotationService().existsUserIdInDb(userId)) 
			throw new UserOperationException(I18nConstants.USER_ID_EXISTS, 
					I18nConstants.USER_ID_EXISTS, 
					new String[]{WebAnnotationFields.USER, userId});

		/** check for mandatory fields  */
		if(StringUtils.isEmpty(webAgent.getName()))
			throw new UserOperationException(I18nConstants.USER_MISSING_MANDATORY_FIELD, 
					I18nConstants.USER_MISSING_MANDATORY_FIELD, 
					new String[]{WebAnnotationFields.USER, userId});
	}
		
}
