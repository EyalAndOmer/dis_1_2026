package uniza.fri.majba.dis1.simulation_core;

import java.util.concurrent.atomic.AtomicBoolean;
/**
 * Core class for running simulations with support for pausing and resuming.
 */
public abstract class SimulationCore {
    private final Replication replication;
    private volatile boolean paused = false;
    private final AtomicBoolean pauseRequested = new AtomicBoolean(false);

    protected SimulationCore(Replication replication) {
        this.replication = replication;
    }

    /**
     * Runs the simulation for a specified number of replications.
     * This is a blocking method and therefore should be called from a separate thread.
     * @param numberOfReplications the number of replications to execute
     */
    public void simulate(int numberOfReplications) {
        this.replication.beforeAllReplications();

        for (int i = 0; i < numberOfReplications; i++) {
            if (Thread.currentThread().isInterrupted()) {
                break;
            }

            while (pauseRequested.get()) {
                try {
                    Thread.sleep(100); // Sleep briefly to avoid busy-waiting
                    this.paused = true;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restore interrupted status
                    break;
                }

            }
            this.replication.beforeReplication();
            this.replication.execute();
            this.replication.afterReplication();
        }

        this.replication.afterAllReplications();
    }

    /**
     * Pauses the simulation. The pause will take effect after the current replication finishes executing.
     * The simulation will remain paused until resumeSimulation() is called.
     */
    public void pauseSimulation() {
        pauseRequested.set(true);
    }

    /**
     * Resumes the simulation if it is currently paused. If the simulation is not paused, this method does nothing.
     */
    public void resumeSimulation() {
        if (paused) {
            pauseRequested.set(false);
        }
    }
}