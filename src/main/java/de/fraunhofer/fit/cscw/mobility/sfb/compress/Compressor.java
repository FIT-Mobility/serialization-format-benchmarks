package de.fraunhofer.fit.cscw.mobility.sfb.compress;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 10.10.2018
 */
public interface Compressor {
    byte[] compress(byte[] data);
    byte[] decompress(byte[] data);
}
