package example.save2;

import example.save2.cli.CommandLineReader;
import example.save2.cli.ParametersStorage;
import example.save2.cli.enums.Operation;
import example.save2.cli.enums.ParameterKey;
import example.save2.exceptions.ExceptionUtils;
import example.save2.fit.Fit2GpxManagerImpl;
import example.save2.gpx.manager.GpxHeartRateManager;
import example.save2.gpx.manager.GpxSimplifyManager;

public class MainApplication {

    @SuppressWarnings("UnnecessaryModifier")
    public static void main(String[] args) {

        try {

            CommandLineReader.readParametersIntoStorage(args);

            Operation operation = ParametersStorage.getParameter(ParameterKey.OPERATION, Operation.class);
            if (operation == Operation.FIT2GPX) {
                var fit2GpxManagerImpl = new Fit2GpxManagerImpl();
                fit2GpxManagerImpl.convertFiles();
            } else if (operation == Operation.HEARTRATE) {
                var gpxHeartRateManager = new GpxHeartRateManager();
                gpxHeartRateManager.copyHrValues();
            } else if (operation == Operation.SIMPLIFY) {
                var gpxSimplifyManager = new GpxSimplifyManager();
                gpxSimplifyManager.simplifyGpx();
            }

        } catch (Exception e) {
            System.out.println(ExceptionUtils.stackTraceToString(e));
        }

    }

}
