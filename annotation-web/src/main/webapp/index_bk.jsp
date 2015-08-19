<!DOCTYPE html>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Europeana Creative - Component Service</title>
<link href="css/css1.css" rel="stylesheet" type="text/css" />
<link href="css/main.css" rel="stylesheet" type="text/css" />
<link href="css/main_002.css" rel="stylesheet" type="text/css" />
</head>
<body>

	<div id="wrapper" class="max-width">
		<header id="banner"><!-- role="banner" ? -->
			<img class="display-none" src="img/creative-logo.png" alt=""> <img
				class="bg" src="img/creative-banner.jpg" alt="Europeana Projects">
			<h2>Europeana Creative - Annotation Component </h2>
		</header>
		<div id="mainblock">
			<div id="content">
				<!-- Box for the Service Description -->
					<div class="">
						<!--  <h3>Annotation</h3>
						
						<div class="left">
							
						</div>
						-->
						<img src="http://www.tehamaschools.org/files/Fether%20Grey.png" height="64" />
						Annotation component is a service that implements the management and indexing of Web Annotations
						This is an utility test page, which supports easy testing of the Rest APIs in different scenarios.
						The API Documentation using swagger console can be accessed here: <a href="./docs/index.html" target="_new">API Docs (swagger)</a> 
					</div>

					<h3>JSON  API  </h3>
					<div id="table" style="width: 100%;">	
					<table>
						<thead>
						<tr>
							<td class="clsHeaderCell">Method</td>
							<td class="clsHeaderCell">Response</td>
							<td class="clsHeaderCell">Path</td>
							<td class="clsHeaderCell">Function</td>
						</tr>
						</thead>
						<tr>
							<td class="clsCellBorder cls0_0">GET</td>
							<td class="clsCellBorder">TEXT</td>
							<td class="clsCellBorder"><a href="annotations/component" target="_new">[/annotation-web]/annotations/component</a></td>
							<td class="clsCellBorder">Display the name of the current component</td>
						</tr>
						<tr>
							<td class="clsCellBorder cls0_0">GET</td>
							<td class="clsCellBorder">JSON</td>
							<td class="clsCellBorder">
								<a href="annotations/15502/GG_8285.json?wskey=xy&profile=annotations" target="_new">
									[/annotation-web]/annotations/{collection}>/{object}.json?wskey=&amp;profile=</a></td>
							<td class="clsCellBorder">get the list of annotations available for the given object</td>
						</tr>
						
						<tr>
							<td class="clsCellBorder cls0_0">GET</td>
							<td class="clsCellBorder">JSON</td>
							<td class="clsCellBorder"><a
								href="annotations/15502/GG_8285/1.json?wskey=xy&profile=annotations" target="_new">
									[annotation-web]/annotations/{collection}/{object_hash}/{annotationNr}.json?wskey=&amp;profile=
							</a></td>
							<td class="clsCellBorder">get the annotation identified by the given annotation NR</td>
						</tr>
<!-- 						
						<tr>
							<td class="clsCellBorder cls0_0" valign="top">POST</td>
							<td class="clsCellBorder" valign="top">JSON</td>
							<td class="clsCellBorder">
									[annotation-web]/annotations/{collection}/{object_hash}<br>
									ImageAnnotation as JSON:
								<form action="annotations/15502/GG_8285/" method="post">
								<input type="hidden" name="wskey" value="key">
								<input type="hidden" name="profile" value="webtest">
								<textarea rows="10" cols="20" name="annotation" >
{
    "creator": "current web user",
    "type": "IMAGE_ANNOTATION",
    "namedEntityIdList": [
        "https://twitter.com/OpenAnnotation"
    ],
    "namedEntityLabelList": [
        "Open Annotation"
    ],
    "imageUrl": "http://www.openannotation.org/spec/core/images/intro_model.png",
    "text": "W3C Open Annotation Data Model",
    "shape": [
        {
            "posX": 0,
            "posY": 0
        },
        {
            "posX": 0,
            "posY": 5
        },
        {
            "posX": 5,
            "posY": 5
        },
        {
            "posX": 5,
            "posY": 0
        }
    ]

}
								</textarea>
								<input type="submit" name="create" value="create">
								</form>
							</td>
							<td class="clsCellBorder" valign="top">
								create a semantic tag and return the object as stored in the database
							</td>
						</tr>
						
						 -->

