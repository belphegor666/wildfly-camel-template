package org.atos.epi.example.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

public class RestDslRoute extends RouteBuilder {

    @Override
    public void configure() {
        restConfiguration().component("servlet");
    	/*
    	restConfiguration().component("jetty")
	        .bindingMode(RestBindingMode.json)
	        .dataFormatProperty("prettyPrint", "true")
	        .host("0.0.0.0")
	        .port(9000);
    	*/
        rest("/say/")
            .produces("text/plain")
            .get("hello")
                .route()
                .transform().constant("Hello World!")
                .endRest()
            .get("hello/{name}")
                .route()
                //.bean("hello")
                .log("${body}");
    }
}
