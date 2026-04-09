package example.save2.xml.osmand;

import com.ctc.wstx.stax.WstxOutputFactory;
import example.save2.xml.elements.*;
import org.codehaus.stax2.XMLStreamWriter2;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static example.save2.xml.GpxNamespaces.*;

public class OsmandGpxXmlWriter {

    private OsmandGpxXmlWriter() {
        /* This utility class should not be instantiated */
    }

    public static void writeGpx(GpxElement gpx, Path path) throws Exception {

        try (FileOutputStream fos = new FileOutputStream(path.toFile());

             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {

            WstxOutputFactory factory = new WstxOutputFactory();
            factory.configureForRobustness();

            XMLStreamWriter2 xmlWriter = (XMLStreamWriter2) factory.createXMLStreamWriter(osw);
            OsmandGpxPrettyWriter prettyWriter = new OsmandGpxPrettyWriter(xmlWriter);

            writeGpxDocument(prettyWriter, gpx);

            xmlWriter.flush();
            xmlWriter.close();

        }
    }

    private static void writeGpxDocument(OsmandGpxPrettyWriter xmlWriter, GpxElement gpx) throws Exception {

        xmlWriter.writeStartDocument("UTF-8", "1.0");

        xmlWriter.writeStartElement("gpx");

        xmlWriter.writeAttribute("version", gpx.getVersion());
        xmlWriter.writeAttribute("creator", gpx.getCreator());

        // Write namespace declarations
        xmlWriter.writeDefaultNamespace("http://www.topografix.com/GPX/1/1");
        xmlWriter.writeNamespace(GPXTPX_PREFIX, GPXTPX_NAMESPACE);
        xmlWriter.writeNamespace("gpxx", "http://www.garmin.com/xmlschemas/GpxExtensions/v3");
        xmlWriter.writeNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");

        // Write schemaLocation
        xmlWriter.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance",
                "schemaLocation",
                "http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd " +
                        "http://www.garmin.com/xmlschemas/GpxExtensions/v3 http://www.garmin.com/xmlschemas/GpxExtensionsv3.xsd " +
                        "http://www.garmin.com/xmlschemas/TrackPointExtension/v1 http://www.garmin.com/xmlschemas/TrackPointExtensionv1.xsd");

        if (gpx.getMetadata() != null) {
            writeMetadata(xmlWriter, gpx.getMetadata());
        }

        if (gpx.getTrk() != null) {
            writeTrack(xmlWriter, gpx.getTrk());
        }

        xmlWriter.writeEndElement();
        xmlWriter.writeEndDocument();
    }

    private static void writeMetadata(OsmandGpxPrettyWriter writer, MetadataElement metadata) throws Exception {

        writer.writeStartElement("metadata");

        if (metadata.getTime() != null) {
            writer.writeStartElement("time");
            writer.writeCharacters(metadata.getTime());
            writer.writeEndElement();
        }

        writer.writeEndElement();
    }

    private static void writeTrack(OsmandGpxPrettyWriter writer, TrkElement trk) throws Exception {

        writer.writeStartElement("trk");

        if (trk.getName() != null) {
            writer.writeStartElement("name");
            writer.writeCharacters(trk.getName());
            writer.writeEndElement();
        }

        if (trk.getTrkseg() != null) {
            for (TrksegElement segment : trk.getTrkseg()) {
                writer.writeStartElement("trkseg");

                for (TrkptElement point : segment.getTrkpt()) {
                    writeTrackPoint(writer, point);
                }

                writer.writeEndElement();
            }
        }

        writer.writeEndElement();
    }

    private static void writeTrackPoint(OsmandGpxPrettyWriter writer, TrkptElement point) throws Exception {

        writer.writeStartElement("trkpt");
        writer.writeAttribute("lat", String.valueOf(point.getLat()));
        writer.writeAttribute("lon", String.valueOf(point.getLon()));

        if (point.getEle() != null) {
            writer.writeStartElement("ele");
            writer.writeCharacters(point.getEle().value);
            writer.writeEndElement();
        }

        if (point.getTime() != null) {
            writer.writeStartElement("time");
            writer.writeCharacters(point.getTime().value);
            writer.writeEndElement();
        }

        if (point.getExtensions() != null && point.getExtensions().getTrackPointExtension() != null) {
            writer.writeStartElement("extensions");

            TrackPointExtensionElement ext = point.getExtensions().getTrackPointExtension();

            writer.writeStartElement(GPXTPX_PREFIX, "TrackPointExtension",
                    GPXTPX_NAMESPACE);

            if (ext.getAtemp() != null) {
                writer.writeStartElement(GPXTPX_PREFIX, "atemp",
                        GPXTPX_NAMESPACE);
                writer.writeCharacters(ext.getAtemp().value);
                writer.writeEndElement();
            }

            if (ext.getHr() != null) {
                writer.writeStartElement(GPXTPX_PREFIX, "hr", GPXTPX_NAMESPACE);
                writer.writeCharacters(ext.getHr().value);
                writer.writeEndElement();
            }

            writer.writeEndElement();

            writer.writeEndElement();
        }

        writer.writeEndElement();
    }
}
