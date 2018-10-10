package com.goekay.xml;

import com.example.myschema.ArrayOfBeer;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.goekay.StringMapper;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 09.10.2018
 */
public class JacksonXmlMapper implements StringMapper<ArrayOfBeer> {

    private final XmlMapper mapper = new XmlMapper();

    public JacksonXmlMapper() {
        mapper.setDefaultUseWrapper(false);
        mapper.registerModule(new JaxbAnnotationModule());
    }

    @Override
    public ArrayOfBeer read(String str) throws Exception {
        return mapper.readValue(str, ArrayOfBeer.class);
    }

    @Override
    public String write(ArrayOfBeer obj) throws Exception {
        return mapper.writeValueAsString(obj);
    }
}
