package com.goekay.json;

import com.example.myschema.ArrayOfBeer;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.goekay.StringMapper;

import java.io.StringWriter;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 09.10.2018
 */
public class JacksonJsonMapper implements StringMapper<ArrayOfBeer> {

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
    public ArrayOfBeer read(String str) throws Exception {
        return mapper.readValue(str, ArrayOfBeer.class);
    }

    @Override
    public String write(ArrayOfBeer obj) throws Exception {
        StringWriter stringWriter = new StringWriter();
        mapper.writeValue(stringWriter, obj);
        return stringWriter.toString();
    }
}
