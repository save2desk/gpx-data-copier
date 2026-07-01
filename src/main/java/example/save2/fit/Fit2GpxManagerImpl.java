package example.save2.fit;

import example.save2.cli.ParametersStorage;
import example.save2.cli.enums.ParameterKey;
import example.save2.file.FileUtils;
import example.save2.gpx.GpxProcessorImpl;
import example.save2.gpx.GpxProcessor;
import example.save2.gpx.dto.GpxPointDto;
import example.save2.gpx.elements.GpxElement;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static example.save2.file.FileUtils.readAllFilesInDirectory;

public class Fit2GpxManagerImpl implements Fit2GpxManager {

    private final String fitPathString;
    private final FitProcessor inputFitProcessor;
    private final String gpxPathString;
    private final GpxProcessor outputGpxProcessor;

    public Fit2GpxManagerImpl() {

        if (ParametersStorage.containsKey(ParameterKey.INPUT_FILE_FOLDER)) {
            fitPathString = ParametersStorage.getParameter(ParameterKey.INPUT_FILE_FOLDER, String.class);
        } else {
            fitPathString = ParametersStorage.getParameter(ParameterKey.FIRST_INPUT_FILE, String.class);
        }
        inputFitProcessor = new FitProcessorImpl();

        if (ParametersStorage.containsKey(ParameterKey.OUTPUT_FILE)) {
            gpxPathString = ParametersStorage.getParameter(ParameterKey.OUTPUT_FILE, String.class);
        } else {
            gpxPathString = null;
        }
        
        outputGpxProcessor = new GpxProcessorImpl();

    }

    public void convertFiles() throws Exception {

        List<String> fitPathList = (Files.isDirectory(Paths.get(fitPathString)))
                ? readAllFilesInDirectory(fitPathString, "*.fit")
                : Collections.singletonList(fitPathString);

        for (String pathString : fitPathList) {

            FileUtils.validateFitFile(pathString);
            List<GpxPointDto> points = inputFitProcessor.readPoints(pathString);

            GpxElement gpxElement = outputGpxProcessor.createDefaultGpxElement(points);
            String outputPathString = gpxPathString == null ? pathString.replace(".fit", ".gpx") : gpxPathString;
            outputGpxProcessor.saveGpxElementIntoFile(gpxElement, outputPathString);

        }
        
    }

}
