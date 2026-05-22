package example.save2.xml;

import example.save2.xml.dto.GpxPointDto;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ParallelGpxProcessorImpl extends DefaultGpxProcessorImpl {

    private final int threads;
    private final int iterations;

    public ParallelGpxProcessorImpl(String pathString, int threads, int iterations) {
        super(pathString);
        this.threads = threads;
        this.iterations = iterations;
    }

    @Override
    public void simplifyGpx() throws Exception {

        List<GpxPointDto> gpxPoints = readPoints();

        for (int i = 0; i < iterations; i++) {
            gpxPoints = simplifyGpxIteration(gpxPoints);
        }

        pathString = pathString.replace(".gpx", "_simplified.gpx");

        gpxElement = createDefaultGpxElement(gpxPoints);

        saveGpxElementIntoFile();

    }

    protected List<GpxPointDto> simplifyGpxIteration(List<GpxPointDto> gpxPoints) throws Exception {

        List<Integer> pointsForEachThread = getPointsForEachThread(gpxPoints.size());

        ConcurrentHashMap<Integer, List<GpxPointDto>> simplifiedPointsMap = new ConcurrentHashMap<>();

        AtomicInteger currentPart = new AtomicInteger(0);

        AtomicInteger nextPartStartPoint = new AtomicInteger(0);

        try (ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threads)) {

            Callable<FutureResult> callable = () -> {
                int threadStartPoint = nextPartStartPoint.get();
                int currentPartLocal = currentPart.getAndIncrement();
                int threadEndPoint = threadStartPoint + pointsForEachThread.get(currentPartLocal);
                nextPartStartPoint.set(threadEndPoint);
                List<GpxPointDto> subList = new ArrayList<>(gpxPoints.subList(threadStartPoint, threadEndPoint));

                GpxSimplifier gpxSimplifier = new GpxSimplifier(subList);
                gpxSimplifier.simplifyPoints();
                List<GpxPointDto> simplifiedPoints = gpxSimplifier.getPoints();
                
                Thread.sleep(1000);
                return new FutureResult(currentPartLocal, simplifiedPoints);
            };

            List<Future<FutureResult>> futures = new LinkedList<>();

            boolean finished = false;

            do {
                if (executor.getActiveCount() < threads && currentPart.get() < pointsForEachThread.size()) {
                    futures.add(executor.submit(callable));
                }
                for (Iterator<Future<FutureResult>> iterator = futures.iterator(); iterator.hasNext(); ) {
                    Future<FutureResult> future = iterator.next();
                    if (future.isDone()) {
                        simplifiedPointsMap.put(future.get().order, future.get().points);
                        iterator.remove();
                        if (simplifiedPointsMap.size() == pointsForEachThread.size()) {
                            finished = true;
                            break;
                        }
                    }
                }
                Thread.sleep(100);
            } while (!finished);

        }

        return mergeResultMap(simplifiedPointsMap);

    }

    private List<Integer> getPointsForEachThread(int pointsCount) {

        List<Integer> result = new ArrayList<>();
        int pointsForThread = pointsCount / (pointsCount / 500 + 1) + 1;
        while (pointsCount > pointsForThread) {
            result.add(pointsForThread);
            pointsCount -= pointsForThread;
        }
        result.add(pointsCount);

        return result;
    }

    private List<GpxPointDto> mergeResultMap(ConcurrentHashMap<Integer, List<GpxPointDto>> groupedPoints) {
        List<GpxPointDto> result = new ArrayList<>();
        for (int i = 0; i < groupedPoints.size(); i++) {
            result.addAll(groupedPoints.get(i));
        }
        return result;
    }

    private record FutureResult(int order, List<GpxPointDto> points) {
    }

}
