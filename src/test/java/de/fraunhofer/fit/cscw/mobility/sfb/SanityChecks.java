package de.fraunhofer.fit.cscw.mobility.sfb;

import com.example.myschema.ArrayOfBeer;
import de.fraunhofer.fit.cscw.mobility.sfb.conversion.protobuf.ProtobufConverter;
import de.fraunhofer.fit.cscw.mobility.sfb.json.JacksonJsonStringMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.msgpack.MessagePackByteArrayMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.protobuf.ProtobufByteArrayMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.xml.JacksonXmlStringMapper;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 10.10.2018
 */
public class SanityChecks {

    @Test
    public void testIfEqual() throws Exception {
        JacksonXmlStringMapper jacksonXmlMapper = new JacksonXmlStringMapper();
        JacksonJsonStringMapper jacksonJsonMapper = new JacksonJsonStringMapper();
        MessagePackByteArrayMapper messagePackMapper = new MessagePackByteArrayMapper();

        ArrayOfBeer groundTruth = Utils.GROUND_TRUTH;
        String xmlFile = Utils.XML_FILE;

        Assert.assertEquals(groundTruth, jacksonXmlMapper.read(xmlFile));
        Assert.assertEquals(groundTruth, jacksonJsonMapper.read(jacksonJsonMapper.write(groundTruth)));
        Assert.assertEquals(groundTruth, messagePackMapper.read(messagePackMapper.write(groundTruth)));
        Assert.assertEquals(groundTruth, ProtobufConverter.INSTANCE.convertBack(
                ProtobufByteArrayMapper.INSTANCE.read(
                        ProtobufByteArrayMapper.INSTANCE.write(
                                ProtobufConverter.INSTANCE.convert(groundTruth)))));
    }
}
