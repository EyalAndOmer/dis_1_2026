package uniza.fri.majba.dis1.traversal_simulation;

import uniza.fri.majba.dis1.simulation_core.Replication;
import uniza.fri.majba.dis1.traversal_simulation.graph.Path;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public final class TraversalReplication implements Replication {
    private List<RouteParameters> routes;
    // Start at 6:00 at the morning
    public final double SIMULATION_START_TIME = 6.00;

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
    }

    @Override
    public void execute() {
        for (RouteParameters route : routes) {
            double currentPathTime = SIMULATION_START_TIME;

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
