package example.save2.osmand;

import com.ctc.wstx.stax.WstxInputFactory;
import com.ctc.wstx.stax.WstxOutputFactory;
import com.fasterxml.jackson.annotation.JsonInclude;
import example.save2.XmlProcessor;
import example.save2.dto.GpxPointDto;
import example.save2.elements.*;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.dataformat.xml.XmlFactory;
import tools.jackson.dataformat.xml.XmlMapper;
import tools.jackson.dataformat.xml.XmlWriteFeature;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import static javax.xml.stream.XMLOutputFactory.IS_REPAIRING_NAMESPACES;

public class OsmandXmlProcessor implements XmlProcessor {

    private final DateTimeFormatter dateTimeFormatter;

    private final DecimalFormat decimalFormat;

    private final GpxElement gpxElement;

    private String pathString;

    public OsmandXmlProcessor(String pathString) {

        this.pathString = pathString;

        XmlMapper xmlMapper = createWoodstoxXmlMapper();
        File file = new File(pathString);
        this.gpxElement = xmlMapper.readValue(file, GpxElement.class);

        this.dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        String pattern = "###.00000000";
        this.decimalFormat = new DecimalFormat(pattern, symbols);
        decimalFormat.setParseBigDecimal(true);

    }

    @Override
    public List<GpxPointDto> readPoints() throws ParseException {

        List<GpxPointDto> points = new LinkedList<>();
        GpxPointDto pointDto;
        for (TrksegElement trkSeg : gpxElement.trk.trkseg) {
            for (TrkptElement trkPt : trkSeg.trkpt) {

                LocalDateTime dateTime = LocalDateTime.parse(trkPt.time.value, dateTimeFormatter);

                BigDecimal latitude = (BigDecimal) decimalFormat.parse(trkPt.lat);
                BigDecimal longitude = (BigDecimal) decimalFormat.parse(trkPt.lon);

                pointDto = new GpxPointDto.GpxPointDtoBuilder()
                        .setDatetime(dateTime)
                        .setLatitude(latitude)
                        .setLongitude(longitude)
                        .build();
                points.add(pointDto);
            }
        }
        return points;

    }

    @Override
    public void mergePoints(List<GpxPointDto> pointsFrom) {

        int i = 0;

        for (TrksegElement trkSeg : gpxElement.trk.trkseg) {

            for (TrkptElement trkPt : trkSeg.trkpt) {

                Integer heartRateValue = pointsFrom.get(i++).getHeartRate();
                if (heartRateValue == null) {
                    continue;
                }

                if (trkPt.extensions == null) {
                    trkPt.extensions = new ExtensionsElement();
                }

                if (trkPt.extensions.trackPointExtension == null) {
                    trkPt.extensions.trackPointExtension = new TrackPointExtensionElement();
                }

                if (trkPt.extensions.trackPointExtension.hr == null) {
                    HrElement hrElement = new HrElement();
                    hrElement.value = heartRateValue.toString();
                    trkPt.extensions.trackPointExtension.hr = hrElement;
                } else {
                    trkPt.extensions.trackPointExtension.hr.value = heartRateValue.toString();
                }

            }

        }

    }

    @Override
    public void savePoints() throws Exception {

        pathString = pathString.replace(".gpx", "_merged.gpx");

        OsmandGpxXmlWriter.writeGpx(gpxElement, Path.of(pathString));

    }

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
