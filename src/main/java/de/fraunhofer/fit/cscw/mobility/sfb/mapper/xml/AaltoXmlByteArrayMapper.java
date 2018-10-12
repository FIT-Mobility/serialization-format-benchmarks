package de.fraunhofer.fit.cscw.mobility.sfb.mapper.xml;

import com.example.myschema.ArrayOfBeer;
import com.fasterxml.aalto.stax.InputFactoryImpl;
import com.fasterxml.aalto.stax.OutputFactoryImpl;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.ByteArrayMapper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 12.10.2018
 */
public class AaltoXmlByteArrayMapper implements ByteArrayMapper<ArrayOfBeer> {

    private final XMLInputFactory inputFactory = new InputFactoryImpl();
    private final XMLOutputFactory outputFactory = new OutputFactoryImpl();
    private final JAXBContext jaxbContext;

    public AaltoXmlByteArrayMapper() {
        try {
            // is thread-safe
            jaxbContext = JAXBContext.newInstance(ArrayOfBeer.class);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayOfBeer read(byte[] data) throws Exception {
        XMLStreamReader sr = inputFactory.createXMLStreamReader(new ByteArrayInputStream(data));
        try {
            return jaxbContext.createUnmarshaller().unmarshal(sr, ArrayOfBeer.class).getValue();
        } finally {
            sr.close();
        }
    }

    @Override
    public byte[] write(ArrayOfBeer obj) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XMLStreamWriter sw = outputFactory.createXMLStreamWriter(bos, StandardCharsets.UTF_8.name());
        try {
            jaxbContext.createMarshaller().marshal(obj, sw);
        } finally {
            sw.close();
        }
        return bos.toByteArray();
    }
}