package example.save2.xml;

import com.ctc.wstx.stax.WstxInputFactory;
import com.ctc.wstx.stax.WstxOutputFactory;
import com.fasterxml.jackson.annotation.JsonInclude;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.dataformat.xml.XmlFactory;
import tools.jackson.dataformat.xml.XmlMapper;
import tools.jackson.dataformat.xml.XmlWriteFeature;

import static javax.xml.stream.XMLOutputFactory.IS_REPAIRING_NAMESPACES;

public class WoodStoxXmlMapperFactory {

    public static XmlMapper createWoodstoxXmlMapper() {

        WstxInputFactory inputFactory = new WstxInputFactory();
        WstxOutputFactory outputFactory = new WstxOutputFactory();

        outputFactory.configureForRobustness();

        outputFactory.setProperty(IS_REPAIRING_NAMESPACES, Boolean.TRUE);

        XmlFactory xmlFactory = new XmlFactory(inputFactory, outputFactory);

        return XmlMapper.builder(xmlFactory)
                .changeDefaultPropertyInclusion(incl -> incl.withValueInclusion(JsonInclude.Include.NON_NULL))
                .enable(SerializationFeature.INDENT_OUTPUT)
                .enable(XmlWriteFeature.WRITE_XML_DECLARATION)
                .build();
    }

}
