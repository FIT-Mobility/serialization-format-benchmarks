package de.fraunhofer.fit.cscw.mobility.sfb.xml;

import com.example.myschema.ArrayOfBeer;
import de.fraunhofer.fit.cscw.mobility.sfb.StringMapper;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 09.10.2018
 */
public class JaxbXmlStringMapper extends AbstractJaxbXmlMapper implements StringMapper<ArrayOfBeer> {

    public JaxbXmlStringMapper(boolean validate) {
        super(validate);
    }

    @Override
    public ArrayOfBeer read(String str) throws Exception {
        return super.read(new StreamSource(new StringReader(str)));
    }

    @Override
    public String write(ArrayOfBeer obj) throws Exception {
        StringWriter sw = new StringWriter();
        super.write(obj, new StreamResult(sw));
        return sw.toString();
    }
}