<tr>
							<td class="clsCellBorder cls0_0" valign="top">POST</td>
							<td class="clsCellBorder" valign="top">JSON</td>
							<td class="clsCellBorder">
									[annotation-web]/annotations/{collection}/{object_hash}.json<br>
									ObjectTag as JSON:
								<form action="annotations/15502/GG_8285.json" method="post" target="_new">
								<input type="hidden" name="wskey" value="key">
								<input type="hidden" name="profile" value="webtest">
								<textarea rows="20" cols="25" name="annotation" >
{
"annotatedAt": 1403852113248,
"type": "OBJECT_TAG",
"annotatedBy": {
    "agentType": "[foaf:Person, euType:PERSON]",
    "name": "annonymous web user",
    "homepage": null,
    "mbox": null,
    "openId": null 
},
"body": {
    "contentType": "Link",
    "mediaType": null,
	"httpUri": "https://www.freebase.com/m/035br4",
    "language": "ro",
    "value": "Vlad Tepes",
    "multilingual": "[ro:Vlad Tepes,en:Vlad the Impaler]",
    "bodyType": "[oa:Tag,euType:SEMANTIC_TAG]"
},
"target": {
    "contentType": "text-html",
    "mediaType": "image",
    "language": "en",
    "value": "Vlad IV. Tzepesch, der Pfähler, Woywode der Walachei 1456-1462 (gestorben 1477)",
    "httpUri": "http://europeana.eu/portal/record/15502/GG_8285.html",
    "targetType": "[euType:WEB_PAGE]",
    "europeanaId": "/15502/GG_8285"
},
"serializedAt": "",
"serializedBy": {
    "agentType": "[prov:SoftwareAgent,euType:SOFTWARE_AGENT]",
    "name": "annonymous web user",
    "homepage": null,
    "mbox": null,
    "openId": null 
},
"styledBy":{
 	"contentType": "style",
    "mediaType": "text/css",
	"httpUri": "http://annotorious.github.io/latest/themes/dark/annotorious-dark.css",
	"value": null,
    "annotationClass": ".annotorious-popup"
}
}</textarea>
								<br>
								<input type="submit" name="create" value="create">
								</form>
							</td>
							<td class="clsCellBorder" valign="top">
								create a semantic tag (BodyType) and return the object as stored in the database
							</td>
						</tr>
						
																	
<tr>
							<td class="clsCellBorder cls0_0" valign="top">POST</td>
							<td class="clsCellBorder" valign="top">JSON</td>
							<td class="clsCellBorder">
									[annotation-web]/annotations/{collection}/{object_hash}.json<br>
									ObjectTag as JSON:
								<form action="annotations/15502/GG_8285.json" method="post" target="_new">
								<input type="hidden" name="wskey" value="key">
								<input type="hidden" name="profile" value="webtest">
								<textarea rows="20" cols="25" name="annotation" >
{
"annotatedAt": 1403852113248,
"type": "OBJECT_TAG",
"annotatedBy": {
    "agentType": "[foaf:Person, euType:PERSON]",
    "name": "annonymous web user",
    "homepage": null,
    "mbox": null,
    "openId": null 
},
"body": {
    "contentType": "text",
    "mediaType": null,
	"httpUri": null,
    "language": "ro",
    "value": "Vlad Tepes",
    "bodyType": "[oa:Tag, euType:TAG]"
},
"target": {
    "contentType": "text-html",
    "mediaType": "image",
    "language": "de",
    "value": "Vlad IV. Tzepesch, der Pfähler, Woywode der Walachei 1456-1462 (gestorben 1477)",
    "httpUri": "http://europeana.eu/portal/record/15502/GG_8285.html",
    "targetType": "[euType:WEB_PAGE]",
    "europeanaId": "/15502/GG_8285"
},
"serializedAt": "",
"serializedBy": {
    "agentType": "[prov:SoftwareAgent,euType:SOFTWARE_AGENT]",
    "name": "annonymous web user",
    "homepage": null,
    "mbox": null,
    "openId": null 
},
"styledBy":{
 	"contentType": "style",
    "mediaType": "text/css",
	"httpUri": "http://annotorious.github.io/latest/themes/dark/annotorious-dark.css",
	"value": null,
    "annotationClass": ".annotorious-popup"
}
}</textarea>
								<br>
								<input type="submit" name="create" value="create">
								</form>
							</td>
							<td class="clsCellBorder" valign="top">
								create a simple tag (BodyType) and return the object as stored in the database
							</td>
						</tr>						

