package eu.europeana.annotation.web.service.controller.admin;

import java.util.List;

import javax.ws.rs.DELETE;

import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import eu.europeana.annotation.definitions.model.WebAnnotationFields;
import eu.europeana.annotation.definitions.model.impl.AbstractAnnotation;
import eu.europeana.annotation.definitions.model.impl.BaseAnnotationId;
import eu.europeana.annotation.definitions.model.whitelist.Whitelist;
import eu.europeana.annotation.solr.vocabulary.SolrAnnotationConst;
import eu.europeana.annotation.utils.JsonUtils;
import eu.europeana.annotation.web.http.SwaggerConstants;
import eu.europeana.annotation.web.model.AnnotationOperationResponse;
import eu.europeana.annotation.web.model.AnnotationSearchResults;
import eu.europeana.annotation.web.model.WhitelistOperationResponse;
import eu.europeana.annotation.web.model.WhitelsitSearchResults;
import eu.europeana.annotation.web.service.controller.BaseRest;
import eu.europeana.api2.utils.JsonWebUtils;

//@Controller
//@Api(value = "whitelist", description = "Whitelist JSON Rest Service")
public class WhitelistRest extends BaseRest {


	@RequestMapping(value = "/whitelist/component", method = RequestMethod.GET
			, produces = MediaType.TEXT_HTML_VALUE)
	@ResponseBody
	public String getComponentName() {
		//return WebAnnotationFields.WHITELIST;
		return "whitelist";
	}

	@RequestMapping(value = "/whitelist/{uri}.json"
			, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ModelAndView getWhitelist (
		@RequestParam(value = "apiKey", required = false) String apiKey,
		@RequestParam(value = "profile", required = false) String profile,
		@RequestParam(value = "uri", required = true) String uri
		) {

		Whitelist whitelist = getAnnotationService().getWhitelistByUrl(uri);

		WhitelistOperationResponse response = new WhitelistOperationResponse(
				apiKey, "/whitelist/uri.json");

		if (whitelist != null) {
			response = new WhitelistOperationResponse(
					apiKey, "/whitelist/uri.json");			
			response.success = true;
			response.setWhitelist(whitelist);
		} else {
			String errorMessage = WhitelistOperationResponse.ERROR_NO_OBJECT_FOUND;
			response.action = "get: /whitelist/"+ uri + ".json";
			response.success = false;
			response.error = errorMessage;
		}
		
		return JsonWebUtils.toJson(response, null);
	}
	
	@RequestMapping(value = "/whitelist/all.json"
			, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ModelAndView getAllWhitelist (
		@RequestParam(value = "apiKey", required = false) String apiKey,
		@RequestParam(value = "profile", required = false) String profile
		) {

		List<? extends Whitelist> whitelist = getAnnotationService().getAllWhitelistEntries();

		String action = "/whitelist/all.json";
		
		WhitelsitSearchResults<Whitelist> response = buildSearchWhitelistResponse(
				whitelist, apiKey, action);

		return JsonWebUtils.toJson(response, null);
	}
	
	@RequestMapping(value = "/whitelist/{uri}.json", method = RequestMethod.POST
			, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(notes=SwaggerConstants.SAMPLES_JSON_LINK, value="")
	public ModelAndView createWhitelist (
		@RequestParam(value = "apiKey", required = false) String apiKey,
		@RequestParam(value = "profile", required = false) String profile,
		@RequestParam(value = "uri", required = true) String uri,
		@RequestBody String whitelist) {

		String action = "create:/whitelist/uri.json";
		
		//parse
		Whitelist webWhitelist = JsonUtils.toWhitelistObject(whitelist);
	
		//store				
		Whitelist storedWhitelist = getAnnotationService().storeWhitelist(webWhitelist);

		//build response
		WhitelistOperationResponse response = new WhitelistOperationResponse(
				apiKey, action);
		response.success = true;

		response.setWhitelist(storedWhitelist);

		return JsonWebUtils.toJson(response, null);
	}

	@RequestMapping(value = "/whitelist/load"
			, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ModelAndView getWhitelistFromResources (
		@RequestParam(value = "apiKey", required = false) String apiKey,
		@RequestParam(value = "profile", required = false) String profile
		) {

		List<? extends Whitelist> whitelist = getAnnotationService().loadWhitelistFromResources();

		WhitelistOperationResponse response = new WhitelistOperationResponse(
				apiKey, "/whitelist/load");

		if (whitelist != null) {
			response = new WhitelistOperationResponse(
					apiKey, "/whitelist/load");			
			response.success = true;
			response.setWhitelistEntries(whitelist);
		} else {
			String errorMessage = WhitelistOperationResponse.ERROR_NO_OBJECT_FOUND;
			response.action = "get: /whitelist/load";
			response.success = false;
			response.error = errorMessage;
		}
		
		return JsonWebUtils.toJson(response, null);
	}

	
	@DELETE
	@RequestMapping(value = "/whitelist/deleteall", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public WhitelistOperationResponse deleteAllWhitelistEntries(
		@RequestParam(value = "apiKey", required = false) String apiKey,
		@RequestParam(value = "profile", required = false) String profile
		) {


		WhitelistOperationResponse response;
		response = new WhitelistOperationResponse(
				apiKey, "/whitelist/delete");
			
		try{
			getAnnotationService().deleteAllWhitelistEntries();
			response.success = true;
		} catch (Exception e){
			Logger.getLogger(SolrAnnotationConst.ROOT).error(e);
			response.success = false;
			response.error = e.getMessage();
		}

		return response;
	}

	
}