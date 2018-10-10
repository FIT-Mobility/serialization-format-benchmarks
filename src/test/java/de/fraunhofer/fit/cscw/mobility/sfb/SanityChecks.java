package de.fraunhofer.fit.cscw.mobility.sfb;

import com.example.myschema.ArrayOfBeer;
import de.fraunhofer.fit.cscw.mobility.sfb.conversion.protobuf.ProtobufConverter;
import de.fraunhofer.fit.cscw.mobility.sfb.json.JacksonJsonStringMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.msgpack.MessagePackByteArrayMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.protobuf.ProtobufByteArrayMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.xml.JacksonXmlStringMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.xml.JaxbXmlStringMapper;
import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 10.10.2018
 */
public class SanityChecks {

    @Test
    public void testIfEqual() throws Exception {
        JaxbXmlStringMapper jaxbXmlStringMapper = new JaxbXmlStringMapper(false);
        JacksonXmlStringMapper jacksonXmlMapper = new JacksonXmlStringMapper();
        JacksonJsonStringMapper jacksonJsonMapper = new JacksonJsonStringMapper();
        MessagePackByteArrayMapper messagePackMapper = new MessagePackByteArrayMapper();

        ArrayOfBeer groundTruth = Utils.GROUND_TRUTH;
        String xmlFile = Utils.XML_FILE;

        Files.write(Paths.get("target", "data-jaxb.xml"), jaxbXmlStringMapper.writeNoThrow(groundTruth).getBytes(), StandardOpenOption.CREATE);
        Files.write(Paths.get("target", "data-jackson.xml"), jacksonXmlMapper.writeNoThrow(groundTruth).getBytes(), StandardOpenOption.CREATE);
        Files.write(Paths.get("target", "data-jackson.json"), jacksonJsonMapper.writeNoThrow(groundTruth).getBytes(), StandardOpenOption.CREATE);
        Files.write(Paths.get("target", "message-pack"), messagePackMapper.writeNoThrow(groundTruth), StandardOpenOption.CREATE);
        Files.write(Paths.get("target", "proto-buf"), toProtobuf(groundTruth), StandardOpenOption.CREATE);

        Assert.assertEquals(groundTruth, jacksonXmlMapper.read(xmlFile));
        Assert.assertEquals(groundTruth, jacksonJsonMapper.read(jacksonJsonMapper.write(groundTruth)));
        Assert.assertEquals(groundTruth, messagePackMapper.read(messagePackMapper.write(groundTruth)));
        Assert.assertEquals(groundTruth, toProtobufAndBack(groundTruth));
    }

    private static byte[] toProtobuf(ArrayOfBeer groundTruth) {
        return ProtobufByteArrayMapper.INSTANCE.writeNoThrow(
                ProtobufConverter.INSTANCE.convert(groundTruth));
    }

    private static ArrayOfBeer toProtobufAndBack(ArrayOfBeer groundTruth) {
        return ProtobufConverter.INSTANCE.convertBack(
                ProtobufByteArrayMapper.INSTANCE.readNoThrow(
                        toProtobuf(groundTruth)));
    }

}
