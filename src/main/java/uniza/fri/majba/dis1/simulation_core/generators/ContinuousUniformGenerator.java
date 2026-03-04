package uniza.fri.majba.dis1.simulation_core.generators;


import java.util.Random;

public class ContinuousUniformGenerator extends Generator {
    private final double lowerBound;
    private final double upperBound;
    private final Random seedGenerator;
    /**
     * Creates a continuous uniform generator instance with an initialized seed and bounds
     * @param lowerBound the lower bound of the generation interval (inclusive)
     * @param upperBound the upper bound of the generation interval (exclusive)
     */
    public ContinuousUniformGenerator(double lowerBound, double upperBound, Random seedGenerator) {
        if (lowerBound >= upperBound) {
            throw new IllegalArgumentException("Lower bound must be less than upper bound");
        }

        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.seedGenerator = seedGenerator;
        super.setSeed(seedGenerator.nextInt());
    }

    @Override
    public double generate() {
        return this.lowerBound + (this.upperBound - this.lowerBound) * super.nextDouble();
    }
}
