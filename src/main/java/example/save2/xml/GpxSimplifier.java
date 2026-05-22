package example.save2.xml;

import example.save2.xml.dto.GpxPointDto;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

public class GpxSimplifier {

    private final List<GpxPointDto> points;

    public GpxSimplifier(List<GpxPointDto> points) {
        this.points = points;
    }

    public List<GpxPointDto> getPoints() {
        return points;
    }

    public void simplifyPoints() {

        removeClosePoints();
        System.out.println("simplifyPoints: " + Thread.currentThread().getName());
    }

    public void removeClosePoints() {

        int lastFilteredPointIndex = 0;

        while (lastFilteredPointIndex < points.size() - 1) {

            GpxPointDto pointOne = points.get(lastFilteredPointIndex);
            GpxPointDto pointTwo = points.get(lastFilteredPointIndex + 1);
            if (pointsAreCloseFlat(pointOne, pointTwo)) {
                points.remove(lastFilteredPointIndex + 1);
            } else {
                lastFilteredPointIndex++;
            }

        }

    }

    public boolean pointsAreCloseSpherical(GpxPointDto pointOne, GpxPointDto pointTwo) {

        BigDecimal lat1Radians = toRadians(pointOne.getLatitude());
        BigDecimal lat2Radians = toRadians(pointTwo.getLatitude());
        BigDecimal latDeltaRadians = toRadians(pointTwo.getLatitude().subtract(pointOne.getLatitude()));
        BigDecimal lonDeltaRadians = toRadians(pointTwo.getLongitude().subtract(pointOne.getLongitude()));

        BigDecimal a = BigDecimal.valueOf(
                Math.pow(Math.sin(
                        latDeltaRadians.divide(BigDecimal.TWO, RoundingMode.HALF_UP).doubleValue()
                ), 2)
                + Math.cos(lat1Radians.doubleValue()) * Math.cos(lat2Radians.doubleValue())
                + Math.pow(Math.sin(
                        lonDeltaRadians.divide(BigDecimal.TWO, RoundingMode.HALF_UP).doubleValue()
                ), 2)
        );

        MathContext sqrtMathContext = new MathContext(6, RoundingMode.HALF_UP);

        BigDecimal c = BigDecimal.valueOf(2 * Math.atan2(
                a.sqrt(sqrtMathContext).doubleValue(),
                BigDecimal.ONE.subtract(a).sqrt(sqrtMathContext).doubleValue()
        ));

        BigDecimal d = c.multiply(BigDecimal.valueOf(6371000));

        return d.compareTo(BigDecimal.valueOf(5)) < 1;
    }

    public boolean pointsAreCloseFlat(GpxPointDto pointOne, GpxPointDto pointTwo) {

        BigDecimal lat1Radians = toRadians(pointOne.getLatitude());
        BigDecimal lat2Radians = toRadians(pointTwo.getLatitude());
        BigDecimal latDeltaRadians = toRadians(pointTwo.getLatitude().subtract(pointOne.getLatitude()));
        BigDecimal lonDeltaRadians = toRadians(pointTwo.getLongitude().subtract(pointOne.getLongitude()));

        BigDecimal x = lonDeltaRadians.multiply(BigDecimal.valueOf(Math.cos(
                        lat1Radians.add(lat2Radians).divide(BigDecimal.TWO, RoundingMode.HALF_UP).doubleValue()
                )));
        BigDecimal d = BigDecimal.valueOf(Math.sqrt(x.pow(2).add(latDeltaRadians.pow(2)).doubleValue())).multiply(BigDecimal.valueOf(6371000));

        return d.compareTo(BigDecimal.valueOf(8)) < 1;
    }

    private BigDecimal toRadians(BigDecimal coordinate) {
        return coordinate.multiply(BigDecimal.valueOf(Math.PI)).divide(BigDecimal.valueOf(180), RoundingMode.HALF_UP);
    }

}
