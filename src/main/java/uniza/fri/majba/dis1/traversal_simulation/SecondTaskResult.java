package uniza.fri.majba.dis1.traversal_simulation;

/**
 * Immutable snapshot of the second-task simulation result.
 */
public record SecondTaskResult(
        String routeName,
        String latestDepartureTime,
        String mustArriveBy,
        double acceptedOnTimePercentage,
        double rejectedOnTimePercentage,
        String rejectedDepartureTime,
        double requiredOnTimePercentage
) {
}

