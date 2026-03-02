package uniza.fri.majba.dis1.traversal_simulation;

import uniza.fri.majba.dis1.simulation_core.statistics.WeightedSumStatistic;
import uniza.fri.majba.dis1.traversal_simulation.graph.Path;

import java.util.List;

// TODO add thread support and connect to model
public record RouteParameters(List<Path> path, WeightedSumStatistic weightedSumStatistic) {
}
