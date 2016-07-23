package org.atos.epi.examples.routes;

import org.apache.camel.ProducerTemplate;
import org.apache.camel.cdi.Uri;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.cdi.Beans;
import org.apache.camel.test.cdi.CamelCdiRunner;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.atos.epi.example.routes.ExampleRoute;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(CamelCdiRunner.class)
@Beans(classes = ExampleRoute.class)
public class ExampleRouteTest extends CamelTestSupport {
	
 	/*
	 * Test the custom preprocess Validator component
	 */
	
	@Test
	public void testValidPreprocessValidatorMessage(
			@Uri("direct:preprocess") ProducerTemplate producer,
			@Uri("mock:preprocessvalidator") MockEndpoint mock
		) throws Exception {
		
		// make sure the endpoint has no messages
		mock.reset();
		// setup test results for mock endpoint
		mock.expectedMessageCount(1);
		mock.expectedHeaderReceived("data", "message valid : abc");
		mock.expectedHeaderReceived("preValidMessage", true);
		// send an 'invalid' message with the data header set to 'AbC'
		producer.requestBodyAndHeader("", "data", "abc");
		
		MockEndpoint.assertIsSatisfied(mock);
		
	}
	
	@Test
	public void testInvalidPreprocessValidatorMessage(
			@Uri("direct:preprocess") ProducerTemplate producer,
			@Uri("mock:preprocessvalidator") MockEndpoint mock
		) throws Exception {
		
		// make sure the endpoint has no messages
		mock.reset();
		// setup test results for mock endpoint
		mock.expectedMessageCount(1);
		mock.expectedHeaderReceived("data", "message invalid : AbC");
		mock.expectedHeaderReceived("preValidMessage", false);		
		
		// send an 'invalid' message with the data header set to 'AbC'
		boolean threwException = false;
        try {
        	producer.requestBodyAndHeader("", "data", "AbC");
        } catch (Throwable e) {
            threwException = true;
        }

        assertTrue(threwException);
        MockEndpoint.assertIsSatisfied(mock);
	}

	@Test
	public void testRepairedPreprocessValidatorMessage(
			@Uri("direct:preprocess") ProducerTemplate producer,
			@Uri("mock:preprocessvalidator") MockEndpoint mock
		) throws Exception {
		
		// make sure the endpoint has no messages
		mock.reset();
		// setup test results for mock endpoint
		mock.expectedMessageCount(1);
		mock.expectedHeaderReceived("data", "message repaired : abc");
		// send an 'invalid' message with the data header set to 'aBc'
		producer.requestBodyAndHeader("", "data", "aBc");
		
		MockEndpoint.assertIsSatisfied(mock);
		
	}
	
 	/*
	 * Test the full preprocessor flow
	 */
	
	@Test
	public void testValidPreprocessMessage(
			@Uri("direct:preprocess") ProducerTemplate producer,
			@Uri("mock:preprocess") MockEndpoint mock
		) throws Exception {
		
		// make sure the endpoint has no messages
		mock.reset();
		// setup test results for mock endpoint
		mock.expectedMessageCount(1);
		mock.expectedHeaderReceived("data", "message valid : abc");
		mock.expectedHeaderReceived("preValidMessage", true);
		// send an 'invalid' message with the data header set to 'AbC'
		producer.requestBodyAndHeader("", "data", "abc");
		
		MockEndpoint.assertIsSatisfied(mock);

		
	}
	
	@Test
	public void testInvalidPreprocessMessage(
			@Uri("direct:preprocess") ProducerTemplate producer,
			@Uri("mock:preprocess") MockEndpoint mock
		) throws Exception {

		// make sure the endpoint has no messages
		mock.reset();
		// setup test results for mock endpoint
		mock.expectedMessageCount(0);
		
		// send an 'invalid' message with the data header set to 'AbC'
		boolean threwException = false;
        try {
        	producer.requestBodyAndHeader("", "data", "AbC");
        } catch (Throwable e) {
            threwException = true;
        }

        assertTrue(threwException);
        MockEndpoint.assertIsSatisfied(mock);
	}
	
	@Test
	public void testRepairedPreprocessMessage(
			@Uri("direct:preprocess") ProducerTemplate producer,
			@Uri("mock:preprocess") MockEndpoint mock
		) throws Exception {
		
		// make sure the endpoint has no messages
		mock.reset();
		// setup test results for mock endpoint
		mock.expectedMessageCount(1);
		mock.expectedHeaderReceived("data", "message repaired : abc");
		// send an 'invalid' message with the data header set to 'aBc'
		producer.requestBodyAndHeader("", "data", "aBc");
		
		MockEndpoint.assertIsSatisfied(mock);
		
	}
}
