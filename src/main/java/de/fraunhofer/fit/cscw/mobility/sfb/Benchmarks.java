package de.fraunhofer.fit.cscw.mobility.sfb;

import com.beust.jcommander.JCommander;
import com.example.myschema.ArrayOfBeer;
import com.example.thrift.ArrayOfBeerType;
import com.siemens.ct.exi.core.CodingMode;
import de.fraunhofer.fit.cscw.mobility.sfb.conversion.ProtobufConverter;
import de.fraunhofer.fit.cscw.mobility.sfb.conversion.ThriftConverter;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.ByteArrayMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.CompressingMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.capnproto.CapnProtoByteArrayMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.exi.EXIficientByteArrayMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.fastinfoset.FastInfosetByteArrayMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.json.JacksonJsonByteArrayMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.json.JacksonSmileByteArrayMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.msgpack.MessagePackByteArrayMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.protobuf.ProtobufByteArrayMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.protostuff.ProtostuffBinaryArrayMapper;
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
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.ChainedOptionsBuilder;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Fabian Ohler <fabian.ohler1@rwth-aachen.de>
 */
public class Benchmarks {

    @BenchmarkMode(Mode.AverageTime)
    @Fork(1)
    @State(Scope.Thread)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
    @RequiredArgsConstructor
    public static abstract class AbstractBenchmark<BASEMODEL> {

        final Function<ArrayOfBeer, BASEMODEL> converter;
        final Supplier<ByteArrayMapper<BASEMODEL>> mapperSupplier;

        ByteArrayMapper<BASEMODEL> mapper;
        BASEMODEL model;
        byte[] bytes;

        @Setup
        public void setup() {
            mapper = mapperSupplier.get();
            model = converter.apply(Utils.GROUND_TRUTH);
            bytes = mapper.writeNoThrow(model);
        }

        @Benchmark
        public void bytesToObject(final Blackhole bh) {
            bh.consume(mapper.readNoThrow(bytes));
        }

        @Benchmark
        public void objectToBytes(final Blackhole bh) {
            bh.consume(mapper.writeNoThrow(model));
        }

        @Benchmark
        public void roundTripAndCheckEquality(final Blackhole bh) {
            final BASEMODEL otherModel = mapper.readNoThrow(mapper.writeNoThrow(model));
            final boolean equal = Objects.equals(model, otherModel);
            if (!equal) {
                System.err.println("NOT EQUAL!!!");
            }
            bh.consume(equal);
        }
    }

    public static abstract class CompressionBenchmark<BASEMODEL> extends AbstractBenchmark<BASEMODEL> {

        final Supplier<CompressingMapper<BASEMODEL>> compressingMapperSupplier;

        @Param({"false", "true"})
        boolean withCompression;

        public CompressionBenchmark(Function<ArrayOfBeer, BASEMODEL> converter,
                                    Supplier<ByteArrayMapper<BASEMODEL>> byteArrayMapperSupplier,
                                    Supplier<CompressingMapper<BASEMODEL>> compressingMapperSupplier) {
            super(converter, byteArrayMapperSupplier);
            this.compressingMapperSupplier = compressingMapperSupplier;
        }

        @Setup
        public void setup() {
            if (withCompression) {
                mapper = compressingMapperSupplier.get();
            } else {
                mapper = mapperSupplier.get();
            }
            model = converter.apply(Utils.GROUND_TRUTH);
            bytes = mapper.writeNoThrow(model);
        }
    }

    public static abstract class GzipCompressionBenchmark<BASEMODEL> extends CompressionBenchmark<BASEMODEL> {

        public GzipCompressionBenchmark(Function<ArrayOfBeer, BASEMODEL> converter,
                                        Supplier<ByteArrayMapper<BASEMODEL>> mapperSupplier) {
            super(converter, mapperSupplier, () -> CompressingMapper.createGzip(mapperSupplier.get()));
        }
    }

    public static class JaxbXml extends GzipCompressionBenchmark<ArrayOfBeer> {
        public JaxbXml() {
            super(Function.identity(), () -> new JaxbXmlByteArrayMapper(false));
        }
    }

