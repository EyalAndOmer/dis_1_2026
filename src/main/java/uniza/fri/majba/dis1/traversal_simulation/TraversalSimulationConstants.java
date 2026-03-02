package uniza.fri.majba.dis1.traversal_simulation;

import uniza.fri.majba.dis1.simulation_core.generators.*;

import java.util.List;

public final class TraversalSimulationConstants {

    public static final DiscreteUniformGenerator RED_EDGE_GENERATOR = new DiscreteUniformGenerator(55, 75);
    public static final ContinuousUniformGenerator GREEN_EDGE_GENERATOR = new ContinuousUniformGenerator(50, 80);

    public static final List<EmpiricGeneratorConfiguration> BLACK_EDGE_GENERATOR_CONFIGURATION = List.of(
            new EmpiricGeneratorConfiguration(10, 20, 0.1),
            new EmpiricGeneratorConfiguration(20, 32, 0.5),
            new EmpiricGeneratorConfiguration(32, 45, 0.2),
            new EmpiricGeneratorConfiguration(45, 75, 0.15),
            new EmpiricGeneratorConfiguration(75, 85, 0.05)
    );
    public static final ContinuousEmpiricGenerator BLACK_EDGE_GENERATOR = new ContinuousEmpiricGenerator(BLACK_EDGE_GENERATOR_CONFIGURATION);

    public static final List<EmpiricGeneratorConfiguration> BLUE_EDGE_GENERATOR_CONFIGURATION = List.of(
            new EmpiricGeneratorConfiguration(15, 28, 0.2),
            new EmpiricGeneratorConfiguration(29, 44, 0.4),
            new EmpiricGeneratorConfiguration(45, 65, 0.4)
    );
    public static final DiscreteEmpiricGenerator BLUE_EDGE_GENERATOR = new DiscreteEmpiricGenerator(BLUE_EDGE_GENERATOR_CONFIGURATION);

    /**
     * Generator maps additional slowdown of a special edge on the graph - due to traffic jams
     */
    public static final ContinuousUniformGenerator K_GENERATOR = new ContinuousUniformGenerator(10, 25);

    private TraversalSimulationConstants() {
        // utility class
    }
}

