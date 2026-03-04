package uniza.fri.majba.dis1.simulation_core.generators;

import java.util.List;
import java.util.Random;

public class ContinuousEmpiricGenerator extends EmpiricGenerator {
    public ContinuousEmpiricGenerator(List<EmpiricGeneratorConfiguration> configurations, Random seedGenerator) {
        super(configurations, seedGenerator);
    }

    @Override
    protected Generator createGenerator(EmpiricGeneratorConfiguration configuration) {
        return new ContinuousUniformGenerator(configuration.lowerBound(), configuration.upperBound(), seedGenerator);
    }
}
