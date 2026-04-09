package example.save2.xml.strava;

import example.save2.xml.XmlProcessor;
import example.save2.dto.GpxPointDto;
import example.save2.xml.elements.GpxElement;
import example.save2.xml.elements.TrackPointExtensionElement;
import example.save2.xml.elements.TrkptElement;
import example.save2.xml.elements.TrksegElement;
import jdk.jshell.spi.ExecutionControl;
import tools.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

public class StravaXmlProcessor implements XmlProcessor {

    private final GpxElement gpxElement;

    private final DateTimeFormatter dateTimeFormatter;

    private final DecimalFormat decimalFormat;

    public StravaXmlProcessor(String pathString) {

        XmlMapper xmlMapper = new XmlMapper();
        File file = new File(pathString);
        gpxElement = xmlMapper.readValue(file, GpxElement.class);

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

        int trkptCounter = 0;

        for (TrksegElement trkSeg : gpxElement.trk.trkseg) {

            for (TrkptElement trkPt : trkSeg.trkpt) {

                try {

                    LocalDateTime dateTime = LocalDateTime.parse(trkPt.time.value, dateTimeFormatter);

                    BigDecimal latitude = (BigDecimal) decimalFormat.parse(trkPt.lat);
                    BigDecimal longitude = (BigDecimal) decimalFormat.parse(trkPt.lon);

                    TrackPointExtensionElement trackPointExtension = trkPt.extensions.trackPointExtension;
                    Integer heartRate = Integer.parseInt(trackPointExtension.hr.value);

                    pointDto = new GpxPointDto.GpxPointDtoBuilder()
                            .setDatetime(dateTime)
                            .setLatitude(latitude)
                            .setLongitude(longitude)
                            .setHeartRate(heartRate)
                            .build();
                    points.add(pointDto);

                } catch (Exception e) {
                    System.out.println("Ошибка при чтении XML в точке " + trkptCounter);
                    throw e;
                }

                trkptCounter++;

            }
        }
        return points;

    }

    @Override
    public void mergePoints(List<GpxPointDto> pointsFrom) throws Exception {
        throw new ExecutionControl.NotImplementedException("Method mergePoints not implemented in StravaXmlProcessor");
    }

    @Override
    public void savePoints() throws Exception {
        throw new ExecutionControl.NotImplementedException("Method savePoints not implemented in StravaXmlProcessor");
    }

}
