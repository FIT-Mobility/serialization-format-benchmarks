package com.goekay.xml;

import com.example.myschema.ArrayOfBeerType;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.goekay.Mapper;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 09.10.2018
 */
public class JacksonXmlMapper implements Mapper<ArrayOfBeerType> {

    private final XmlMapper mapper = new XmlMapper();

    public JacksonXmlMapper() {
        mapper.setDefaultUseWrapper(false);
        mapper.registerModule(new JaxbAnnotationModule());
    }

    @Override
    public ArrayOfBeerType read(String str) throws Exception {
        return mapper.readValue(str, ArrayOfBeerType.class);
    }

    @Override
    public String write(ArrayOfBeerType obj) throws Exception {
        return mapper.writeValueAsString(obj);
    }
}
