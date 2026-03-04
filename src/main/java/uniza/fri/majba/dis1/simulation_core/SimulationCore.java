package uniza.fri.majba.dis1.simulation_core;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Core class for running simulations with support for pausing and resuming.
 */
public abstract class SimulationCore {
    private final Replication replication;
    // TODO odstranit, zatial nepotrebne
    private final ReentrantLock pauseLock = new ReentrantLock();
    private final Condition resumeCondition = pauseLock.newCondition();
    private volatile boolean paused = false;

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

            waitWhilePaused();

            this.replication.beforeReplication();
            this.replication.execute();
            this.replication.afterReplication();
        }

        this.replication.afterAllReplications();
    }

    /**
     * Waits while the simulation is paused. If the simulation is not paused, the method does nothing.
     */
    void waitWhilePaused() {
        pauseLock.lock();
        try {
            while (paused) {
                resumeCondition.await();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // restore interrupt flag
        } finally {
            pauseLock.unlock();
        }
    }

    /**
     * Pauses the simulation. The pause will take effect after the current replication finishes executing.
     * The simulation will remain paused until resumeSimulation() is called.
     */
    public void pauseSimulation() {
        pauseLock.lock();
        try {
            paused = true;
        } finally {
            pauseLock.unlock();
        }
    }

    /**
     * Resumes the simulation if it is currently paused. If the simulation is not paused, this method does nothing.
     */
    public void resumeSimulation() {
        pauseLock.lock();
        try {
            paused = false;
            resumeCondition.signalAll();
        } finally {
            pauseLock.unlock();
        }
    }
}
