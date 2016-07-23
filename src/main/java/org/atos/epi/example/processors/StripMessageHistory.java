package org.atos.epi.example.processors;

import java.util.Collections;

import org.apache.camel.Exchange;
import org.apache.camel.MessageHistory;
import org.apache.camel.Processor;

public class StripMessageHistory implements Processor {
 
	@Override
	public void process(Exchange exchange)throws Exception {
	  exchange.setProperty(Exchange.MESSAGE_HISTORY, Collections.<MessageHistory> emptyList());
	}

}
