package uniza.fri.majba.dis1.simulation_core.generators;

import java.util.List;

/**
 * A generator that produces discrete random values based on empiric configurations.
 * The bounds of the generators are rounded to the nearest integer.
 */
public class DiscreteEmpiricGenerator extends EmpiricGenerator {
    public DiscreteEmpiricGenerator(List<EmpiricGeneratorConfiguration> configurations) {
        super(configurations);
    }

    @Override
    public Generator createGenerator(EmpiricGeneratorConfiguration configuration) {
        return new DiscreteUniformGenerator((int) Math.round(configuration.lowerBound()), (int) Math.round(configuration.upperBound()));
    }
}
