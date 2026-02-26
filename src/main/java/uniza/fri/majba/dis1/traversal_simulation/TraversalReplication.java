package uniza.fri.majba.dis1.traversal_simulation;

import uniza.fri.majba.dis1.simulation_core.Replication;
import uniza.fri.majba.dis1.simulation_core.generators.*;
import uniza.fri.majba.dis1.traversal_simulation.graph.Edge;
import uniza.fri.majba.dis1.traversal_simulation.graph.EdgeColor;
import uniza.fri.majba.dis1.traversal_simulation.graph.Node;
import uniza.fri.majba.dis1.traversal_simulation.graph.NodeType;

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


        // Center edges
        Edge<Node> zilinaK = new Edge<>(zilina, k, EdgeColor.BLACK, 2, blackEdgeGenerator);
        Edge<Node> kZilina = new Edge<>(k, zilina, EdgeColor.BLACK, 2, blackEdgeGenerator);

        Edge<Node> divinkaK = new Edge<>(divinka, k, EdgeColor.RED, 2, redEdgeGenerator);
        Edge<Node> kDivinka = new Edge<>(k, divinka, EdgeColor.RED, 2, redEdgeGenerator);

        Edge<Node> rajeckeTepliceK = new Edge<>(rajeckeTeplice, k, EdgeColor.GREEN, 2, greenEdgeGenerator);
        Edge<Node> kRajeckeTeplice = new Edge<>(k, rajeckeTeplice, EdgeColor.GREEN, 2, greenEdgeGenerator);

        Edge<Node> strecnoK = new Edge<>(strecno, k, EdgeColor.BLUE, 4, blueEdgeGenerator);
        Edge<Node> kStrecno = new Edge<>(k, strecno, EdgeColor.BLUE, 4, blueEdgeGenerator);


        // Žilina - Divinka
        Edge<Node> zilinaDivinka1 = new Edge<>(zilina, divinka, EdgeColor.GREEN, 4, greenEdgeGenerator);
        Edge<Node> divinkaZilina1 = new Edge<>(divinka, zilina, EdgeColor.GREEN, 4, greenEdgeGenerator);

        Edge<Node> zilinaDivinka2 = new Edge<>(zilina, divinka, EdgeColor.RED, 4, redEdgeGenerator);
        Edge<Node> divinkaZilina2 = new Edge<>(divinka, zilina, EdgeColor.RED, 4, redEdgeGenerator);


        // Žilina - Strečno
        Edge<Node> zilinaStrecnoNorthEdge = new Edge<>(zilina, zilinaStrecnoNorth, EdgeColor.RED, 3, redEdgeGenerator);
        Edge<Node> strecnoNorthZilinaEdge = new Edge<>(zilinaStrecnoNorth, zilina, EdgeColor.RED, 3, redEdgeGenerator);

        Edge<Node> zilinaStrecnoNorthToStrecno = new Edge<>(zilinaStrecnoNorth, strecno, EdgeColor.RED, 4, redEdgeGenerator);
        Edge<Node> strecnoToZilinaStrecnoNorth = new Edge<>(strecno, zilinaStrecnoNorth, EdgeColor.RED, 4, redEdgeGenerator);

        Edge<Node> zilinaToZilinaStrecnoSouth = new Edge<>(zilina, zilinaStrecnoSouth, EdgeColor.GREEN, 4, greenEdgeGenerator);
        Edge<Node> zilinaStrecnoSouthToZilina = new Edge<>(zilinaStrecnoSouth, zilina, EdgeColor.GREEN, 4, greenEdgeGenerator);

        Edge<Node> zilinaStrecnoSouthToStrecno = new Edge<>(zilinaStrecnoSouth, strecno, EdgeColor.BLACK, 3, blackEdgeGenerator);
        Edge<Node> strecnoToZilinaStrecnoSouth = new Edge<>(strecno, zilinaStrecnoSouth, EdgeColor.BLACK, 3, blackEdgeGenerator);


        // Strečno - Rajecké Teplice (cez junction)
        Edge<Node> strecnoToStrecnoRajeckeTepliceBlue = new Edge<>(strecno, strecnoRajeckeTeplice, EdgeColor.BLUE, 5, blueEdgeGenerator);
        Edge<Node> strecnoRajeckeTepliceToStrecnoBlue = new Edge<>(strecnoRajeckeTeplice, strecno, EdgeColor.BLUE, 5, blueEdgeGenerator);

        Edge<Node> strecnoToStrecnoRajeckeTepliceBlack = new Edge<>(strecno, strecnoRajeckeTeplice, EdgeColor.BLACK, 5, blackEdgeGenerator);
        Edge<Node> strecnoRajeckeTepliceToStrecnoBlack = new Edge<>(strecnoRajeckeTeplice, strecno, EdgeColor.BLACK, 5, blackEdgeGenerator);

        // POZOR: v tvojom kóde je EdgeColor.BLUE, váha 8, ale generator je redEdgeGenerator (asi preklep)
        Edge<Node> strecnoRajeckeTepliceToRajeckeTeplice = new Edge<>(strecnoRajeckeTeplice, rajeckeTeplice, EdgeColor.BLUE, 8, redEdgeGenerator);
        Edge<Node> rajeckeTepliceToStrecnoRajeckeTeplice = new Edge<>(rajeckeTeplice, strecnoRajeckeTeplice, EdgeColor.BLUE, 8, redEdgeGenerator);


        // Rajecké Teplice - Divinka (cez X1, X1-X2, X3-Divinka)
        Edge<Node> rajeckeTepliceToRajeckeTepliceX1 = new Edge<>(rajeckeTeplice, rajeckeTepliceX1, EdgeColor.BLUE, 1, blueEdgeGenerator);
        Edge<Node> rajeckeTepliceX1ToRajeckeTeplice = new Edge<>(rajeckeTepliceX1, rajeckeTeplice, EdgeColor.BLUE, 1, blueEdgeGenerator);

        Edge<Node> rajeckeTepliceX1ToX1X2 = new Edge<>(rajeckeTepliceX1, x1X2, EdgeColor.RED, 2, redEdgeGenerator);
        Edge<Node> x1X2ToRajeckeTepliceX1 = new Edge<>(x1X2, rajeckeTepliceX1, EdgeColor.RED, 2, redEdgeGenerator);

        Edge<Node> rajeckeTepliceX1ToX3Divinka = new Edge<>(rajeckeTepliceX1, x3Divinka, EdgeColor.RED, 3, redEdgeGenerator);
        Edge<Node> x3DivinkaToRajeckeTepliceX1 = new Edge<>(x3Divinka, rajeckeTepliceX1, EdgeColor.RED, 3, redEdgeGenerator);

        Edge<Node> x1X2ToX3Divinka = new Edge<>(x1X2, x3Divinka, EdgeColor.BLUE, 1, blueEdgeGenerator);
        Edge<Node> x3DivinkaToX1X2 = new Edge<>(x3Divinka, x1X2, EdgeColor.BLUE, 1, blueEdgeGenerator);

        Edge<Node> x3DivinkaToDivinka = new Edge<>(x3Divinka, divinka, EdgeColor.BLACK, 1, blackEdgeGenerator);
        Edge<Node> divinkaToX3Divinka = new Edge<>(divinka, x3Divinka, EdgeColor.BLACK, 1, blackEdgeGenerator);


        // TODO pridat do konfiguracie aplikacie -
        // 1. Kolko bodov chcem mat v grafe
        // 2. Kolko percent replikacii chcem vidiet

        /**
         * Chart requirements
         *
         */

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
