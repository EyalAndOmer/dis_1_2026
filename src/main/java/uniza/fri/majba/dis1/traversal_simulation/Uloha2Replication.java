package uniza.fri.majba.dis1.traversal_simulation;

import java.util.List;

import uniza.fri.majba.dis1.simulation_core.Replication;
import uniza.fri.majba.dis1.simulation_core.statistics.WeightedSumStatistic;

public class Uloha2Replication implements Replication {
    private List<RouteParameters> routes = TraversalGraph.buildRoutes().stream().map(route -> new RouteParameters(route, new WeightedSumStatistic())).toList();


    @Override
    public void beforeAllReplications() {

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
        RouteParameters bestRoute = routes.get(1);
//        double currentstartTime = SIMULATION_START_TIME;
    }
}
