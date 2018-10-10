package com.goekay.conversion.protobuf;

import com.example.myproto.Protobuf;
import com.example.myschema.ArrayOfBeer;
import com.example.myschema.BeerType;
import com.example.myschema.SortType;
import com.goekay.conversion.Converter;
import com.google.common.collect.Iterables;

/**
 * @author Fabian Ohler <fabian.ohler1@rwth-aachen.de>
 */
public class ProtobufConverter implements Converter<ArrayOfBeer, Protobuf.ArrayOfBeerType> {
    @Override
    public Protobuf.ArrayOfBeerType convert(final ArrayOfBeer arrayOfBeer) {
        return Protobuf.ArrayOfBeerType.newBuilder()
                                       .addAllBeer(Iterables.transform(arrayOfBeer.getBeers(), this::convert))
                                       .build();
    }

    public Protobuf.ArrayOfBeerType.BeerType convert(final BeerType beer) {
        return Protobuf.ArrayOfBeerType.BeerType.newBuilder()
                                                .setBrand(beer.getBrand())
                                                .setSort(convert(beer.getSort()))
                                                .setAlcohol(beer.getAlcohol())
                                                .setBrewery(beer.getBrewery())
                                                .build();
    }

    public Protobuf.ArrayOfBeerType.BeerType.SortType convert(final SortType sort) {
        return Protobuf.ArrayOfBeerType.BeerType.SortType.newBuilder()
                                                         .addAllString(sort.getStrings())
                                                         .build();
    }
}
