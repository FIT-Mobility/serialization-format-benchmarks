package com.goekay;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 10.10.2018
 */
public interface Mapper<POJO, SERIALIZATION_FORMAT> {

    POJO read(SERIALIZATION_FORMAT data) throws Exception;

    SERIALIZATION_FORMAT write(POJO obj) throws Exception;

    default POJO readNoThrow(SERIALIZATION_FORMAT data) {
        try {
            return read(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    default SERIALIZATION_FORMAT writeNoThrow(POJO obj) {
        try {
            return write(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
