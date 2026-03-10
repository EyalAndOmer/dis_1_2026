package uniza.fri.majba.dis1.traversal_simulation;

import uniza.fri.majba.dis1.simulation_core.Replication;
import uniza.fri.majba.dis1.traversal_simulation.graph.Path;

public class SecondTaskReplication implements Replication {

    private static final int MONTE_CARLO_SAMPLES = 100_000;

    private static final double EARLIEST_DEPARTURE = 6.0;
    private static final double MUST_ARRIVE_BY = 7 + 35.0 / 60;
    private static final double REQUIRED_ON_TIME_PERCENTAGE = 0.80;
    private static final double INITIAL_STEP = 2.0;              // 1 hour
    private static final double PRECISION = 1.0 / 3600;          // 1 second

    private RouteParameters selectedRoute;

    /** On-time percentage at the last accepted departure time (from the search). */
    private double lastAcceptedOnTimePercentage;
    /** On-time percentage at the first rejected candidate (departure + 1 step that failed). */
    private double lastRejectedOnTimePercentage;

    @Override
    public void beforeAllReplications() {
        // TODO refactor - combo box from view
        this.selectedRoute = TraversalGraph.buildRoutes().get(3);
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
                REQUIRED_ON_TIME_PERCENTAGE * 100,
                formatTime(MUST_ARRIVE_BY),
                formatTime(latestDeparture));

        System.out.printf("  On-time at %s: %.4f%% (>= %.0f%% ✓)%n",
                formatTime(latestDeparture),
                lastAcceptedOnTimePercentage * 100,
                REQUIRED_ON_TIME_PERCENTAGE * 100);

        System.out.printf("  On-time at %s (+1s): %.4f%% (< %.0f%% ✗)%n",
                formatTime(oneSecondLater),
                lastRejectedOnTimePercentage * 100,
                REQUIRED_ON_TIME_PERCENTAGE * 100);
    }

    /**
     * Finds the latest departure time starting from {@link #EARLIEST_DEPARTURE}
     * that still guarantees at least
     * {@link #REQUIRED_ON_TIME_PERCENTAGE} probability of arriving before
     * {@link #MUST_ARRIVE_BY}.
     * <p>
     * The departure time is gradually increased from {@link #EARLIEST_DEPARTURE}
     * with a decreasing step. When a candidate time fails the on-time check,
     * the algorithm steps back and reduces the step size, refining the result
     * until it reaches {@link #PRECISION} (1 second).
     * <p>
     * After the search, {@link #lastAcceptedOnTimePercentage} holds the on-time
     * percentage at the returned departure time, and {@link #lastRejectedOnTimePercentage}
     * holds the percentage at the first departure time that was rejected (1 step later).
     */
    private double findLatestDepartureTime() {
        double departureTime = EARLIEST_DEPARTURE;
        double step = INITIAL_STEP;

        lastAcceptedOnTimePercentage = estimateOnTimePercentage(departureTime);

        while (step >= PRECISION) {
            double candidate = departureTime + step;
            double onTimePercentage = estimateOnTimePercentage(candidate);

            if (onTimePercentage >= REQUIRED_ON_TIME_PERCENTAGE) {
                departureTime = candidate;
                lastAcceptedOnTimePercentage = onTimePercentage;
            } else {
                lastRejectedOnTimePercentage = onTimePercentage;
                step /= 2.0;
            }
        }

        return departureTime;
    }

    /**
     * Runs {@link #MONTE_CARLO_SAMPLES} random trips departing at
     * {@code departureTime} and returns the fraction that arrive
     * before {@link #MUST_ARRIVE_BY}.
     */
    private double estimateOnTimePercentage(double departureTime) {
        int onTimeCount = 0;

        for (int i = 0; i < MONTE_CARLO_SAMPLES; i++) {
            double currentTime = departureTime;

            for (Path path : selectedRoute.path()) {
                currentTime += path.calculateBestCompleteTotalTime(currentTime);
            }

            if (currentTime < MUST_ARRIVE_BY) {
                onTimeCount++;
            }
        }

        return (double) onTimeCount / MONTE_CARLO_SAMPLES;
    }

    private static String formatTime(double hours) {
        long totalSeconds = Math.round(hours * 3600);
        return String.format("%d:%02d:%02d", totalSeconds / 3600, (totalSeconds % 3600) / 60, totalSeconds % 60);
    }
}
