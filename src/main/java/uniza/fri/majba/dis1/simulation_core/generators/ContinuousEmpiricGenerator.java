package uniza.fri.majba.dis1.simulation_core.generators;

import java.util.List;

public class ContinuousEmpiricGenerator extends EmpiricGenerator {
    public ContinuousEmpiricGenerator(List<EmpiricGeneratorConfiguration> configurations) {
        super(configurations);
    }

    @Override
    protected Generator createGenerator(EmpiricGeneratorConfiguration configuration) {
        return null;
    }
}
