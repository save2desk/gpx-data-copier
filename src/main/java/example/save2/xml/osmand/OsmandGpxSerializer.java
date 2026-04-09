package example.save2.xml.osmand;

import com.ctc.wstx.stax.WstxOutputFactory;
import com.fasterxml.jackson.annotation.JsonInclude;
import example.save2.exceptions.ExceptionUtils;
import example.save2.xml.GpxNamespaces;
import example.save2.xml.elements.GpxElement;
import org.codehaus.stax2.XMLStreamWriter2;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.ser.std.StdSerializer;
import tools.jackson.dataformat.xml.XmlMapper;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

@Deprecated
public class OsmandGpxSerializer extends StdSerializer<GpxElement> {

    private final XmlMapper delegateMapper;

    public OsmandGpxSerializer() {
        super(GpxElement.class);
        this.delegateMapper = XmlMapper.builder()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .changeDefaultPropertyInclusion(incl -> incl.withValueInclusion(JsonInclude.Include.NON_NULL))
                .build();
    }

    @Override
    public void serialize(GpxElement value, JsonGenerator gen, SerializationContext context) throws JacksonException {

        StringWriter stringWriter = new StringWriter();
        WstxOutputFactory factory = new WstxOutputFactory();

        try {

            XMLStreamWriter2 xmlWriter = factory.createXMLStreamWriter(stringWriter, StandardCharsets.UTF_8.name());

            xmlWriter.writeStartElement("gpx");

            xmlWriter.writeAttribute("version", value.version);
            xmlWriter.writeAttribute("creator", value.creator);

            xmlWriter.writeNamespace(GpxNamespaces.GPXTPX_PREFIX, GpxNamespaces.GPXTPX_NAMESPACE);
            xmlWriter.writeNamespace(GpxNamespaces.GPXX_PREFIX, GpxNamespaces.GPXX_NAMESPACE);
            xmlWriter.writeNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");

            xmlWriter.writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance",
                    "schemaLocation",
                    "http://www.topografix.com/GPX/1/1 " +
                            "http://www.topografix.com/GPX/1/1/gpx.xsd " +
                            "http://www.garmin.com/xmlschemas/GpxExtensions/v3 " +
                            "http://www.garmin.com/xmlschemas/GpxExtensionsv3.xsd " +
                            "http://www.garmin.com/xmlschemas/TrackPointExtension/v1 " +
                            "http://www.garmin.com/xmlschemas/TrackPointExtensionv1.xsd");

            if (value.metadata != null) {
                String metadataXml = delegateMapper.writeValueAsString(value.metadata);
                metadataXml = metadataXml.replaceAll("<\\?xml.*\\?>", "").trim();
                metadataXml = metadataXml.replaceFirst("^<metadata>", "")
                        .replaceFirst("</metadata>$", "");
                xmlWriter.writeRaw(metadataXml);
            }

            if (value.trk != null) {
                String trkXml = delegateMapper.writeValueAsString(value.trk);
                trkXml = trkXml.replaceAll("<\\?xml.*\\?>", "").trim();
                trkXml = trkXml.replaceFirst("^<trk>", "")
                        .replaceFirst("</trk>$", "");
                xmlWriter.writeRaw(trkXml);
            }

            xmlWriter.writeEndElement();
            xmlWriter.flush();
            xmlWriter.close();

            gen.writeRaw(stringWriter.toString());

        } catch (Exception e) {
            System.out.println(ExceptionUtils.stackTraceToString(e));
        }

    }

}
