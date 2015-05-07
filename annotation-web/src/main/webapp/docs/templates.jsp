<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Europeana Creative - Annotation Templates</title>
<link href="../css/css1.css" rel="stylesheet" type="text/css" />
<link href="../css/main.css" rel="stylesheet" type="text/css" />
<link href="../css/main_002.css" rel="stylesheet" type="text/css" />
</head>
<body>

	<div id="wrapper" class="max-width">
		<header id="banner"><!-- role="banner" ? -->
			<img class="display-none" src="../img/creative-logo.png" alt=""> <img
				class="bg" src="../img/creative-banner.jpg" alt="Europeana Projects">
			<h2>Europeana Creative - Annotation Templates </h2>
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
							<td class="clsCellBorder cls0_0" valign="top">POST</td>
							<td class="clsCellBorder" valign="top">JSON</td>
							<td class="clsCellBorder">
									[annotation-web]/annotations/{collection}/{object_hash}.json<br>
									ObjectTag as JSON:
								<form action="annotations/15502/GG_8285.json" method="post" target="_new">
								<input type="hidden" name="wskey" value="key">
								<input type="hidden" name="profile" value="webtest">
								<textarea rows="20" cols="60" name="annotation" >
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
    "value": "Vlad IV. Tzepesch, der PfÃ¤hler, Woywode der Walachei 1456-1462 (gestorben 1477)",
    "httpUri": "http://europeana.eu/portal/record/15502/GG_8285.html",
    "targetType": "[euType:WEB_PAGE]"
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
}
</textarea>
								<br>
								</form>
							</td>
							<td class="clsCellBorder" valign="top">
								create a semantic tag (BodyType) for 'webanno' provider and return the object as stored in the database. Whereas, 'resourceId' is extracted from 'target.httpUri' and 'annotationNr' is generated.
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
								<textarea rows="20" cols="60" name="annotation" >
{
"annotatedAt": 1403852113248,
"type": "OBJECT_TAG",
"sameAs": "http://historypin.com/annotation/1234",
"equivalentTo": "http://historypin.com/annotation/1234",
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
    "value": "Vlad IV. Tzepesch, der PfÃ¤hler, Woywode der Walachei 1456-1462 (gestorben 1477)",
    "httpUri": "http://europeana.eu/portal/record/15502/GG_8285.html",
    "targetType": "[euType:WEB_PAGE]"
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
}
</textarea>
								<br>
								</form>
							</td>
							<td class="clsCellBorder" valign="top">
								create a semantic tag (BodyType) for 'historypin' provider and return the object as stored in the database. Note that field 'sameAs' is employed for that task.
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
								<textarea rows="20" cols="60" name="annotation" >
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
    "value": "Vlad IV. Tzepesch, der PfÃ¤hler, Woywode der Walachei 1456-1462 (gestorben 1477)",
    "httpUri": "http://europeana.eu/portal/record/15502/GG_8285.html",
    "targetType": "[euType:WEB_PAGE]"
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
								</form>
							</td>
							<td class="clsCellBorder" valign="top">
								create a simple tag (BodyType) and return the object as stored in the database
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
						<tr>
							<td class="clsCellBorder cls0_0" valign="top">POST</td>
							<td class="clsCellBorder" valign="top">JSON_LD</td>
							<td class="clsCellBorder">
									[annotation-web]/annotationld/{collection}/{object_hash}.jsonld<br>
									Object as JSON-LD:
								<form action="annotations/15502/GG_8285.jsonld" method="post" target="_new">
								<input type="hidden" name="wskey" value="key">
								<input type="hidden" name="profile" value="webtest">
								<textarea rows="20" cols="60" name="annotation" >
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
    "motivatedBy": "oa:tagging",
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
								</form>
							</td>
							<td class="clsCellBorder" valign="top">
								create a semantic annotation (ObjectTag) and return the object as stored in the database for 'webanno' provider
							</td>
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
								<textarea rows="20" cols="60" name="annotation" >
{
    "@context": {
        "oa": "http://www.w3.org/ns/oa-context-20130208.json"
    },
    "@type": "[oa:annotation,euType:OBJECT_TAG]",
    "sameAs": "http://historypin.com/annotation/1234",
    "equivalentTo": "http://historypin.com/annotation/1234",
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
    "motivatedBy": "oa:tagging",
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
								</form>
							</td>
							<td class="clsCellBorder" valign="top">
								create a semantic annotation (ObjectTag) and return the object as stored in the database for 'historypin' provider. Note that field 'sameAs' is employed for that task.
							</td>
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
								<textarea rows="20" cols="60" name="annotation" >
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
        "multilingual": "[ro:Vlad Tepes,en:Vlad the Impaler]",
        "concept": {
            "notation": "skos:notation",
            "prefLabel": {
                "@id": "skos:prefLabel",
                "@container": "@language"
            },
            "altLabel": {
                "@id": "skos:altLabel",
                "@container": "@language"
            },
            "hiddenLabel": {
                "@id": "skos:altLabel",
                "@container": "@language"
            },
            "narrower": "skos:narrower",
            "broader": "skos:broader",
            "related": "skos:related"
        }
    },
    "motivatedBy": "oa:tagging",
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
								</form>
							</td>
							<td class="clsCellBorder" valign="top">
								create a semantic annotation (ObjectTag) and return the object as stored in the database for 'webanno' provider using SKOS concept in Body.
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
											
					<h3>EUROPEANA-LD  API  </h3>
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
							<td class="clsCellBorder cls0_0" valign="top">POST</td>
							<td class="clsCellBorder" valign="top">JSON_LD</td>
							<td class="clsCellBorder">
									[annotation-web]/annotation/create.jsonld<br>
									Object as JSON-LD:
								<form action="annotation/create.jsonld" method="post" target="_new">
								<input type="hidden" name="wskey" value="key">
								<input type="hidden" name="profile" value="webtest">
								<textarea rows="20" cols="60" name="annotation" >
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
								</form>
							</td>
							<td class="clsCellBorder" valign="top">
								create an europeana annotation (SimpleTag) and return the object as stored in the database for 'historypin' provider. 
								Identification of annotation types works as follows. Simple Tagging (when motivation is 'oa:tagging' and body is a string literal).
							</td>
						</tr>						

						<tr>
							<td class="clsCellBorder cls0_0" valign="top">POST</td>
							<td class="clsCellBorder" valign="top">JSON_LD</td>
							<td class="clsCellBorder">
									[annotation-web]/annotation/create.jsonld<br>
									Object as JSON-LD:
								<form action="annotation/create.jsonld" method="post" target="_new">
								<input type="hidden" name="wskey" value="key">
								<input type="hidden" name="profile" value="webtest">
								<textarea rows="20" cols="60" name="annotation" >
{
    "@context": "http://www.europeana.eu/annotation/context.jsonld",
    "@type": "oa:Annotation",
    "annotatedBy": {
        "@id": "https://www.historypin.org/en/person/55376/",
        "@type": "foaf:Person",
        "name": "John Smith"
    },
    "annotatedAt": "2015-02-27T12:00:43Z",
    "serializedAt": "2015-02-28T13:00:34Z",
    "serializedBy": "http://www.historypin.org",
    "motivation": "oa:linking",
    "target": [
        "http://www.europeana.eu/portal/record/123/xyz.html", 
        "http://www.europeana.eu/portal/record/333/xxx.html"
    ],
    "oa:equivalentTo": "https://www.historypin.org/en/item/456"
}
</textarea>
								<br>
								</form>
							</td>
							<td class="clsCellBorder" valign="top">
								create an europeana annotation (SimpleLink) and return the object as stored in the database for 'historypin' provider. 
								Simple Object Linking (when motivation is 'oa:linking' and no body is provided and target contains an array with at least 2 urls)
							</td>
						</tr>						

						<tr>
							<td class="clsCellBorder cls0_0" valign="top">POST</td>
							<td class="clsCellBorder" valign="top">JSON_LD</td>
							<td class="clsCellBorder">
									[annotation-web]/annotation/create.jsonld<br>
									Object as JSON-LD:
								<form action="annotation/create.jsonld" method="post" target="_new">
								<input type="hidden" name="wskey" value="key">
								<input type="hidden" name="profile" value="webtest">
								<textarea rows="20" cols="60" name="annotation" >
{
    "@context": "http://www.europeana.eu/annotation/context.jsonld",
    "@type": "oa:Annotation",
    "annotatedBy": {
        "@id": "https://www.historypin.org/en/person/55376/",
        "@type": "foaf:Person",
        "name": "John Smith"
    },
    "annotatedAt": "2015-02-27T12:00:43Z",
    "serializedAt": "2015-02-28T13:00:34Z",
    "serializedBy": "http://www.historypin.org",
    "motivation": "oa:tagging",
    "body": {
        "@type": [
        	"oa:Tag",
        	"cnt:ContentAsText",
        	"dctypes:Text"
        ],
        "chars": "Vlad Tepes",
        "format": "text/plain",
        "language": "ro"
    },
    "target": "http://data.europeana.eu/item/123/xyz",
    "oa:equivalentTo": "https://www.historypin.org/en/item/456"
}
</textarea>
								<br>
								</form>
							</td>
							<td class="clsCellBorder" valign="top">
								create an europeana annotation (SimpleTag) and return the object as stored in the database for 'historypin' provider. 
								Identification of annotation types works as follows. Simple Tagging (when motivation is 'oa:tagging' and body type is a string list).
							</td>
						</tr>											

						<tr>
							<td class="clsCellBorder cls0_0" valign="top">POST</td>
							<td class="clsCellBorder" valign="top">JSON_LD</td>
							<td class="clsCellBorder">
									[annotation-web]/annotation/create.jsonld<br>
									Object as JSON-LD:
								<form action="annotation/create.jsonld" method="post" target="_new">
								<input type="hidden" name="wskey" value="key">
								<input type="hidden" name="profile" value="webtest">
								<textarea rows="20" cols="60" name="annotation" >
{
 "@context": "http://www.europeana.eu/annotation/context.jsonld",
 "@type": "oa:Annotation", 
 "annotatedBy": {
   "@id": "https://www.historypin.org/en/person/55376/",
   "@type": "foaf:Person",
   "name": "John Smith"
 },
 "annotatedAt": "2015-02-27T12:00:43Z",
 "serializedAt": "2015-02-28T13:00:34Z",
 "serializedBy": "http://data.europeana.eu/provider/Historypin",
 "motivation": "oa:tagging",
 "body": ["church", "orthodox"] ,
 "target": "http://data.europeana.eu/item/123/xyz",
 "oa:equivalentTo": "https://www.historypin.org/en/item/456"
}
</textarea>
								<br>
								</form>
							</td>
							<td class="clsCellBorder" valign="top">
								Multiple Simple Tags (when motivation is 'oa:tagging' and body is a string array).
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
