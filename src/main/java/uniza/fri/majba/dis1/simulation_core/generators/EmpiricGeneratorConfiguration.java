package uniza.fri.majba.dis1.simulation_core.generators;

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
                    "lowerBound must be less than upperBound"
            );
        }
    }
}
