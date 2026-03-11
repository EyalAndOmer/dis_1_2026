package uniza.fri.majba.dis1.traversal_simulation;

import uniza.fri.majba.dis1.simulation_core.Replication;
import uniza.fri.majba.dis1.traversal_simulation.graph.Path;
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
