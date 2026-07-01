package example.save2.gpx;

import example.save2.file.FileUtils;
import example.save2.gpx.dto.GpxPointDto;
import example.save2.gpx.elements.GpxElement;

import java.util.List;

public class RunnableWithPathString implements Runnable {

    private final String inputPathString;
    private String outputPathString;

    public RunnableWithPathString(String inputPathString, String outputPathString) {
        this.inputPathString = inputPathString;
        this.outputPathString = outputPathString;
    }

    @Override
    public void run() {

        FileUtils.validateFitFile(inputPathString);

        GpxProcessor gpxProcessor = new GpxProcessorImpl();
        GpxElement gpxElement = gpxProcessor.readGpxElement(inputPathString);
        List<GpxPointDto> gpxPoints;
        try {
            gpxPoints = gpxProcessor.readPoints(gpxElement);
        } catch (Exception e) {
            System.err.println("Error when reading GPX: " + e.getMessage());
            e.printStackTrace(System.out);
            return;
        }

        GpxSimplifier gpxSimplifier = new GpxSimplifier(gpxPoints);
        gpxSimplifier.simplifyPoints();
        gpxPoints = gpxSimplifier.getPoints();

        if (outputPathString == null) {
            outputPathString = inputPathString.replace(".gpx", "_simplified.gpx");
        }

        gpxElement = gpxProcessor.createDefaultGpxElement(gpxPoints);

        try {
            gpxProcessor.saveGpxElementIntoFile(gpxElement, outputPathString);
        } catch (Exception e) {
            System.err.println("Error when saving GPX: " + e.getMessage());
            e.printStackTrace(System.out);
            return;
        }

    }
}
