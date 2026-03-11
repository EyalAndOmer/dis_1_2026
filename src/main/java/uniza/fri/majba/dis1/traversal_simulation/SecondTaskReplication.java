package uniza.fri.majba.dis1.traversal_simulation;

import uniza.fri.majba.dis1.simulation_core.Replication;
import uniza.fri.majba.dis1.traversal_simulation.dto.RouteParameters;
import uniza.fri.majba.dis1.traversal_simulation.graph.Path;
import uniza.fri.majba.dis1.traversal_simulation.graph.TraversalGraph;

import java.util.function.Consumer;

public class SecondTaskReplication implements Replication {

    public static final double EARLIEST_DEPARTURE       = 6.0;
    public static final double MUST_ARRIVE_BY           = 7.0 + 35.0 / 60.0; // 7:35
    public static final double REQUIRED_VALID_PERCENTAGE = 0.80;
    public static final double INITIAL_STEP             = 2.0;               // 2 hours
    public static final double PRECISION                = 1.0 / 3600.0;      // 1 second

    private RouteParameters selectedRoute;
    private int selectedRouteIndex = 3;

    /** On-time percentage at the last accepted departure time (from the search). */
    private double lastAcceptedOnTimePercentage;
    /** On-time percentage at the first rejected candidate (departure + 1 step that failed). */
    private double lastRejectedOnTimePercentage;

    /** Callback to publish the result to the UI. */
    private Consumer<SecondTaskResult> onResultReady;

    public void setSelectedRouteIndex(int index) {
        this.selectedRouteIndex = index;
    }

    public void setOnResultReady(Consumer<SecondTaskResult> onResultReady) {
        this.onResultReady = onResultReady;
    }

    @Override
    public void beforeAllReplications() {

        this.selectedRoute = TraversalGraph.buildRoutes().get(selectedRouteIndex);
    }

    @Override
    public void afterAllReplications() {
    }

    @Override
    public void beforeReplication() {
    }

    @Override
    public void afterReplication() {
    }

    @Override
    public void execute() {
        double latestDeparture = findLatestDepartureTime();
        double oneSecondLater = latestDeparture + PRECISION;

        System.out.printf("Latest departure time for %.0f%% on-time arrival by %s: %s%n",
                REQUIRED_VALID_PERCENTAGE * 100,
                formatTime(MUST_ARRIVE_BY),
                formatTime(latestDeparture));

        System.out.printf("  On-time at %s: %.4f%% %n",
                formatTime(latestDeparture),
                lastAcceptedOnTimePercentage * 100);

        System.out.printf("  On-time at %s (+1s): %.4f%%%n",
                formatTime(oneSecondLater),
                lastRejectedOnTimePercentage * 100);

        if (onResultReady == null) {
            return;
        }

        onResultReady.accept(new SecondTaskResult(
                selectedRoute.routeName(),
                formatTime(latestDeparture),
                formatTime(MUST_ARRIVE_BY),
                lastAcceptedOnTimePercentage,
                lastRejectedOnTimePercentage,
                formatTime(oneSecondLater),
                REQUIRED_VALID_PERCENTAGE
        ));
    }

    private double findLatestDepartureTime() {
        double departureTime = EARLIEST_DEPARTURE;
        double step = INITIAL_STEP;

        lastAcceptedOnTimePercentage = estimateOnTimePercentage(departureTime);

        while (step >= PRECISION) {
            double candidate = departureTime + step;
            double onTimePercentage = estimateOnTimePercentage(candidate);

            if (onTimePercentage >= REQUIRED_VALID_PERCENTAGE) {
                departureTime = candidate;
                lastAcceptedOnTimePercentage = onTimePercentage;
            } else {
                lastRejectedOnTimePercentage = onTimePercentage;
                step /= 2.0;
            }
        }

        return departureTime;
    }

    /** Number of Monte Carlo samples used per binary-search step.
     *  Kept well below getDefaultReplications() to keep the search tractable:
     *  the binary search calls this method O(log2(INITIAL_STEP / PRECISION)) times,
     *  so using 10M here would mean hundreds of billions of path evaluations. */
    private static final int ESTIMATION_SAMPLES = 100_000;

    private double estimateOnTimePercentage(double departureTime) {
        int onTimeCount = 0;

        for (int i = 0; i < ESTIMATION_SAMPLES; i++) {
            double currentTime = departureTime;

            for (Path path : selectedRoute.path()) {
                currentTime += path.calculateBestCompleteTotalTime(currentTime);
            }

            if (currentTime < MUST_ARRIVE_BY) {
                onTimeCount++;
            }
        }

        return (double) onTimeCount / ESTIMATION_SAMPLES;
    }

    private static String formatTime(double hours) {
        long totalSeconds = Math.round(hours * 3600);
        return String.format("%d:%02d:%02d", totalSeconds / 3600, (totalSeconds % 3600) / 60, totalSeconds % 60);
    }
}
