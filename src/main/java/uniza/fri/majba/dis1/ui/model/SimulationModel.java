package uniza.fri.majba.dis1.ui.model;

import io.fair_acc.chartfx.XYChart;
import io.fair_acc.chartfx.axes.spi.DefaultNumericAxis;
import io.fair_acc.chartfx.plugins.DataPointTooltip;
import io.fair_acc.chartfx.plugins.Zoomer;
import io.fair_acc.dataset.spi.DoubleDataSet;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulationModel {

    public static class RouteRow {
        private final IntegerProperty rank = new SimpleIntegerProperty();
        private final StringProperty sequence = new SimpleStringProperty();
        private final StringProperty avg = new SimpleStringProperty();

        public RouteRow(int r, String s, String a) {
            rank.set(r);
            sequence.set(s);
            avg.set(a);
        }

        public IntegerProperty rankProperty() {
            return rank;
        }

        public StringProperty sequenceProperty() {
            return sequence;
        }

        public StringProperty avgProperty() {
            return avg;
        }
    }

    public static final int ROWS_PER_PAGE = 10;
    public static final int CHARTS_PER_PAGE = 4;

    private ObservableList<RouteRow> allRoutes;
    private List<XYChart> allCharts;
    private ObservableList<String> cityCombinations;

    public SimulationModel() {
        cityCombinations = FXCollections.observableArrayList();
        initializeRoutes();
        initializeCharts();
    }

    public void initializeRoutes() {
        allRoutes = FXCollections.observableArrayList(
                new RouteRow(1,  "Žilina → Višňové → Bod K → Strečno → Rosina → Žilina",   "138.2 min"),
                new RouteRow(2,  "Žilina → Rosina → Strečno → Višňové → Bod K → Žilina",   "141.5 min"),
                new RouteRow(3,  "Žilina → Bod K → Višňové → Rosina → Strečno → Žilina",   "145.8 min"),
                new RouteRow(4,  "Žilina → Strečno → Višňové → Bod K → Rosina → Žilina",   "148.1 min"),
                new RouteRow(5,  "Žilina → Višňové → Rosina → Strečno → Bod K → Žilina",   "152.4 min"),
                new RouteRow(6,  "Žilina → Bod K → Strečno → Rosina → Višňové → Žilina",   "156.7 min"),
                new RouteRow(7,  "Žilina → Strečno → Bod K → Višňové → Rosina → Žilina",   "160.3 min"),
                new RouteRow(8,  "Žilina → Rosina → Višňové → Bod K → Strečno → Žilina",   "164.9 min"),
                new RouteRow(9,  "Žilina → Bod K → Rosina → Strečno → Višňové → Žilina",   "168.2 min"),
                new RouteRow(10, "Žilina → Višňové → Strečno → Rosina → Bod K → Žilina",   "172.5 min"),
                new RouteRow(11, "Žilina → Strečno → Rosina → Višňové → Bod K → Žilina",   "176.8 min"),
                new RouteRow(12, "Žilina → Bod K → Višňové → Strečno → Rosina → Žilina",   "180.1 min")
        );
    }

    public void initializeCharts() {
        allCharts = new ArrayList<>();

        String[] strategies = {
                "Žilina → Višňové → Bod K → Strečno → Rosina",
                "Žilina → Rosina → Strečno → Višňové → Bod K",
                "Žilina → Bod K → Višňové → Rosina → Strečno",
                "Žilina → Strečno → Višňové → Bod K → Rosina",
                "Žilina → Višňové → Rosina → Strečno → Bod K",
                "Žilina → Bod K → Strečno → Rosina → Višňové",
                "Žilina → Strečno → Bod K → Višňové → Rosina",
                "Žilina → Rosina → Višňové → Bod K → Strečno",
                "Žilina → Bod K → Rosina → Strečno → Višňové",
                "Žilina → Višňové → Strečno → Rosina → Bod K",
                "Žilina → Strečno → Rosina → Višňové → Bod K",
                "Žilina → Bod K → Višňové → Strečno → Rosina"
        };
        double[] avgTimes = {138.2, 141.5, 145.8, 148.1, 152.4, 156.7, 160.3, 164.9, 168.2, 172.5, 176.8, 180.1};

        for (int i = 0; i < strategies.length; i++) {
            allCharts.add(createStrategyChart(i + 1, strategies[i], avgTimes[i]));
        }
    }

    public XYChart createStrategyChart(int strategyNum, String strategyName, double avgTime) {
        DefaultNumericAxis xAxis = new DefaultNumericAxis("Iterácia");
        DefaultNumericAxis yAxis = new DefaultNumericAxis("Čas (min)");

        XYChart chart = new XYChart(xAxis, yAxis);
        chart.setTitle("Stratégia #" + strategyNum);
        chart.setLegendVisible(false);

        chart.getPlugins().add(new Zoomer());
        chart.getPlugins().add(new DataPointTooltip());

        Random random = new Random(strategyNum * 42L);
        DoubleDataSet dataSet = new DoubleDataSet(strategyName);

        int iterations = 50;
        double currentTime = avgTime + random.nextDouble() * 30;

        for (int i = 0; i < iterations; i++) {
            double x = i * 200;
            double convergenceFactor = Math.exp(-i / 15.0);
            double noise = (random.nextDouble() - 0.5) * 5;
            double y = avgTime + (currentTime - avgTime) * convergenceFactor + noise;
            currentTime = y;
            dataSet.add(x, y);
        }

        chart.getDatasets().add(dataSet);
        chart.setStyle("-fx-background-color: transparent;");

        return chart;
    }

    // --- Pagination helpers ---

    public List<RouteRow> getRoutesPage(int pageIndex) {
        int from = pageIndex * ROWS_PER_PAGE;
        int to = Math.min(from + ROWS_PER_PAGE, allRoutes.size());
        return new ArrayList<>(allRoutes.subList(from, to));
    }

    public int getTablePageCount() {
        return (int) Math.ceil((double) allRoutes.size() / ROWS_PER_PAGE);
    }

    public List<XYChart> getChartsPage(int pageIndex) {
        if (allCharts == null || allCharts.isEmpty()) {
            return new ArrayList<>();
        }
        int from = pageIndex * CHARTS_PER_PAGE;
        int to = Math.min(from + CHARTS_PER_PAGE, allCharts.size());
        return new ArrayList<>(allCharts.subList(from, to));
    }

    public int getChartPageCount() {
        if (allCharts == null) return 1;
        return (int) Math.ceil((double) allCharts.size() / CHARTS_PER_PAGE);
    }

    // --- City combinations ---

    public void setCityCombinations(ObservableList<String> combinations) {
        this.cityCombinations = combinations;
    }

    public ObservableList<String> getCityCombinations() {
        return cityCombinations;
    }

    public int getCombinationCount() {
        return cityCombinations == null ? 0 : cityCombinations.size();
    }

    // --- Data accessors ---

    public ObservableList<RouteRow> getAllRoutes() {
        return allRoutes;
    }

    public List<XYChart> getAllCharts() {
        return allCharts;
    }
}

