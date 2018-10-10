package de.fraunhofer.fit.cscw.mobility.sfb;

import com.example.myschema.ArrayOfBeer;
import de.fraunhofer.fit.cscw.mobility.sfb.xml.JaxbXmlStringMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 10.10.2018
 */
public final class Utils {

    public static final String XML_FILE = readFile(Paths.get("src/main/resources", "beers.xml"));

    public static final ArrayOfBeer GROUND_TRUTH = new JaxbXmlStringMapper(false).readNoThrow(XML_FILE);
    
    private static String readFile(final Path path) {
        final byte[] encoded;
        try {
            encoded = Files.readAllBytes(path);
        } catch (IOException e) {
            throw new Error(e);
        }
        return new String(encoded, StandardCharsets.UTF_8);
    }
}
