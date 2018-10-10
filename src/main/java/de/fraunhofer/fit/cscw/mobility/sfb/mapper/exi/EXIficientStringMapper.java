package de.fraunhofer.fit.cscw.mobility.sfb.mapper.exi;

import com.example.myschema.ArrayOfBeer;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.StringMapper;
import org.xml.sax.InputSource;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

/**
 * @author Fabian Ohler <fabian.ohler1@rwth-aachen.de>
 */
public class EXIficientStringMapper extends AbstractExiMapper implements StringMapper<ArrayOfBeer> {
    @Override
    public ArrayOfBeer read(final String data) throws Exception {
        return super.read(new InputSource(new StringReader(data)));
    }

    @Override
    public String write(final ArrayOfBeer obj) throws Exception {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        super.write(obj, baos);
        return baos.toString(StandardCharsets.UTF_8);
    }
}
