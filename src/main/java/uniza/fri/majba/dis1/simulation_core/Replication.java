package uniza.fri.majba.dis1.simulation_core;

/**
 * Interface representing a replication in a simulation. It defines the lifecycle methods for executing a replication,
 */
public interface Replication {
    void beforeAllReplications();
    void afterAllReplications();
    void beforeReplication();
    void afterReplication();
    void execute();
}
