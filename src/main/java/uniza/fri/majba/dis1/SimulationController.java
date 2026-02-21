package uniza.fri.majba.dis1;

import io.fair_acc.chartfx.XYChart;
import io.fair_acc.chartfx.axes.spi.DefaultNumericAxis;
import io.fair_acc.chartfx.plugins.Zoomer;
import io.fair_acc.chartfx.plugins.DataPointTooltip;
import io.fair_acc.dataset.spi.DoubleDataSet;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulationController {
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

    @FXML
    private TableView<RouteRow> routesTable;
    @FXML
    private TableColumn<RouteRow, Number> colRank;
    @FXML
    private TableColumn<RouteRow, String> colSequence;
    @FXML
    private TableColumn<RouteRow, String> colAvg;
    @FXML
    private Pagination pagination;
    @FXML
    private VBox contentPanel;
    @FXML
    private Button startButton;
    @FXML
    private Button stopButton;
    @FXML
    private Button configButton;

    // Chart-related fields
    @FXML
    private TabPane tabPane;
    @FXML
    private VBox chartsContainer;
    @FXML
    private GridPane chartsGrid;

    private static final int ROWS_PER_PAGE = 10;
    private static final int CHARTS_PER_PAGE = 4;
    private ObservableList<RouteRow> allRoutes;

    // Store city combinations from the configuration dialog
    private ObservableList<String> cityCombinations;

    // Reference to the city combination controller
    private CityCombinationController cityCombinationController;

    // Chart management
    private List<XYChart> allCharts;

    @FXML
    private void initialize() {
        // Set column resize policy
        routesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colRank.setCellValueFactory(c -> c.getValue().rankProperty());
        colSequence.setCellValueFactory(c -> c.getValue().sequenceProperty());
        colAvg.setCellValueFactory(c -> c.getValue().avgProperty());

        // Initialize all routes data (this would be populated after simulation)
        allRoutes = FXCollections.observableArrayList(
                new RouteRow(1, "Žilina → Višňové → Bod K → Strečno → Rosina → Žilina", "138.2 min"),
                new RouteRow(2, "Žilina → Rosina → Strečno → Višňové → Bod K → Žilina", "141.5 min"),
                new RouteRow(3, "Žilina → Bod K → Višňové → Rosina → Strečno → Žilina", "145.8 min"),
                new RouteRow(4, "Žilina → Strečno → Višňové → Bod K → Rosina → Žilina", "148.1 min"),
                new RouteRow(5, "Žilina → Višňové → Rosina → Strečno → Bod K → Žilina", "152.4 min"),
                new RouteRow(6, "Žilina → Bod K → Strečno → Rosina → Višňové → Žilina", "156.7 min"),
                new RouteRow(7, "Žilina → Strečno → Bod K → Višňové → Rosina → Žilina", "160.3 min"),
                new RouteRow(8, "Žilina → Rosina → Višňové → Bod K → Strečno → Žilina", "164.9 min"),
                new RouteRow(9, "Žilina → Bod K → Rosina → Strečno → Višňové → Žilina", "168.2 min"),
                new RouteRow(10, "Žilina → Višňové → Strečno → Rosina → Bod K → Žilina", "172.5 min"),
                new RouteRow(11, "Žilina → Strečno → Rosina → Višňové → Bod K → Žilina", "176.8 min"),
                new RouteRow(12, "Žilina → Bod K → Višňové → Strečno → Rosina → Žilina", "180.1 min")
        );

        // Initialize charts
        initializeCharts();
    }

    private void configurePaginationForTab(int tabIndex) {
        if (tabIndex == 0) {
            // Table tab
            int pageCount = (int) Math.ceil((double) allRoutes.size() / ROWS_PER_PAGE);
            pagination.setPageCount(pageCount);
            pagination.setCurrentPageIndex(0);
            updateTablePage(0);
        } else if (tabIndex == 1) {
            // Charts tab
            int chartsPageCount = (int) Math.ceil((double) allCharts.size() / CHARTS_PER_PAGE);
            pagination.setPageCount(chartsPageCount);
            pagination.setCurrentPageIndex(0);
            updateChartsPage(0);
        }
    }

    private void updateCurrentTabContent(int pageIndex) {
        int currentTab = tabPane.getSelectionModel().getSelectedIndex();
        if (currentTab == 0) {
            updateTablePage(pageIndex);
        } else if (currentTab == 1) {
            updateChartsPage(pageIndex);
        }
    }

    private void updateTablePage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, allRoutes.size());

        routesTable.setItems(FXCollections.observableArrayList(
                allRoutes.subList(fromIndex, toIndex)
        ));
    }

    @FXML
    private void onStartSimulation() {
        // Check if combinations have been configured
        if (cityCombinationController == null || cityCombinationController.getCombinationCount() == 0) {
            showAlert("Chýbajúca konfigurácia",
                     "Najprv musíte nakonfigurovať kombinácie miest.\n\n" +
                     "Kliknite na tlačidlo 'Konfigurovať kombinácie' a pridajte aspoň jednu kombináciu.");
            return;
        }

        // Show the content panel when simulation starts
        contentPanel.setVisible(true);
        contentPanel.setManaged(true);

        // Setup shared pagination with single listener
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            updateCurrentTabContent(newIndex.intValue());
        });

        // Setup tab change listener - reconfigure pagination when switching tabs
        tabPane.getSelectionModel().selectedIndexProperty().addListener((obs, oldTab, newTab) -> configurePaginationForTab(newTab.intValue()));

        // Initialize pagination for the first tab (table)
        configurePaginationForTab(0);
    }

    @FXML
    private void onStopSimulation() {
        // Hide the content panel when simulation stops
        contentPanel.setVisible(false);
        contentPanel.setManaged(false);
    }

    @FXML
    private void onConfigureCombinations() {
        try {
            // Load the city combinations FXML
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/city_combinations.fxml")
            );
            VBox root = loader.load();

            // Get the controller and store reference
            cityCombinationController = loader.getController();

            // Create a new stage for the dialog
            Stage dialog = new Stage();
            dialog.setTitle("Konfigurácia kombinácií miest");
            dialog.initModality(Modality.APPLICATION_MODAL);

            // Get the main window to use as owner
            Stage owner = (Stage) configButton.getScene().getWindow();
            dialog.initOwner(owner);

            // Create scene and apply styles
            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                getClass().getResource("/chart_styles.css").toExternalForm()
            );

            dialog.setScene(scene);
            dialog.setResizable(false);

            // Show dialog and wait for it to close
            dialog.showAndWait();

            // After dialog closes, you can get the updated combinations if needed
            // ObservableList<String> combinations = controller.getCombinationsAsStrings();
            // Update simulation with new combinations...

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Chyba", "Nepodarilo sa otvoriť okno konfigurácie kombinácií.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Chart methods
    private void initializeCharts() {
        allCharts = new ArrayList<>();

        // Create charts for different route strategies (12 strategies from the table data)
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

    private XYChart createStrategyChart(int strategyNum, String strategyName, double avgTime) {
        // Create axes
        DefaultNumericAxis xAxis = new DefaultNumericAxis("Iterácia");
        DefaultNumericAxis yAxis = new DefaultNumericAxis("Čas (min)");

        // Create chart
        XYChart chart = new XYChart(xAxis, yAxis);
        chart.setTitle("Stratégia #" + strategyNum);
        chart.setLegendVisible(false);

        // Add plugins for interaction
        chart.getPlugins().add(new Zoomer());
        chart.getPlugins().add(new DataPointTooltip());

        // Generate convergence data showing how the strategy's performance stabilizes
        Random random = new Random(strategyNum * 42L);
        DoubleDataSet dataSet = new DoubleDataSet(strategyName);

        int iterations = 50;
        double currentTime = avgTime + random.nextDouble() * 30; // Start with higher time

        for (int i = 0; i < iterations; i++) {
            double x = i * 200; // Iteration number (scaled)
            // Gradually converge to average time
            double convergenceFactor = Math.exp(-i / 15.0);
            double noise = (random.nextDouble() - 0.5) * 5;
            double y = avgTime + (currentTime - avgTime) * convergenceFactor + noise;
            currentTime = y;
            dataSet.add(x, y);
        }

        chart.getDatasets().add(dataSet);

        // Style the chart
        chart.setStyle("-fx-background-color: transparent;");

        return chart;
    }


    private void updateChartsPage(int pageIndex) {
        if (allCharts == null || allCharts.isEmpty()) {
            return;
        }

        // Clear current charts
        chartsGrid.getChildren().clear();

        // Calculate which charts to display
        int startIndex = pageIndex * CHARTS_PER_PAGE;
        int endIndex = Math.min(startIndex + CHARTS_PER_PAGE, allCharts.size());

        // Add up to 4 charts in a 2x2 grid
        for (int i = startIndex; i < endIndex; i++) {
            int position = i - startIndex;
            int row = position / 2;
            int col = position % 2;

            XYChart chart = allCharts.get(i);

            // Wrap chart in a StackPane for styling
            StackPane chartContainer = new StackPane(chart);
            chartContainer.setStyle(
                    "-fx-background-color: rgba(255,255,255,0.02);" +
                            "-fx-background-radius: 12;" +
                            "-fx-border-color: rgba(255,255,255,0.06);" +
                            "-fx-border-radius: 12;" +
                            "-fx-border-width: 1;" +
                            "-fx-padding: 10;"
            );

            chartsGrid.add(chartContainer, col, row);
        }
    }
}
