package de.fraunhofer.fit.cscw.mobility.sfb.mapper.fastinfoset;

import com.example.myschema.ArrayOfBeer;
import com.sun.xml.fastinfoset.stax.StAXDocumentParser;
import com.sun.xml.fastinfoset.stax.StAXDocumentSerializer;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.ByteArrayMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.xml.AbstractJaxbXmlMapper;

import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author Fabian Ohler <fabian.ohler1@rwth-aachen.de>
 */
public class FastInfosetByteArrayMapper extends AbstractJaxbXmlMapper implements ByteArrayMapper<ArrayOfBeer> {

    public FastInfosetByteArrayMapper(boolean validate) {
        super(validate);
    }

    @Override
    public ArrayOfBeer read(final byte[] data) throws Exception {
        return read(new StAXDocumentParser(new BufferedInputStream(new ByteArrayInputStream(data))));
    }

    public ArrayOfBeer read(final XMLStreamReader reader) throws Exception {
        final Unmarshaller um = jaxbContext.createUnmarshaller();

        if (validate) {
            um.setSchema(schema);
        }

        return um.unmarshal(reader, ArrayOfBeer.class).getValue();
    }


    public void write(ArrayOfBeer obj, XMLStreamWriter out) throws Exception {
        final Marshaller m = jaxbContext.createMarshaller();

        if (validate) {
            m.setSchema(schema);
        }

        // Pretty print?
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);

        m.marshal(obj, out);
    }

    @Override
    public byte[] write(final ArrayOfBeer obj) throws Exception {
        final ByteArrayOutputStream result = new ByteArrayOutputStream();
        write(obj, new StAXDocumentSerializer(new BufferedOutputStream(result)));
        return result.toByteArray();
    }
}
