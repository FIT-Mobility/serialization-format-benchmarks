package com.goekay.conversion;

/**
 * @author Fabian Ohler <fabian.ohler1@rwth-aachen.de>
 */
public interface Converter<BASEMODEL, POJO> {
    POJO convert(BASEMODEL basemodel);

    BASEMODEL convertBack(POJO pojo);
}
