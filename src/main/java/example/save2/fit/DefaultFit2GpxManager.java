package example.save2.fit;

import example.save2.xml.GpxProcessor;
import example.save2.xml.dto.GpxPointDto;

import java.util.List;

public class DefaultFit2GpxManager implements Fit2GpxManager {

    private final FitProcessor inputFitProcessor;
    private final GpxProcessor outputGpxProcessor;

    public DefaultFit2GpxManager(FitProcessor inputFitProcessor, GpxProcessor outputGpxProcessor) {
        this.inputFitProcessor = inputFitProcessor;
        this.outputGpxProcessor = outputGpxProcessor;
    }

    public void convertFile() throws Exception {

        List<GpxPointDto> points = inputFitProcessor.readPoints();

        outputGpxProcessor.createDefaultGpxElement(points);
        outputGpxProcessor.saveGpxElementIntoFile();

    }

}
