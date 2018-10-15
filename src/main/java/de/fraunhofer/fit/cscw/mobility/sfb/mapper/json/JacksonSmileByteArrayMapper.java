package de.fraunhofer.fit.cscw.mobility.sfb.mapper.json;

import com.example.myschema.ArrayOfBeer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import com.fasterxml.jackson.dataformat.smile.SmileGenerator;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.ByteArrayMapper;

/**
 * Smile: Binary JSON
 *
 * https://github.com/FasterXML/jackson-dataformats-binary/tree/master/smile
 *
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 15.10.2018
 */
public class JacksonSmileByteArrayMapper implements ByteArrayMapper<ArrayOfBeer> {

    private final ObjectMapper mapper;

    public JacksonSmileByteArrayMapper() {
        SmileFactory f = new SmileFactory();
        f.configure(SmileGenerator.Feature.CHECK_SHARED_NAMES, true);
        f.configure(SmileGenerator.Feature.CHECK_SHARED_STRING_VALUES, false);

        mapper = new ObjectMapper(f);
        mapper.setAnnotationIntrospector(new JaxbAnnotationIntrospector(mapper.getTypeFactory()));
        mapper.registerModule(new AfterburnerModule()); // to add dynamic bytecode generation and avoid reflection
    }

    @Override
    public ArrayOfBeer read(byte[] data) throws Exception {
        return mapper.readValue(data, ArrayOfBeer.class);
    }

    @Override
    public byte[] write(ArrayOfBeer obj) throws Exception {
        return mapper.writeValueAsBytes(obj);
    }
}
