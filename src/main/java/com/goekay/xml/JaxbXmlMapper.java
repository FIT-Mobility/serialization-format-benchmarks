package com.goekay.xml;

import com.example.myschema.ArrayOfBeerType;
import com.example.myschema.ObjectFactory;
import com.goekay.Mapper;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
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
public class JaxbXmlMapper implements Mapper<ArrayOfBeerType> {

    private final JAXBContext jaxbContext;
    private final ObjectFactory objectFactory;
    private final Schema schema;
    private final boolean validate;

    public JaxbXmlMapper(boolean validate) {
        this.validate = validate;

        try {
            // is thread-safe
            jaxbContext = JAXBContext.newInstance(ArrayOfBeerType.class);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        objectFactory = new ObjectFactory();

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
    public ArrayOfBeerType read(String str) throws Exception {
        Unmarshaller um = jaxbContext.createUnmarshaller();
        if (validate) {
            um.setSchema(schema);
        }
        return um.unmarshal(new StreamSource(new StringReader(str)), ArrayOfBeerType.class).getValue();
    }

    @Override
    public String write(ArrayOfBeerType obj) throws Exception {
        JAXBElement<ArrayOfBeerType> outgoing = objectFactory.createArrayOfBeer(obj);
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
        m.marshal(outgoing, stringWriter);
        return stringWriter.toString();
    }
}
