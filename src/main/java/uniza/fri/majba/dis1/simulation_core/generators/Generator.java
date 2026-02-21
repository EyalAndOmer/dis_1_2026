package uniza.fri.majba.dis1.simulation_core.generators;

import java.util.Random;

/**
 * Abstract class representing a generator for random values.
 */
public abstract class Generator extends Random {
    public abstract double generate();
}
