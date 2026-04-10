package example.save2;

import example.save2.cli.CommandLineReader;
import example.save2.cli.dto.CommandLineParameters;
import example.save2.cli.enums.Operation;
import example.save2.exceptions.ExceptionUtils;
import example.save2.file.FileUtils;
import example.save2.fit.DefaultFit2GpxManager;
import example.save2.fit.FitProcessor;
import example.save2.fit.FitProcessorFactory;
import example.save2.xml.Gpx2GpxManager;
import example.save2.xml.GpxProcessor;
import example.save2.xml.GpxProcessorFactory;

public class MainApplication {

    public static void main(String[] args) {

        try {

            CommandLineParameters commandLineParameters = CommandLineReader.readParameters(args);

            if (commandLineParameters.operation == Operation.FIT2GPX) {

                FileUtils.validateFitFile(commandLineParameters.inputPathString);
                FitProcessor fitProcessor = FitProcessorFactory.createProcessor(commandLineParameters.inputPathString);

                GpxProcessor outputProcessor = GpxProcessorFactory.createProcessor(commandLineParameters.outputPathString);

                DefaultFit2GpxManager defaultFit2GpxManager = new DefaultFit2GpxManager(fitProcessor, outputProcessor);
                defaultFit2GpxManager.convertFile();

            } else if (commandLineParameters.operation == Operation.HEARTRATE) {

                FileUtils.validateXmlFile(commandLineParameters.inputPathString);
                GpxProcessor inputProcessor = GpxProcessorFactory.createProcessor(commandLineParameters.inputPathString);

                FileUtils.validateXmlFile(commandLineParameters.outputPathString);
                GpxProcessor outputProcessor = GpxProcessorFactory.createProcessor(commandLineParameters.outputPathString);

                Gpx2GpxManager gpx2GpxManager = new Gpx2GpxManager(inputProcessor, outputProcessor);
                gpx2GpxManager.copyHrValues();

            }

        } catch (Exception e) {
            System.out.println(ExceptionUtils.stackTraceToString(e));
        }

    }

}
