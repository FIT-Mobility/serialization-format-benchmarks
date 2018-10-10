package de.fraunhofer.fit.cscw.mobility.sfb.protobuf;

import com.example.myproto.Protobuf.ArrayOfBeerType;
import de.fraunhofer.fit.cscw.mobility.sfb.ByteArrayMapper;

/**
 * @author Fabian Ohler <fabian.ohler1@rwth-aachen.de>
 */
public enum ProtobufByteArrayMapper implements ByteArrayMapper<ArrayOfBeerType> {
    INSTANCE;

    @Override
    public ArrayOfBeerType read(final byte[] data) throws Exception {
        return ArrayOfBeerType.parseFrom(data);
    }

    @Override
    public byte[] write(final ArrayOfBeerType obj) throws Exception {
        return obj.toByteArray();
    }
}
