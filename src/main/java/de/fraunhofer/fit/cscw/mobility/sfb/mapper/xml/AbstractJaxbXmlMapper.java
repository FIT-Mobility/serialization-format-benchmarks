package de.fraunhofer.fit.cscw.mobility.sfb.mapper.xml;

import com.example.myschema.ArrayOfBeer;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.net.URL;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 10.10.2018
 */
public abstract class AbstractJaxbXmlMapper {

    protected static final JAXBContext jaxbContext;
    protected static final Schema schema;

    protected final boolean validate;

    static {
        try {
            // is thread-safe
            jaxbContext = JAXBContext.newInstance(ArrayOfBeer.class);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        URL xsdURL = AbstractJaxbXmlMapper.class.getClassLoader().getResource("beers.xsd");

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

    protected AbstractJaxbXmlMapper(boolean validate) {
        this.validate = validate;
    }

    ArrayOfBeer read(StreamSource source) throws Exception {
        Unmarshaller um = jaxbContext.createUnmarshaller();

        if (validate) {
            um.setSchema(schema);
        }

        return um.unmarshal(source, ArrayOfBeer.class).getValue();
    }

    void write(ArrayOfBeer obj, StreamResult out) throws Exception {
        Marshaller m = jaxbContext.createMarshaller();

        if (validate) {
            m.setSchema(schema);
        }

        // Pretty print?
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
        // Drop the XML declaration?
        m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

        m.marshal(obj, out);
    }

}
