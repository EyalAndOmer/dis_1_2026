package uniza.fri.majba.dis1.simulation_core.generators;


import uniza.fri.majba.dis1.Config;

public class ContinuousUniformGenerator extends Generator {
    private final double lowerBound;
    private final double upperBound;
    /**
     * Creates a continuous uniform generator instance with an initialized seed and bounds
     * @param lowerBound the lower bound of the generation interval (inclusive)
     * @param upperBound the upper bound of the generation interval (exclusive)
     */
    public ContinuousUniformGenerator(double lowerBound, double upperBound) {
        if (lowerBound >= upperBound) {
            throw new IllegalArgumentException("Lower bound must be less than upper bound");
        }

        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        super.setSeed(Config.getSeedGenerator().nextInt());
    }

    @Override
    public double generate() {
        return this.lowerBound + (this.upperBound - this.lowerBound) * super.nextDouble();
    }
}
