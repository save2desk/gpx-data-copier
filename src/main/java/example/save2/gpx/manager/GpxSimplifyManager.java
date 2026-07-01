package example.save2.gpx.manager;

import example.save2.cli.ParametersStorage;
import example.save2.cli.enums.ParameterKey;
import example.save2.gpx.RunnableWithPathString;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static example.save2.file.FileUtils.readAllFilesInDirectory;

public class GpxSimplifyManager {

    private final String inputFullGpxPathString;
    private final String outputSimpleGpxPathString;
    private final Integer threads;

    public GpxSimplifyManager() {

        if (ParametersStorage.containsKey(ParameterKey.INPUT_FILE_FOLDER)) {
            inputFullGpxPathString = ParametersStorage.getParameter(ParameterKey.INPUT_FILE_FOLDER, String.class);
            outputSimpleGpxPathString = null;
        } else {
            inputFullGpxPathString = ParametersStorage.getParameter(ParameterKey.FIRST_INPUT_FILE, String.class);
            outputSimpleGpxPathString = ParametersStorage.getParameter(ParameterKey.OUTPUT_FILE, String.class);
        }

        threads = ParametersStorage.getParameter(ParameterKey.PARALLEL_THREADS, Integer.class);

    }

    public void simplifyGpx() throws Exception {

        List<String> fullGpxPathList = (Files.isDirectory(Paths.get(inputFullGpxPathString)))
                ? readAllFilesInDirectory(inputFullGpxPathString, "*.gpx")
                : Collections.singletonList(inputFullGpxPathString);

        try (ThreadPoolExecutor executor = new ThreadPoolExecutor(
                threads, threads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(threads * 3))) {

            for (String inputPathString : fullGpxPathList) {

                RunnableWithPathString runnable = new RunnableWithPathString(inputPathString, outputSimpleGpxPathString);
                executor.submit(runnable);

            }

            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        }

    }

}
