package com.goekay.xml;

import com.example.myschema.ArrayOfBeer;
import com.goekay.ByteArrayMapper;

import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 10.10.2018
 */
public class JaxbXmlByteArrayMapper implements ByteArrayMapper<ArrayOfBeer> {

    private final JaxbXmlStringMapper delegate;

    public JaxbXmlByteArrayMapper(JaxbXmlStringMapper delegate) {
        this.delegate = delegate;
    }

    @Override
    public ArrayOfBeer read(byte[] data) throws Exception {
        Unmarshaller um = delegate.jaxbContext.createUnmarshaller();
        if (delegate.validate) {
            um.setSchema(delegate.schema);
        }

        try (ByteArrayInputStream in = new ByteArrayInputStream(data)) {
            return um.unmarshal(new StreamSource(in), ArrayOfBeer.class).getValue();
        }
    }

    @Override
    public byte[] write(ArrayOfBeer obj) throws Exception {
        Marshaller m = delegate.jaxbContext.createMarshaller();

        if (delegate.validate) {
            // Validate against the schema
            m.setSchema(delegate.schema);
        }

        // Pretty print?
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
        // Drop the XML declaration?
        m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            m.marshal(obj, out);
            return out.toByteArray();
        }
    }
}
