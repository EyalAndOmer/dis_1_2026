package uniza.fri.majba.dis1.traversal_simulation.graph;

import java.util.List;

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

    public PathOutput pickNextPath(double departureTime) {
        if (connectingPaths == null || connectingPaths.isEmpty()) {
            throw new IllegalStateException("No connecting paths available");
        }

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

    public record PathOutput(double time, Path path) {}
}
