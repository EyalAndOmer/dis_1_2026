package uniza.fri.majba.dis1.traversal_simulation;

import uniza.fri.majba.dis1.simulation_core.generators.*;
import uniza.fri.majba.dis1.ui.model.SimulationConfig;

import java.util.List;
import java.util.Random;

public final class TraversalSimulationConstants {

    private static Random seedGenerator;

    public static final List<EmpiricGeneratorConfiguration> BLACK_EDGE_GENERATOR_CONFIGURATION = List.of(
            new EmpiricGeneratorConfiguration(10, 20, 0.1),
            new EmpiricGeneratorConfiguration(20, 32, 0.5),
            new EmpiricGeneratorConfiguration(32, 45, 0.2),
            new EmpiricGeneratorConfiguration(45, 75, 0.15),
            new EmpiricGeneratorConfiguration(75, 85, 0.05)
    );

    public static final List<EmpiricGeneratorConfiguration> BLUE_EDGE_GENERATOR_CONFIGURATION = List.of(
            new EmpiricGeneratorConfiguration(15, 28, 0.2),
            new EmpiricGeneratorConfiguration(29, 44, 0.4),
            new EmpiricGeneratorConfiguration(45, 64, 0.4)
    );

    // Generators are rebuilt on each call so they pick up the latest seed from config
    public static DiscreteUniformGenerator RED_EDGE_GENERATOR;
    public static ContinuousUniformGenerator GREEN_EDGE_GENERATOR;
    public static ContinuousEmpiricGenerator BLACK_EDGE_GENERATOR;
    public static DiscreteEmpiricGenerator BLUE_EDGE_GENERATOR;
    public static ContinuousUniformGenerator K_GENERATOR;

    /**
     * Rebuilds all generators using the current seed from {@link SimulationConfig}.
     * Must be called before each simulation run to pick up config changes.
     */
    public static void rebuildGenerators() {
        seedGenerator = new Random(SimulationConfig.getInstance().getSeedGeneratorSeed());

        RED_EDGE_GENERATOR = new DiscreteUniformGenerator(55, 75, seedGenerator);
        GREEN_EDGE_GENERATOR = new ContinuousUniformGenerator(50, 80, seedGenerator);
        BLACK_EDGE_GENERATOR = new ContinuousEmpiricGenerator(BLACK_EDGE_GENERATOR_CONFIGURATION, seedGenerator);
        BLUE_EDGE_GENERATOR = new DiscreteEmpiricGenerator(BLUE_EDGE_GENERATOR_CONFIGURATION, seedGenerator);
        K_GENERATOR = new ContinuousUniformGenerator(10, 25, seedGenerator);
    }

    static {
        rebuildGenerators();
    }

    private TraversalSimulationConstants() {
        // utility class
    }
}

