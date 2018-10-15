package de.fraunhofer.fit.cscw.mobility.sfb.mapper.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 10.10.2018
 */
abstract class AbstractJacksonJsonMapper {

    static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.setAnnotationIntrospector(new JaxbAnnotationIntrospector(mapper.getTypeFactory()));
        mapper.registerModule(new AfterburnerModule()); // to add dynamic bytecode generation and avoid reflection
    }
}
