package example.save2.gpx;

import example.save2.cli.ParametersStorage;
import example.save2.cli.enums.ParameterKey;
import example.save2.gpx.dto.GpxPointDto;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

public class GpxSimplifier {

    private final List<GpxPointDto> points;
    private final Integer iterations;
    private final BigDecimal closePointsDistance;
    private final BigDecimal linePointsDistance;

    public GpxSimplifier(List<GpxPointDto> points) {
        this.points = points;
        this.iterations = ParametersStorage.getParameter(ParameterKey.SIMPLIFY_ITERATIONS, Integer.class);
        this.closePointsDistance = BigDecimal.valueOf(ParametersStorage.getParameter(ParameterKey.CLOSE_POINTS_DISTANCE, Double.class));
        this.linePointsDistance = BigDecimal.valueOf(ParametersStorage.getParameter(ParameterKey.LINE_POINTS_DISTANCE, Double.class));
    }

    public List<GpxPointDto> getPoints() {
        return points;
    }

    public void simplifyPoints() {
        removeRedundantPoints();
        for (int i = 0; i < iterations; i++) {
            removePointsOnTheLine();
        }
    }

    protected void removeRedundantPoints() {

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

    protected void removePointsOnTheLine() {

        int lastFilteredPointIndex = 0;

        while (lastFilteredPointIndex < points.size() - 2) {

            GpxPointDto pointOne = points.get(lastFilteredPointIndex);
            GpxPointDto pointTwo = points.get(lastFilteredPointIndex + 1);
            GpxPointDto pointThree = points.get(lastFilteredPointIndex + 2);
            if (pointsAreOnTheLineFlat(pointOne, pointTwo, pointThree)) {
                points.remove(lastFilteredPointIndex + 1);
            } else {
                lastFilteredPointIndex++;
            }

        }

    }

    protected boolean pointsAreCloseFlat(GpxPointDto pointOne, GpxPointDto pointTwo) {

        BigDecimal lat1Radians = toRadians(pointOne.getLatitude());
        BigDecimal lat2Radians = toRadians(pointTwo.getLatitude());
        BigDecimal latDeltaRadians = toRadians(pointTwo.getLatitude().subtract(pointOne.getLatitude()));
        BigDecimal lonDeltaRadians = toRadians(pointTwo.getLongitude().subtract(pointOne.getLongitude()));

        BigDecimal x = lonDeltaRadians.multiply(BigDecimal.valueOf(Math.cos(
                        lat1Radians.add(lat2Radians).divide(BigDecimal.TWO, RoundingMode.HALF_UP).doubleValue()
                )));
        BigDecimal d = BigDecimal.valueOf(Math.sqrt(x.pow(2).add(latDeltaRadians.pow(2)).doubleValue())).multiply(BigDecimal.valueOf(6371000));

        return d.compareTo(closePointsDistance) < 1;
    }

    protected boolean pointsAreOnTheLineFlat(GpxPointDto pointOne, GpxPointDto pointTwo, GpxPointDto pointThree) {

        final BigDecimal epsilon = BigDecimal.valueOf(1e-12);

        BigDecimal latScale = BigDecimal.valueOf(111320.0);
        BigDecimal lat1Lat3Average = pointOne.getLatitude()
                .add(pointThree.getLatitude())
                .divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP);
        BigDecimal lonScale = latScale.multiply(BigDecimal.valueOf(Math.cos(toRadians(lat1Lat3Average).doubleValue())));

        BigDecimal x1 = pointOne.getLatitude().multiply(latScale);
        BigDecimal y1 = pointOne.getLongitude().multiply(lonScale);
        BigDecimal x2 = pointTwo.getLatitude().multiply(latScale);
        BigDecimal y2 = pointTwo.getLongitude().multiply(lonScale);
        BigDecimal x3 = pointThree.getLatitude().multiply(latScale);
        BigDecimal y3 = pointThree.getLongitude().multiply(lonScale);

        BigDecimal area = (x3.subtract(x1).multiply(y2.subtract(y1))).subtract(y3.subtract(y1).multiply(x2.subtract(x1))).abs();
        BigDecimal distAB = hypot(x3.subtract(x1), y3.subtract(y1));

        if (distAB.compareTo((epsilon)) < 1) {
            return hypot(x2.subtract(x1), y2.subtract(y1)).compareTo(linePointsDistance) <= 0;
        }

        BigDecimal perpendicularDist = area.divide(distAB, RoundingMode.HALF_UP);
        return perpendicularDist.compareTo(linePointsDistance) <= 0;

    }

    protected static BigDecimal hypot(BigDecimal x, BigDecimal y) {
        MathContext mc = new MathContext(20, RoundingMode.HALF_UP);
        BigDecimal sumOfSquares = x.pow(2, mc).add(y.pow(2, mc), mc);
        return sumOfSquares.sqrt(mc);
    }

    protected BigDecimal toRadians(BigDecimal coordinate) {
        return coordinate.multiply(BigDecimal.valueOf(Math.PI)).divide(BigDecimal.valueOf(180), RoundingMode.HALF_UP);
    }

}
