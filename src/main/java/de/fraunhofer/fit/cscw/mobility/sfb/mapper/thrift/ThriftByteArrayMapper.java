package de.fraunhofer.fit.cscw.mobility.sfb.mapper.thrift;

import com.example.thrift.ArrayOfBeerType;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.ByteArrayMapper;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TIOStreamTransport;

import java.io.ByteArrayOutputStream;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 11.10.2018
 */
public class ThriftByteArrayMapper implements ByteArrayMapper<ArrayOfBeerType> {

    @Override
    public ArrayOfBeerType read(byte[] data) throws Exception {
        TDeserializer des = new TDeserializer();
        ArrayOfBeerType obj = new ArrayOfBeerType();
        des.deserialize(obj, data);
        return obj;
    }

    @Override
    public byte[] write(ArrayOfBeerType obj) throws Exception {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             TIOStreamTransport buffer = new TIOStreamTransport(bos)) {
            TBinaryProtocol protocol = new TBinaryProtocol(buffer);
            obj.write(protocol);
            return bos.toByteArray();
        }
    }
}