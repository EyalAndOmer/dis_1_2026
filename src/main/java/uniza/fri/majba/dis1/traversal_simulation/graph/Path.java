package uniza.fri.majba.dis1.traversal_simulation.graph;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Path {
    private final Node startingNode;
    private final List<Path> connectingPaths;
    private final List<Edge<Node>> pathEdges;

    public Path(Node startingNode, List<Path> connectingPaths, List<Edge<Node>> pathEdges) {
        this.startingNode = startingNode;
        this.connectingPaths = connectingPaths;
        this.pathEdges = pathEdges;
    }

    public static Builder from(Node startingNode) {
        return new Builder(startingNode);
    }

    /**
     * Picks the next path to traverse based on the traversal time of the edges in the path. The path with the lowest traversal time is chosen.
     * @param departureTime The simulation time (in hours) at which the courier departs onto the next path.
     * @return A PathOutput containing the chosen path and its traversal time.
     */
    public PathOutput pickNextPath(double departureTime) {
        double minimumTime = Double.MAX_VALUE;
        int minimumTimeIndex = 0;

        for (int i = 0; i < connectingPaths.size(); i++) {
            double traversalTime = pathEdges.get(i).getTraversalTime(departureTime);

            if (traversalTime < minimumTime) {
                minimumTime = traversalTime;
                minimumTimeIndex = i;
            }
        }

        return new PathOutput(minimumTime, connectingPaths.get(minimumTimeIndex));
    }

    public List<Path> getConnectingPaths() {
        return connectingPaths;
    }

    public Node getStartingNode() {
        return startingNode;
    }

    /**
     * Recursively calculates the total traversal time for all possible complete routes from this path
     * to a terminal node, and returns the minimum total time found.
     * <p>
     * Unlike {@link #pickNextPath(double)}, which only compares the first edge,
     * this method evaluates the entire path from the current node to the destination city.
     *
     * @param departureTime The simulation time (in hours) at which the courier departs from this node.
     * @return The minimum total traversal time (in hours) across all possible complete routes to the terminal.
     */
    public double calculateBestCompleteTotalTime(double departureTime) {
        if (connectingPaths.isEmpty()) {
            // Terminal node — no more edges to traverse
            return 0.0;
        }

        double bestTotalTime = Double.MAX_VALUE;

        for (int i = 0; i < connectingPaths.size(); i++) {
            double edgeTime = pathEdges.get(i).getTraversalTime(departureTime);
            double remainingTime = connectingPaths.get(i).calculateBestCompleteTotalTime(departureTime + edgeTime);
            double totalTime = edgeTime + remainingTime;

            if (totalTime < bestTotalTime) {
                bestTotalTime = totalTime;
            }
        }

        return bestTotalTime;
    }

    public record PathOutput(double time, Path path) {}

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Path path = (Path) o;
        return Objects.equals(startingNode, path.startingNode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startingNode, connectingPaths, pathEdges);
    }

    @Override
    public String toString() {
        return "Path{" +
                "startingNode=" + startingNode +
                ", connectingPaths=" + connectingPaths +
                ", pathEdges=" + pathEdges +
                '}';
    }

    public static class Builder {
        private final Node startingNode;
        private final List<Path> connectingPaths = new ArrayList<>();
        private final List<Edge<Node>> pathEdges = new ArrayList<>();

        private Builder(Node startingNode) {
            this.startingNode = startingNode;
        }

        public Builder nextPaths(List<Path> paths) {
            this.connectingPaths.addAll(paths);
            return this;
        }

        public Builder throughEdges(List<Edge<Node>> edges) {
            // Validate that all edges start or end at the starting node
            edges.forEach(edge -> {
                if (!edge.from().equals(startingNode) && !edge.to().equals(startingNode)) {
                    throw new IllegalArgumentException(
                            "Edge " + edge + " does not start or end at the starting node " + startingNode
                    );
                }
            });

            this.pathEdges.addAll(edges);
            return this;
        }

        public Path build() {
            Objects.requireNonNull(connectingPaths);
            Objects.requireNonNull(pathEdges);

            // Validate that all edges start or end at the starting node of their corresponding path
            for (int i = 0; i < connectingPaths.size(); i++) {
                Path path = connectingPaths.get(i);
                Edge<Node> edge = pathEdges.get(i);

                if (!edge.from().equals(path.startingNode) && !edge.to().equals(path.startingNode)) {
                    throw new IllegalStateException(
                            "Edge " + edge + " does not start or end at the path starting node of " + path
                    );
                }
            }
            return new Path(startingNode, List.copyOf(connectingPaths), List.copyOf(pathEdges));
        }
    }
}
