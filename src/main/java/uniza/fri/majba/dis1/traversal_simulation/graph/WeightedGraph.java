package uniza.fri.majba.dis1.traversal_simulation.graph;

import uniza.fri.majba.dis1.simulation_core.generators.Generator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class WeightedGraph<V> {
    public static final String BEGINNING_NODE_NAME = "Žilina";
    private final Map<V, List<Edge<V>>> nodes = new HashMap<>();

    /**
     * Adds a node to the graph. If the node already exists, it does nothing.
     * @param node The node to be added to the graph.
     */
    public void addNode(V node) {
        nodes.computeIfAbsent(node, k -> List.of());
    }

    /**
     * Adds an undirected edge between the nodes `from` and `to` with the specified edge color and weight.
     * @param from The start node of the edge.
     * @param to The end node of the edge.
     * @param edgeColor The color of the edge - represents the average speed on the edge.
     * @param weight The weight of the edge - represents the length of the edge
     * @param generator The generator that generates the traversal average speed on this edge.
     */
    public void addUndirectedEdge(V from, V to, EdgeColor edgeColor, int weight, Generator generator) {
        addDirectedEdge(from, to, edgeColor, weight, generator);
        addDirectedEdge(to, from, edgeColor, weight, generator);
    }

    /**
     * Adds a directed edge from the node `from` to the node `to` with the specified edge color and weight.
     * @param from The start node of the edge.
     * @param to The end node of the edge.
     * @param edgeColor The color of the edge - represents the average speed on the edge.
     * @param weight The weight of the edge - represents the length of the edge
     * @param generator The generator that generates the traversal average speed on this edge.
     */
    public void addDirectedEdge(V from, V to, EdgeColor edgeColor, int weight, Generator generator) {
        if (!nodes.containsKey(from) || !nodes.containsKey(to)) {
            throw new IllegalArgumentException("Both nodes must be present in the graph.");
        }

        nodes.computeIfPresent(from, (k, v) -> {
            v.add(new Edge<>(from, to, edgeColor, weight, generator));
            return v;
        });
    }
}
