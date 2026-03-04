package uniza.fri.majba.dis1;

import uniza.fri.majba.dis1.traversal_simulation.MonteCarloSimulationCore;
import uniza.fri.majba.dis1.traversal_simulation.TraversalReplication;

public class Main {
    public static void main(String[] args) {
        for (int i = -100; i < 100; i++) {
            TraversalReplication traversalReplication = new TraversalReplication();
            MonteCarloSimulationCore simulationCore = new MonteCarloSimulationCore(traversalReplication);
            simulationCore.simulate(500);
        }


    }
}
