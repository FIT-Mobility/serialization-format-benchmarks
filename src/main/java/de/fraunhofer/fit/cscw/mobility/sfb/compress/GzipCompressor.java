package de.fraunhofer.fit.cscw.mobility.sfb.compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 10.10.2018
 */
public enum GzipCompressor implements Compressor {
    INSTANCE;

    @Override
    public byte[] compress(byte[] data) {
        // why nested try blocks: for bos.toByteArray() to be available, we need to close GZIPOutputStream first
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
                 GZIPOutputStream gzipOS = new GZIPOutputStream(bos)) {
                bis.transferTo(gzipOS);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] decompress(byte[] data) {
        // why nested try blocks: for bos.toByteArray() to be available, we need to close GZIPOutputStream first
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
                 GZIPInputStream gzipIS = new GZIPInputStream(bis)) {
                gzipIS.transferTo(bos);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
