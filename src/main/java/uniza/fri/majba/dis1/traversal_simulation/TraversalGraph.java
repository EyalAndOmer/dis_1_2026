package uniza.fri.majba.dis1.traversal_simulation;

import uniza.fri.majba.dis1.traversal_simulation.graph.Edge;
import uniza.fri.majba.dis1.traversal_simulation.graph.EdgeColor;
import uniza.fri.majba.dis1.traversal_simulation.graph.KEdge;
import uniza.fri.majba.dis1.traversal_simulation.graph.Node;
import uniza.fri.majba.dis1.traversal_simulation.graph.NodeType;
import uniza.fri.majba.dis1.traversal_simulation.graph.Path;

import java.util.List;

import static uniza.fri.majba.dis1.traversal_simulation.TraversalSimulationConstants.*;

public class TraversalGraph {

    private TraversalGraph() {
        // utility class
    }

    public static List<List<Path>> buildRoutes() {
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
        Edge<Node> zilinaK = new Edge<>(zilina, k, EdgeColor.BLACK, 2, BLACK_EDGE_GENERATOR);
        Edge<Node> kZilina = new KEdge<>(k, zilina, EdgeColor.BLACK, 2, BLACK_EDGE_GENERATOR, K_GENERATOR);

        Edge<Node> divinkaK = new Edge<>(divinka, k, EdgeColor.RED, 2, RED_EDGE_GENERATOR);
        Edge<Node> kDivinka = new KEdge<>(k, divinka, EdgeColor.RED, 2, RED_EDGE_GENERATOR, K_GENERATOR);

        Edge<Node> rajeckeTepliceK = new Edge<>(rajeckeTeplice, k, EdgeColor.GREEN, 2, GREEN_EDGE_GENERATOR);
        Edge<Node> kRajeckeTeplice = new KEdge<>(k, rajeckeTeplice, EdgeColor.GREEN, 2, GREEN_EDGE_GENERATOR, K_GENERATOR);

        Edge<Node> strecnoK = new Edge<>(strecno, k, EdgeColor.BLUE, 4, BLUE_EDGE_GENERATOR);
        Edge<Node> kStrecno = new KEdge<>(k, strecno, EdgeColor.BLUE, 4, BLUE_EDGE_GENERATOR, K_GENERATOR);


        // Žilina - Divinka
        Edge<Node> zilinaDivinka1 = new Edge<>(zilina, divinka, EdgeColor.GREEN, 4, GREEN_EDGE_GENERATOR);
        Edge<Node> divinkaZilina1 = new Edge<>(divinka, zilina, EdgeColor.GREEN, 4, GREEN_EDGE_GENERATOR);

        Edge<Node> zilinaDivinka2 = new Edge<>(zilina, divinka, EdgeColor.RED, 4, RED_EDGE_GENERATOR);
        Edge<Node> divinkaZilina2 = new Edge<>(divinka, zilina, EdgeColor.RED, 4, RED_EDGE_GENERATOR);


        // Žilina - Strečno
        Edge<Node> zilinaStrecnoNorthEdge = new Edge<>(zilina, zilinaStrecnoNorth, EdgeColor.RED, 3, RED_EDGE_GENERATOR);
        Edge<Node> strecnoNorthZilinaEdge = new Edge<>(zilinaStrecnoNorth, zilina, EdgeColor.RED, 3, RED_EDGE_GENERATOR);

        Edge<Node> zilinaStrecnoNorthToStrecno = new Edge<>(zilinaStrecnoNorth, strecno, EdgeColor.RED, 4, RED_EDGE_GENERATOR);
        Edge<Node> strecnoToZilinaStrecnoNorth = new Edge<>(strecno, zilinaStrecnoNorth, EdgeColor.RED, 4, RED_EDGE_GENERATOR);

        Edge<Node> zilinaToZilinaStrecnoSouth = new Edge<>(zilina, zilinaStrecnoSouth, EdgeColor.GREEN, 4, GREEN_EDGE_GENERATOR);
        Edge<Node> zilinaStrecnoSouthToZilina = new Edge<>(zilinaStrecnoSouth, zilina, EdgeColor.GREEN, 4, GREEN_EDGE_GENERATOR);

        Edge<Node> zilinaStrecnoSouthToStrecno = new Edge<>(zilinaStrecnoSouth, strecno, EdgeColor.BLACK, 3, BLACK_EDGE_GENERATOR);
        Edge<Node> strecnoToZilinaStrecnoSouth = new Edge<>(strecno, zilinaStrecnoSouth, EdgeColor.BLACK, 3, BLACK_EDGE_GENERATOR);


        // Strečno - Rajecké Teplice (cez junction)
        Edge<Node> strecnoToStrecnoRajeckeTepliceBlue = new Edge<>(strecno, strecnoRajeckeTeplice, EdgeColor.BLUE, 5, BLUE_EDGE_GENERATOR);
        Edge<Node> strecnoRajeckeTepliceToStrecnoBlue = new Edge<>(strecnoRajeckeTeplice, strecno, EdgeColor.BLUE, 5, BLUE_EDGE_GENERATOR);

        Edge<Node> strecnoToStrecnoRajeckeTepliceBlack = new Edge<>(strecno, strecnoRajeckeTeplice, EdgeColor.BLACK, 5, BLACK_EDGE_GENERATOR);
        Edge<Node> strecnoRajeckeTepliceToStrecnoBlack = new Edge<>(strecnoRajeckeTeplice, strecno, EdgeColor.BLACK, 5, BLACK_EDGE_GENERATOR);

        Edge<Node> strecnoRajeckeTepliceToRajeckeTeplice = new Edge<>(strecnoRajeckeTeplice, rajeckeTeplice, EdgeColor.BLUE, 8, BLUE_EDGE_GENERATOR);
        Edge<Node> rajeckeTepliceToStrecnoRajeckeTeplice = new Edge<>(rajeckeTeplice, strecnoRajeckeTeplice, EdgeColor.BLUE, 8, BLUE_EDGE_GENERATOR);


        // Rajecké Teplice - Divinka (cez X1, X1-X2, X3-Divinka)
        Edge<Node> rajeckeTepliceToRajeckeTepliceX1 = new Edge<>(rajeckeTeplice, rajeckeTepliceX1, EdgeColor.BLUE, 1, BLUE_EDGE_GENERATOR);
        Edge<Node> rajeckeTepliceX1ToRajeckeTeplice = new Edge<>(rajeckeTepliceX1, rajeckeTeplice, EdgeColor.BLUE, 1, BLUE_EDGE_GENERATOR);

        Edge<Node> rajeckeTepliceX1ToX1X2 = new Edge<>(rajeckeTepliceX1, x1X2, EdgeColor.RED, 2, RED_EDGE_GENERATOR);
        Edge<Node> x1X2ToRajeckeTepliceX1 = new Edge<>(x1X2, rajeckeTepliceX1, EdgeColor.RED, 2, RED_EDGE_GENERATOR);

        Edge<Node> rajeckeTepliceX1ToX3Divinka = new Edge<>(rajeckeTepliceX1, x3Divinka, EdgeColor.RED, 3, RED_EDGE_GENERATOR);
        Edge<Node> x3DivinkaToRajeckeTepliceX1 = new Edge<>(x3Divinka, rajeckeTepliceX1, EdgeColor.RED, 3, RED_EDGE_GENERATOR);

        Edge<Node> x1X2ToX3Divinka = new Edge<>(x1X2, x3Divinka, EdgeColor.BLUE, 1, BLUE_EDGE_GENERATOR);

        Edge<Node> x3DivinkaToDivinka = new Edge<>(x3Divinka, divinka, EdgeColor.BLACK, 1, BLACK_EDGE_GENERATOR);
        Edge<Node> divinkaToX3Divinka = new Edge<>(divinka, x3Divinka, EdgeColor.BLACK, 1, BLACK_EDGE_GENERATOR);


        // Paths
        // Žilina – Divinka
        // Žilina --> Divinka
        Path zilinaDivinka_A = new Path(zilina, List.of(new Path(divinka, List.of(), List.of(), divinka)), List.of(zilinaDivinka1), null);
        //Žilina --> Divinka
        Path zilinaDivinka_B = new Path(zilina, List.of(new Path(divinka, List.of(), List.of(), divinka)), List.of(zilinaDivinka2), null);
        // Žilina --> K --> Divinka
        Path zilinaDivinka_C = new Path(zilina,
                List.of(new Path(k, List.of(new Path(divinka, List.of(), List.of(), divinka)), List.of(kDivinka), null)),
                List.of(zilinaK), null);
        Path pathZilinaDivinka = new Path(zilina,
                List.of(zilinaDivinka_A, zilinaDivinka_B, zilinaDivinka_C),
                List.of(zilinaDivinka1, zilinaDivinka2, zilinaK), null);

        // Divinka --> Žilina
        Path divinkaZilina_A = new Path(divinka, List.of(new Path(zilina, List.of(), List.of(), zilina)), List.of(divinkaZilina1), null);
        Path divinkaZilina_B = new Path(divinka, List.of(new Path(zilina, List.of(), List.of(), zilina)), List.of(divinkaZilina2), null);
        Path divinkaZilina_C = new Path(divinka,
                List.of(new Path(k, List.of(new Path(zilina, List.of(), List.of(), zilina)), List.of(kZilina), null)),
                List.of(divinkaK), null);
        Path pathDivinkaZilina = new Path(divinka,
                List.of(divinkaZilina_A, divinkaZilina_B, divinkaZilina_C),
                List.of(divinkaZilina1, divinkaZilina2, divinkaK), null);


        // Žilina – Strečno
        // Žilina --> zilinaStrecnoNorth --> Strečno
        Path zilinaNorthJunction = new Path(zilinaStrecnoNorth,
                List.of(new Path(strecno, List.of(), List.of(), strecno)),
                List.of(zilinaStrecnoNorthToStrecno), null);
        Path zilinaStrecno_A = new Path(zilina, List.of(zilinaNorthJunction), List.of(zilinaStrecnoNorthEdge), null);
        // Žilina --> zilinaStrecnoSouth --> Strečno
        Path zilinaSouthJunction = new Path(zilinaStrecnoSouth,
                List.of(new Path(strecno, List.of(), List.of(), strecno)),
                List.of(zilinaStrecnoSouthToStrecno), null);
        Path zilinaStrecno_B = new Path(zilina, List.of(zilinaSouthJunction), List.of(zilinaToZilinaStrecnoSouth), null);
        // Žilina --> K --> Strečno
        Path zilinaStrecno_C = new Path(zilina,
                List.of(new Path(k, List.of(new Path(strecno, List.of(), List.of(), strecno)), List.of(kStrecno), null)),
                List.of(zilinaK), null);
        Path pathZilinaStrecno = new Path(zilina,
                List.of(zilinaStrecno_A, zilinaStrecno_B, zilinaStrecno_C),
                List.of(zilinaStrecnoNorthEdge, zilinaToZilinaStrecnoSouth, zilinaK), null);

        // Strečno --> zilinaStrecnoNorth --> Žilina
        Path strecnoNorthJunction = new Path(zilinaStrecnoNorth,
                List.of(new Path(zilina, List.of(), List.of(), zilina)),
                List.of(strecnoNorthZilinaEdge), null);
        Path strecnoZilina_A = new Path(strecno, List.of(strecnoNorthJunction), List.of(strecnoToZilinaStrecnoNorth), null);
        // Strečno --> zilinaStrecnoSouth --> Žilina
        Path strecnoSouthJunction = new Path(zilinaStrecnoSouth,
                List.of(new Path(zilina, List.of(), List.of(), zilina)),
                List.of(zilinaStrecnoSouthToZilina), null);
        Path strecnoZilina_B = new Path(strecno, List.of(strecnoSouthJunction), List.of(strecnoToZilinaStrecnoSouth), null);
        // Strečno --> K --> Žilina
        Path strecnoZilina_C = new Path(strecno,
                List.of(new Path(k, List.of(new Path(zilina, List.of(), List.of(), zilina)), List.of(kZilina), null)),
                List.of(strecnoK), null);
        Path pathStrecnoZilina = new Path(strecno,
                List.of(strecnoZilina_A, strecnoZilina_B, strecnoZilina_C),
                List.of(strecnoToZilinaStrecnoNorth, strecnoToZilinaStrecnoSouth, strecnoK), null);


        // Žilina – Rajecké Teplice
        // Žilina --> zilinaStrecnoNorth --> Strečno --> strecnoRajeckeTeplice --> RT
        Path strecnoRT_junction = new Path(strecnoRajeckeTeplice,
                List.of(new Path(rajeckeTeplice, List.of(), List.of(), rajeckeTeplice)),
                List.of(strecnoRajeckeTepliceToRajeckeTeplice), null);
        Path strecnoToRT_blue = new Path(strecno, List.of(strecnoRT_junction), List.of(strecnoToStrecnoRajeckeTepliceBlue), null);
        Path strecnoToRT_black = new Path(strecno, List.of(strecnoRT_junction), List.of(strecnoToStrecnoRajeckeTepliceBlack), null);
        Path strecnoToRT = new Path(strecno,
                List.of(strecnoToRT_blue, strecnoToRT_black),
                List.of(strecnoToStrecnoRajeckeTepliceBlue, strecnoToStrecnoRajeckeTepliceBlack), null);

        Path northJunctionToStrecnoToRT = new Path(zilinaStrecnoNorth, List.of(strecnoToRT), List.of(zilinaStrecnoNorthToStrecno), null);
        Path zilinaRT_A = new Path(zilina, List.of(northJunctionToStrecnoToRT), List.of(zilinaStrecnoNorthEdge), null);

        // Žilina --> zilinaStrecnoSouth --> Strečno --> strecnoRajeckeTeplice --> RT
        Path southJunctionToStrecnoToRT = new Path(zilinaStrecnoSouth, List.of(strecnoToRT), List.of(zilinaStrecnoSouthToStrecno), null);
        Path zilinaRT_B = new Path(zilina, List.of(southJunctionToStrecnoToRT), List.of(zilinaToZilinaStrecnoSouth), null);

        // Žilina --> K --> RT
        Path zilinaRT_C = new Path(zilina,
                List.of(new Path(k, List.of(new Path(rajeckeTeplice, List.of(), List.of(), rajeckeTeplice)), List.of(kRajeckeTeplice), null)),
                List.of(zilinaK), null);

        Path pathZilinaRT = new Path(zilina,
                List.of(zilinaRT_A, zilinaRT_B, zilinaRT_C),
                List.of(zilinaStrecnoNorthEdge, zilinaToZilinaStrecnoSouth, zilinaK), null);

        // Rajecké Teplice --> Žilina
        // RT --> strecnoRajeckeTeplice --> Strečno --> north junction --> Žilina
        Path rtJunction = new Path(strecnoRajeckeTeplice,
                List.of(
                        new Path(strecno, List.of(strecnoZilina_A), List.of(strecnoToZilinaStrecnoNorth), null),
                        new Path(strecno, List.of(strecnoZilina_B), List.of(strecnoToZilinaStrecnoSouth), null),
                        new Path(strecno, List.of(strecnoZilina_C), List.of(strecnoK), null)
                ),
                List.of(strecnoRajeckeTepliceToStrecnoBlue, strecnoRajeckeTepliceToStrecnoBlack, strecnoRajeckeTepliceToStrecnoBlue),
                null);
        Path rtZilina_A = new Path(rajeckeTeplice, List.of(rtJunction), List.of(rajeckeTepliceToStrecnoRajeckeTeplice), null);

        // RT --> K --> Žilina
        Path rtZilina_B = new Path(rajeckeTeplice,
                List.of(new Path(k, List.of(new Path(zilina, List.of(), List.of(), zilina)), List.of(kZilina), null)),
                List.of(rajeckeTepliceK), null);

        Path pathRTZilina = new Path(rajeckeTeplice,
                List.of(rtZilina_A, rtZilina_B),
                List.of(rajeckeTepliceToStrecnoRajeckeTeplice, rajeckeTepliceK), null);


        // Divinka – Strečno
        // Divinka --> K --> Strečno
        Path pathDivinkaStrecno = new Path(divinka,
                List.of(new Path(k, List.of(new Path(strecno, List.of(), List.of(), strecno)), List.of(kStrecno), null)),
                List.of(divinkaK), null);

        // Strečno --> Divinka
        Path pathStrechnoDivinka = new Path(strecno,
                List.of(new Path(k, List.of(new Path(divinka, List.of(), List.of(), divinka)), List.of(kDivinka), null)),
                List.of(strecnoK), null);


        // Divinka – Rajecké Teplice
        // Divinka --> x3Divinka --> rajeckeTepliceX1 --> RT
        Path x1ToRT = new Path(rajeckeTepliceX1,
                List.of(new Path(rajeckeTeplice, List.of(), List.of(), rajeckeTeplice)),
                List.of(rajeckeTepliceX1ToRajeckeTeplice), null);
        Path x3ToX1_direct = new Path(x3Divinka, List.of(x1ToRT), List.of(x3DivinkaToRajeckeTepliceX1), null);

        // Divinka --> x3Divinka --> x1X2 --> rajeckeTepliceX1 --> RT
        Path x1X2ToX1 = new Path(x1X2, List.of(x1ToRT), List.of(x1X2ToRajeckeTepliceX1), null);
        Path x3ToX1X2 = new Path(x3Divinka, List.of(x1X2ToX1), List.of(x1X2ToX3Divinka), null);
        Path x3ToRT = new Path(x3Divinka,
                List.of(x3ToX1_direct, x3ToX1X2),
                List.of(x3DivinkaToRajeckeTepliceX1, x1X2ToX3Divinka), null);

        Path divinkaRT_A = new Path(divinka, List.of(x3ToRT), List.of(divinkaToX3Divinka), null);

        // Divinka --> K --> RT
        Path divinkaRT_B = new Path(divinka,
                List.of(new Path(k, List.of(new Path(rajeckeTeplice, List.of(), List.of(), rajeckeTeplice)), List.of(kRajeckeTeplice), null)),
                List.of(divinkaK), null);

        Path pathDivinkaRT = new Path(divinka,
                List.of(divinkaRT_A, divinkaRT_B),
                List.of(divinkaToX3Divinka, divinkaK), null);

        // Rajecké Teplice --> Divinka
        // RT --> rajeckeTepliceX1 --> x1X2 --> x3Divinka --> Divinka
        Path x3ToDivinka = new Path(x3Divinka,
                List.of(new Path(divinka, List.of(), List.of(), divinka)),
                List.of(x3DivinkaToDivinka), null);
        Path x1X2ToX3 = new Path(x1X2, List.of(x3ToDivinka), List.of(x1X2ToX3Divinka), null);
        Path x1ToX1X2 = new Path(rajeckeTepliceX1, List.of(x1X2ToX3), List.of(rajeckeTepliceX1ToX1X2), null);

        // RT --> rajeckeTepliceX1 --> x3Divinka --> Divinka
        Path x1ToX3_direct = new Path(rajeckeTepliceX1, List.of(x3ToDivinka), List.of(rajeckeTepliceX1ToX3Divinka), null);

        Path x1ToDivinka = new Path(rajeckeTepliceX1,
                List.of(x1ToX1X2, x1ToX3_direct),
                List.of(rajeckeTepliceX1ToX1X2, rajeckeTepliceX1ToX3Divinka), null);

        Path rtDivinka_A = new Path(rajeckeTeplice, List.of(x1ToDivinka), List.of(rajeckeTepliceToRajeckeTepliceX1), null);

        // RT --> K --> Divinka
        Path rtDivinka_B = new Path(rajeckeTeplice,
                List.of(new Path(k, List.of(new Path(divinka, List.of(), List.of(), divinka)), List.of(kDivinka), null)),
                List.of(rajeckeTepliceK), null);

        Path pathRTDivinka = new Path(rajeckeTeplice,
                List.of(rtDivinka_A, rtDivinka_B),
                List.of(rajeckeTepliceToRajeckeTepliceX1, rajeckeTepliceK), null);


        // Strečno – Rajecké Teplice
        // Strečno --> strecnoRajeckeTeplice --> RT
        Path strecnoRT_A = new Path(strecno, List.of(strecnoRT_junction), List.of(strecnoToStrecnoRajeckeTepliceBlue), null);
        // Strečno --> strecnoRajeckeTeplice --> RT
        Path strecnoRT_B = new Path(strecno, List.of(strecnoRT_junction), List.of(strecnoToStrecnoRajeckeTepliceBlack), null);
        // Strečno --> K --> RT
        Path strecnoRT_C = new Path(strecno,
                List.of(new Path(k, List.of(new Path(rajeckeTeplice, List.of(), List.of(), rajeckeTeplice)), List.of(kRajeckeTeplice), null)),
                List.of(strecnoK), null);
        Path pathStrecnoRT = new Path(strecno,
                List.of(strecnoRT_A, strecnoRT_B, strecnoRT_C),
                List.of(strecnoToStrecnoRajeckeTepliceBlue, strecnoToStrecnoRajeckeTepliceBlack, strecnoK), null);

        // Rajecké Teplice --> Strečno
        // RT --> strecnoRajeckeTeplice --> Strečno
        Path rtStrecno_junction_blue = new Path(strecnoRajeckeTeplice,
                List.of(new Path(strecno, List.of(), List.of(), strecno)),
                List.of(strecnoRajeckeTepliceToStrecnoBlue), null);
        Path rtStrecno_A = new Path(rajeckeTeplice, List.of(rtStrecno_junction_blue), List.of(rajeckeTepliceToStrecnoRajeckeTeplice), null);
        // RT --> strecnoRajeckeTeplice --> Strečno
        Path rtStrecno_junction_black = new Path(strecnoRajeckeTeplice,
                List.of(new Path(strecno, List.of(), List.of(), strecno)),
                List.of(strecnoRajeckeTepliceToStrecnoBlack), null);
        Path rtStrecno_B = new Path(rajeckeTeplice, List.of(rtStrecno_junction_black), List.of(rajeckeTepliceToStrecnoRajeckeTeplice), null);
        // RT --> K --> Strečno
        Path rtStrecno_C = new Path(rajeckeTeplice,
                List.of(new Path(k, List.of(new Path(strecno, List.of(), List.of(), strecno)), List.of(kStrecno), null)),
                List.of(rajeckeTepliceK), null);
        Path pathRTStrecno = new Path(rajeckeTeplice,
                List.of(rtStrecno_A, rtStrecno_B, rtStrecno_C),
                List.of(rajeckeTepliceToStrecnoRajeckeTeplice, rajeckeTepliceToStrecnoRajeckeTeplice, rajeckeTepliceK), null);


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

        return List.of(route1, route2, route3, route4, route5, route6);
    }
}

