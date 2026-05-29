package example.save2;

import example.save2.cli.CommandLineReader;
import example.save2.cli.dto.CommandLineParameters;
import example.save2.cli.enums.Operation;
import example.save2.exceptions.ExceptionUtils;
import example.save2.file.FileUtils;
import example.save2.fit.DefaultFit2GpxManager;
import example.save2.gpx.Gpx2GpxManager;
import example.save2.gpx.GpxProcessor;
import example.save2.gpx.GpxProcessorFactory;

public class MainApplication {

    public static void main(String[] args) {

        try {

            CommandLineParameters commandLineParameters = CommandLineReader.readParameters(args);

            if (commandLineParameters.getOperation() == Operation.FIT2GPX) {

                DefaultFit2GpxManager defaultFit2GpxManager = new DefaultFit2GpxManager(commandLineParameters);
                defaultFit2GpxManager.convertFile();

            } else if (commandLineParameters.getOperation() == Operation.HEARTRATE) {

                Gpx2GpxManager gpx2GpxManager = new Gpx2GpxManager(commandLineParameters);
                gpx2GpxManager.copyHrValues();

            } else if (commandLineParameters.getOperation() == Operation.SIMPLIFY) {

                FileUtils.validateXmlFile(commandLineParameters.getFirstInputFilePathString());
                GpxProcessor inputProcessor = GpxProcessorFactory.createProcessor(commandLineParameters.isParallel());
                inputProcessor.simplifyGpx(commandLineParameters.getFirstInputFilePathString());

            }

        } catch (Exception e) {
            System.out.println(ExceptionUtils.stackTraceToString(e));
        }

    }

}
