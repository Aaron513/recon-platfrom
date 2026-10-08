package com.reconplatform.ingestion_service.service;

import com.reconplatform.common.error.AppException;
import com.reconplatform.common.error.ErrorCode;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlSchemaValidator {

    private final Schema schema;

    public XmlSchemaValidator(String xsdClassPath) throws SAXException {
        SchemaFactory factory =
                SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        InputStream xsdStream =
                getClass().getClassLoader().getResourceAsStream(xsdClassPath);

        if (xsdStream == null) {
            throw new IllegalArgumentException(
                    "XSD not found on classpath: " + xsdClassPath
            );
        }

        this.schema = factory.newSchema(new StreamSource(xsdStream));
    }

    public void validate(InputStream xmlInput) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setSchema(schema);
            XMLReader reader = factory.newSAXParser().getXMLReader();

            List<SAXParseException> errors = new ArrayList<>();
            reader.setErrorHandler(new ErrorHandler() {
                public void warning(SAXParseException e) {}

                public void error(SAXParseException e) {
                    errors.add(e);
                }

                public void fatalError(SAXParseException e) {
                    errors.add(e);
                }
            });
            reader.parse(new InputSource(xmlInput));

            if (!errors.isEmpty()) {
                throw new AppException(ErrorCode.INGESTION_XML_INVALID);
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new AppException(ErrorCode.INGESTION_XML_INVALID, e);
        }
    }
}