package com.goekay.json;

import com.example.myschema.ArrayOfBeerType;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.goekay.Mapper;

import java.io.StringWriter;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 09.10.2018
 */
public class JacksonJsonMapper implements Mapper<ArrayOfBeerType> {

    private final ObjectMapper mapper;

    public JacksonJsonMapper() {
        mapper = new ObjectMapper();

        mapper.setAnnotationIntrospector(
                AnnotationIntrospector.pair(
                        new JacksonAnnotationIntrospector(),
                        new JaxbAnnotationIntrospector(mapper.getTypeFactory())
                )
        );
    }

    @Override
    public ArrayOfBeerType read(String str) throws Exception {
        return mapper.readValue(str, ArrayOfBeerType.class);
    }

    @Override
    public String write(ArrayOfBeerType obj) throws Exception {
        StringWriter stringWriter = new StringWriter();
        mapper.writeValue(stringWriter, obj);
        return stringWriter.toString();
    }
}
