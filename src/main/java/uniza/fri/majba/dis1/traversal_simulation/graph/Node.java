package uniza.fri.majba.dis1.traversal_simulation.graph;

import java.util.Objects;

/**
 * @param name The name of the node. Must be unique across the graph.
 * @param type The type of the node
 */
public record Node(String name, NodeType type) {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(name, node.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
