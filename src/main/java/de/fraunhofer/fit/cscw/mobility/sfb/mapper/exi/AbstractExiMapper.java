package de.fraunhofer.fit.cscw.mobility.sfb.mapper.exi;

import com.example.myschema.ArrayOfBeer;
import com.siemens.ct.exi.core.CodingMode;
import com.siemens.ct.exi.core.EXIFactory;
import com.siemens.ct.exi.core.FidelityOptions;
import com.siemens.ct.exi.core.exceptions.UnsupportedOption;
import com.siemens.ct.exi.core.helpers.DefaultEXIFactory;
import com.siemens.ct.exi.main.api.sax.EXIResult;
import com.siemens.ct.exi.main.api.sax.EXISource;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.xml.JaxbXmlByteArrayMapper;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * @author Fabian Ohler <fabian.ohler1@rwth-aachen.de>
 */
public abstract class AbstractExiMapper {

    private static final JaxbXmlByteArrayMapper xmlMapper = new JaxbXmlByteArrayMapper(false);
    private static final TransformerFactory tf = TransformerFactory.newInstance();
    protected static final FidelityOptions fidelityOptions;
    protected final EXIFactory exiFactory;

    public AbstractExiMapper(final CodingMode codingMode) {
        this.exiFactory = DefaultEXIFactory.newInstance();
        this.exiFactory.setFidelityOptions(fidelityOptions);
        this.exiFactory.setCodingMode(codingMode);
    }

    static {
        fidelityOptions = FidelityOptions.createDefault();
        try {
            fidelityOptions.setFidelity(FidelityOptions.FEATURE_PREFIX, true);
            fidelityOptions.setFidelity(FidelityOptions.FEATURE_LEXICAL_VALUE, true);
        } catch (final UnsupportedOption error) {
            throw new RuntimeException(error);
        }
    }

    ArrayOfBeer read(InputSource source) throws Exception {
        final Transformer transformer = tf.newTransformer();

        final SAXSource exiSource = new EXISource(exiFactory);
        exiSource.setInputSource(source);

        final ByteArrayOutputStream xmlByteStream = new ByteArrayOutputStream();
        transformer.transform(exiSource, new StreamResult(xmlByteStream));
        return xmlMapper.read(xmlByteStream.toByteArray());
    }

    void write(ArrayOfBeer obj, OutputStream out) throws Exception {
        final EXIResult exiResult = new EXIResult(exiFactory);
        exiResult.setOutputStream(out);
        final XMLReader xmlReader = XMLReaderFactory.createXMLReader();
        xmlReader.setContentHandler(exiResult.getHandler());
        xmlReader.parse(new InputSource(new ByteArrayInputStream(xmlMapper.write(obj))));
    }
}
