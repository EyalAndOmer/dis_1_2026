package uniza.fri.majba.dis1.ui.model;

import javafx.beans.property.*;

/**
 * Singleton holding all user-configurable simulation parameters.
 * Both the configuration view and the simulation classes read/write through this.
 */
public final class SimulationConfig {

    private static final SimulationConfig INSTANCE = new SimulationConfig();

    public static SimulationConfig getInstance() {
        return INSTANCE;
    }

    // ── Second task constants ──

    private final IntegerProperty monteCarloSamples = new SimpleIntegerProperty(100_000);
    private final DoubleProperty earliestDeparture = new SimpleDoubleProperty(6.0);
    private final IntegerProperty mustArriveByHour = new SimpleIntegerProperty(7);
    private final IntegerProperty mustArriveByMinute = new SimpleIntegerProperty(35);
    private final DoubleProperty requiredOnTimePercentage = new SimpleDoubleProperty(0.80);
    private final DoubleProperty initialStep = new SimpleDoubleProperty(2.0);
    private final DoubleProperty precision = new SimpleDoubleProperty(1.0 / 3600);

    // ── Seed generator ──

    private final LongProperty seedGeneratorSeed = new SimpleLongProperty(-53);

    // ── First task constants ──

    private final IntegerProperty defaultReplications = new SimpleIntegerProperty(10_000_000);
    private final DoubleProperty skipPercentage = new SimpleDoubleProperty(10);
    private final DoubleProperty renderPercentage = new SimpleDoubleProperty(0.1);

    private SimulationConfig() {
    }

    // ── Computed ──

    public double getMustArriveBy() {
        return mustArriveByHour.get() + mustArriveByMinute.get() / 60.0;
    }

    // ── Properties ──

    public IntegerProperty monteCarloSamplesProperty() { return monteCarloSamples; }
    public int getMonteCarloSamples() { return monteCarloSamples.get(); }
    public void setMonteCarloSamples(int value) { monteCarloSamples.set(value); }

    public DoubleProperty earliestDepartureProperty() { return earliestDeparture; }
    public double getEarliestDeparture() { return earliestDeparture.get(); }
    public void setEarliestDeparture(double value) { earliestDeparture.set(value); }

    public IntegerProperty mustArriveByHourProperty() { return mustArriveByHour; }
    public int getMustArriveByHour() { return mustArriveByHour.get(); }
    public void setMustArriveByHour(int value) { mustArriveByHour.set(value); }

    public IntegerProperty mustArriveByMinuteProperty() { return mustArriveByMinute; }
    public int getMustArriveByMinute() { return mustArriveByMinute.get(); }
    public void setMustArriveByMinute(int value) { mustArriveByMinute.set(value); }

    public DoubleProperty requiredOnTimePercentageProperty() { return requiredOnTimePercentage; }
    public double getRequiredOnTimePercentage() { return requiredOnTimePercentage.get(); }
    public void setRequiredOnTimePercentage(double value) { requiredOnTimePercentage.set(value); }

    public DoubleProperty initialStepProperty() { return initialStep; }
    public double getInitialStep() { return initialStep.get(); }
    public void setInitialStep(double value) { initialStep.set(value); }

    public DoubleProperty precisionProperty() { return precision; }
    public double getPrecision() { return precision.get(); }
    public void setPrecision(double value) { precision.set(value); }

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
}

