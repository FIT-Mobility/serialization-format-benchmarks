package de.fraunhofer.fit.cscw.mobility.sfb.mapper;

import de.fraunhofer.fit.cscw.mobility.sfb.compress.Compressor;
import de.fraunhofer.fit.cscw.mobility.sfb.compress.GzipCompressor;
import lombok.RequiredArgsConstructor;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 12.10.2018
 */
@RequiredArgsConstructor
public class CompressingMapper<T> implements ByteArrayMapper<T>, Compressor {

    private final ByteArrayMapper<T> delegateMapper;
    private final Compressor delegateCompressor;

    public static <T> CompressingMapper<T> createGzip(ByteArrayMapper<T> delegate) {
        return new CompressingMapper<>(delegate, GzipCompressor.INSTANCE);
    }

    @Override
    public T read(byte[] data) throws Exception {
        return delegateMapper.read(decompress(data));
    }

    @Override
    public byte[] write(T obj) throws Exception {
        return compress(delegateMapper.write(obj));
    }

    @Override
    public byte[] compress(byte[] data) {
        return delegateCompressor.compress(data);
    }

    @Override
    public byte[] decompress(byte[] data) {
        return delegateCompressor.decompress(data);
    }
}
