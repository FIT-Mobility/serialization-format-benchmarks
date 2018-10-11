package de.fraunhofer.fit.cscw.mobility.sfb;

import com.example.myproto.Protobuf;
import com.example.myschema.ArrayOfBeer;
import de.fraunhofer.fit.cscw.mobility.sfb.compress.GzipCompressor;
import de.fraunhofer.fit.cscw.mobility.sfb.conversion.protobuf.ProtobufConverter;
import de.fraunhofer.fit.cscw.mobility.sfb.conversion.thrift.ThriftConverter;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.ByteArrayMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.exi.EXIficientByteArrayMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.fastinfoset.FastInfosetByteArrayMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.json.JacksonJsonByteArrayMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.msgpack.MessagePackByteArrayMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.protobuf.ProtobufByteArrayMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.thrift.ThriftByteArrayMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.xml.JacksonXmlByteArrayMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.xml.JaxbXmlByteArrayMapper;
import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

import static de.fraunhofer.fit.cscw.mobility.sfb.Utils.GROUND_TRUTH;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 10.10.2018
 */
public class SanityChecks {

    @Test
    public void testIfEqual() {
        List<MapperTestCase> cases = Arrays.asList(
                new MapperTestCase(new JaxbXmlByteArrayMapper(false), "data-jaxb.xml"),
                new MapperTestCase(new JacksonXmlByteArrayMapper(), "data-jackson.xml", false),
                new MapperTestCase(new JacksonJsonByteArrayMapper(), "data-jackson.json"),
                new MapperTestCase(new InternalProtobufMapper(), "proto-buf"),
                new MapperTestCase(new MessagePackByteArrayMapper(), "message-pack"),
                new MapperTestCase(new EXIficientByteArrayMapper(), "data-exificient", true),
                new MapperTestCase(new FastInfosetByteArrayMapper(false), "data-fastInfoset", true),
                new MapperTestCase(new InternalThriftMapper(), "thrift")
        );

        cases.forEach(MapperTestCase::writeToFile);
        cases.forEach(MapperTestCase::writeToFileCompressed);
        cases.forEach(MapperTestCase::convertAndAssert);
    }

    private static class InternalProtobufMapper implements ByteArrayMapper<ArrayOfBeer> {

        @Override
        public ArrayOfBeer read(byte[] data) throws Exception {
            Protobuf.ArrayOfBeerType read = ProtobufByteArrayMapper.INSTANCE.read(data);
            return ProtobufConverter.INSTANCE.convertBack(read);
        }

        @Override
        public byte[] write(ArrayOfBeer obj) throws Exception {
            Protobuf.ArrayOfBeerType convert = ProtobufConverter.INSTANCE.convert(obj);
            return ProtobufByteArrayMapper.INSTANCE.write(convert);
        }
    }

    private static class InternalThriftMapper implements ByteArrayMapper<ArrayOfBeer> {

        private final ThriftByteArrayMapper mapper = new ThriftByteArrayMapper();

        @Override
        public ArrayOfBeer read(byte[] data) throws Exception {
            return ThriftConverter.INSTANCE.convertBack(mapper.read(data));
        }

        @Override
        public byte[] write(ArrayOfBeer obj) throws Exception {
            return mapper.write(ThriftConverter.INSTANCE.convert(obj));
        }
    }

    @RequiredArgsConstructor
    private static class MapperTestCase {
        private final ByteArrayMapper<ArrayOfBeer> mapper;
        private final String fileName;
        private final boolean runAssert;

        MapperTestCase(ByteArrayMapper<ArrayOfBeer> mapper, String fileName) {
            this(mapper, fileName, true);
        }

        void writeToFile() {
            write(fileName, mapper.writeNoThrow(GROUND_TRUTH));
        }

        void writeToFileCompressed() {
            write(fileName + ".gz", GzipCompressor.INSTANCE.compress(mapper.writeNoThrow(GROUND_TRUTH)));
        }

        void convertAndAssert() {
            if (runAssert) {
                Assert.assertEquals(GROUND_TRUTH, mapper.readNoThrow(mapper.writeNoThrow(GROUND_TRUTH)));
            }
        }

        private static void write(String name, byte[] data) {
            try {
                Files.write(Paths.get("target", name), data, StandardOpenOption.CREATE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
