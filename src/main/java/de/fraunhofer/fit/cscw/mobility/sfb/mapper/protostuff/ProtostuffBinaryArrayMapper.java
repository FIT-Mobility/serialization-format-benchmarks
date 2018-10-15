package de.fraunhofer.fit.cscw.mobility.sfb.mapper.protostuff;

import com.example.myschema.ArrayOfBeer;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.ByteArrayMapper;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 15.10.2018
 */
@NotThreadSafe
public class ProtostuffBinaryArrayMapper implements ByteArrayMapper<ArrayOfBeer> {

    // Re-use (manage) this buffer to avoid allocating on every serialization
    private final LinkedBuffer buffer = LinkedBuffer.allocate(1024);

    @Override
    public ArrayOfBeer read(byte[] data) throws Exception {
        Schema<ArrayOfBeer> schema = RuntimeSchema.getSchema(ArrayOfBeer.class);
        ArrayOfBeer arrayOfBeer = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(data, arrayOfBeer, schema);
        return arrayOfBeer;
    }

    @Override
    public byte[] write(ArrayOfBeer obj) throws Exception {
        Schema<ArrayOfBeer> schema = RuntimeSchema.getSchema(ArrayOfBeer.class);
        try {
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } finally {
            buffer.clear();
        }
    }
}
