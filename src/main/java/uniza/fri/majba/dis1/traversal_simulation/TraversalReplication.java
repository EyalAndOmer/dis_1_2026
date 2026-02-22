package uniza.fri.majba.dis1.traversal_simulation;

import uniza.fri.majba.dis1.simulation_core.Replication;
import uniza.fri.majba.dis1.simulation_core.generators.*;
import uniza.fri.majba.dis1.traversal_simulation.graph.EdgeColor;
import uniza.fri.majba.dis1.traversal_simulation.graph.Node;
import uniza.fri.majba.dis1.traversal_simulation.graph.NodeType;
import uniza.fri.majba.dis1.traversal_simulation.graph.WeightedGraph;

import java.util.List;

public class TraversalReplication implements Replication {
    // Generators
    DiscreteUniformGenerator redEdgeGenerator = new DiscreteUniformGenerator(55, 75);
    ContinuousUniformGenerator greenEdgeGenerator = new ContinuousUniformGenerator(50, 80);
    List<EmpiricGeneratorConfiguration> blackEdgeGeneratorConfiguration = List.of(
            new EmpiricGeneratorConfiguration(10, 20, 0.1),
            new EmpiricGeneratorConfiguration(20, 32, 0.5),
            new EmpiricGeneratorConfiguration(32, 45, 0.2),
            new EmpiricGeneratorConfiguration(45, 75, 0.15),
            new EmpiricGeneratorConfiguration(75, 85, 0.05)
    );

    ContinuousEmpiricGenerator blackEdgeGenerator = new ContinuousEmpiricGenerator(blackEdgeGeneratorConfiguration);

    List<EmpiricGeneratorConfiguration> blueEdgeGeneratorConfiguration = List.of(
            new EmpiricGeneratorConfiguration(15, 29, 0.2),
            new EmpiricGeneratorConfiguration(29, 45, 0.4),
            new EmpiricGeneratorConfiguration(45, 65, 0.4)
    );

    DiscreteEmpiricGenerator blueEdgeGenerator = new DiscreteEmpiricGenerator(blueEdgeGeneratorConfiguration);

    /**
     * Generator maps additional slowdown of a special edge on the graph - due to traffic jams
     */
    ContinuousUniformGenerator KGenerator = new ContinuousUniformGenerator(10, 25);


    WeightedGraph<Node> graph = new WeightedGraph<>();

    @Override
    public void beforeAllReplications() {
        // Init graph
        // Nodes
        Node zilina = new Node("Žilina", NodeType.CITY);
        Node divinka = new Node("Divinka", NodeType.CITY);
        Node strecno = new Node("Strečno", NodeType.CITY);
        Node rajeckeTeplice = new Node("Rajecké Teplice", NodeType.CITY);

        Node k = new Node("K", NodeType.JUNCTION);

        Node zilinaStrecnoNorth = new Node("Žilina-Strečno_North", NodeType.JUNCTION);
        Node zilinaStrecnoSouth = new Node("Žilina-Strečno_South", NodeType.JUNCTION);

        Node strecnoRajeckeTeplice = new Node("Strečno-Rajecké Teplice", NodeType.JUNCTION);

        Node rajeckeTepliceX1 = new Node("Rajecké Teplice-X1", NodeType.JUNCTION);
        Node x1X2 = new Node("X1-X2", NodeType.JUNCTION);
        Node x3Divinka = new Node("X3-Divinka", NodeType.JUNCTION);

        graph.addNode(zilina);
        graph.addNode(divinka);
        graph.addNode(strecno);
        graph.addNode(rajeckeTeplice);
        graph.addNode(k);
        graph.addNode(zilinaStrecnoNorth);
        graph.addNode(zilinaStrecnoSouth);
        graph.addNode(strecnoRajeckeTeplice);
        graph.addNode(rajeckeTepliceX1);
        graph.addNode(x1X2);
        graph.addNode(x3Divinka);

        // Center edges
        graph.addUndirectedEdge(zilina, k, EdgeColor.BLACK, 2, blackEdgeGenerator);
        graph.addUndirectedEdge(divinka, k, EdgeColor.RED, 2, redEdgeGenerator);
        graph.addUndirectedEdge(rajeckeTeplice, k, EdgeColor.GREEN, 2, greenEdgeGenerator);
        graph.addUndirectedEdge(strecno, k, EdgeColor.BLUE, 4, blueEdgeGenerator);

        // Zilina - Divinka
        graph.addUndirectedEdge(zilina, divinka, EdgeColor.GREEN, 4, greenEdgeGenerator);
        graph.addUndirectedEdge(zilina, divinka, EdgeColor.RED, 4, redEdgeGenerator);

        // Zilina - Strečno
        graph.addUndirectedEdge(zilina, zilinaStrecnoNorth, EdgeColor.RED, 3, redEdgeGenerator);
        graph.addUndirectedEdge(zilinaStrecnoNorth, strecno, EdgeColor.RED, 4, redEdgeGenerator);
        graph.addUndirectedEdge(zilina, zilinaStrecnoSouth, EdgeColor.GREEN, 4, greenEdgeGenerator);
        graph.addUndirectedEdge(zilinaStrecnoSouth, strecno, EdgeColor.BLACK, 3, blackEdgeGenerator);

        // Strečno - Rajecké Teplice
        graph.addUndirectedEdge(strecno, strecnoRajeckeTeplice, EdgeColor.BLUE, 5, blueEdgeGenerator);
        graph.addUndirectedEdge(strecno, strecnoRajeckeTeplice, EdgeColor.BLACK, 5, blackEdgeGenerator);
        graph.addUndirectedEdge(strecnoRajeckeTeplice, rajeckeTeplice, EdgeColor.BLUE, 8, redEdgeGenerator);

        // Rajecké Teplice - Divinka
        graph.addUndirectedEdge(rajeckeTeplice, rajeckeTepliceX1, EdgeColor.BLUE, 1, blueEdgeGenerator);
        graph.addUndirectedEdge(rajeckeTepliceX1, x1X2, EdgeColor.RED, 2, redEdgeGenerator);
        graph.addUndirectedEdge(rajeckeTepliceX1, x3Divinka, EdgeColor.RED, 3, redEdgeGenerator);
        graph.addUndirectedEdge(x1X2, x3Divinka, EdgeColor.BLUE, 1, blueEdgeGenerator);
        graph.addUndirectedEdge(x3Divinka, divinka, EdgeColor.BLACK, 1, blackEdgeGenerator);
    }

    @Override
    public void afterAllReplications() {

    }

    @Override
    public void beforeReplication() {

    }

    @Override
    public void afterReplication() {

    }

    @Override
    public void execute() {

    }
}
