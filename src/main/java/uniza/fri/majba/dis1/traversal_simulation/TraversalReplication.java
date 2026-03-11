package uniza.fri.majba.dis1.traversal_simulation;

import uniza.fri.majba.dis1.simulation_core.Replication;
import uniza.fri.majba.dis1.traversal_simulation.dto.ReplicationResult;
import uniza.fri.majba.dis1.traversal_simulation.dto.RouteParameters;
import uniza.fri.majba.dis1.traversal_simulation.dto.RouteResult;
import uniza.fri.majba.dis1.traversal_simulation.graph.Path;
import uniza.fri.majba.dis1.traversal_simulation.graph.TraversalGraph;
import uniza.fri.majba.dis1.ui.model.SimulationConfig;

import java.util.List;
import java.util.function.Consumer;

public final class TraversalReplication implements Replication {
    private List<RouteParameters> routes;
    private Consumer<ReplicationResult> onAfterReplication;

    @Override
    public void beforeAllReplications() {
        routes = TraversalGraph.buildRoutes();
    }

    @Override
    public void afterAllReplications() {
        for (RouteParameters route : routes) {
            double hours = route.weightedSumStatistic().calculateStatistic();
            // Convert hours (possibly fractional) to milliseconds to preserve sub-second precision
            long totalMillis = Math.round(hours * 3_600_000.0); // 1 hour = 3_600_000 ms

            long hrs = totalMillis / 3_600_000;
            long rem = totalMillis % 3_600_000;
            long mins = rem / 60_000;
            rem = rem % 60_000;
            long secs = rem / 1000;
            long millis = rem % 1000;

            // Format as H:MM:SS.mmm and also provide a human-readable breakdown
            String formatted = String.format("%d:%02d:%02d.%03d", hrs, mins, secs, millis);

            System.out.println("Route " + route.routeName() + " average time: " + formatted);
        }
    }

    @Override
    public void beforeReplication() {

    }

    @Override
    public void afterReplication() {
        if (onAfterReplication == null) {
            return;
        }

        List<RouteResult> routeResults = routes.stream()
                .map(r -> new RouteResult(
                        r.routeName(),
                        r.weightedSumStatistic().calculateStatistic()))
                .toList();
        onAfterReplication.accept(new ReplicationResult(routeResults));
    }

    public void setOnAfterReplication(Consumer<ReplicationResult> onAfterReplication) {
        this.onAfterReplication = onAfterReplication;
    }

    @Override
    public void execute() {
        for (RouteParameters route : routes) {
            double currentPathTime = SimulationConfig.getInstance().getSimulationStartTime();

            for (Path path : route.path()) {
                // Recursively evaluate all possible complete routes between the two cities
                // and pick the one with the minimum total traversal time
                double segmentTime = path.calculateBestCompleteTotalTime(currentPathTime);
                currentPathTime += segmentTime;
            }

            route.weightedSumStatistic().addValue(currentPathTime);
        }
    }
}
