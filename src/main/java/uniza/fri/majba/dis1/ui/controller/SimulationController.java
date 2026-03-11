package uniza.fri.majba.dis1.ui.controller;

import io.fair_acc.chartfx.XYChart;
import io.fair_acc.chartfx.axes.spi.DefaultNumericAxis;
import io.fair_acc.chartfx.renderer.spi.ErrorDataSetRenderer;
import io.fair_acc.dataset.spi.DoubleDataSet;
import javafx.application.Platform;
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
import uniza.fri.majba.dis1.traversal_simulation.*;
import uniza.fri.majba.dis1.ui.chart.NumberAxisConverter;
import uniza.fri.majba.dis1.ui.model.SimulationConfig;
import uniza.fri.majba.dis1.ui.util.TimeUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    @FXML
    void initialize() {
        TraversalReplication replication = new TraversalReplication();
        replication.setOnAfterReplication(this::onReplicationComplete);
        this.simulationCore = new MonteCarloSimulationCore(replication);

        createCharts();
        configurePagination();
    }

    private void createCharts() {
        for (int idx = 0; idx < ROUTE_NAMES.length; idx++) {
            String name = ROUTE_NAMES[idx];
            DoubleDataSet ds = new DoubleDataSet(name);
            ds.setStyle("strokeColor=#fbbf24; fillColor=rgba(251,191,36,0.15);");
            dataSetMap.put(name, ds);

            DefaultNumericAxis xAxis = new DefaultNumericAxis("Replikácia", "");
            xAxis.setAutoRanging(true);
            xAxis.setForceZeroInRange(false);
            xAxis.setTickLabelFormatter(new NumberAxisConverter("#,##0.00"));

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
            renderer.setPolyLineStyle(io.fair_acc.chartfx.renderer.LineStyle.NORMAL);
            renderer.getDatasets().add(ds);
            chart.getRenderers().setAll(renderer);

            charts.add(chart);

            Label valueLabel = new Label("—");
            valueLabel.setStyle(
                    "-fx-text-fill: #fbbf24;" +
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

    @FXML
    void onStartSimulation() {
        this.startButton.setDisable(true);
        this.stopButton.setDisable(false);
        this.resumeButton.setDisable(false);
        // Reset datasets
        replicationCounter = 0;
        startTimeMillis = System.currentTimeMillis();
        kpiIterationsValue.setText("0");
        kpiDurationValue.setText("0.0s");
        for (DoubleDataSet ds : dataSetMap.values()) {
            ds.clearData();
        }
        for (Label lbl : chartValueLabels) {
            lbl.setText("—");
        }

        // Compute rendering schedule
        SimulationConfig cfg = SimulationConfig.getInstance();
        int numberOfReplications = cfg.getDefaultReplications();
        double skipPercentage = cfg.getSkipPercentage();
        double renderPercentage = cfg.getRenderPercentage();

        skipCount = skipPercentage == 0 ? 0 : (int) Math.floor((skipPercentage / 100.0) * numberOfReplications);
        stepCount = Math.round(numberOfReplications / (numberOfReplications * (renderPercentage / 100.0)));

        // Rebuild generators so they pick up the latest seed
        TraversalSimulationConstants.rebuildGenerators();

        contentPanel.setVisible(true);
        contentPanel.setManaged(true);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                simulationCore.simulate(numberOfReplications);
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
            kpiIterationsValue.setText(String.format("%,d", replicationCounter));
            kpiDurationValue.setText(TimeUtil.formatElapsedDuration(startTimeMillis));
            int idx = 0;
            for (DoubleDataSet ds : dataSetMap.values()) {
                if (ds.getDataCount() > 0) {
                    double latestY = ds.get(DoubleDataSet.DIM_Y, ds.getDataCount() - 1);
                    chartValueLabels.get(idx).setText("Ø " + TimeUtil.formatDecimalHoursAsTimestamp(latestY));
                }
                idx++;
            }

            this.startButton.setDisable(false);
            this.stopButton.setDisable(true);
            this.resumeButton.setDisable(true);
        });

        simulationThread = new Thread(task);
        simulationThread.setDaemon(true);
        simulationThread.setName("simulation-thread");
        simulationThread.start();
    }

    @FXML
    private void onStopSimulation() {
        this.stopButton.setDisable(true);
        this.resumeButton.setDisable(false);
        simulationCore.pauseSimulation();
    }

    @FXML
    private void onResumeSimulation() {
        this.stopButton.setDisable(false);
        this.resumeButton.setDisable(true);
        simulationCore.resumeSimulation();
    }

    @FXML
    void onGoToSecondTask() {
        if (simulationThread != null && simulationThread.isAlive()) {
            simulationThread.interrupt();
        }

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

    void onReplicationComplete(ReplicationResult result) {
        replicationCounter++;

        if (replicationCounter <= skipCount) {
            int repNumber = replicationCounter;
            if (repNumber % 1_000 == 0) {
                Platform.runLater(() -> {
                    kpiIterationsValue.setText(String.format("%,d", repNumber));
                    kpiDurationValue.setText(TimeUtil.formatElapsedDuration(startTimeMillis));
                });
            }

            return;
        }

        if ((replicationCounter - skipCount) % stepCount != 0) {
            return;
        }

        int repNumber = replicationCounter;
        List<RouteResult> snapshot = List.copyOf(result.routeResults());

        Platform.runLater(() -> {
            kpiIterationsValue.setText(String.format("%,d", repNumber));
            kpiDurationValue.setText(TimeUtil.formatElapsedDuration(startTimeMillis));
            for (RouteResult rr : snapshot) {
                DoubleDataSet ds = dataSetMap.get(rr.routeName());
                if (ds != null) {
                    ds.add(repNumber, rr.averageTime());
                }
            }
            int i = 0;
            for (DoubleDataSet ds : dataSetMap.values()) {
                if (ds.getDataCount() > 0) {
                    double latestY = ds.get(DoubleDataSet.DIM_Y, ds.getDataCount() - 1);
                    chartValueLabels.get(i).setText("Ø " + TimeUtil.formatDecimalHoursAsTimestamp(latestY));
                }
                i++;
            }
        });
    }
}