    public static class JaxbXmlViaAalto extends GzipCompressionBenchmark<ArrayOfBeer> {
        public JaxbXmlViaAalto() {
            super(Function.identity(), AaltoXmlByteArrayMapper::new);
        }
    }

    public static class JaxbXmlViaJackson extends GzipCompressionBenchmark<ArrayOfBeer> {
        public JaxbXmlViaJackson() {
            super(Function.identity(), JacksonXmlByteArrayMapper::new);
        }
    }

    public static class JsonViaJackson extends GzipCompressionBenchmark<ArrayOfBeer> {
        public JsonViaJackson() {
            super(Function.identity(), JacksonJsonByteArrayMapper::new);
        }
    }

    public static class SmileViaJackson extends GzipCompressionBenchmark<ArrayOfBeer> {
        public SmileViaJackson() {
            super(Function.identity(), JacksonSmileByteArrayMapper::new);
        }
    }

    public static class MessagePack extends GzipCompressionBenchmark<ArrayOfBeer> {
        public MessagePack() {
            super(Function.identity(), MessagePackByteArrayMapper::new);
        }
    }

    public static class Protobuf extends GzipCompressionBenchmark<com.example.myproto.Protobuf.ArrayOfBeerType> {
        public Protobuf() {
            super(ProtobufConverter.INSTANCE::convert, () -> ProtobufByteArrayMapper.INSTANCE);
        }
    }

    public static class Protostuff extends GzipCompressionBenchmark<ArrayOfBeer> {
        public Protostuff() {
            super(Function.identity(), ProtostuffBinaryArrayMapper::new);
        }
    }

    public static class Thrift extends GzipCompressionBenchmark<ArrayOfBeerType> {
        public Thrift() {
            super(ThriftConverter.INSTANCE::convert, ThriftByteArrayMapper::new);
        }
    }

    public static class ExiBitPacked extends GzipCompressionBenchmark<ArrayOfBeer> {
        public ExiBitPacked() {
            super(Function.identity(), () -> new EXIficientByteArrayMapper(CodingMode.BIT_PACKED));
        }
    }

    public static class ExiPreCompression extends GzipCompressionBenchmark<ArrayOfBeer> {
        public ExiPreCompression() {
            super(Function.identity(), () -> new EXIficientByteArrayMapper(CodingMode.PRE_COMPRESSION));
        }
    }

    public static class ExiInternalCompression extends AbstractBenchmark<ArrayOfBeer> {
        public ExiInternalCompression() {
            super(Function.identity(), () -> new EXIficientByteArrayMapper(CodingMode.COMPRESSION));
        }
    }

    public static class FastInfoset extends GzipCompressionBenchmark<ArrayOfBeer> {
        public FastInfoset() {
            super(Function.identity(), () -> new FastInfosetByteArrayMapper(false));
        }
    }

    public static class CapnProto extends GzipCompressionBenchmark<ArrayOfBeer> {
        public CapnProto() {
            super(Function.identity(), CapnProtoByteArrayMapper::new);
        }
    }

    public static void main(String[] args) throws RunnerException {
        BenchmarksConfig config = new BenchmarksConfig();
        JCommander jCommander = new JCommander(config);

        if (printInfo(args)) {
            jCommander.setProgramName("serialization-format-benchmarks");
            jCommander.usage();
        } else {
            jCommander.parse(args);
            run(config);
        }
    }

    private static boolean printInfo(String[] args) {
        return args.length == 1 && "info".equals(args[0]);
    }

    private static void run(BenchmarksConfig config) throws RunnerException {
        ChainedOptionsBuilder opt = new OptionsBuilder();

        switch (config.getCompressionTests()) {
            case without:
                opt.param("withCompression", "false");
                break;
            case with:
                opt.param("withCompression", "true");
                break;
            case both:
            default:
                // Do nothing
                break;
        }

        config.getIncludeRegex().forEach(opt::include);
        config.getExcludeRegex().forEach(opt::exclude);

        new Runner(opt.build()).run();
    }

}
