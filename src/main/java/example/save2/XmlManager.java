package example.save2;

import example.save2.dto.GpxPointDto;
import example.save2.exceptions.FileValidationException;
import example.save2.osmand.OsmandXmlProcessor;
import example.save2.strava.StravaXmlProcessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneOffset;
import java.util.List;

public class XmlManager {

    private final XmlProcessor inputXmlProcessor;

    private final XmlProcessor outputXmlProcessor;

    public XmlManager(String inputPathString, String outputPathString) {

        validateFile(inputPathString);
        validateFile(outputPathString);

        inputXmlProcessor = createXmlProcessor(inputPathString);
        outputXmlProcessor = createXmlProcessor(outputPathString);

    }

    public void copyHrValues() throws Exception {

        List<GpxPointDto> pointsFrom = inputXmlProcessor.readPoints();

        List<GpxPointDto> pointsTo = outputXmlProcessor.readPoints();

        mergeHeartRates(pointsTo, pointsFrom);

        outputXmlProcessor.mergePoints(pointsTo);

        outputXmlProcessor.savePoints();

    }

    private void mergeHeartRates(List<GpxPointDto> pointsTo, List<GpxPointDto> pointsFrom) {

        int fromCount = 0;
        int toCount = 0;

        while (fromCount < pointsFrom.size() && toCount < pointsTo.size()) {

            GpxPointDto currentPointTo = pointsTo.get(toCount);

            GpxPointDto currentPointFrom = pointsFrom.get(fromCount);

            if (currentPointFrom.getHeartRate() != null) {

                long millisFrom = currentPointFrom.getDateTime().toInstant(ZoneOffset.ofHours(0)).getEpochSecond();
                long millisTo = currentPointTo.getDateTime().toInstant(ZoneOffset.ofHours(0)).getEpochSecond();
                long millisDifference = millisFrom - millisTo;

                if (millisDifference > 3) {
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

    public void validateFile(String filePath) throws FileValidationException {

        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            throw new FileValidationException("File does not exist: " + filePath);
        }

        if (!Files.isRegularFile(path)) {
            throw new FileValidationException("File " + filePath + " is not a regular file");
        }

        try {

            if (Files.size(path) > 10 * 1024 * 1024) {
                throw new FileValidationException("File " + filePath + " is too big");
            }

            try (InputStream is = Files.newInputStream(path)) {
                byte[] header = new byte[5];
                if (is.read(header) != 5) {
                    throw new FileValidationException("Reading error");
                }
                if (!new String(header).equals("<?xml")) {
                    throw new FileValidationException("File is not XML-based");
                }
            }

        } catch (IOException e) {
            throw new FileValidationException("Reading error", e);
        }

    }

    public GpxSource findGpxSource(String filePath) {

        try {

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

                String line;
                int lineNumber = 0;

                while ((line = reader.readLine()) != null && lineNumber < 10) {

                    if (line.trim().isEmpty()) {
                        throw new FileValidationException("Empty line at " + lineNumber);
                    }

                    if (lineNumber == 0 && !line.startsWith("<?xml")) {
                        throw new FileValidationException("File is not XML");
                    }

                    if (line.contains("creator=\"StravaGPX")) {
                        return GpxSource.STRAVA;
                    }

                    if (line.contains("creator=\"OsmAnd")) {
                        return GpxSource.OSMAND;
                    }

                    lineNumber++;

                }

            }

            throw new FileValidationException("The parser could not determine the source of file " + filePath);

        } catch (IOException e) {
            throw new FileValidationException("Reading error", e);
        }

    }

    private XmlProcessor createXmlProcessor(String pathString) {

        GpxSource gpxSource = findGpxSource(pathString);

        XmlProcessor xmlProcessor;
        switch (gpxSource) {
            case GpxSource.STRAVA -> xmlProcessor = new StravaXmlProcessor(pathString);
            case GpxSource.OSMAND -> xmlProcessor = new OsmandXmlProcessor(pathString);
            default -> throw new FileValidationException("Не удалось установить тип файла");
        }

        return xmlProcessor;

    }

}
