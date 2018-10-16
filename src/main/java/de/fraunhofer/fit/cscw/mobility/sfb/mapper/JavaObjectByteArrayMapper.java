package de.fraunhofer.fit.cscw.mobility.sfb.mapper;

import com.example.myschema.ArrayOfBeer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 16.10.2018
 */
public class JavaObjectByteArrayMapper implements ByteArrayMapper<ArrayOfBeer> {

    @Override
    public ArrayOfBeer read(byte[] data) throws Exception {
        try (ByteArrayInputStream fis = new ByteArrayInputStream(data);
             ObjectInputStream in = new ObjectInputStream(fis)) {
            return (ArrayOfBeer) in.readObject();
        }
    }

    @Override
    public byte[] write(ArrayOfBeer obj) throws Exception {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
            return bos.toByteArray();
        }
    }
}
