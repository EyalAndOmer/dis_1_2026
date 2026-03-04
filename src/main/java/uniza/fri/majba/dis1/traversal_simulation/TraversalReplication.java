package uniza.fri.majba.dis1.traversal_simulation;

import uniza.fri.majba.dis1.simulation_core.Replication;
import uniza.fri.majba.dis1.simulation_core.statistics.WeightedSumStatistic;
import uniza.fri.majba.dis1.traversal_simulation.graph.Path;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

public final class TraversalReplication implements Replication {
    private List<RouteParameters> routes;
    // Start at 6:00 at the morning
    public final int SIMULATION_START_TIME = 6;

    // Uloha 2 - v 80% pripadoch chce byt v Ziline uz 7:35. Kedy, v akom case, ma zacat obsluhovat ?
    // Replikacie - zaciatok o 6:00, zbehni, zisti kolko trvalo 80% hodnot (sort, zober 80%). ODcitaj tu hodnotu od 7:35 a ides znova
    // Presnost na sekundy
    // Ziadny novy kod, simulacia rovnaka
    // Bezi to len nad najlepsou trasou

    @Override
    public void beforeAllReplications() {
        // TODO pridat do konfiguracie aplikacie -
        // TODO kolony sa tvoria po 6:25
        // 1. Kolko bodov chcem mat v grafe
        // 2. Kolko percent replikacii chcem vidiet
        routes = TraversalGraph.buildRoutes().stream().map(route -> new RouteParameters(route, new WeightedSumStatistic())).toList();
    }

    @Override
    public void afterAllReplications() {
        for (int i = 0; i < routes.size(); i++) {
            RouteParameters route = routes.get(i);
            long totalSeconds = Math.round(route.weightedSumStatistic().calculateStatistic() * 3600);
            Duration d = Duration.ofSeconds(totalSeconds);
            System.out.println("Route " + i + " with time: " + d.minus(Duration.ofHours(6)));
        }
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
