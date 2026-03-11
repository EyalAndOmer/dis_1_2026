package uniza.fri.majba.dis1.ui.model;

import javafx.beans.property.*;

/**
 * Singleton holding all user-configurable simulation parameters.
 */
public final class SimulationConfig {

    private static final SimulationConfig INSTANCE = new SimulationConfig();

    public static SimulationConfig getInstance() {
        return INSTANCE;
    }

    private final LongProperty seedGeneratorSeed = new SimpleLongProperty(10);

    private final IntegerProperty defaultReplications = new SimpleIntegerProperty(10_000_000);
    private final DoubleProperty skipPercentage = new SimpleDoubleProperty(10);
    private final DoubleProperty renderPercentage = new SimpleDoubleProperty(0.1);
    private final DoubleProperty simulationStartTime = new SimpleDoubleProperty(6.0);

    private SimulationConfig() {
    }

    public LongProperty seedGeneratorSeedProperty() { return seedGeneratorSeed; }
    public long getSeedGeneratorSeed() { return seedGeneratorSeed.get(); }
    public void setSeedGeneratorSeed(long value) { seedGeneratorSeed.set(value); }

    public IntegerProperty defaultReplicationsProperty() { return defaultReplications; }
    public int getDefaultReplications() { return defaultReplications.get(); }
    public void setDefaultReplications(int value) { defaultReplications.set(value); }

    public DoubleProperty skipPercentageProperty() { return skipPercentage; }
    public double getSkipPercentage() { return skipPercentage.get(); }
    public void setSkipPercentage(double value) { skipPercentage.set(value); }

    public DoubleProperty renderPercentageProperty() { return renderPercentage; }
    public double getRenderPercentage() { return renderPercentage.get(); }
    public void setRenderPercentage(double value) { renderPercentage.set(value); }

    public DoubleProperty simulationStartTimeProperty() { return simulationStartTime; }
    public double getSimulationStartTime() { return simulationStartTime.get(); }
    public void setSimulationStartTime(double value) { simulationStartTime.set(value); }
}