<!-- 
<tr>
							<td class="clsCellBorder cls0_0" valign="top">POST</td>
							<td class="clsCellBorder" valign="top">JSON</td>
							<td class="clsCellBorder">
									[annotation-web]/annotations/{collection}/{object_hash}<br>
									ObjectTag as JSON:
								<form action="annotations/15502/GG_8285/" method="post">
								<input type="hidden" name="wskey" value="key">
								<input type="hidden" name="profile" value="webtest">
								<textarea rows="20" cols="25" name="annotation" >
{
"type" : "IMAGE_ANNOTATION", 
"annotatedAt" : null,
"annotatedBy" : { 
"agentType" : "PERSON", 
"name" : "Another web user", 
"homepage" : "http://www.pro.europeana.eu/web/europeana-creative" }, 

"body" : {
"bodyType" : "TEXT", 
"contentType" : "dcTypes:Text", 
"value" : "cool guy" }, 

"target" : {
"targetType" : "IMAGE", 
"europeanaId" : "/15502/GG_8285", 
"contentType" : "image/jpeg", 
"mediaType" : "image", 
"httpUri" : "http://europeanastatic.eu/api/image?uri=http%3A%2F%2Fbilddatenbank.khm.at%2Fimages%2F500%2FGG_8285.jpg&size=FULL_DOC&type=IMAGE",

"selector" : {
"selectorType": "SVG_RECTANGLE_SELECTOR",  
"dimensionMap" : { "height" : 100, "width" : 200, "rx" : 5, "ry" : 5 } }, 

"state" : {
"timestamp" : 0, 
"versionUri" : "http://bilddatenbank.khm.at/images/350/GG_8285.jpg", 
"format" : "image/jpeg", 
"authenticationRequired" : false }
 }, 

"motivation" : "COMMENTING" 

}
</textarea>
								<br>
								<input type="submit" name="create" value="create">
								</form>
							</td>
							<td class="clsCellBorder" valign="top">
								create an (simple) image annotation (BodyType) and return the object as stored in the database
							</td>
						</tr>						
-->
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
					</table>
					
					</div>
					<!--  end table div -->	

<!-- 
					<div class="">
						<img src="http://www.tehamaschools.org/files/Fether%20Grey.png" height="64" />
						Annotation component is a service that implements the management and indexing of ImageAnnotations
					</div>
-->
					<h3>JSON-LD  API  </h3>
					<div id="table" style="width: 100%;">	
					<table>
						<thead>
						<tr>
							<td class="clsHeaderCell">Method</td>
							<td class="clsHeaderCell">Response</td>
							<td class="clsHeaderCell">Path</td>
							<td class="clsHeaderCell">Function</td>
						</tr>
						</thead>
						
						<!-- 
						<tr>
							<td class="clsCellBorder cls0_0">GET</td>
							<td class="clsCellBorder">JSON-LD</td>
							<td class="clsCellBorder">
								<a href="annotations/15502/GG_8285.jsonld?wskey=xy&profile=annotations">
									[/annotation-web]/annotations/{collection}>/{object}.jsonld?wskey=&amp;profile=</a></td>
							<td class="clsCellBorder">get the list of annotations available for the given object</td>
						</tr>
						 -->
						
						<tr>
							<td class="clsCellBorder cls0_0">GET</td>
							<td class="clsCellBorder">JSON-LD</td>
							<td class="clsCellBorder"><a
								href="annotations/15502/GG_8285/1.jsonld?wskey=xy&profile=annotations" target="_new">
									[annotation-web]/annotationld/{collection}/{object_hash}/{annotationNr}.jsonld?wskey=&amp;profile=
							</a></td>
							<td class="clsCellBorder">get the annotation identified by the given annotation NR</td>
						</tr> 
 						

