package de.fraunhofer.fit.cscw.mobility.sfb.mapper.exi;

import com.example.myschema.ArrayOfBeer;
import com.siemens.ct.exi.core.CodingMode;
import com.siemens.ct.exi.core.EXIFactory;
import com.siemens.ct.exi.core.helpers.DefaultEXIFactory;
import com.siemens.ct.exi.main.api.sax.EXISource;
import com.siemens.ct.exi.main.api.sax.SAXEncoder;
import com.siemens.ct.exi.main.api.sax.SAXFactory;
import de.fraunhofer.fit.cscw.mobility.sfb.mapper.xml.JacksonXmlByteArrayMapper;
import org.xml.sax.InputSource;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
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

    private final JacksonXmlByteArrayMapper xmlMapper = new JacksonXmlByteArrayMapper();
    private final EXIFactory exiFactory = DefaultEXIFactory.newInstance();

    AbstractExiMapper() {
        exiFactory.setCodingMode(CodingMode.COMPRESSION);
    }

    ArrayOfBeer read(InputSource source) throws Exception {
        final TransformerFactory tf = TransformerFactory.newInstance();
        final Transformer transformer = tf.newTransformer();

        final SAXSource exiSource = new EXISource(exiFactory);
        exiSource.setInputSource(source);

        final ByteArrayOutputStream xmlByteStream = new ByteArrayOutputStream();
        transformer.transform(exiSource, new StreamResult(xmlByteStream));
        return xmlMapper.read(xmlByteStream.toByteArray());
    }

    void write(ArrayOfBeer obj, OutputStream out) throws Exception {
        final SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        final SAXParser saxParser = saxParserFactory.newSAXParser();
        final SAXEncoder exiWriter = new SAXFactory(exiFactory).createEXIWriter();
        exiWriter.setOutputStream(out);
        saxParser.parse(new InputSource(new ByteArrayInputStream(xmlMapper.write(obj))), exiWriter);
    }
}
