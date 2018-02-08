<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@include file="header.html"%>
<% 
String pageDescription="JSON templates for creation of provider objects";
String withType = request.getParameter("withType");
boolean hasType = withType != null;		
%>	
<%@include file="description.jspf"%>

<ul id="toc">
	<li><a href="#provider">Create provider</a></li>
	<li><a href="#user">Promote user</a></li>
</ul>

<h3 id="provider">Create provider</h3>
The json serialization available in the following box is a valid input to be used for the creation of <b>provider</b>.
&nbsp;&nbsp;&nbsp; <a href="#top">top</a> 
<br>
<textarea rows="8" cols="40" name="json-provider">
{
  "@context": "http://www.w3.org/ns/anno.jsonld",
  "id": "http://data.europeana.eu/annotation/provider/1",
  "type": "Organization",
  "name": "Europeana.eu"
}
</textarea>
<br>

<h3 id="user">Promote user</h3>
The json serialization available in the following box is a valid input to be used for the promotion of <b>user</b>.
&nbsp;&nbsp;&nbsp; <a href="#top">top</a> 
<br>
<textarea rows="8" cols="40" name="json-user">
{
  "@context": "http://www.w3.org/ns/anno.jsonld",
  "id": "http://data.europeana.eu/annotation/user/1",
  "type": "Person",
  "name": "Europeana.user"
}
</textarea>
<br>

<br>			
<%@include file="footer.html"%>