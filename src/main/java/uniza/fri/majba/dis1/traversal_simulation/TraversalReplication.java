package uniza.fri.majba.dis1.traversal_simulation;

import uniza.fri.majba.dis1.simulation_core.Replication;
import uniza.fri.majba.dis1.simulation_core.statistics.WeightedSumStatistic;
import uniza.fri.majba.dis1.traversal_simulation.graph.Path;

import java.util.List;
import java.util.Objects;

public final class TraversalReplication implements Replication {
    private List<RouteParameters> routes;
    // Start at 6:00 at the morning
    public final int SIMULATION_START_TIME = 6;

    @Override
    public void beforeAllReplications() {
        // TODO pridat do konfiguracie aplikacie -
        // 1. Kolko bodov chcem mat v grafe
        // 2. Kolko percent replikacii chcem vidiet
        routes = TraversalGraph.buildRoutes().stream().map(route -> new RouteParameters(route, new WeightedSumStatistic())).toList();
    }

    @Override
    public void afterAllReplications() {
        double minTime = Double.MAX_VALUE;
        int minTimeRouteIndex = -1;
        for (int i = 0; i < routes.size(); i++) {
            RouteParameters route = routes.get(i);
            if (route.weightedSumStatistic().calculateStatistic() < minTime) {
                minTime = route.weightedSumStatistic().calculateStatistic();
                minTimeRouteIndex = i;
            }
        }

        System.out.println("Best route is " + (minTimeRouteIndex + 1) + " with time: " + minTime);
    }

    @Override
    public void beforeReplication() {

    }

    @Override
    public void afterReplication() {
    }

    @Override
    public void execute() {
        for (RouteParameters route : routes) {
            double currentPathTime = SIMULATION_START_TIME;

            for (Path path : route.path()) {
                Path.PathOutput pathOutput = path.pickNextPath(currentPathTime);
                currentPathTime += pathOutput.time();

                // Traverse through the paths until we reach the end of the route (i.e., a path with no connecting paths).
                while (!pathOutput.path().getConnectingPaths().isEmpty()) {
                    pathOutput = pathOutput.path().pickNextPath(currentPathTime);
                    currentPathTime += pathOutput.time();
                }
            }

            route.weightedSumStatistic().addValue(currentPathTime);
            System.out.println("====================================================");
        }
    }
}
