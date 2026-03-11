package uniza.fri.majba.dis1.traversal_simulation.graph;

import uniza.fri.majba.dis1.simulation_core.generators.*;
import uniza.fri.majba.dis1.simulation_core.statistics.WeightedSumStatistic;
import uniza.fri.majba.dis1.traversal_simulation.dto.RouteParameters;
import uniza.fri.majba.dis1.ui.model.SimulationConfig;

import java.util.List;
import java.util.Random;

public final class TraversalGraph {

    private TraversalGraph() {
        // utility class
    }

    public static List<RouteParameters> buildRoutes(SimulationConfig config) {
        final List<EmpiricGeneratorConfiguration> BLACK_EDGE_GENERATOR_CONFIGURATION = List.of(
                new EmpiricGeneratorConfiguration(10, 20, 0.1),
                new EmpiricGeneratorConfiguration(20, 32, 0.5),
                new EmpiricGeneratorConfiguration(32, 45, 0.2),
                new EmpiricGeneratorConfiguration(45, 75, 0.15),
                new EmpiricGeneratorConfiguration(75, 85, 0.05)
        );

        final List<EmpiricGeneratorConfiguration> BLUE_EDGE_GENERATOR_CONFIGURATION = List.of(
                new EmpiricGeneratorConfiguration(15, 28, 0.2),
                new EmpiricGeneratorConfiguration(29, 44, 0.4),
                new EmpiricGeneratorConfiguration(45, 64, 0.4)
        );

        Random seedGenerator = new Random(config.getSeedGeneratorSeed());
        DiscreteUniformGenerator redEdgeGenerator = new DiscreteUniformGenerator(55, 75, seedGenerator);
        ContinuousUniformGenerator greenEdgeGenerator = new ContinuousUniformGenerator(50, 80, seedGenerator);
        ContinuousEmpiricGenerator blackEdgeGenerator = new ContinuousEmpiricGenerator(BLACK_EDGE_GENERATOR_CONFIGURATION, seedGenerator);
        DiscreteEmpiricGenerator blueEdgeGenerator = new DiscreteEmpiricGenerator(BLUE_EDGE_GENERATOR_CONFIGURATION, seedGenerator);
        ContinuousUniformGenerator kGenerator = new ContinuousUniformGenerator(10, 25, seedGenerator);
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


        // Edges
        Edge<Node> zilinaK = new Edge<>(zilina, k, EdgeColor.BLACK, 2, blackEdgeGenerator);
        Edge<Node> kZilina = new KEdge<>(k, zilina, EdgeColor.BLACK, 2, blackEdgeGenerator, kGenerator);

        Edge<Node> divinkaK = new Edge<>(divinka, k, EdgeColor.RED, 2, redEdgeGenerator);
        Edge<Node> kDivinka = new KEdge<>(k, divinka, EdgeColor.RED, 2, redEdgeGenerator, kGenerator);

        Edge<Node> rajeckeTepliceK = new Edge<>(rajeckeTeplice, k, EdgeColor.GREEN, 2, greenEdgeGenerator);
        Edge<Node> kRajeckeTeplice = new KEdge<>(k, rajeckeTeplice, EdgeColor.GREEN, 2, greenEdgeGenerator, kGenerator);

        Edge<Node> strecnoK = new Edge<>(strecno, k, EdgeColor.BLUE, 4, blueEdgeGenerator);
        Edge<Node> kStrecno = new KEdge<>(k, strecno, EdgeColor.BLUE, 4, blueEdgeGenerator, kGenerator);


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


        // Strečno - Rajecké Teplice
        Edge<Node> strecnoToStrecnoRajeckeTepliceBlue = new Edge<>(strecno, strecnoRajeckeTeplice, EdgeColor.BLUE, 5, blueEdgeGenerator);
        Edge<Node> strecnoRajeckeTepliceToStrecnoBlue = new Edge<>(strecnoRajeckeTeplice, strecno, EdgeColor.BLUE, 5, blueEdgeGenerator);

        Edge<Node> strecnoToStrecnoRajeckeTepliceBlack = new Edge<>(strecno, strecnoRajeckeTeplice, EdgeColor.BLACK, 5, blackEdgeGenerator);
        Edge<Node> strecnoRajeckeTepliceToStrecnoBlack = new Edge<>(strecnoRajeckeTeplice, strecno, EdgeColor.BLACK, 5, blackEdgeGenerator);

        Edge<Node> strecnoRajeckeTepliceToRajeckeTeplice = new Edge<>(strecnoRajeckeTeplice, rajeckeTeplice, EdgeColor.BLUE, 8, blueEdgeGenerator);
        Edge<Node> rajeckeTepliceToStrecnoRajeckeTeplice = new Edge<>(rajeckeTeplice, strecnoRajeckeTeplice, EdgeColor.BLUE, 8, blueEdgeGenerator);


        // Rajecké Teplice - Divinka
        Edge<Node> rajeckeTepliceToRajeckeTepliceX1 = new Edge<>(rajeckeTeplice, rajeckeTepliceX1, EdgeColor.BLUE, 1, blueEdgeGenerator);
        Edge<Node> rajeckeTepliceX1ToRajeckeTeplice = new Edge<>(rajeckeTepliceX1, rajeckeTeplice, EdgeColor.BLUE, 1, blueEdgeGenerator);

        Edge<Node> rajeckeTepliceX1ToX1X2 = new Edge<>(rajeckeTepliceX1, x1X2, EdgeColor.RED, 2, redEdgeGenerator);
        Edge<Node> x1X2ToRajeckeTepliceX1 = new Edge<>(x1X2, rajeckeTepliceX1, EdgeColor.RED, 2, redEdgeGenerator);

        Edge<Node> rajeckeTepliceX1ToX3Divinka = new Edge<>(rajeckeTepliceX1, x3Divinka, EdgeColor.RED, 3, redEdgeGenerator);
        Edge<Node> x3DivinkaToRajeckeTepliceX1 = new Edge<>(x3Divinka, rajeckeTepliceX1, EdgeColor.RED, 3, redEdgeGenerator);

        Edge<Node> x1X2ToX3Divinka = new Edge<>(x1X2, x3Divinka, EdgeColor.BLUE, 1, blueEdgeGenerator);

        Edge<Node> x3DivinkaToDivinka = new Edge<>(x3Divinka, divinka, EdgeColor.BLACK, 1, blackEdgeGenerator);
        Edge<Node> divinkaToX3Divinka = new Edge<>(divinka, x3Divinka, EdgeColor.BLACK, 1, blackEdgeGenerator);


        Path terminalDivinka = Path.from(divinka).build();
        Path terminalZilina = Path.from(zilina).build();
        Path terminalStrecno = Path.from(strecno).build();
        Path terminalRT = Path.from(rajeckeTeplice).build();

        Path kToDivinka = Path.from(k)
                .nextPaths(List.of(terminalDivinka))
                .throughEdges(List.of(kDivinka))
                .build();
        Path kToZilina = Path.from(k)
                .nextPaths(List.of(terminalZilina))
                .throughEdges(List.of(kZilina))
                .build();
        Path kToStrecno = Path.from(k)
                .nextPaths(List.of(terminalStrecno))
                .throughEdges(List.of(kStrecno))
                .build();
        Path kToRT = Path.from(k)
                .nextPaths(List.of(terminalRT))
                .throughEdges(List.of(kRajeckeTeplice))
                .build();

        // Žilina - Divinka
        Path pathZilinaDivinka = Path.from(zilina)
                .nextPaths(List.of(terminalDivinka, terminalDivinka, kToDivinka))
                .throughEdges(List.of(zilinaDivinka1, zilinaDivinka2, zilinaK))
                .build();

        Path pathDivinkaZilina = Path.from(divinka)
                .nextPaths(List.of(terminalZilina, terminalZilina, kToZilina))
                .throughEdges(List.of(divinkaZilina1, divinkaZilina2, divinkaK))
                .build();

        // Žilina - Strečno
        Path zilinaNorthJunction = Path.from(zilinaStrecnoNorth)
                .nextPaths(List.of(terminalStrecno))
                .throughEdges(List.of(zilinaStrecnoNorthToStrecno))
                .build();
        Path strecnoZilinaA = Path.from(zilinaStrecnoNorth)
                .nextPaths(List.of(terminalZilina))
                .throughEdges(List.of(strecnoNorthZilinaEdge))
                .build();

        Path zilinaSouthJunction = Path.from(zilinaStrecnoSouth)
                .nextPaths(List.of(terminalStrecno))
                .throughEdges(List.of(zilinaStrecnoSouthToStrecno))
                .build();
        Path strecnoZilinaB = Path.from(zilinaStrecnoSouth)
                .nextPaths(List.of(terminalZilina))
                .throughEdges(List.of(zilinaStrecnoSouthToZilina))
                .build();

        Path pathZilinaStrecno = Path.from(zilina)
                .nextPaths(List.of(zilinaNorthJunction, zilinaSouthJunction, kToStrecno))
                .throughEdges(List.of(zilinaStrecnoNorthEdge, zilinaToZilinaStrecnoSouth, zilinaK))
                .build();

        Path pathStrecnoZilina = Path.from(strecno)
                .nextPaths(List.of(strecnoZilinaA, strecnoZilinaB, kToZilina))
                .throughEdges(List.of(strecnoToZilinaStrecnoNorth, strecnoToZilinaStrecnoSouth, strecnoK))
                .build();

        // Žilina - Rajecké Teplice
        Path pathZilinaRT = Path.from(zilina)
                .nextPaths(List.of(kToRT))
                .throughEdges(List.of(zilinaK))
                .build();

        Path pathRTZilina = Path.from(rajeckeTeplice)
                .nextPaths(List.of(kToZilina))
                .throughEdges(List.of(rajeckeTepliceK))
                .build();

        // Divinka - Strečno
        Path pathDivinkaStrecno = Path.from(divinka)
                .nextPaths(List.of(kToStrecno))
                .throughEdges(List.of(divinkaK))
                .build();

        Path pathStrechnoDivinka = Path.from(strecno)
                .nextPaths(List.of(kToDivinka))
                .throughEdges(List.of(strecnoK))
                .build();

        // Divinka - Rajecké Teplice
        Path x1ToRT = Path.from(rajeckeTepliceX1)
                .nextPaths(List.of(terminalRT))
                .throughEdges(List.of(rajeckeTepliceX1ToRajeckeTeplice))
                .build();
        Path divinkaRtADirect = Path.from(x3Divinka)
                .nextPaths(List.of(x1ToRT))
                .throughEdges(List.of(x3DivinkaToRajeckeTepliceX1))
                .build();

        Path x1X2ToX1Path = Path.from(x1X2)
                .nextPaths(List.of(x1ToRT))
                .throughEdges(List.of(x1X2ToRajeckeTepliceX1))
                .build();
        Path divinkaRtAViaX1X2 = Path.from(x3Divinka)
                .nextPaths(List.of(x1X2ToX1Path))
                .throughEdges(List.of(x1X2ToX3Divinka))
                .build();

        Path pathDivinkaRT = Path.from(divinka)
                .nextPaths(List.of(divinkaRtADirect, divinkaRtAViaX1X2, kToRT))
                .throughEdges(List.of(divinkaToX3Divinka, divinkaToX3Divinka, divinkaK))
                .build();

        // Rajecké Teplice - Divinka
        Path x3ToDivinka = Path.from(x3Divinka)
                .nextPaths(List.of(terminalDivinka))
                .throughEdges(List.of(x3DivinkaToDivinka))
                .build();
        Path x1X2ToX3 = Path.from(x1X2)
                .nextPaths(List.of(x3ToDivinka))
                .throughEdges(List.of(x1X2ToX3Divinka))
                .build();

        Path rtDivinkaA = Path.from(rajeckeTepliceX1)
                .nextPaths(List.of(x1X2ToX3, x3ToDivinka))
                .throughEdges(List.of(rajeckeTepliceX1ToX1X2, rajeckeTepliceX1ToX3Divinka))
                .build();

        Path pathRTDivinka = Path.from(rajeckeTeplice)
                .nextPaths(List.of(rtDivinkaA, kToDivinka))
                .throughEdges(List.of(rajeckeTepliceToRajeckeTepliceX1, rajeckeTepliceK))
                .build();

        // Strečno - Rajecké Teplice
        Path strecnoRtJunction = Path.from(strecnoRajeckeTeplice)
                .nextPaths(List.of(terminalRT))
                .throughEdges(List.of(strecnoRajeckeTepliceToRajeckeTeplice))
                .build();
        Path pathStrecnoRT = Path.from(strecno)
                .nextPaths(List.of(strecnoRtJunction, strecnoRtJunction, kToRT))
                .throughEdges(List.of(strecnoToStrecnoRajeckeTepliceBlue, strecnoToStrecnoRajeckeTepliceBlack, strecnoK))
                .build();

        Path rtStrecnoA = Path.from(strecnoRajeckeTeplice)
                .nextPaths(List.of(terminalStrecno))
                .throughEdges(List.of(strecnoRajeckeTepliceToStrecnoBlue))
                .build();

        Path rtStrecnoB = Path.from(strecnoRajeckeTeplice)
                .nextPaths(List.of(terminalStrecno))
                .throughEdges(List.of(strecnoRajeckeTepliceToStrecnoBlack))
                .build();

        Path pathRTStrecno = Path.from(rajeckeTeplice)
                .nextPaths(List.of(rtStrecnoA, rtStrecnoB, kToStrecno))
                .throughEdges(List.of(rajeckeTepliceToStrecnoRajeckeTeplice, rajeckeTepliceToStrecnoRajeckeTeplice, rajeckeTepliceK))
                .build();


        // Žilina --> Divinka --> Strečno --> Rajecké Teplice --> Žilina
        List<Path> route1 = List.of(pathZilinaDivinka, pathDivinkaStrecno, pathStrecnoRT, pathRTZilina);

        // Žilina --> Divinka --> Rajecké Teplice --> Strečno --> Žilina
        List<Path> route2 = List.of(pathZilinaDivinka, pathDivinkaRT, pathRTStrecno, pathStrecnoZilina);

        // Žilina --> Strečno --> Divinka --> Rajecké Teplice --> Žilina
        List<Path> route3 = List.of(pathZilinaStrecno, pathStrechnoDivinka, pathDivinkaRT, pathRTZilina);

        // Žilina --> Strečno --> Rajecké Teplice --> Divinka --> Žilina
        List<Path> route4 = List.of(pathZilinaStrecno, pathStrecnoRT, pathRTDivinka, pathDivinkaZilina);

        // Žilina --> Rajecké Teplice --> Divinka --> Strečno --> Žilina
        List<Path> route5 = List.of(pathZilinaRT, pathRTDivinka, pathDivinkaStrecno, pathStrecnoZilina);

        // Žilina --> Rajecké Teplice --> Strečno --> Divinka --> Žilina
        List<Path> route6 = List.of(pathZilinaRT, pathRTStrecno, pathStrechnoDivinka, pathDivinkaZilina);

        // TODO ak nebude fungovat, tak jedno monte carlo jadro pre kazdu zo 6 moznosti
        return List.of(
                new RouteParameters(route1, new WeightedSumStatistic(), "Žilina --> Divinka --> Strečno --> Rajecké Teplice --> Žilina"),
                new RouteParameters(route2, new WeightedSumStatistic(), "Žilina --> Divinka --> Rajecké Teplice --> Strečno --> Žilina"),
                new RouteParameters(route3, new WeightedSumStatistic(), "Žilina --> Strečno --> Divinka --> Rajecké Teplice --> Žilina"),
                new RouteParameters(route4, new WeightedSumStatistic(), "Žilina --> Strečno --> Rajecké Teplice --> Divinka --> Žilina"),
                new RouteParameters(route5, new WeightedSumStatistic(), "Žilina --> Rajecké Teplice --> Divinka --> Strečno --> Žilina"),
                new RouteParameters(route6, new WeightedSumStatistic(), "Žilina --> Rajecké Teplice --> Strečno --> Divinka --> Žilina")
        );
    }
}

