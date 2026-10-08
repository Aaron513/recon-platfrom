package com.reconplatform.ingestion_service.route;

import com.reconplatform.common.error.AppException;
import com.reconplatform.common.error.ErrorCode;
import com.reconplatform.common.messaging.KafkaTopics;
import com.reconplatform.ingestion_service.model.RawBatchEntity;
import com.reconplatform.ingestion_service.rawbatchfile.BatchHeaderProcessor;
import com.reconplatform.ingestion_service.rawbatchfile.TransactionMappingProcessor;
import com.reconplatform.ingestion_service.rawbatchfile.TransactionValidationProcessor;
import com.reconplatform.ingestion_service.repository.RawBatchRepository;
import com.reconplatform.ingestion_service.service.BatchValidationService;
import com.reconplatform.ingestion_service.service.XmlSchemaValidator;
import org.apache.camel.processor.aggregate.GroupedBodyAggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import java.io.InputStream;
import java.util.Queue;

@Component
public class TransactionBatchRoute extends RouteBuilder {
    private static final Logger log = LoggerFactory.getLogger(TransactionBatchRoute.class);

    private final XmlSchemaValidator xmlValidator;
    private final BatchValidationService batchValidator;
    private final BatchHeaderProcessor headerProcessor;
    private final TransactionMappingProcessor mappingProcessor;
    private final TransactionValidationProcessor validationProcessor;
    private final RawBatchRepository rawBatchRepository;

    public TransactionBatchRoute(BatchValidationService batchValidator,
                                 BatchHeaderProcessor headerProcessor,
                                 TransactionMappingProcessor mappingProcessor,
                                 TransactionValidationProcessor validationProcessor,
                                 RawBatchRepository rawBatchRepository) throws SAXException {
        this.xmlValidator = new XmlSchemaValidator("xsd/transaction_batch.xsd");
        this.batchValidator = batchValidator;
        this.headerProcessor = headerProcessor;
        this.mappingProcessor = mappingProcessor;
        this.validationProcessor = validationProcessor;
        this.rawBatchRepository = rawBatchRepository;
    }

    @Override
    public void configure() throws Exception {

        onException(Exception.class)
                .process(exchange -> {
                    Throwable cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
                    String code = (cause instanceof AppException ae) ? ae.getErrorCode().getCode() : "ING-000";
                    log.error("File {} failed [{}]: {}",
                            exchange.getIn().getHeader(Exchange.FILE_NAME, String.class), code,
                            cause != null ? cause.getMessage() : "unknown");
                });

        from("file:/D:\\data\\input"
                + "?include=.*[.]xml&exclude=.*_error[.]xml"
                + "&move=processed/${date:now:yyyyMMddHHmmss}_${file:name}"
                + "&moveFailed=${file:name.noext}_error.${file:ext}")
                .routeId("transactionBatchIngestionRoute")
                .log("Picked up file ${header.CamelFileName}")
                .process(exchange -> xmlValidator.validate(exchange.getIn().getBody(InputStream.class)))
                .process(headerProcessor)                       // sets batchId etc. as properties, NO db write
                .split(xpath("//transaction"), new GroupedBodyAggregationStrategy())
                .parallelProcessing()
                .process(validationProcessor)               // map + validate, records errors instead of throwing
                .end()
                .process(exchange -> {
                    Queue<String> errors = exchange.getProperty("validationErrors", Queue.class);
                    if (!errors.isEmpty()) {
                        errors.forEach(e -> log.error("Validation failed: {}", e));
                        throw new AppException(ErrorCode.INGESTION_VALIDATION_FAILED);
                    }
                })
                .split(body()).parallelProcessing()             // pass 2: only reached if pass 1 was clean
                .to("kafka:" + KafkaTopics.TRANSACTION_INGESTED
                        + "?brokers=localhost:9092&valueSerializer=com.reconplatform.ingestion_service.serialization.TransactionDtoJsonSerializer")
                .end()
                .process(exchange -> rawBatchRepository.save(new RawBatchEntity(
                        exchange.getProperty("batchId", String.class),
                        exchange.getProperty("sourceName", String.class),
                        exchange.getIn().getHeader(Exchange.FILE_NAME, String.class),
                        exchange.getProperty("transactionCount", Integer.class),
                        "PUBLISHED")))
                .log("Batch ${exchangeProperty.batchId} published successfully");
    }
}