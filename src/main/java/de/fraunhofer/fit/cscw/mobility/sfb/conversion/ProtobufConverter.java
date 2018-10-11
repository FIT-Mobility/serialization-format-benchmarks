package de.fraunhofer.fit.cscw.mobility.sfb.conversion;

import com.example.myproto.Protobuf;
import com.example.myschema.ArrayOfBeer;
import com.example.myschema.BeerType;
import com.example.myschema.SortType;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import de.fraunhofer.fit.cscw.mobility.sfb.conversion.Converter;

/**
 * @author Fabian Ohler <fabian.ohler1@rwth-aachen.de>
 */
public enum ProtobufConverter implements Converter<ArrayOfBeer, Protobuf.ArrayOfBeerType> {
    INSTANCE;

    @Override
    public Protobuf.ArrayOfBeerType convert(final ArrayOfBeer arrayOfBeer) {
        return Protobuf.ArrayOfBeerType.newBuilder()
                                       .addAllBeer(Iterables.transform(arrayOfBeer.getBeers(), this::convert))
                                       .build();
    }

    private Protobuf.ArrayOfBeerType.BeerType convert(final BeerType beer) {
        return Protobuf.ArrayOfBeerType.BeerType.newBuilder()
                                                .setBrand(beer.getBrand())
                                                .setSort(convert(beer.getSort()))
                                                .setAlcohol(beer.getAlcohol())
                                                .setBrewery(beer.getBrewery())
                                                .build();
    }

    private Protobuf.ArrayOfBeerType.BeerType.SortType convert(final SortType sort) {
        return Protobuf.ArrayOfBeerType.BeerType.SortType.newBuilder()
                                                         .addAllString(sort.getStrings())
                                                         .build();
    }

    @Override
    public ArrayOfBeer convertBack(final Protobuf.ArrayOfBeerType arrayOfBeer) {
        return new ArrayOfBeer().withBeers(Collections2.transform(arrayOfBeer.getBeerList(), this::convertBack));
    }

    private BeerType convertBack(final Protobuf.ArrayOfBeerType.BeerType beer) {
        return new BeerType().withBrand(beer.getBrand())
                             .withSort(convertBack(beer.getSort()))
                             .withAlcohol(beer.getAlcohol())
                             .withBrewery(beer.getBrewery());
    }

    private SortType convertBack(final Protobuf.ArrayOfBeerType.BeerType.SortType sort) {
        return new SortType().withStrings(sort.getStringList());
    }
}
