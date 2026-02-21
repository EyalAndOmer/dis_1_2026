package uniza.fri.majba.dis1.simulation_core.generators;

import java.util.List;

public class UniformEmpiricGenerator extends EmpiricGenerator {
    public UniformEmpiricGenerator(List<EmpiricGeneratorConfiguration> configurations) {
        super(configurations);
    }

    @Override
    public Generator createGenerator(EmpiricGeneratorConfiguration configuration) {
        return new ContinuousUniformGenerator(configuration.lowerBound(), configuration.upperBound());
    }
}
