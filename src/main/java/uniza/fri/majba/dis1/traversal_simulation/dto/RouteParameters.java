package uniza.fri.majba.dis1.traversal_simulation.dto;

import uniza.fri.majba.dis1.simulation_core.statistics.WeightedSumStatistic;
import uniza.fri.majba.dis1.traversal_simulation.graph.Path;

import java.util.List;

public record RouteParameters(List<Path> path, WeightedSumStatistic weightedSumStatistic, String routeName) {
}
