package example.save2.fit;

import example.save2.cli.dto.CommandLineParameters;
import example.save2.file.FileUtils;
import example.save2.gpx.GpxProcessor;
import example.save2.gpx.GpxProcessorFactory;
import example.save2.gpx.dto.GpxPointDto;
import example.save2.gpx.elements.GpxElement;

import java.util.List;

public class DefaultFit2GpxManager implements Fit2GpxManager {

    private final String fitPathString;
    private final FitProcessor inputFitProcessor;
    private final String gpxPathString;
    private final GpxProcessor outputGpxProcessor;

    public DefaultFit2GpxManager(CommandLineParameters commandLineParameters) {

        FileUtils.validateFitFile(commandLineParameters.getFirstInputFilePathString());
        this.fitPathString = commandLineParameters.getFirstInputFilePathString();
        this.inputFitProcessor = FitProcessorFactory.createProcessor();
        this.gpxPathString = commandLineParameters.getOutputFilePathString();
        this.outputGpxProcessor = GpxProcessorFactory.createProcessor(commandLineParameters.isParallel());

    }

    public void convertFile() throws Exception {

        List<GpxPointDto> points = inputFitProcessor.readPoints(fitPathString);

        GpxElement gpxElement = outputGpxProcessor.createDefaultGpxElement(points);
        outputGpxProcessor.saveGpxElementIntoFile(gpxElement, gpxPathString);

    }

}
