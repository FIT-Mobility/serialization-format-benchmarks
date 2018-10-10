package com.goekay.json;

import com.example.myschema.ArrayOfBeer;
import com.goekay.ByteArrayMapper;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 10.10.2018
 */
public class JacksonJsonByteArrayMapper extends AbstractJacksonJsonMapper implements ByteArrayMapper<ArrayOfBeer> {

    @Override
    public ArrayOfBeer read(byte[] data) throws Exception {
        return mapper.readValue(data, ArrayOfBeer.class);
    }

    @Override
    public byte[] write(ArrayOfBeer obj) throws Exception {
        return mapper.writeValueAsBytes(obj);
    }
}
