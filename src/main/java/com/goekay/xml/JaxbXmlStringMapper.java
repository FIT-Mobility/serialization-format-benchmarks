package com.goekay.xml;

import com.example.myschema.ArrayOfBeer;
import com.goekay.StringMapper;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 09.10.2018
 */
public class JaxbXmlStringMapper implements StringMapper<ArrayOfBeer> {

    final JAXBContext jaxbContext;
    final Schema schema;
    final boolean validate;

    public JaxbXmlStringMapper(boolean validate) {
        this.validate = validate;

        try {
            // is thread-safe
            jaxbContext = JAXBContext.newInstance(ArrayOfBeer.class);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        URL xsdURL = getClass().getClassLoader().getResource("beers.xsd");

        if (xsdURL == null) {
            throw new RuntimeException("XML schema could not be found/loaded");
        } else {
            try {
                schema = schemaFactory.newSchema(xsdURL);
            } catch (SAXException e) {
                throw new RuntimeException("Error occurred", e);
            }
        }
    }

    @Override
    public ArrayOfBeer read(String str) throws Exception {
        Unmarshaller um = jaxbContext.createUnmarshaller();
        if (validate) {
            um.setSchema(schema);
        }
        return um.unmarshal(new StreamSource(new StringReader(str)), ArrayOfBeer.class).getValue();
    }

    @Override
    public String write(ArrayOfBeer obj) throws Exception {
        Marshaller m = jaxbContext.createMarshaller();

        if (validate) {
            // Validate against the schema
            m.setSchema(schema);
        }

        // Pretty print?
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
        // Drop the XML declaration?
        m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

        StringWriter stringWriter = new StringWriter();
        m.marshal(obj, stringWriter);
        return stringWriter.toString();
    }
}