<tr>
							<td class="clsCellBorder cls0_0" valign="top">POST</td>
							<td class="clsCellBorder" valign="top">JSON_LD</td>
							<td class="clsCellBorder">
									[annotation-web]/annotationld/{collection}/{object_hash}.jsonld<br>
									Object as JSON-LD:
								<form action="annotations/15502/GG_8285.jsonld" method="post" target="_new">
								<input type="hidden" name="wskey" value="key">
								<input type="hidden" name="profile" value="webtest">
								<textarea rows="20" cols="25" name="annotation" >
{
    "@context": {
        "oa": "http://www.w3.org/ns/oa-context-20130208.json"
    },
    "@type": "[oa:annotation,euType:OBJECT_TAG]",
    "annotatedAt": "2012-11-10T09:08:07",
    "annotatedBy": {
        "@id": "open_id_1",
        "@type": "[foaf:Person, euType:PERSON]",
        "name": "annonymous web user"
    },
    "body": {
        "@type": "[oa:Tag,cnt:ContentAsText,dctypes:Text,euType:SEMANTIC_TAG]",
        "chars": "Vlad Tepes",
        "foaf:page": "https://www.freebase.com/m/035br4",
        "format": "text/plain",
        "language": "ro",
        "multilingual": "[ro:Vlad Tepes,en:Vlad the Impaler]"
    },
    "motivation": "oa:tagging",
    "serializedAt": "2012-11-10T09:08:07",
    "serializedBy": {
        "@id": "open_id_2",
        "@type": "[prov:SoftwareAgent,euType:SOFTWARE_AGENT]",
        "foaf:homepage": "http://annotorious.github.io/",
        "name": "Annotorious"
    },
    "styledBy": {
        "@type": "[oa:CssStyle,euType:CSS]",
        "source": "http://annotorious.github.io/latest/themes/dark/annotorious-dark.css",
        "styleClass": "annotorious-popup"
    },
    "target": {
        "@type": "[oa:SpecificResource,euType:IMAGE]",
        "contentType": "image/jpeg",
        "httpUri": "http://europeanastatic.eu/api/image?uri=http%3A%2F%2Fbilddatenbank.khm.at%2Fimages%2F500%2FGG_8285.jpg&size=FULL_DOC&type=IMAGE",
        "selector": {
            "@type": "[oa:SvgRectangle,euType:SVG_RECTANGLE_SELECTOR]",
            "dimensionMap": "[left:5,right:3]"
        },
        "source": {
            "@id": "/15502/GG_8285",
            "contentType": "text/html",
            "format": "dctypes:Text"
        },
        "targetType": "[oa:SpecificResource,euType:IMAGE]"
    },
    "type": "OBJECT_TAG"
}
</textarea>
								<br>
								<input type="submit" name="create" value="create">
								</form>
							</td>
							<td class="clsCellBorder" valign="top">
								create a semantic annotation (ObjectTag) and return the object as stored in the database
							</td>
						</tr>						

<tr>
							<td class="clsCellBorder cls0_0" valign="top">POST</td>
							<td class="clsCellBorder" valign="top">JSON_LD</td>
							<td class="clsCellBorder">
									[annotation-web]/annotation.jsonld<br>
									Simple Tag as (Europeana) JSON-LD:
								<form action="annotation.jsonld" method="post" target="_new">
								<input type="hidden" name="wskey" value="key">
								<input type="hidden" name="provider" value="historypin">
								<textarea rows="20" cols="25" name="annotation" >
{
 "@context": "http://www.europeana.eu/annotation/context.jsonld",
 "@type": "oa:Annotation",
 "@id": "http://data.europeana.eu/annotation/historypin/456",
 "annotatedBy": {
   "@id": "https://www.historypin.org/en/person/55376/",
   "@type": "foaf:Person",
   "name": "John Smith"
 },
 "annotatedAt": "2015-02-27T12:00:43Z",
 "serializedAt": "2015-02-28T13:00:34Z",
 "serializedBy": "http://www.historypin.org",
 "motivation": "oa:tagging",
 "body": "church",
 "target": "http://data.europeana.eu/item/123/xyz",
 "oa:equivalentTo": "https://www.historypin.org/en/item/456"
}
</textarea>
								<br>
								<input type="submit" name="create" value="create">
								</form>
							</td>
							<td class="clsCellBorder" valign="top">
								create a simple tag (ObjectTag) and return the object as stored in the database
							</td>
						</tr>						


						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
					</table>
					
					</div>
					<!--  end table div -->	
						
						
<!-- 		
					<div class="">
						<img src="http://www.tehamaschools.org/files/Fether%20Grey.png" height="64" />
						Annotation component is a service that implements the search for Annotations
					</div>
