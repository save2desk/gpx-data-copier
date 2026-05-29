package example.save2.fit;

import com.garmin.fit.Decoder;
import com.garmin.fit.MesgListener;
import com.garmin.fit.RecordMesg;
import example.save2.file.FileUtils;
import example.save2.gpx.dto.GpxPointDto;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DefaultFitProcessor implements FitProcessor {

    private final MathContext sqrtMathContext = new MathContext(7, RoundingMode.HALF_UP);

    @Override
    public List<GpxPointDto> readPoints(String pathString) throws Exception {

        byte[] fileBytes = FileUtils.readFileBytes(pathString);

        Decoder decoder = new Decoder(fileBytes);

        List<GpxPointDto> points = new ArrayList<>();

        decoder.addListener((MesgListener) mesg -> {
            if (Objects.equals(mesg.getName(), "record")) {
                RecordMesg recordMesg = new RecordMesg(mesg);
                processRecordMessage(recordMesg, points);
            }
        });

        decoder.read();

        return points;

    }

    private void processRecordMessage(RecordMesg recordMesg, List<GpxPointDto> points) {

        GpxPointDto pointDto = new GpxPointDto();

        if (recordMesg.getPositionLat() == null || recordMesg.getPositionLong() == null) {
            return;
        }

        pointDto.setLatitude(semicirclesToDegrees(recordMesg.getPositionLat()));
        pointDto.setLongitude(semicirclesToDegrees(recordMesg.getPositionLong()));

        pointDto.setAltitude(BigDecimal.valueOf(recordMesg.getAltitude()));

        pointDto.setDateTime(LocalDateTime.ofEpochSecond(
                recordMesg.getTimestamp().getInstant().getEpochSecond(),
                recordMesg.getTimestamp().getInstant().getNano(),
                ZoneOffset.ofHours(0)
        ));

        pointDto.setHeartRate(Integer.valueOf(recordMesg.getHeartRate()));

        points.add(pointDto);

    }

    private BigDecimal semicirclesToDegrees(long semicircles) {
        BigDecimal result = BigDecimal.valueOf(2L).pow(31);
        result = BigDecimal.valueOf(180D).divide(result, sqrtMathContext);
        result = BigDecimal.valueOf(semicircles).multiply(result);
        result = result.setScale(6, RoundingMode.HALF_UP);
        return result;
    }

}
