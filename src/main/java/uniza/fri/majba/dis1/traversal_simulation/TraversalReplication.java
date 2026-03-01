package uniza.fri.majba.dis1.traversal_simulation;

import uniza.fri.majba.dis1.simulation_core.Replication;
import uniza.fri.majba.dis1.traversal_simulation.graph.Path;

import java.util.List;

public final class TraversalReplication implements Replication {
    private List<List<Path>> routes;

    @Override
    public void beforeAllReplications() {
        // TODO pridat do konfiguracie aplikacie -
        // 1. Kolko bodov chcem mat v grafe
        // 2. Kolko percent replikacii chcem vidiet
        List<List<Path>> routes = TraversalGraph.buildRoutes();
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
        for (List<Path> route : routes) {
            double departureTime = 0;

            for (Path path : route) {
                Path.PathOutput pathOutput = path.pickNextPath(departureTime);
                departureTime += pathOutput.time();
            }

        }
    }
}
