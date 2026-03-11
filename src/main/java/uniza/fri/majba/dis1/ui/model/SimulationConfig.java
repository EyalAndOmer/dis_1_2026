package uniza.fri.majba.dis1.ui.model;

import javafx.beans.property.*;

/**
 * Holds all user-configurable simulation parameters.
 */
public final class SimulationConfig {
    private final LongProperty seedGeneratorSeed = new SimpleLongProperty(10);
    private final IntegerProperty defaultReplications = new SimpleIntegerProperty(10_000_000);
    private final DoubleProperty skipPercentage = new SimpleDoubleProperty(10);
    private final DoubleProperty renderPercentage = new SimpleDoubleProperty(0.1);
    private final DoubleProperty simulationStartTime = new SimpleDoubleProperty(6.0);

    public SimulationConfig() {
    }

    public long getSeedGeneratorSeed() { return seedGeneratorSeed.get(); }
    public void setSeedGeneratorSeed(long value) { seedGeneratorSeed.set(value); }

    public int getDefaultReplications() { return defaultReplications.get(); }
    public void setDefaultReplications(int value) { defaultReplications.set(value); }

    public double getSkipPercentage() { return skipPercentage.get(); }
    public void setSkipPercentage(double value) { skipPercentage.set(value); }

    public double getRenderPercentage() { return renderPercentage.get(); }
    public void setRenderPercentage(double value) { renderPercentage.set(value); }

    public double getSimulationStartTime() { return simulationStartTime.get(); }
    public void setSimulationStartTime(double value) { simulationStartTime.set(value); }
}
