package uniza.fri.majba.dis1.simulation_core;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Core class for running simulations with support for pausing and resuming.
 * It manages the lifecycle of replications and allows for controlled execution.
 */
public abstract class SimulationCore {
    private final Replication replication;
    private final ReentrantLock pauseLock = new ReentrantLock();
    private final Condition resumeCondition = pauseLock.newCondition();
    private volatile boolean paused = false;

    protected SimulationCore(Replication replication) {
        this.replication = replication;
    }

    /**
     * Runs the simulation for a specified number of replications.
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
    private void waitWhilePaused() {
        if (!paused) {
            return;
        }

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
     * Pauses the simulation. The simulation will remain paused until resumeSimulation() is called.
     */
    public void pauseSimulation() {
        paused = true;
    }

    /**
     * Resumes the simulation if it is currently paused. If the simulation is not paused, this method does nothing.
     */
    public void resumeSimulation() {
        pauseLock.lock();
        try {
            paused = false;
            resumeCondition.signal();
        } finally {
            pauseLock.unlock();
        }
    }
}
