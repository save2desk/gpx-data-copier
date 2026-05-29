package example.save2.fit;

public class FitProcessorFactory {

    private FitProcessorFactory() {
        /* This utility class should not be instantiated */
    }

    public static FitProcessor createProcessor() {

        return new DefaultFitProcessor();

    }

}
