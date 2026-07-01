package example.save2.gpx.manager;

import example.save2.cli.ParametersStorage;
import example.save2.cli.enums.ParameterKey;
import example.save2.file.FileUtils;
import example.save2.gpx.GpxProcessorImpl;
import example.save2.gpx.GpxProcessor;
import example.save2.gpx.dto.GpxPointDto;
import example.save2.gpx.elements.GpxElement;

import java.time.ZoneOffset;
import java.util.List;

public class GpxHeartRateManager {

    private final GpxProcessor heartrateGpxProcessor;
    private final String heartrateGpxPath;

    private final GpxProcessor gpsGpxProcessor;
    private final String gpsGpxPath;

    private final String outputGpxPath;

    public GpxHeartRateManager() {

        heartrateGpxPath = ParametersStorage.getParameter(ParameterKey.FIRST_INPUT_FILE, String.class);
        FileUtils.validateXmlFile(heartrateGpxPath);
        heartrateGpxProcessor = new GpxProcessorImpl();

        gpsGpxPath = ParametersStorage.getParameter(ParameterKey.SECOND_INPUT_FILE, String.class);
        FileUtils.validateXmlFile(gpsGpxPath);
        gpsGpxProcessor = new GpxProcessorImpl();

        outputGpxPath = ParametersStorage.getParameter(ParameterKey.OUTPUT_FILE, String.class);

    }

    public void copyHrValues() throws Exception {

        GpxElement gpxElementFrom = heartrateGpxProcessor.readGpxElement(heartrateGpxPath);

        List<GpxPointDto> pointsFrom = heartrateGpxProcessor.readPoints(gpxElementFrom);

        GpxElement gpxElementTo = gpsGpxProcessor.readGpxElement(gpsGpxPath);

        List<GpxPointDto> pointsTo = gpsGpxProcessor.readPoints(gpxElementTo);

        mergeHeartRates(pointsTo, pointsFrom);

        gpsGpxProcessor.mergePoints(pointsTo, gpxElementTo);

        gpsGpxProcessor.saveGpxElementIntoFile(gpxElementTo, outputGpxPath);

    }

    private static void mergeHeartRates(List<GpxPointDto> pointsTo, List<GpxPointDto> pointsFrom) {

        int fromCount = 0;
        int toCount = 0;

        while (fromCount < pointsFrom.size() && toCount < pointsTo.size()) {

            GpxPointDto currentPointTo = pointsTo.get(toCount);

            GpxPointDto currentPointFrom = pointsFrom.get(fromCount);

            long millisFrom = currentPointFrom.getDateTime().toInstant(ZoneOffset.ofHours(0)).getEpochSecond();
            long millisTo = currentPointTo.getDateTime().toInstant(ZoneOffset.ofHours(0)).getEpochSecond();
            long millisDifference = millisFrom - millisTo;

            if (millisDifference > 5) {
                toCount++;
            } else if (millisDifference >= 0) {
                if (currentPointFrom.getHeartRate() != null) {
                    currentPointTo.setHeartRate(currentPointFrom.getHeartRate());
                }
                fromCount++;
            } else {
                fromCount++;
            }

        }

    }

}
