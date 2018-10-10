package de.fraunhofer.fit.cscw.mobility.sfb.mapper.exi;

import com.example.myschema.ArrayOfBeer;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.ByteArrayMapper;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author Fabian Ohler <fabian.ohler1@rwth-aachen.de>
 */
public class EXIficientByteArrayMapper extends AbstractExiMapper implements ByteArrayMapper<ArrayOfBeer> {
    @Override
    public ArrayOfBeer read(final byte[] data) throws Exception {
        return super.read(new InputSource(new ByteArrayInputStream(data)));
    }

    @Override
    public byte[] write(final ArrayOfBeer obj) throws Exception {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        super.write(obj, baos);
        return baos.toByteArray();
    }
}
