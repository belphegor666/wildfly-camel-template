package org.atos.epi.example.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.processor.DefaultExchangeFormatter;
import org.apache.camel.util.MessageHelper;

public class LogMessageHistory implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		DefaultExchangeFormatter exchangeFormatter = new DefaultExchangeFormatter();
		exchangeFormatter.setShowAll(true);
		exchangeFormatter.setShowHeaders(true);
		exchangeFormatter.setShowBody(true);
		exchangeFormatter.setShowFuture(true);
		String msgHistTrace = MessageHelper.dumpMessageHistoryStacktrace(exchange, exchangeFormatter, false);
		System.out.println(msgHistTrace);
	}

}
