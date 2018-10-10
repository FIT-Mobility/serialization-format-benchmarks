package com.goekay.xml;

import com.example.myschema.ArrayOfBeer;
import com.goekay.ByteArrayMapper;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 10.10.2018
 */
public class JacksonXmlByteArrayMapper implements ByteArrayMapper<ArrayOfBeer> {

    private final JacksonXmlStringMapper delegate;

    public JacksonXmlByteArrayMapper(JacksonXmlStringMapper delegate) {
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
