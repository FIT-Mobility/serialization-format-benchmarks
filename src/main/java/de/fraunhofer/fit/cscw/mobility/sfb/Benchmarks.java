package de.fraunhofer.fit.cscw.mobility.sfb;

import com.example.myschema.ArrayOfBeer;
import com.example.thrift.ArrayOfBeerType;
import com.siemens.ct.exi.core.CodingMode;
import de.fraunhofer.fit.cscw.mobility.sfb.compress.Compressor;
import de.fraunhofer.fit.cscw.mobility.sfb.compress.GzipCompressor;
import de.fraunhofer.fit.cscw.mobility.sfb.conversion.ProtobufConverter;
import de.fraunhofer.fit.cscw.mobility.sfb.conversion.ThriftConverter;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.ByteArrayMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.capnproto.CapnProtoByteArrayMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.exi.EXIficientByteArrayMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.fastinfoset.FastInfosetByteArrayMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.json.JacksonJsonByteArrayMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.msgpack.MessagePackByteArrayMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.protobuf.ProtobufByteArrayMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.thrift.ThriftByteArrayMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.xml.AaltoXmlByteArrayMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.xml.JacksonXmlByteArrayMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.xml.JaxbXmlByteArrayMapper;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author Fabian Ohler <fabian.ohler1@rwth-aachen.de>
 */
public class Benchmarks {

    private static final Compressor COMPRESSOR = GzipCompressor.INSTANCE;

    @BenchmarkMode(Mode.AverageTime)
    @Fork(1)
    @State(Scope.Thread)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
    @RequiredArgsConstructor
    public static abstract class AbstractBenchmark<BASEMODEL> {
        final Function<ArrayOfBeer, BASEMODEL> converter;
        final ByteArrayMapper<BASEMODEL> noCompressionMapper;
        final ByteArrayMapper<BASEMODEL> externalCompressionMapper;
        BASEMODEL model;
        byte[] nonCompressedBytes;
        byte[] externallyCompressedBytes;

        public AbstractBenchmark(final Function<ArrayOfBeer, BASEMODEL> converter,
                                 final ByteArrayMapper<BASEMODEL> mapper) {
            this(converter, mapper, mapper);
        }

        @Setup
        public void setup() {
            model = converter.apply(Utils.GROUND_TRUTH);
            nonCompressedBytes = noCompressionMapper.writeNoThrow(model);
            externallyCompressedBytes = COMPRESSOR.compress(externalCompressionMapper.writeNoThrow(model));
        }

        @Benchmark
        public void bytesToObject(final Blackhole bh) {
            bh.consume(noCompressionMapper.readNoThrow(nonCompressedBytes));
        }

        @Benchmark
        public void bytesToObjectExternalCompression(final Blackhole bh) {
            bh.consume(externalCompressionMapper.readNoThrow(COMPRESSOR.decompress(externallyCompressedBytes)));
        }

        @Benchmark
        public void objectToBytes(final Blackhole bh) {
            bh.consume(noCompressionMapper.writeNoThrow(model));
        }

        @Benchmark
        public void objectToBytesExternalCompression(final Blackhole bh) {
            bh.consume(COMPRESSOR.compress(externalCompressionMapper.writeNoThrow(model)));
        }

        @Benchmark
        public void roundTripAndCheckEquality(final Blackhole bh) {
            final BASEMODEL otherModel = noCompressionMapper.readNoThrow(noCompressionMapper.writeNoThrow(model));
            final boolean equal = Objects.equals(model, otherModel);
            if (!equal) {
                System.err.println("NOT EQUAL!!!");
            }
            bh.consume(equal);
        }
    }

    public static abstract class AbstractInternalCompressionBenchmark<BASEMODEL> extends AbstractBenchmark<BASEMODEL> {
        final ByteArrayMapper<BASEMODEL> internalCompressionMapper;
        byte[] internallyCompressedBytes;

        public AbstractInternalCompressionBenchmark(
                final Function<ArrayOfBeer, BASEMODEL> converter,
                final ByteArrayMapper<BASEMODEL> noCompressionMapper,
                final ByteArrayMapper<BASEMODEL> externalCompressionMapper,
                final ByteArrayMapper<BASEMODEL> internalCompressionMapper) {
            super(converter, noCompressionMapper, externalCompressionMapper);
            this.internalCompressionMapper = internalCompressionMapper;
        }

        @Override
        public void setup() {
            super.setup();
            internallyCompressedBytes = internalCompressionMapper.writeNoThrow(model);
        }

        @Benchmark
        public void bytesToObjectInternalCompression(final Blackhole bh) {
            bh.consume(internalCompressionMapper.readNoThrow(internallyCompressedBytes));
        }

        @Benchmark
        public void objectToBytesInternalCompression(final Blackhole bh) {
            bh.consume(internalCompressionMapper.writeNoThrow(model));
        }
    }

    public static class JaxbXml extends AbstractBenchmark<ArrayOfBeer> {
        public JaxbXml() {
            super(Function.identity(), new JaxbXmlByteArrayMapper(false));
        }
    }

    public static class JaxbXmlViaAalto extends AbstractBenchmark<ArrayOfBeer> {
        public JaxbXmlViaAalto() {
            super(Function.identity(), new AaltoXmlByteArrayMapper());
        }
    }

    public static class JaxbXmlViaJackson extends AbstractBenchmark<ArrayOfBeer> {
        public JaxbXmlViaJackson() {
            super(Function.identity(), new JacksonXmlByteArrayMapper());
        }
    }

    public static class JsonViaJackson extends AbstractBenchmark<ArrayOfBeer> {
        public JsonViaJackson() {
            super(Function.identity(), new JacksonJsonByteArrayMapper());
        }
    }

    public static class MessagePack extends AbstractBenchmark<ArrayOfBeer> {
        public MessagePack() {
            super(Function.identity(), new MessagePackByteArrayMapper());
        }
    }

    public static class Protobuf extends AbstractBenchmark<com.example.myproto.Protobuf.ArrayOfBeerType> {
        public Protobuf() {
            super(ProtobufConverter.INSTANCE::convert, ProtobufByteArrayMapper.INSTANCE);
        }
    }

    public static class Thrift extends AbstractBenchmark<ArrayOfBeerType> {
        public Thrift() {
            super(ThriftConverter.INSTANCE::convert, new ThriftByteArrayMapper());
        }
    }

    public static class EXIficient extends AbstractInternalCompressionBenchmark<ArrayOfBeer> {
        public EXIficient() {
            super(Function.identity(),
                    new EXIficientByteArrayMapper(CodingMode.BIT_PACKED),
                    new EXIficientByteArrayMapper(CodingMode.PRE_COMPRESSION),
                    new EXIficientByteArrayMapper(CodingMode.COMPRESSION)
            );
        }
    }

    public static class FastInfoset extends AbstractBenchmark<ArrayOfBeer> {
        public FastInfoset() {
            super(Function.identity(), new FastInfosetByteArrayMapper(false));
        }
    }

    public static class CapnProto extends AbstractBenchmark<ArrayOfBeer> {
        public CapnProto() {
            super(Function.identity(), new CapnProtoByteArrayMapper());
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(Benchmarks.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }
}
