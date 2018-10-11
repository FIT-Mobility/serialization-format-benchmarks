package de.fraunhofer.fit.cscw.mobility.sfb;

import com.example.myproto.Protobuf;
import com.example.myschema.ArrayOfBeer;
import com.example.thrift.ArrayOfBeerType;
import de.fraunhofer.fit.cscw.mobility.sfb.compress.GzipCompressor;
import de.fraunhofer.fit.cscw.mobility.sfb.conversion.protobuf.ProtobufConverter;
import de.fraunhofer.fit.cscw.mobility.sfb.conversion.thrift.ThriftConverter;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.ByteArrayMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.exi.EXIficientByteArrayMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.json.JacksonJsonByteArrayMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.msgpack.MessagePackByteArrayMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.protobuf.ProtobufByteArrayMapper;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.thrift.ThriftByteArrayMapper;
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

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

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
        final ByteArrayMapper<BASEMODEL> mapper;
        BASEMODEL model;
        byte[] bytes;

        @Setup
        public void setup() {
            model = converter.apply(Utils.GROUND_TRUTH);
            bytes = mapper.writeNoThrow(model);
        }

        @Benchmark
        public void benchToObject(final Blackhole bh) {
            bh.consume(mapper.readNoThrow(bytes));
        }

        @Benchmark
        public void benchToByteArray(final Blackhole bh) {
            bh.consume(mapper.writeNoThrow(model));
        }

        @Benchmark
        public void benchToByteArrayAndGzipCompress(final Blackhole bh) {
            bh.consume(GzipCompressor.INSTANCE.compress(mapper.writeNoThrow(model)));
        }
    }

    public static class JaxbXmlBenchmark extends AbstractBenchmark<ArrayOfBeer> {
        public JaxbXmlBenchmark() {
            super(Function.identity(), new JaxbXmlByteArrayMapper(false));
        }
    }

    public static class JacksonXmlBenchmark extends AbstractBenchmark<ArrayOfBeer> {
        public JacksonXmlBenchmark() {
            super(Function.identity(), new JacksonXmlByteArrayMapper());
        }
    }

    public static class JacksonJsonBenchmark extends AbstractBenchmark<ArrayOfBeer> {
        public JacksonJsonBenchmark() {
            super(Function.identity(), new JacksonJsonByteArrayMapper());
        }
    }

    public static class MessagePackBenchmark extends AbstractBenchmark<ArrayOfBeer> {
        public MessagePackBenchmark() {
            super(Function.identity(), new MessagePackByteArrayMapper());
        }
    }

    public static class ProtobufBenchmark extends AbstractBenchmark<Protobuf.ArrayOfBeerType> {
        public ProtobufBenchmark() {
            super(ProtobufConverter.INSTANCE::convert, ProtobufByteArrayMapper.INSTANCE);
        }
    }

    public static class ThriftBenchmark extends AbstractBenchmark<ArrayOfBeerType> {
        public ThriftBenchmark() {
            super(ThriftConverter.INSTANCE::convert, new ThriftByteArrayMapper());
        }
    }

    public static class EXIficientBenchmark extends AbstractBenchmark<ArrayOfBeer> {
        public EXIficientBenchmark() {
            super(Function.identity(), new EXIficientByteArrayMapper());
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(Benchmarks.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }
}
