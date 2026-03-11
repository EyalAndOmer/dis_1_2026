package uniza.fri.majba.dis1.traversal_simulation;

import java.util.List;

/**
 * Immutable snapshot of the results produced by a single replication.
 * Acts as the data contract between the simulation layer and the UI.
 */
public record ReplicationResult(List<RouteResult> routeResults) {
}

