package uniza.fri.majba.dis1.simulation_core.statistics;

/**
 * Class representing a weighted sum statistic, which calculates the average of added values.
 */
public final class WeightedSumStatistic implements Statistic {
    double totalSum;
    int totalCount;

    public WeightedSumStatistic() {
        this.totalSum = 0;
        this.totalCount = 0;
    }

    @Override
    public double calculateStatistic() {
        return this.totalSum / this.totalCount;
    }

    @Override
    public void clearStatistic() {
        this.totalCount = 0;
        this.totalSum = 0;
    }

    public void addValue(double value) {
        this.totalSum += value;
        this.totalCount++;
    }
}
