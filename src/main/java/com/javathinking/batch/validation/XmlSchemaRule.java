package com.javathinking.batch.validation;

import com.javathinking.commons.validation.Errors;
import com.javathinking.commons.validation.ValidationError;
import com.javathinking.commons.validation.ValidationRule;
import com.javathinking.commons.validation.ValidationRuntimeException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Checks that a given XML file validates against the defined XSL schema.
 *
 * @see javax.xml.validation.Validator#validate(javax.xml.transform.Source)
 */
public class XmlSchemaRule implements ValidationRule<File> {
    private URI schema;

    public XmlSchemaRule(File schema) {
        this(schema.toURI());
    }

    public XmlSchemaRule(URI resource) {
        this.schema = resource;
    }

    public XmlSchemaRule(URL resource) {
        try {
            this.schema = resource.toURI();
        } catch (URISyntaxException ex) {
            throw new RuntimeException("Schema not available : " + resource.toString(), ex);
        }
    }


    public Errors validate(File input) {
        Errors errors = new Errors();
        // validate the DOM tree -- from http://java.sun.com/j2se/1.5.0/docs/api/javax/xml/validation/package-summary.html
        try {
            // parse an XML document into a DOM tree
            DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = parser.parse(input);
            // create a SchemaFactory capable of understanding WXS schemas
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            // load a WXS schema, represented by a Schema instance
            Source schemaFile = new StreamSource(schema.toURL().openStream());
            Schema schema = factory.newSchema(schemaFile);
            // create a Validator instance, which can be used to validate an instance document
            javax.xml.validation.Validator validator = schema.newValidator();
            // validate the DOM tree
            try {
                validator.validate(new DOMSource(document));
            } catch (SAXException e) {
                errors.add(new ValidationError(this.getClass().getSimpleName(), "XML Schema validation failed - " + e.getMessage()));
            }
        } catch (Exception ex) {
            throw new ValidationRuntimeException(ex);
        }
        return errors;

    }
}
