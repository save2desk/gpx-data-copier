package example.save2.gpx;

import example.save2.exceptions.XmlReadingException;
import example.save2.gpx.dto.GpxPointDto;
import example.save2.gpx.elements.*;
import jdk.jshell.spi.ExecutionControl;
import tools.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultGpxProcessorImpl implements GpxProcessor {

    protected final DateTimeFormatter dateTimeFormatter;

    protected final DateTimeFormatter dateTimeFormatterWithoutZone;

    private final DecimalFormat decimalFormat;

    public DefaultGpxProcessorImpl() {

        this.dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");
        this.dateTimeFormatterWithoutZone = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        String pattern = "###.00000000";
        this.decimalFormat = new DecimalFormat(pattern, symbols);
        decimalFormat.setParseBigDecimal(true);

    }

    public GpxElement readGpxElement(String pathString) {

        XmlMapper xmlMapper = WoodStoxXmlMapperFactory.createWoodstoxXmlMapper();
        File file = new File(pathString);
        return xmlMapper.readValue(file, GpxElement.class);

    }

    public List<GpxPointDto> readPoints(GpxElement gpxElement) throws ParseException {

        List<GpxPointDto> points = new ArrayList<>();
        GpxPointDto pointDto;

        int trkptCounter = 0;

        try {

            for (TrksegElement trkSeg : gpxElement.trk.trkseg) {

                for (TrkptElement trkPt : trkSeg.trkpt) {

                    LocalDateTime dateTime = LocalDateTime.parse(trkPt.time.value, dateTimeFormatter);

                    BigDecimal latitude = (BigDecimal) decimalFormat.parse(trkPt.lat);
                    BigDecimal longitude = (BigDecimal) decimalFormat.parse(trkPt.lon);

                    Integer heartRate = null;
                    if (trkPt.extensions != null && trkPt.extensions.trackPointExtension != null) {
                        TrackPointExtensionElement trackPointExtension = trkPt.extensions.trackPointExtension;
                        if (trackPointExtension.hr != null) {
                            heartRate = Integer.parseInt(trackPointExtension.hr.value);
                        }
                    }

                    pointDto = new GpxPointDto.GpxPointDtoBuilder()
                            .setDatetime(dateTime)
                            .setLatitude(latitude)
                            .setLongitude(longitude)
                            .setHeartRate(heartRate)
                            .build();
                    points.add(pointDto);

                    trkptCounter++;

                }

            }

        } catch (RuntimeException e) {
            throw new XmlReadingException("Ошибка при чтении XML в точке " + trkptCounter + ": " + e.getMessage());
        }

        return points;

    }

    public void mergePoints(List<GpxPointDto> points, GpxElement gpxElementTo) {

        int i = 0;

        for (TrksegElement trkSeg : gpxElementTo.trk.trkseg) {

            for (TrkptElement trkPt : trkSeg.trkpt) {

                Integer heartRateValue = points.get(i++).getHeartRate();
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

    public GpxElement createDefaultGpxElement(List<GpxPointDto> points) {

        List<TrkptElement> trkptElements = new ArrayList<>();
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
            timeElement.value = dateTimeFormatterWithoutZone.format(point.getDateTime()) + 'Z';
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

        return gpx;

    }

    public GpxElement replaceGpxPoints(GpxElement gpxElement, List<GpxPointDto> points) {

        List<TrkptElement> trkptElements = new ArrayList<>();
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

        gpxElement.trk = trkElement;

        return gpxElement;

    }

    @Override
    public void simplifyGpx(String pathString) throws Exception {
        throw new ExecutionControl.NotImplementedException("Single-thread simplifyGpx()");
    }

    public void saveGpxElementIntoFile(GpxElement gpxElement, String pathString) throws Exception {
        GpxWriter.writeGpx(gpxElement, Path.of(pathString));
    }

}
