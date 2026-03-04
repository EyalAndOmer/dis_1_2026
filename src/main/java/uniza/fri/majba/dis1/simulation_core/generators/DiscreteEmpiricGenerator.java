package uniza.fri.majba.dis1.simulation_core.generators;

import java.util.List;
import java.util.Random;

/**
 * A generator that produces discrete random values based on empiric configurations.
 * The bounds of the generators are rounded to the nearest integer.
 */
public class DiscreteEmpiricGenerator extends EmpiricGenerator {
    public DiscreteEmpiricGenerator(List<EmpiricGeneratorConfiguration> configurations, Random seedGenerator) {
        super(configurations, seedGenerator);
    }

    @Override
    public Generator createGenerator(EmpiricGeneratorConfiguration configuration) {
        return new DiscreteUniformGenerator((int) Math.round(configuration.lowerBound()), (int) Math.round(configuration.upperBound()), seedGenerator);
    }
}
