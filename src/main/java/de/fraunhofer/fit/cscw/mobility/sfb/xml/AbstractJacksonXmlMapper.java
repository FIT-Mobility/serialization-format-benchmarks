package de.fraunhofer.fit.cscw.mobility.sfb.xml;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 10.10.2018
 */
abstract class AbstractJacksonXmlMapper {

    static final XmlMapper mapper = new XmlMapper();

    static {
        mapper.setDefaultUseWrapper(false);
        mapper.registerModule(new JaxbAnnotationModule());
    }
}
