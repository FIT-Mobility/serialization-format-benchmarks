package de.fraunhofer.fit.cscw.mobility.sfb.conversion.thrift;

import com.example.myschema.ArrayOfBeer;
import com.example.thrift.ArrayOfBeerType;
import com.example.thrift.BeerType;
import com.example.thrift.SortType;
import de.fraunhofer.fit.cscw.mobility.sfb.conversion.Converter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 11.10.2018
 */
public enum ThriftConverter implements Converter<ArrayOfBeer, ArrayOfBeerType> {
    INSTANCE;

    @Override
    public ArrayOfBeerType convert(ArrayOfBeer arrayOfBeer) {
        List<BeerType> beers = arrayOfBeer.getBeers()
                                          .stream()
                                          .map(this::convert)
                                          .collect(Collectors.toList());
        return new ArrayOfBeerType(beers);
    }

    private BeerType convert(com.example.myschema.BeerType jaxbBeer) {
        BeerType b = new BeerType();
        b.setBrand(jaxbBeer.getBrand());
        b.setSort(convert(jaxbBeer.getSort()));
        b.setAlcohol(jaxbBeer.getAlcohol());
        b.setBrewery(jaxbBeer.getBrewery());
        return b;
    }

    private SortType convert(com.example.myschema.SortType sort) {
        SortType s = new SortType();
        sort.getStrings().forEach(s::addTo_string);
        return s;
    }

    @Override
    public ArrayOfBeer convertBack(ArrayOfBeerType arrayOfBeerType) {
        List<com.example.myschema.BeerType> collect = arrayOfBeerType.getBeer()
                                                                     .stream()
                                                                     .map(this::convertBack)
                                                                     .collect(Collectors.toList());
        return new ArrayOfBeer().withBeers(collect);
    }

    private com.example.myschema.BeerType convertBack(BeerType beer) {
        return new com.example.myschema.BeerType().withBrand(beer.getBrand())
                                                  .withSort(convertBack(beer.getSort()))
                                                  .withAlcohol(beer.getAlcohol())
                                                  .withBrewery(beer.getBrewery());
    }

    private com.example.myschema.SortType convertBack(SortType thriftSort) {
        return new com.example.myschema.SortType().withStrings(thriftSort.get_string());
    }
}
