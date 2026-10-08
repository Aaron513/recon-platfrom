package com.reconplatform.ingestion_service.rawbatchfile;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class BatchHeaderProcessor implements Processor {
    @Override
    public void process(Exchange exchange) {
        Document document = exchange.getIn().getBody(Document.class);
        Element root = document.getDocumentElement();
        exchange.setProperty("batchId", root.getAttribute("batchId"));
        exchange.setProperty("sourceName", root.getAttribute("sourceName"));
        exchange.setProperty("transactionCount", root.getElementsByTagName("transaction").getLength());
        exchange.setProperty("validationErrors", new ConcurrentLinkedQueue<String>());
    }
}