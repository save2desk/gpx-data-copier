package example.save2.xml;

import example.save2.xml.dto.GpxPointDto;
import example.save2.xml.elements.*;
import tools.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DefaultGpxProcessor implements GpxProcessor {

    private final DateTimeFormatter dateTimeFormatter;

    private final DecimalFormat decimalFormat;

    private GpxElement gpxElement;

    private String pathString;

    public DefaultGpxProcessor(String pathString) {

        this.pathString = pathString;

        this.dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        String pattern = "###.00000000";
        this.decimalFormat = new DecimalFormat(pattern, symbols);
        decimalFormat.setParseBigDecimal(true);

    }

    public void readGpxElement() {

        if (gpxElement == null) {
            XmlMapper xmlMapper = WoodStoxXmlMapperFactory.createWoodstoxXmlMapper();
            File file = new File(pathString);
            this.gpxElement = xmlMapper.readValue(file, GpxElement.class);
        }

    }

    @Override
    public List<GpxPointDto> readPoints() throws Exception {

        readGpxElement();

        List<GpxPointDto> points = new LinkedList<>();
        GpxPointDto pointDto;

        int trkptCounter = 0;

        try {

            for (TrksegElement trkSeg : gpxElement.trk.trkseg) {

                for (TrkptElement trkPt : trkSeg.trkpt) {

                    LocalDateTime dateTime = LocalDateTime.parse(trkPt.time.value, dateTimeFormatter);

                    BigDecimal latitude = (BigDecimal) decimalFormat.parse(trkPt.lat);
                    BigDecimal longitude = (BigDecimal) decimalFormat.parse(trkPt.lon);

                    Integer heartRate = null;
                    if (trkPt.extensions != null) {
                        TrackPointExtensionElement trackPointExtension = trkPt.extensions.trackPointExtension;
                        heartRate = Integer.parseInt(trackPointExtension.hr.value);
                    }

                    pointDto = new GpxPointDto.GpxPointDtoBuilder()
                            .setDatetime(dateTime)
                            .setLatitude(latitude)
                            .setLongitude(longitude)
                            .setHeartRate(heartRate)
                            .build();
                    points.add(pointDto);

                }

            }

        } catch (Exception e) {
            throw new Exception("Ошибка при чтении XML в точке " + trkptCounter, e);
        }

        return points;

    }

    @Override
    public void mergePoints(List<GpxPointDto> pointsFrom) {

        readGpxElement();

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

        pathString = pathString.replace(".gpx", "_merged.gpx");

    }

    @Override
    public void saveGpxElementIntoFile() throws Exception {

        GpxWriter.writeGpx(gpxElement, Path.of(pathString));

    }

    @Override
    public void createDefaultGpxElement(List<GpxPointDto> points) throws Exception {

        List<TrkptElement> trkptElements = new LinkedList<>();
        for (GpxPointDto point : points) {

            TrkptElement trkptElement = new TrkptElement();

            EleElement eleElement = new EleElement();
            eleElement.value = String.valueOf(point.getAltitude());
            trkptElement.ele = eleElement;

            HrElement hrElement = new HrElement();
            hrElement.value = String.valueOf(point.getHeartRate());

            TrackPointExtensionElement trackPointExtensionElement = new TrackPointExtensionElement();
            trackPointExtensionElement.hr = hrElement;

            ExtensionsElement extensionsElement = new ExtensionsElement();
            extensionsElement.trackPointExtension = trackPointExtensionElement;

            trkptElement.extensions = extensionsElement;

            trkptElement.lat = String.valueOf(point.getLatitude());
            trkptElement.lon = String.valueOf(point.getLongitude());

            TimeElement timeElement = new TimeElement();
            timeElement.value = String.valueOf(point.getDateTime());
            trkptElement.time = timeElement;

            trkptElements.add(trkptElement);

        }

        TrksegElement trksegElement = new TrksegElement();
        trksegElement.trkpt = trkptElements;

        TrkElement trkElement = new TrkElement();
        trkElement.trkseg = Collections.singletonList(trksegElement);

        GpxElement gpx = new GpxElement();
        gpx.trk = trkElement;

        gpx.version = "1";
        gpx.creator = "save2 fit to gpx converter";

        this.gpxElement = gpx;

    }

}
