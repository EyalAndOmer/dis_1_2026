package uniza.fri.majba.dis1.ui.controller;

import io.fair_acc.chartfx.XYChart;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import uniza.fri.majba.dis1.ui.model.SimulationModel;
import uniza.fri.majba.dis1.ui.model.SimulationModel.RouteRow;

import java.io.IOException;
import java.util.List;

public class SimulationController {

    @FXML private TableView<RouteRow> routesTable;
    @FXML private TableColumn<RouteRow, Number> colRank;
    @FXML private TableColumn<RouteRow, String> colSequence;
    @FXML private TableColumn<RouteRow, String> colAvg;
    @FXML private Pagination pagination;
    @FXML private VBox contentPanel;
    @FXML private Button startButton;
    @FXML private Button stopButton;
    @FXML private Button configButton;
    @FXML private TabPane tabPane;
    @FXML private VBox chartsContainer;
    @FXML private GridPane chartsGrid;

    private SimulationModel model;

    @FXML
    private void initialize() {
        model = new SimulationModel();

        routesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colRank.setCellValueFactory(c -> c.getValue().rankProperty());
        colSequence.setCellValueFactory(c -> c.getValue().sequenceProperty());
        colAvg.setCellValueFactory(c -> c.getValue().avgProperty());
    }

    private void configurePaginationForTab(int tabIndex) {
        if (tabIndex == 0) {
            pagination.setPageCount(model.getTablePageCount());
            pagination.setCurrentPageIndex(0);
            updateTablePage(0);
        } else if (tabIndex == 1) {
            pagination.setPageCount(model.getChartPageCount());
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
        routesTable.setItems(FXCollections.observableArrayList(model.getRoutesPage(pageIndex)));
    }

    @FXML
    private void onStartSimulation() {
        if (model.getCombinationCount() == 0) {
            showAlert("Chýbajúca konfigurácia",
                    "Najprv musíte nakonfigurovať kombinácie miest.\n\n" +
                    "Kliknite na tlačidlo 'Konfigurovať kombinácie' a pridajte aspoň jednu kombináciu.");
            return;
        }

        contentPanel.setVisible(true);
        contentPanel.setManaged(true);

        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) ->
                updateCurrentTabContent(newIndex.intValue()));

        tabPane.getSelectionModel().selectedIndexProperty().addListener((obs, oldTab, newTab) ->
                configurePaginationForTab(newTab.intValue()));

        configurePaginationForTab(0);
    }

    @FXML
    private void onStopSimulation() {
        contentPanel.setVisible(false);
        contentPanel.setManaged(false);
    }

    @FXML
    private void onConfigureCombinations() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/city_combinations.fxml"));
            VBox root = loader.load();

            CityCombinationController combinationController = loader.getController();

            Stage dialog = new Stage();
            dialog.setTitle("Konfigurácia kombinácií miest");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner((Stage) configButton.getScene().getWindow());

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/chart_styles.css").toExternalForm());
            dialog.setScene(scene);
            dialog.setResizable(false);
            dialog.showAndWait();

            // Transfer combinations from dialog into simulation model
            model.setCityCombinations(combinationController.getCombinationsAsStrings());

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

    private void updateChartsPage(int pageIndex) {
        List<XYChart> pageCharts = model.getChartsPage(pageIndex);
        chartsGrid.getChildren().clear();

        for (int i = 0; i < pageCharts.size(); i++) {
            int row = i / 2;
            int col = i % 2;
            XYChart chart = pageCharts.get(i);

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
