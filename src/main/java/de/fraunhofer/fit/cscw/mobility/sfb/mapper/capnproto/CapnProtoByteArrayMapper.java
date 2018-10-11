package de.fraunhofer.fit.cscw.mobility.sfb.mapper.capnproto;

import com.example.myschema.ArrayOfBeer;
import com.example.myschema.BeerType;
import com.example.myschema.SortType;
import com.examples.capnp.ArrayOfBeerMain;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.ByteArrayMapper;
import org.capnproto.MessageBuilder;
import org.capnproto.MessageReader;
import org.capnproto.SerializePacked;
import org.capnproto.StructList;
import org.capnproto.Text;
import org.capnproto.TextList;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.List;

/**
 * DISCLAIMER:
 *
 * In benchmarks, we try to measure the raw conversions between byte arrays and "somewhat usable data-holding POJOs"
 * (example: {@link ArrayOfBeer)}. Unfortunately, read and write models of the generated stubs of cap-n-proto are so
 * low-level (as can be seen from internal read and write methods), the stubs cannot be treated as POJOs (and hence be
 * used in other parts of a regular Java application). That's why this mapper applies more logic than others, which
 * probably affects its scores negatively, but it is justified.
 *
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 11.10.2018
 */
public class CapnProtoByteArrayMapper implements ByteArrayMapper<ArrayOfBeer> {

    @Override
    public ArrayOfBeer read(byte[] data) throws Exception {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data)) {
            MessageReader message = SerializePacked.readFromUnbuffered(Channels.newChannel(bis));
            ArrayOfBeerMain.ArrayOfBeerType.Reader reader = message.getRoot(ArrayOfBeerMain.ArrayOfBeerType.factory);
            return readInternal(reader);
        }
    }

    @Override
    public byte[] write(ArrayOfBeer obj) throws Exception {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            MessageBuilder message = writeInternal(obj);
            SerializePacked.writeToUnbuffered(Channels.newChannel(bos), message);
            return bos.toByteArray();
        }
    }

    private static ArrayOfBeer readInternal(ArrayOfBeerMain.ArrayOfBeerType.Reader capnp) {
        StructList.Reader<ArrayOfBeerMain.BeerType.Reader> listOfBeers = capnp.getBeer();
        ArrayList<BeerType> tmp = new ArrayList<>(listOfBeers.size());

        for (ArrayOfBeerMain.BeerType.Reader beerReader : listOfBeers) {
            tmp.add(readBeerType(beerReader));
        }

        return new ArrayOfBeer().withBeers(tmp);
    }

    private static MessageBuilder writeInternal(ArrayOfBeer jaxb) {
        MessageBuilder message = new MessageBuilder();
        ArrayOfBeerMain.ArrayOfBeerType.Builder b = message.initRoot(ArrayOfBeerMain.ArrayOfBeerType.factory);

        List<BeerType> listOfBeers = jaxb.getBeers();
        StructList.Builder<ArrayOfBeerMain.BeerType.Builder> beers = b.initBeer(listOfBeers.size());

        for (int i = 0; i < listOfBeers.size(); i++) {
            BeerType beerType = listOfBeers.get(i);
            ArrayOfBeerMain.BeerType.Builder beer = beers.get(i);
            writeBeerType(beerType, beer);
        }

        return message;
    }

    private static BeerType readBeerType(ArrayOfBeerMain.BeerType.Reader beerReader) {
        return new BeerType().withAlcohol(beerReader.getAlcohol().toString())
                             .withBrand(beerReader.getBrand().toString())
                             .withBrewery(beerReader.getBrewery().toString())
                             .withSort(readSortType(beerReader.getSort()));
    }

    private static SortType readSortType(ArrayOfBeerMain.SortType.Reader capnp) {
        TextList.Reader listOfStrings = capnp.getString();
        ArrayList<String> tmp = new ArrayList<>(listOfStrings.size());

        for (Text.Reader item : listOfStrings) {
            tmp.add(item.toString());
        }

        return new SortType().withStrings(tmp);
    }

    private static void writeBeerType(BeerType jaxb, ArrayOfBeerMain.BeerType.Builder capnp) {
        capnp.setAlcohol(jaxb.getAlcohol());
        capnp.setBrand(jaxb.getBrand());
        capnp.setBrewery(jaxb.getBrewery());
        writeSortType(jaxb, capnp);
    }

    private static void writeSortType(BeerType jaxb, ArrayOfBeerMain.BeerType.Builder capnp) {
        List<String> jaxbStrings = jaxb.getSort().getStrings();
        TextList.Builder textBuilders = capnp.initSort().initString(jaxbStrings.size());

        for (int i = 0; i < jaxbStrings.size(); i++) {
            textBuilders.set(i, new Text.Reader(jaxbStrings.get(i)));
        }
    }
}
