package com.reconplatform.ingestion_service.rawbatchfile;

import com.reconplatform.common.dto.TransactionDTO;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class TransactionMappingProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Element element = (Element) exchange.getIn().getBody(Node.class);

        TransactionDTO dto = new TransactionDTO();
        dto.setTransactionId(text(element, "transactionId"));
        dto.setBatchId(exchange.getProperty("batchId", String.class));
        dto.setAccountNumber(text(element, "accountNumber"));
        dto.setAmount(new BigDecimal(text(element, "amount")));
        dto.setCurrency(text(element, "currency"));
        dto.setTransactionDate(LocalDateTime.parse(text(element, "transactionDate")).toInstant(ZoneOffset.UTC));
        dto.setOriginIp(text(element, "originIp"));

        exchange.getIn().setBody(dto);
    }

    private String text(Element parent, String tag) {
        NodeList list = parent.getElementsByTagName(tag);
        return list.getLength() > 0 ? list.item(0).getTextContent() : null;
    }
}