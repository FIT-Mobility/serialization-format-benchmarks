package com.goekay;

import com.example.myschema.ArrayOfBeer;
import com.goekay.json.JacksonJsonStringMapper;
import com.goekay.msgpack.MessagePackByteArrayMapper;
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

    @Test
    public void testIfEqual() throws Exception {
        String data = readFile(Paths.get("src/main/resources", "beers.xml"));

        JaxbXmlStringMapper jaxbXmlMapper = new JaxbXmlStringMapper(false);
        JacksonXmlStringMapper jacksonXmlMapper = new JacksonXmlStringMapper();
        JacksonJsonStringMapper jacksonJsonMapper = new JacksonJsonStringMapper();
        MessagePackByteArrayMapper messagePackMapper = new MessagePackByteArrayMapper();

        ArrayOfBeer data1 = jaxbXmlMapper.read(data);
        ArrayOfBeer data2 = jacksonXmlMapper.read(data);
        ArrayOfBeer data3 = jacksonJsonMapper.read(jacksonJsonMapper.write(data1));
        ArrayOfBeer data4 = messagePackMapper.read(messagePackMapper.write(data1));

        Assert.assertEquals(data1, data2);
        Assert.assertEquals(data2, data3);
        Assert.assertEquals(data3, data4);
    }

    private String readFile(Path path) throws IOException {
        byte[] encoded = Files.readAllBytes(path);
        return new String(encoded, StandardCharsets.UTF_8);
    }
}