-->					

					<h3>Search Annotations API</h3>
					<div id="table" style="width: 100%;">	
					<table>
						<thead>
						<tr>
							<td class="clsHeaderCell">Method</td>
							<td class="clsHeaderCell">Response</td>
							<td class="clsHeaderCell">Path</td>
							<td class="clsHeaderCell">Function</td>
						</tr>
						</thead>
											

						<tr>
							<td class="clsCellBorder cls0_0" valign="top">GET</td>
							<td class="clsCellBorder" valign="top">JSON</td>
							<td class="clsCellBorder">
									[annotation-web]/annotations/search<br>
									Search for annotation (all fields or by field):
								<form action="annotations/search" method="get" target="_new">
								<input type="hidden" name="wskey" value="key">
								<input type="hidden" name="profile" value="webtest">
								<textarea rows="1" cols="10" name="query" >
								</textarea>
								<br>
								<select name="field">
								<option value=all> All </option>
								<option value=label> label </option>
								<option value=body_value> body value </option>
								<option value=tag_id> tag ID </option>
								<option value=multilingual> multilingual </option>
								</select>
								<select name="language">
								<option value=en> EN </option>
								<option value=de> DE </option>
								<option value=ro> RO </option>
								</select>
								&nbsp;start on:&nbsp;<input name="startOn" type="text" size="5" maxlength="5" value="0">
								&nbsp;limit:&nbsp;<input name="limit" type="text" size="5" maxlength="5" value="">
								<!-- 
								&nbsp;Facet
								<select name="facet">
								<option name=all value=all> All </option>
								<option name=language value=language> language </option>
								<option name=body_type value=body_type> body type </option>
								<option name=annotationId_string value=annotationId_string> annotation ID string </option>
								</select>
								 -->
								<br>
								<input type="submit" name="search" value="search">
								</form>
							</td>
							<td class="clsCellBorder" valign="top">
								Search for the annotation identified by the given query string and Solr field
							</td>
						</tr>						
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
					</table>
					
					</div>
					<!--  end table div -->
					
					<h3>Search Tags  API  </h3>
					<div id="table" style="width: 100%;">	
					<table>
						<thead>
						<tr>
							<td class="clsHeaderCell">Method</td>
							<td class="clsHeaderCell">Response</td>
							<td class="clsHeaderCell">Path</td>
							<td class="clsHeaderCell">Function</td>
						</tr>
						</thead>
											

						<tr>
							<td class="clsCellBorder cls0_0" valign="top">GET</td>
							<td class="clsCellBorder" valign="top">JSON</td>
							<td class="clsCellBorder">
									[annotation-web]/tags/search<br>
									Search for tags (all fields or by field):
								<form action="tags/search" method="get" target="_new">
								<input type="hidden" name="wskey" value="key">
								<input type="hidden" name="profile" value="webtest">
								<textarea rows="1" cols="10" name="query" >
								</textarea>
								<br>
								<select name="field">
								<option value=all> All </option>
								<option value=label> label </option>
								<option value=tag_id> tag ID </option>
								<option value=multilingual> multilingual </option>
								</select>
								<select name="language">
								<option value=en> EN </option>
								<option value=de> DE </option>
								<option value=ro> RO </option>
								</select>
								&nbsp;start on:&nbsp;<input name="startOn" type="text" size="5" maxlength="5" value="0">
								&nbsp;limit:&nbsp;<input name="limit" type="text" size="5" maxlength="5" value="">
								<br>
								<input type="submit" name="search" value="search">
								</form>
							</td>
							<td class="clsCellBorder" valign="top">
								Search for the tag identified by the given query string and Solr field
							</td>
						</tr>						
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
					</table>
					
					</div>
					<!--  end table div -->	
						
			</div>
			<!-- end of content -->
		</div>
		<!-- end of mainblock -->

		<hr />
		<!--Europeana Group Block-->
		<footer id="footer">
			<div class="middle f-left">
				<a href="http://www.pro.europeana.eu/web/guest/projects"
					class="f-left">Europeana Network projects</a>
			</div>
			<span class="f-right">co-funded by the European Union<img
				alt="european union flag" src="img/eu-flag.gif"></span>
		</footer>


	</div>
	<!-- end of wrapper -->
</body>
</html>
