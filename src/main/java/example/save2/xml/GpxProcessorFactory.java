package example.save2.xml;

public class GpxProcessorFactory {

    private GpxProcessorFactory() {
        /* This utility class should not be instantiated */
    }

    public static GpxProcessor createProcessor(String pathString) {

        return new DefaultGpxProcessor(pathString);

    }

}
