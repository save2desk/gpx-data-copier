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

            if (commandLineParameters.getOperation() == Operation.FIT2GPX) {

                FileUtils.validateFitFile(commandLineParameters.getFirstFilePathString());
                FitProcessor fitProcessor = FitProcessorFactory.createProcessor(commandLineParameters.getFirstFilePathString());

                GpxProcessor outputProcessor = GpxProcessorFactory.createProcessor(commandLineParameters);

                DefaultFit2GpxManager defaultFit2GpxManager = new DefaultFit2GpxManager(fitProcessor, outputProcessor);
                defaultFit2GpxManager.convertFile();

            } else if (commandLineParameters.getOperation() == Operation.HEARTRATE) {

                FileUtils.validateXmlFile(commandLineParameters.getFirstFilePathString());
                GpxProcessor inputProcessor = GpxProcessorFactory.createProcessor(commandLineParameters);

                FileUtils.validateXmlFile(commandLineParameters.getSecondFilePathString());
                GpxProcessor outputProcessor = GpxProcessorFactory.createProcessor(commandLineParameters);

                Gpx2GpxManager gpx2GpxManager = new Gpx2GpxManager(inputProcessor, outputProcessor);
                gpx2GpxManager.copyHrValues();

            } else if (commandLineParameters.getOperation() == Operation.SIMPLIFY) {

                FileUtils.validateXmlFile(commandLineParameters.getFirstFilePathString());
                GpxProcessor inputProcessor = GpxProcessorFactory.createProcessor(commandLineParameters);
                inputProcessor.simplifyGpx();

            }

        } catch (Exception e) {
            System.out.println(ExceptionUtils.stackTraceToString(e));
        }

    }

}
