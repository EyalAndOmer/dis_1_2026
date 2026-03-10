package uniza.fri.majba.dis1.simulation_core.generators;

/**
 * Configuration for empiric generator. It defines the lower and upper bounds of the generated values and the probability of generation.
 * @param lowerBound the lower bound of the generated values (inclusive)
 * @param upperBound the upper bound of the generated values (inclusive)
 * @param generationProbability the probability of generation (must be in range [0.0, 1.0])
 */
public record EmpiricGeneratorConfiguration(
        double lowerBound,
        double upperBound,
        double generationProbability) {

    public EmpiricGeneratorConfiguration {
        if (generationProbability < 0.0 || generationProbability > 1.0) {
            throw new IllegalArgumentException(
                    "generationProbability must be in range [0.0, 1.0]"
            );
        }
        if (lowerBound >= upperBound) {
            throw new IllegalArgumentException(
                    "Lower bound must be less than upper bound"
            );
        }
    }
}
