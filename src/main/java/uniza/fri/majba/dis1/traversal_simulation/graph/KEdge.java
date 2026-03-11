package uniza.fri.majba.dis1.traversal_simulation.graph;

import uniza.fri.majba.dis1.simulation_core.generators.ContinuousUniformGenerator;
import uniza.fri.majba.dis1.simulation_core.generators.Generator;

/**
 * A special edge departing from junction K.
 * After 6:25, traffic jams cause additional slowdown modeled as Uniform(10, 25) percent.
 */
public final class KEdge<V> extends Edge<V> {

    /** 6 hours + 30 minutes expressed in hours */
    private static final double K_SLOWDOWN_THRESHOLD = 6.0 + 30.0 / 60.0;

    private final ContinuousUniformGenerator slowdownGenerator;

    public KEdge(V from, V to, EdgeColor edgeColor, int weight, Generator generator,
                 ContinuousUniformGenerator slowdownGenerator) {
        super(from, to, edgeColor, weight, generator);
        this.slowdownGenerator = slowdownGenerator;
    }

    /**
     * If the courier departs from K after 6:25, the generated speed is reduced
     * by a uniformly distributed slowdown percentage in [10, 25).
     */
    @Override
    public double getTraversalTime(double departureTime) {
        double speed = generator().generate();
        if (departureTime > K_SLOWDOWN_THRESHOLD) {
            double slowdownPercentage = slowdownGenerator.generate();
            speed = speed * ((100.0 - slowdownPercentage) / 100.0);
        }
        return weight() / speed;
    }
}

