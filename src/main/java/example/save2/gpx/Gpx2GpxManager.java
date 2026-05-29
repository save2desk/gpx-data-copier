package example.save2.gpx;

import example.save2.cli.dto.CommandLineParameters;
import example.save2.file.FileUtils;
import example.save2.gpx.dto.GpxPointDto;
import example.save2.gpx.elements.GpxElement;

import java.time.ZoneOffset;
import java.util.List;

public class Gpx2GpxManager {

    private final GpxProcessor heartrateGpxProcessor;
    private final String heartrateGpxPath;

    private final GpxProcessor gpsGpxProcessor;
    private final String gpsGpxPath;

    private final String outputGpxPath;

    public Gpx2GpxManager(CommandLineParameters commandLineParameters) {

        heartrateGpxPath = commandLineParameters.getFirstInputFilePathString();
        FileUtils.validateXmlFile(heartrateGpxPath);
        heartrateGpxProcessor = GpxProcessorFactory.createProcessor(commandLineParameters.isParallel());

        gpsGpxPath = commandLineParameters.getSecondInputFilePathString();
        FileUtils.validateXmlFile(gpsGpxPath);
        gpsGpxProcessor = GpxProcessorFactory.createProcessor(commandLineParameters.isParallel());

        outputGpxPath = commandLineParameters.getOutputFilePathString();

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
