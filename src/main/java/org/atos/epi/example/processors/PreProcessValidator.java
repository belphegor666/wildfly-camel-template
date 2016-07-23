package org.atos.epi.example.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ValidationException;

public class PreProcessValidator implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		final String HEADER_DATA = "data";
		final String data = exchange.getIn().getHeader(HEADER_DATA).toString();

		// We look for CAPITAL letters in the data
		if(countUppercase(data) == 1) {
			// There's only one capital, so we'll correct the data
			exchange.getIn().setHeader(HEADER_DATA, "message repaired : "+data.toLowerCase());
			exchange.getIn().setHeader("preValidMessage", true);
		} else if (countUppercase(data) > 1) {
			// There's more than one capital, so we'll declare the data invalid
			exchange.getIn().setHeader(HEADER_DATA, "message invalid : "+data);
			exchange.getIn().setHeader("preValidMessage", false);
			throw new ValidationException(exchange, "invalid message");
		} else {
			exchange.getIn().setHeader(HEADER_DATA, "message valid : "+data);
			exchange.getIn().setHeader("preValidMessage", true);
		}
	}

	
	private int countUppercase(String s) {
		int result = 0;
		for (char c : s.toCharArray()) {
			if (Character.isUpperCase(c)) result++;
		}
		return result;
	}

}
