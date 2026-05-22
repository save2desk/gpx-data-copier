package example.save2.xml;

import example.save2.cli.dto.CommandLineParameters;

public class GpxProcessorFactory {

    private GpxProcessorFactory() {
        /* This utility class should not be instantiated */
    }

    public static GpxProcessor createProcessor(CommandLineParameters cmdParameters) {

        if (cmdParameters.isParallel()) {
            return new ParallelGpxProcessorImpl(cmdParameters.getFirstFilePathString(), 4, 1);
        } else {
            return new DefaultGpxProcessorImpl(cmdParameters.getFirstFilePathString());
        }

    }

}
