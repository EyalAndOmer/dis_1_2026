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

    public boolean isPathEnd() {
        return endingNode != null;
    }

    public PathOutput pickNextPath() {
        if (connectingPaths.size() == 1) {
            return new PathOutput(pathEdges.getFirst().getTraversalTime(), connectingPaths.getFirst());
        }

        double mininumTime = Double.MAX_VALUE;
        int minimumTimeIndex = 0;

        for (int i = 1; i < connectingPaths.size(); i++) {
            Edge<Node> currentEdge = pathEdges.get(i);
            double traversalTime = currentEdge.getTraversalTime();

            if (traversalTime < mininumTime) {
                mininumTime = traversalTime;
                minimumTimeIndex = i;
            }
        }

        return new PathOutput(mininumTime, connectingPaths.get(minimumTimeIndex));
    }

    public record PathOutput(double time, Path path) {}
}
