package com.goekay;

import com.example.myschema.ArrayOfBeer;
import com.goekay.conversion.protobuf.ProtobufConverter;
import com.goekay.json.JacksonJsonStringMapper;
import com.goekay.msgpack.MessagePackByteArrayMapper;
import com.goekay.protobuf.ProtobufByteArrayMapper;
import com.goekay.xml.JacksonXmlStringMapper;
import com.goekay.xml.JaxbXmlStringMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 10.10.2018
 */
public class SanityChecks {
    private static final String xmlFile = readFile(Paths.get("src/main/resources", "beers.xml"));
    private static final ArrayOfBeer groundTruth = new JaxbXmlStringMapper(false).readNoThrow(xmlFile);

    private static String readFile(final Path path) {
        final byte[] encoded;
        try {
            encoded = Files.readAllBytes(path);
        } catch (IOException e) {
            throw new Error(e);
        }
        return new String(encoded, StandardCharsets.UTF_8);
    }

    @Test
    public void testIfEqual() throws Exception {
        JacksonXmlStringMapper jacksonXmlMapper = new JacksonXmlStringMapper();
        JacksonJsonStringMapper jacksonJsonMapper = new JacksonJsonStringMapper();
        MessagePackByteArrayMapper messagePackMapper = new MessagePackByteArrayMapper();

        Assert.assertEquals(groundTruth, jacksonXmlMapper.read(xmlFile));
        Assert.assertEquals(groundTruth, jacksonJsonMapper.read(jacksonJsonMapper.write(groundTruth)));
        Assert.assertEquals(groundTruth, messagePackMapper.read(messagePackMapper.write(groundTruth)));
        Assert.assertEquals(groundTruth, ProtobufConverter.INSTANCE.convertBack(
                ProtobufByteArrayMapper.INSTANCE.read(
                        ProtobufByteArrayMapper.INSTANCE.write(
                                ProtobufConverter.INSTANCE.convert(groundTruth)))));
    }
}
