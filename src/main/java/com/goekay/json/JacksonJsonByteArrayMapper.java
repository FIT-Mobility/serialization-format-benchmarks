package com.goekay.json;

import com.example.myschema.ArrayOfBeer;
import com.goekay.ByteArrayMapper;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 10.10.2018
 */
public class JacksonJsonByteArrayMapper implements ByteArrayMapper<ArrayOfBeer> {

    private final JacksonJsonStringMapper delegate;

    public JacksonJsonByteArrayMapper(JacksonJsonStringMapper delegate) {
        this.delegate = delegate;
    }

    @Override
    public ArrayOfBeer read(byte[] data) throws Exception {
        return delegate.mapper.readValue(data, ArrayOfBeer.class);
    }

    @Override
    public byte[] write(ArrayOfBeer obj) throws Exception {
        return delegate.mapper.writeValueAsBytes(obj);
    }
}
