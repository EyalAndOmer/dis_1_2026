package uniza.fri.majba.dis1.traversal_simulation.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Path {
    private final Node startingNode;
    private final List<Path> connectingPaths;
    private final List<Edge<Node>> pathEdges;
    private final Node endingNode;

    public Path(Node startingNode, List<Path> connectingPaths, List<Edge<Node>> pathEdges, Node endingNode) {
        this.startingNode = startingNode;
        this.connectingPaths = connectingPaths;
        this.pathEdges = pathEdges;
        this.endingNode = endingNode;
    }

    public static Builder from(Node startingNode) {
        return new Builder(startingNode);
    }

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

        System.out.println(pathEdges.get(minimumTimeIndex));

        return new PathOutput(minimumTime, connectingPaths.get(minimumTimeIndex));
    }

    public List<Path> getConnectingPaths() {
        return connectingPaths;
    }

    public record PathOutput(double time, Path path) {}

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Path path = (Path) o;
        return Objects.equals(startingNode, path.startingNode) && Objects.equals(endingNode, path.endingNode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startingNode, connectingPaths, pathEdges, endingNode);
    }

    public static class Builder {
        private final Node startingNode;
        private final List<Path> connectingPaths = new ArrayList<>();
        private final List<Edge<Node>> pathEdges = new ArrayList<>();
        private Node endingNode;

        private Builder(Node startingNode) {
            this.startingNode = startingNode;
        }

        public Builder nextPaths(List<Path> paths) {
            this.connectingPaths.addAll(paths);
            return this;
        }

        public Builder throughEdges(List<Edge<Node>> edges) {
            this.pathEdges.addAll(edges);
            return this;
        }

        public Builder to(Node endingNode) {
            this.endingNode = endingNode;
            return this;
        }

        public Path build() {
            return new Path(startingNode, List.copyOf(connectingPaths), List.copyOf(pathEdges), endingNode);
        }
    }
}
