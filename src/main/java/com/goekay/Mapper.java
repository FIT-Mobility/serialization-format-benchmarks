package com.goekay;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 09.10.2018
 */
public interface Mapper<T> {

    T read(String str) throws Exception;

    String write(T obj) throws Exception;

    default T readNoThrow(String str) {
        try {
            return read(str);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    default String writeNoThrow(T obj) {
        try {
            return write(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
