package de.fraunhofer.fit.cscw.mobility.sfb;

import com.example.myschema.ArrayOfBeer;
import de.fraunhofer.fit.cscw.mobility.sfb.compress.GzipCompressor;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.xml.JaxbXmlByteArrayMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.xml.JaxbXmlStringMapper;
import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 10.10.2018
 */
public class GzipCompressorTest {

    @Test
    public void testWrite() throws Exception {
        ArrayOfBeer groundTruth = Utils.GROUND_TRUTH;

        JaxbXmlByteArrayMapper jaxbXmlByteArrayMapper = new JaxbXmlByteArrayMapper(false);
        byte[] bytes = jaxbXmlByteArrayMapper.writeNoThrow(groundTruth);
        byte[] bytesCompressed = GzipCompressor.INSTANCE.compress(bytes);

        Files.write(Paths.get("target", "data-jaxb.xml"), bytes, StandardOpenOption.CREATE);
        Files.write(Paths.get("target", "data-jaxb.xml.gz"), bytesCompressed, StandardOpenOption.CREATE);
    }

    @Test
    public void testRead() throws Exception {
        ArrayOfBeer groundTruth = Utils.GROUND_TRUTH;

        JaxbXmlStringMapper jaxbXmlStringMapper = new JaxbXmlStringMapper(false);
        byte[] xmlData = jaxbXmlStringMapper.writeNoThrow(groundTruth).getBytes();

        byte[] xmlDataCompressed = Files.readAllBytes(Paths.get("target", "data-jaxb.xml.gz"));
        byte[] xmlDataDecompressed = GzipCompressor.INSTANCE.decompress(xmlDataCompressed);

        Assert.assertTrue(Arrays.equals(xmlData, xmlDataDecompressed));
    }
}
