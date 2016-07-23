package org.atos.epi.example.routes;

import javax.inject.Inject;

import org.apache.camel.BeanInject;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.ValidationException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.Uri;
import org.apache.camel.component.rest.RestEndpoint;
import org.atos.epi.example.exceptions.InvalidMessageException;
import org.atos.epi.example.processors.LogMessageHistory;
import org.atos.epi.example.processors.PreProcessValidator;
import org.atos.epi.example.processors.StripMessageHistory;

public class ExampleRoute extends RouteBuilder {
	
	/*
	 * Examples of using CDI to inject end points
	 */
	
	@Inject
    @Uri("servlet:example?httpMethodRestrict=GET")
    Endpoint servletIn;
	
	@Inject
    @Uri("direct:jaxrs")
    Endpoint jaxrsIn;	
    
    @Inject
    @Uri("direct:message-store")
    Endpoint messageStore;

    @Inject
    @Uri("direct:preprocess")
    Endpoint preProcess;
    
    @Inject
    @Uri("direct:postprocess")
    Endpoint postProcess;
    
    @Inject
    @Uri("direct:subflow")
    Endpoint subFlow;

	/*
	 *  Example Route Template
	 */
	@Override
	public void configure() throws Exception {
		// configure the rest implementation
		restConfiguration().component("servlet");
		
		/*
		 * Main Route Template
		 */
		from(servletIn)
			.to(preProcess)		/* send to pre-processor */
			.to(subFlow)		/* send to subflow */
			.to(postProcess); 	/* send to post-processor */
		
		/*
		 * Auxiliary Route Template
		 */
		from(jaxrsIn)
			.to(preProcess)		/* send to pre-processor */
			.to(subFlow)		/* send to subflow */
			.to(postProcess); 	/* send to post-processor */		

		
		/*
		 * Preprocessor
		 */
		from(preProcess)
			.wireTap(messageStore) /* send copy of message to store */
			.doTry()
				.process(new PreProcessValidator()) /* custom processor example */
			.doCatch(ValidationException.class)
				.log(LoggingLevel.INFO, "**** ${header.data} *****")
			.doFinally()
				.to("mock:preprocessvalidator")
			.end()
			.to("log:?level=INFO&showBody=true") /* debug output */
			.filter(header("preValidMessage").isNotEqualTo(true))
				.throwException(new InvalidMessageException("invalid input message")) /* trivial filter example */
			.end()
			.to("mock:preprocess");
		
		/* 
		 * Postprocessor
		 */
		from(postProcess)
			.doTry()  /* example of an anonymous processor - BAD PRACTICE! NO REUSE! */
				.process(new Processor() {
					@Override
					public void process(Exchange exchange) throws Exception {
						if(exchange.getIn().getBody() == null) {
							exchange.getIn().setHeader("postValidMessage", false);
							throw new ValidationException(exchange, "invalid output message");
						} else {
							exchange.getIn().setHeader("postValidMessage", true);
						}
						
					}
				})
			.doCatch(ValidationException.class)
				.transform().body().setBody(simple("invalid message")) /* alternate syntax for transforming messages */
			.doFinally()
				.to("mock:postprocess")
			.end()
			.filter(header("postValidMessage").isNotEqualTo(true)) /* trivial validation example */
				.throwException(new InvalidMessageException("invalid outbound message")) /* trivial filter example */
			.end()
			.wireTap(messageStore) /* send copy of message to store */
			.process(new StripMessageHistory());	
	
	
		/*
		 * Example of a subflow
		 */
		from(subFlow)
			.log("entry to subflow")
			.transform()
				.simple("${header.data}")
			.to("log:?level=INFO&showBody=true")
			.log("exit from subflow");
		
		/*
		 * Trivial Message Store
		 */
		from(messageStore)
			.process(new LogMessageHistory());

	}

}
