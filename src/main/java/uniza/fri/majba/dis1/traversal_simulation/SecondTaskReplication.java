package uniza.fri.majba.dis1.traversal_simulation;

import uniza.fri.majba.dis1.simulation_core.Replication;
import uniza.fri.majba.dis1.traversal_simulation.graph.Path;
import uniza.fri.majba.dis1.ui.model.SimulationConfig;

import java.util.function.Consumer;

public class SecondTaskReplication implements Replication {

    private int monteCarloSamples;
    private double earliestDeparture;
    private double mustArriveBy;
    private double requiredOnTimePercentage;
    private double initialStep;
    private double precision;

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
        SimulationConfig cfg = SimulationConfig.getInstance();
        this.monteCarloSamples = cfg.getMonteCarloSamples();
        this.earliestDeparture = cfg.getEarliestDeparture();
        this.mustArriveBy = cfg.getMustArriveBy();
        this.requiredOnTimePercentage = cfg.getRequiredOnTimePercentage();
        this.initialStep = cfg.getInitialStep();
        this.precision = cfg.getPrecision();

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
        double oneSecondLater = latestDeparture + precision;

        System.out.printf("Latest departure time for %.0f%% on-time arrival by %s: %s%n",
                requiredOnTimePercentage * 100,
                formatTime(mustArriveBy),
                formatTime(latestDeparture));

        System.out.printf("  On-time at %s: %.4f%% (>= %.0f%% ✓)%n",
                formatTime(latestDeparture),
                lastAcceptedOnTimePercentage * 100,
                requiredOnTimePercentage * 100);

        System.out.printf("  On-time at %s (+1s): %.4f%% (< %.0f%% ✗)%n",
                formatTime(oneSecondLater),
                lastRejectedOnTimePercentage * 100,
                requiredOnTimePercentage * 100);

        if (onResultReady != null) {
            onResultReady.accept(new SecondTaskResult(
                    selectedRoute.routeName(),
                    formatTime(latestDeparture),
                    formatTime(mustArriveBy),
                    lastAcceptedOnTimePercentage,
                    lastRejectedOnTimePercentage,
                    formatTime(oneSecondLater),
                    requiredOnTimePercentage
            ));
        }
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
        double departureTime = earliestDeparture;
        double step = initialStep;

        lastAcceptedOnTimePercentage = estimateOnTimePercentage(departureTime);

        while (step >= precision) {
            double candidate = departureTime + step;
            double onTimePercentage = estimateOnTimePercentage(candidate);

            if (onTimePercentage >= requiredOnTimePercentage) {
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

        for (int i = 0; i < monteCarloSamples; i++) {
            double currentTime = departureTime;

            for (Path path : selectedRoute.path()) {
                currentTime += path.calculateBestCompleteTotalTime(currentTime);
            }

            if (currentTime < mustArriveBy) {
                onTimeCount++;
            }
        }

        return (double) onTimeCount / monteCarloSamples;
    }

    private static String formatTime(double hours) {
        long totalSeconds = Math.round(hours * 3600);
        return String.format("%d:%02d:%02d", totalSeconds / 3600, (totalSeconds % 3600) / 60, totalSeconds % 60);
    }
}
