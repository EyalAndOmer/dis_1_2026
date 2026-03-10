package uniza.fri.majba.dis1.ui.controller;

import io.fair_acc.chartfx.XYChart;
import io.fair_acc.chartfx.axes.spi.DefaultNumericAxis;
import io.fair_acc.chartfx.renderer.spi.ErrorDataSetRenderer;
import io.fair_acc.dataset.spi.DoubleDataSet;
import javafx.animation.AnimationTimer;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uniza.fri.majba.dis1.simulation_core.SimulationCore;
import uniza.fri.majba.dis1.traversal_simulation.MonteCarloSimulationCore;
import uniza.fri.majba.dis1.traversal_simulation.ReplicationResult;
import uniza.fri.majba.dis1.traversal_simulation.TraversalReplication;
import uniza.fri.majba.dis1.traversal_simulation.TraversalSimulationConstants;
import uniza.fri.majba.dis1.ui.model.SimulationConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class SimulationController {

    @FXML private Pagination pagination;
    @FXML private VBox contentPanel;
    @FXML private Button startButton;
    @FXML private Button stopButton;
    @FXML private Button resumeButton;
    @FXML private GridPane chartsGrid;
    @FXML private Label kpiIterationsValue;
    @FXML private Label kpiDurationValue;
    @FXML private Button task2Button;
    @FXML private Button configButton;

    private static final int CHARTS_PER_PAGE = 2;


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
    private final List<VBox> chartWrappers = new ArrayList<>();
    private final List<Label> chartValueLabels = new ArrayList<>();
    private int replicationCounter;
    private long startTimeMillis;
    private int skipCount;
    private long stepCount;
    private int totalRendered;

    // ── lock-free buffer: simulation thread produces, AnimationTimer consumes ──

    private record ChartSnapshot(int replicationNumber, List<ReplicationResult.RouteResult> routeResults) {}

    /** Replication counter snapshot for KPI-only updates (during skip phase). */
    private volatile int latestReplicationCount;

    /** Buffered chart data points produced by the simulation thread. */
    private final ConcurrentLinkedQueue<ChartSnapshot> chartBuffer = new ConcurrentLinkedQueue<>();

    /** Drains the buffer once per frame (~60 fps) — never starves the FX event queue. */
    private final AnimationTimer chartUpdater = new AnimationTimer() {
        private static final int MAX_DRAIN_PER_FRAME = 20;

        @Override
        public void handle(long now) {
            // Update KPIs every frame
            kpiIterationsValue.setText(String.format("%,d", latestReplicationCount));
            kpiDurationValue.setText(formatElapsedTime());

            // Drain a limited number of buffered chart points per frame
            int drained = 0;
            ChartSnapshot snapshot;
            while (drained < MAX_DRAIN_PER_FRAME && (snapshot = chartBuffer.poll()) != null) {
                for (ReplicationResult.RouteResult rr : snapshot.routeResults()) {
                    DoubleDataSet ds = dataSetMap.get(rr.routeName());
                    if (ds != null) {
                        ds.add(snapshot.replicationNumber(), rr.averageTime());
                    }
                }
                drained++;
            }

            // Update value labels with the latest Y value from each dataset
            int i = 0;
            for (DoubleDataSet ds : dataSetMap.values()) {
                if (ds.getDataCount() > 0) {
                    double latestY = ds.get(DoubleDataSet.DIM_Y, ds.getDataCount() - 1);
                    chartValueLabels.get(i).setText("Ø " + formatHoursAsTime(latestY));
                }
                i++;
            }
        }
    };

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
        for (int idx = 0; idx < ROUTE_NAMES.length; idx++) {
            String name = ROUTE_NAMES[idx];
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

            Label valueLabel = new Label("—");
            valueLabel.setStyle(
                    "-fx-text-fill: rgba(230,237,247,0.85);" +
                    "-fx-font-size: 14px;" +
                    "-fx-font-weight: 700;"
            );

            VBox wrapper = new VBox(6, chart, valueLabel);
            wrapper.setAlignment(Pos.CENTER);
            wrapper.setStyle(
                    "-fx-background-color: rgba(255,255,255,0.02);" +
                    "-fx-background-radius: 12;" +
                    "-fx-border-color: rgba(255,255,255,0.06);" +
                    "-fx-border-radius: 12;" +
                    "-fx-border-width: 1;" +
                    "-fx-padding: 10;"
            );
            VBox.setVgrow(chart, Priority.ALWAYS);
            // Start hidden; showChartsPage will reveal the first page
            wrapper.setVisible(false);
            wrapper.setManaged(false);
            chartWrappers.add(wrapper);
            chartValueLabels.add(valueLabel);

            // Place all wrappers into the grid permanently (column cycles 0..1)
            int col = idx % CHARTS_PER_PAGE;
            int row = idx / CHARTS_PER_PAGE;
            chartsGrid.add(wrapper, col, row);
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
        int from = pageIndex * CHARTS_PER_PAGE;
        int to = Math.min(from + CHARTS_PER_PAGE, chartWrappers.size());

        for (int i = 0; i < chartWrappers.size(); i++) {
            boolean onPage = i >= from && i < to;
            chartWrappers.get(i).setVisible(onPage);
            chartWrappers.get(i).setManaged(onPage);
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
        latestReplicationCount = 0;
        startTimeMillis = System.currentTimeMillis();
        kpiIterationsValue.setText("0");
        kpiDurationValue.setText("0.0s");
        chartBuffer.clear();
        for (DoubleDataSet ds : dataSetMap.values()) {
            ds.clearData();
        }
        for (Label lbl : chartValueLabels) {
            lbl.setText("—");
        }

        // Compute rendering schedule
        SimulationConfig cfg = SimulationConfig.getInstance();
        int numberOfReplications = cfg.getDefaultReplications();
        double skipPct = cfg.getSkipPercentage();
        double renderPct = cfg.getRenderPercentage();

        skipCount = skipPct == 0 ? 0 : (int) Math.floor((skipPct / 100.0) * numberOfReplications);
        stepCount = Math.round(numberOfReplications / (numberOfReplications * (renderPct / 100.0)));
        totalRendered = (int) Math.floor((double) (numberOfReplications - skipCount) / stepCount);

        // Rebuild generators so they pick up the latest seed
        TraversalSimulationConstants.rebuildGenerators();

        contentPanel.setVisible(true);
        contentPanel.setManaged(true);

        // Start the frame-based UI updater
        chartUpdater.start();

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                simulationCore.simulate(numberOfReplications);
                return null;
            }
        };

        task.setOnFailed(event -> {
            chartUpdater.stop();
            Throwable exception = task.getException();
            if (exception != null) {
                exception.printStackTrace();
                System.exit(1);
            }
        });

        task.setOnSucceeded(event -> {
            // Final drain + KPI update, then stop the timer
            chartUpdater.stop();
            kpiIterationsValue.setText(String.format("%,d", replicationCounter));
            kpiDurationValue.setText(formatElapsedTime());
            ChartSnapshot s;
            while ((s = chartBuffer.poll()) != null) {
                for (ReplicationResult.RouteResult rr : s.routeResults()) {
                    DoubleDataSet ds = dataSetMap.get(rr.routeName());
                    if (ds != null) {
                        ds.add(s.replicationNumber(), rr.averageTime());
                    }
                }
            }
            // Final label update
            int idx = 0;
            for (DoubleDataSet ds : dataSetMap.values()) {
                if (ds.getDataCount() > 0) {
                    double latestY = ds.get(DoubleDataSet.DIM_Y, ds.getDataCount() - 1);
                    chartValueLabels.get(idx).setText("Ø " + formatHoursAsTime(latestY));
                }
                idx++;
            }
            System.out.println("DONE");
        });

        simulationThread = new Thread(task);
        simulationThread.setDaemon(true);
        simulationThread.setName("simulation-thread");
        simulationThread.start();
    }

    @FXML
    private void onStopSimulation() {
        simulationCore.pauseSimulation();
        chartUpdater.stop();
    }

    @FXML
    private void onResumeSimulation() {
        simulationCore.resumeSimulation();
        chartUpdater.start();
    }

    // ──────────────────── replication callback (called on simulation thread) ──

    private void onReplicationComplete(ReplicationResult result) {
        replicationCounter++;
        latestReplicationCount = replicationCounter;

        // Skip initial replications (warm-up phase) — KPIs still update via AnimationTimer
        if (replicationCounter <= skipCount) {
            return;
        }

        // Only buffer a data point every stepCount-th replication after the skip
        if ((replicationCounter - skipCount) % stepCount != 0) {
            return;
        }

        chartBuffer.add(new ChartSnapshot(
                replicationCounter,
                List.copyOf(result.routeResults())
        ));
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

    /** Formats fractional hours (e.g. 7.5833) as H:MM:SS.mmm */
    private static String formatHoursAsTime(double hours) {
        long totalMillis = Math.round(hours * 3_600_000.0);
        long h = totalMillis / 3_600_000;
        long rem = totalMillis % 3_600_000;
        long m = rem / 60_000;
        rem = rem % 60_000;
        long s = rem / 1000;
        long ms = rem % 1000;
        return String.format("%d:%02d:%02d.%03d", h, m, s, ms);
    }

    // ──────────────────── navigation ────────────────────

    @FXML
    void onGoToSecondTask() {
        // Stop any running simulation
        if (simulationThread != null && simulationThread.isAlive()) {
            simulationThread.interrupt();
        }
        chartUpdater.stop();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/second_task_view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) task2Button.getScene().getWindow();
            Scene scene = stage.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onGoToConfig() {
        if (simulationThread != null && simulationThread.isAlive()) {
            simulationThread.interrupt();
        }
        chartUpdater.stop();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/config_view.fxml"));
            Parent root = loader.load();
            ConfigController controller = loader.getController();
            controller.setReturnViewPath("/chart_view.fxml");

            Stage stage = (Stage) configButton.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
