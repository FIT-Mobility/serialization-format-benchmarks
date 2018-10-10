package com.goekay;

import com.example.myproto.Protobuf;
import com.example.myschema.ArrayOfBeer;
import com.goekay.conversion.protobuf.ProtobufConverter;
import com.goekay.json.JacksonJsonByteArrayMapper;
import com.goekay.msgpack.MessagePackByteArrayMapper;
import com.goekay.protobuf.ProtobufByteArrayMapper;
import com.goekay.xml.JacksonXmlByteArrayMapper;
import com.goekay.xml.JaxbXmlByteArrayMapper;
import com.goekay.xml.JaxbXmlStringMapper;
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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author Fabian Ohler <fabian.ohler1@rwth-aachen.de>
 */
public class Benchmarks {
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

    @BenchmarkMode(Mode.AverageTime)
    @Fork(1)
    @State(Scope.Thread)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
    @RequiredArgsConstructor
    public static abstract class AbstractBenchmark<BASEMODEL> {
        final String fileNameAndSuffix;
        final Function<ArrayOfBeer, BASEMODEL> converter;
        final ByteArrayMapper<BASEMODEL> mapper;
        BASEMODEL model;
        byte[] bytes;

        @Setup
        public void setup() {
            model = converter.apply(groundTruth);
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
    }

    public static class JaxbXmlBenchmark extends AbstractBenchmark<ArrayOfBeer> {
        public JaxbXmlBenchmark() {
            super("jaxb.xml", Function.identity(), new JaxbXmlByteArrayMapper(false));
        }
    }

    public static class JacksonXmlBenchmark extends AbstractBenchmark<ArrayOfBeer> {
        public JacksonXmlBenchmark() {
            super("jackson.xml", Function.identity(), new JacksonXmlByteArrayMapper());
        }
    }

    public static class JacksonJsonBenchmark extends AbstractBenchmark<ArrayOfBeer> {
        public JacksonJsonBenchmark() {
            super("jackson.json", Function.identity(), new JacksonJsonByteArrayMapper());
        }
    }

    public static class MessagePackBenchmark extends AbstractBenchmark<ArrayOfBeer> {
        public MessagePackBenchmark() {
            super("msgpack", Function.identity(), new MessagePackByteArrayMapper());
        }
    }

    public static class ProtobufBenchmark extends AbstractBenchmark<Protobuf.ArrayOfBeerType> {
        public ProtobufBenchmark() {
            super("protobuf", ProtobufConverter.INSTANCE::convert, ProtobufByteArrayMapper.INSTANCE);
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(Benchmarks.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }
}
