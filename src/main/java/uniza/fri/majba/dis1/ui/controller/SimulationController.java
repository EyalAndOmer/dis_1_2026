package uniza.fri.majba.dis1.ui.controller;

import io.fair_acc.chartfx.XYChart;
import io.fair_acc.chartfx.axes.spi.DefaultNumericAxis;
import io.fair_acc.chartfx.renderer.spi.ErrorDataSetRenderer;
import io.fair_acc.dataset.spi.DoubleDataSet;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import uniza.fri.majba.dis1.simulation_core.SimulationCore;
import uniza.fri.majba.dis1.traversal_simulation.MonteCarloSimulationCore;
import uniza.fri.majba.dis1.traversal_simulation.ReplicationResult;
import uniza.fri.majba.dis1.traversal_simulation.TraversalReplication;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class SimulationController {

    @FXML private Pagination pagination;
    @FXML private VBox contentPanel;
    @FXML private Button startButton;
    @FXML private Button stopButton;
    @FXML private GridPane chartsGrid;
    @FXML private Label kpiIterationsValue;
    @FXML private Label kpiDurationValue;

    private static final int DEFAULT_REPLICATIONS = 10_000_000;
    private static final int CHARTS_PER_PAGE = 2;

    /** Percentage of initial replications to skip before rendering any chart points. */
    private static final double SKIP_PERCENTAGE = 10;
    /** Percentage of (non-skipped) replications to actually render as chart data points. */
    private static final double RENDER_PERCENTAGE = 0.1;

    /** Route names in the order returned by TraversalGraph.buildRoutes(). */
    private static final String[] ROUTE_NAMES = {
            "Žilina --> Divinka --> Strečno --> Rajecké Teplice --> Žilina",
            "Žilina --> Divinka --> Rajecké Teplice --> Strečno --> Žilina",
            "Žilina --> Strečno --> Divinka --> Rajecké Teplice --> Žilina",
            "Žilina --> Strečno --> Rajecké Teplice --> Divinka --> Žilina",
            "Žilina --> Rajecké Teplice --> Divinka --> Strečno --> Žilina",
            "Žilina --> Rajecké Teplice --> Strečno --> Divinka --> Žilina"
    };

    private SimulationCore simulationCore;
    private Thread simulationThread;

    /** Ordered map: route name → its dataset. Preserves insertion order. */
    private final Map<String, DoubleDataSet> dataSetMap = new LinkedHashMap<>();
    private final List<XYChart> charts = new ArrayList<>();
    private int replicationCounter;
    private long startTimeMillis;
    private int skipCount;
    private long stepCount;
    private int totalRendered;

    @FXML
    void initialize() {
        TraversalReplication replication = new TraversalReplication();
        replication.setOnAfterReplication(this::onReplicationComplete);
        this.simulationCore = new MonteCarloSimulationCore(replication);

        createCharts();
        configurePagination();
    }

    // ──────────────────── chart creation ────────────────────

    private void createCharts() {
        for (String name : ROUTE_NAMES) {
            DoubleDataSet ds = new DoubleDataSet(name);
            dataSetMap.put(name, ds);

            DefaultNumericAxis xAxis = new DefaultNumericAxis("Replikácia", "");
            xAxis.setAutoRanging(true);
            xAxis.setForceZeroInRange(false);

            DefaultNumericAxis yAxis = new DefaultNumericAxis("Priemerný čas", "h");
            yAxis.setAutoRanging(true);
            yAxis.setForceZeroInRange(false);

            XYChart chart = new XYChart(xAxis, yAxis);
            chart.setTitle(name);
            chart.setAnimated(false);
            chart.setLegendVisible(false);

            ErrorDataSetRenderer renderer = new ErrorDataSetRenderer();
            renderer.setDrawMarker(false);
            renderer.setDrawBars(false);
            renderer.getDatasets().add(ds);
            chart.getRenderers().setAll(renderer);

            charts.add(chart);
        }
    }

    // ──────────────────── pagination ────────────────────

    private void configurePagination() {
        int pageCount = (int) Math.ceil((double) charts.size() / CHARTS_PER_PAGE);
        pagination.setPageCount(pageCount);
        pagination.setCurrentPageIndex(0);
        pagination.currentPageIndexProperty().addListener((obs, oldIdx, newIdx) ->
                showChartsPage(newIdx.intValue()));
        showChartsPage(0);
    }

    private void showChartsPage(int pageIndex) {
        chartsGrid.getChildren().clear();

        int from = pageIndex * CHARTS_PER_PAGE;
        int to = Math.min(from + CHARTS_PER_PAGE, charts.size());

        for (int i = from; i < to; i++) {
            int col = i - from;
            XYChart chart = charts.get(i);

            StackPane wrapper = new StackPane(chart);
            wrapper.setStyle(
                    "-fx-background-color: rgba(255,255,255,0.02);" +
                    "-fx-background-radius: 12;" +
                    "-fx-border-color: rgba(255,255,255,0.06);" +
                    "-fx-border-radius: 12;" +
                    "-fx-border-width: 1;" +
                    "-fx-padding: 10;"
            );
            chartsGrid.add(wrapper, col, 0);
        }
    }

    // ──────────────────── simulation lifecycle ────────────────────

    @FXML
    void onStartSimulation() {
        if (simulationThread != null && simulationThread.isAlive()) {
            simulationThread.interrupt();
        }

        // Reset datasets
        replicationCounter = 0;
        startTimeMillis = System.currentTimeMillis();
        kpiIterationsValue.setText("0");
        kpiDurationValue.setText("0.0s");
        for (DoubleDataSet ds : dataSetMap.values()) {
            ds.clearData();
        }

        // Compute rendering schedule
        int numberOfPoints = DEFAULT_REPLICATIONS;
        skipCount = SKIP_PERCENTAGE == 0 ? 0 : (int) Math.floor((SKIP_PERCENTAGE / 100.0) * numberOfPoints);
        stepCount = Math.round(numberOfPoints / (numberOfPoints * (RENDER_PERCENTAGE / 100.0)));
        totalRendered = (int) Math.floor((double) (numberOfPoints - skipCount) / stepCount);

        contentPanel.setVisible(true);
        contentPanel.setManaged(true);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                simulationCore.simulate(DEFAULT_REPLICATIONS);
                return null;
            }
        };

        task.setOnFailed(event -> {
            Throwable exception = task.getException();
            if (exception != null) {
                exception.printStackTrace();
                System.exit(1);
            }
        });

        task.setOnSucceeded(event -> {
            System.out.println("DONE");
        });

        simulationThread = new Thread(task);
        simulationThread.setDaemon(true);
        simulationThread.setName("simulation-thread");
        simulationThread.start();
    }

    @FXML
    private void onStopSimulation() {
        if (simulationThread != null && simulationThread.isAlive()) {
            simulationThread.interrupt();
        }
        contentPanel.setVisible(false);
        contentPanel.setManaged(false);
    }

    // ──────────────────── replication callback ────────────────────

    private void onReplicationComplete(ReplicationResult result) {
        replicationCounter++;

        // During skip phase, update KPIs at stepCount intervals so the user sees progress
        if (replicationCounter <= skipCount) {
            if (replicationCounter % stepCount == 0) {
                final int count = replicationCounter;
                Platform.runLater(() -> {
                    kpiIterationsValue.setText(String.format("%,d", count));
                    kpiDurationValue.setText(formatElapsedTime());
                });
            }
            return;
        }

        // Only render every stepCount-th replication after the skip
        if ((replicationCounter - skipCount) % stepCount != 0) {
            return;
        }

        final int x = replicationCounter;
        final List<ReplicationResult.RouteResult> snapshot = List.copyOf(result.routeResults());

        Platform.runLater(() -> {
            kpiIterationsValue.setText(String.format("%,d", x));
            kpiDurationValue.setText(formatElapsedTime());

            for (ReplicationResult.RouteResult rr : snapshot) {
                DoubleDataSet ds = dataSetMap.get(rr.routeName());
                if (ds != null) {
                    ds.add(x, rr.averageTime());
                }
            }
        });
    }

    private String formatElapsedTime() {
        long elapsed = System.currentTimeMillis() - startTimeMillis;
        long seconds = elapsed / 1000;
        if (seconds < 60) {
            return String.format("%.1fs", elapsed / 1000.0);
        }
        long minutes = seconds / 60;
        long secs = seconds % 60;
        if (minutes < 60) {
            return String.format("%dm %ds", minutes, secs);
        }
        long hours = minutes / 60;
        long mins = minutes % 60;
        return String.format("%dh %dm %ds", hours, mins, secs);
    }

    // ──────────────────── helpers ────────────────────

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
