package example.save2.xml;

import example.save2.xml.dto.GpxPointDto;

import java.time.ZoneOffset;
import java.util.List;

public class Gpx2GpxManager {

    private final GpxProcessor inputGpxProcessor;
    private final GpxProcessor outputGpxProcessor;

    public Gpx2GpxManager(GpxProcessor inputGpxProcessor, GpxProcessor outputGpxProcessor) {
        this.inputGpxProcessor = inputGpxProcessor;
        this.outputGpxProcessor = outputGpxProcessor;
    }

    public void copyHrValues() throws Exception {

        List<GpxPointDto> pointsFrom = inputGpxProcessor.readPoints();

        List<GpxPointDto> pointsTo = outputGpxProcessor.readPoints();

        mergeHeartRates(pointsTo, pointsFrom);

        outputGpxProcessor.mergePoints(pointsTo);

        outputGpxProcessor.saveGpxElementIntoFile();

    }

    private static void mergeHeartRates(List<GpxPointDto> pointsTo, List<GpxPointDto> pointsFrom) {

        int fromCount = 0;
        int toCount = 0;

        while (fromCount < pointsFrom.size() && toCount < pointsTo.size()) {

            GpxPointDto currentPointTo = pointsTo.get(toCount);

            GpxPointDto currentPointFrom = pointsFrom.get(fromCount);

            if (currentPointFrom.getHeartRate() != null) {

                long millisFrom = currentPointFrom.getDateTime().toInstant(ZoneOffset.ofHours(0)).getEpochSecond();
                long millisTo = currentPointTo.getDateTime().toInstant(ZoneOffset.ofHours(0)).getEpochSecond();
                long millisDifference = millisFrom - millisTo;

                if (millisDifference > 5) {
                    toCount++;
                } else if (millisDifference >= 0) {
                    currentPointTo.setHeartRate(currentPointFrom.getHeartRate());
                    fromCount++;
                } else {
                    fromCount++;
                }

            }

        }

    }

}
