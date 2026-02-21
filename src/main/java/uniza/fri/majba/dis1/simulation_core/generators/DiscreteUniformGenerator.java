package uniza.fri.majba.dis1.simulation_core.generators;

import uniza.fri.majba.dis1.Config;

/**
 * A generator for producing random values following a discrete uniform distribution.
 * The generated values are integers within the specified bounds (inclusive).
 */
public class DiscreteUniformGenerator extends Generator {
    private final int lowerBound;
    private final int upperBound;
    /**
     * Creates a discrete uniform generator instance with an initialized seed and bounds
     * @param lowerBound the lower bound of the generation interval (inclusive)
     * @param upperBound the upper bound of the generation interval (inclusive)
     */
    public DiscreteUniformGenerator(int lowerBound, int upperBound) {
        if (lowerBound >= upperBound) {
            throw new IllegalArgumentException("Lower bound must be less than upper bound");
        }
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        super.setSeed(Config.getSeedGenerator().nextInt());
    }

    public double generate() {
        return this.lowerBound + (double) super.nextInt(this.upperBound - this.lowerBound + 1);
    }
}
