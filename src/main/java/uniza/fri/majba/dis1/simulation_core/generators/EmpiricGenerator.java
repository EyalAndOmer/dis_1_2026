package uniza.fri.majba.dis1.simulation_core.generators;

import uniza.fri.majba.dis1.Config;

import java.util.List;
import java.util.Random;

import java.util.ArrayList;

/**
 * Generator that produces random values based on a list of configurations, where each configuration has an associated probability.
 * The generator selects one of the configurations based on the probabilities and generates a value accordingly.
 */
public abstract class EmpiricGenerator extends Generator {
    /**
     * A small constant used to check if the sum of probabilities is close enough to 1.0, accounting for floating-point precision issues.
     */
    public static final double DELTA = 1.0E-12;
    private final Random probabilityRandom;
    private final List<ProbabilityGeneratorPair> probabilityGeneratorPairs;


    protected EmpiricGenerator(List<EmpiricGeneratorConfiguration> configurations) {
        this.probabilityRandom = new Random(Config.getSeedGenerator().nextInt());

        double sum = 0.0;
        List<ProbabilityGeneratorPair> pairs = new ArrayList<>();

        for (EmpiricGeneratorConfiguration configuration : configurations) {
            sum += configuration.generationProbability();
            pairs.add(new ProbabilityGeneratorPair(sum, createGenerator(configuration)));
        }

        if (Math.abs(sum - 1.0) > DELTA) {
            throw new IllegalStateException("Total probabilities must sum to 1.0. Actual sum: " + sum);
        }

        this.probabilityGeneratorPairs = List.copyOf(pairs);
    }

    Generator selectGenerator(double probability) {
        if (probability < 0.0 || probability >= 1.0) {
            throw new IllegalArgumentException("Probability out of range [0.0, 1.0): " + probability);
        }

        for (ProbabilityGeneratorPair pair : probabilityGeneratorPairs) {
            if (probability <= pair.probability()) {
                return pair.generator();
            }
        }

        // Handles the edge case where probability is equal to 1.0
        return probabilityGeneratorPairs.getLast().generator();
    }

    protected abstract Generator createGenerator(EmpiricGeneratorConfiguration configuration);

    @Override
    public double generate() {
        double probability = this.probabilityRandom.nextDouble();
        Generator pickedGenerator = this.selectGenerator(probability);

        return pickedGenerator.generate();
    }

    private record ProbabilityGeneratorPair(double probability, Generator generator) { }
}
