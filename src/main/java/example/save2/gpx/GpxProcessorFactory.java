package example.save2.gpx;

public class GpxProcessorFactory {

    private GpxProcessorFactory() {
        /* This utility class should not be instantiated */
    }

    public static GpxProcessor createProcessor(boolean isParallel) {

        if (isParallel) {
            return new ParallelGpxProcessorImpl(4, 1);
        } else {
            return new DefaultGpxProcessorImpl();
        }

    }

}
