package de.fraunhofer.fit.cscw.mobility.sfb.xml;

import com.example.myschema.ArrayOfBeer;
import de.fraunhofer.fit.cscw.mobility.sfb.StringMapper;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 09.10.2018
 */
public class JacksonXmlStringMapper extends AbstractJacksonXmlMapper implements StringMapper<ArrayOfBeer> {

    @Override
    public ArrayOfBeer read(String str) throws Exception {
        return mapper.readValue(str, ArrayOfBeer.class);
    }

    @Override
    public String write(ArrayOfBeer obj) throws Exception {
        return mapper.writeValueAsString(obj);
    }
}
