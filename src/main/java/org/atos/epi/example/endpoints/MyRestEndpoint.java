package org.atos.epi.example.endpoints;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;

@Path("/")
public class MyRestEndpoint {

	@Inject
	CamelContext camel;
	
	@GET
	@Path("/example/{data}")
	public Response myService(@PathParam("data") String data) {
		ProducerTemplate producer = camel.createProducerTemplate();
		String result = (String) producer.requestBodyAndHeader("direct:jaxrs", "", "data", data);
		return Response.ok(result).build();
	}
}
