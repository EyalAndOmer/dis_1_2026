package uniza.fri.majba.dis1.ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import uniza.fri.majba.dis1.ui.model.SimulationConfig;

import java.io.IOException;

public final class ConfigController {

    @FXML private Button backButton;
    @FXML private Label statusLabel;
    @FXML private Label chartPointsLabel;

    @FXML private TextField seedField;
    @FXML private TextField replicationsField;
    @FXML private TextField skipPercentageField;
    @FXML private TextField renderPercentageField;

    @FXML private Spinner<Double> simulationStartTimeSpinner;

    /** Which view to return to: "/chart_view.fxml" or "/second_task_view.fxml". */
    private String returnViewPath = "/chart_view.fxml";

    private SimulationConfig config = new SimulationConfig();

    public void setReturnViewPath(String path) {
        this.returnViewPath = path;
    }

    public void setConfig(SimulationConfig config) {
        this.config = config;
        populateFields();
    }

    @FXML
    void initialize() {
        populateFields();

        replicationsField.textProperty().addListener((obs, o, n) -> updateChartPoints());
        skipPercentageField.textProperty().addListener((obs, o, n) -> updateChartPoints());
        renderPercentageField.textProperty().addListener((obs, o, n) -> updateChartPoints());
        updateChartPoints();
    }

    private void populateFields() {
        SimulationConfig cfg = config;

        seedField.setText(String.valueOf(cfg.getSeedGeneratorSeed()));
        replicationsField.setText(String.valueOf(cfg.getDefaultReplications()));
        skipPercentageField.setText(String.valueOf(cfg.getSkipPercentage()));
        renderPercentageField.setText(String.valueOf(cfg.getRenderPercentage()));

        SpinnerValueFactory<Double> svf = new SpinnerValueFactory.DoubleSpinnerValueFactory(
                0.0, 23.99, cfg.getSimulationStartTime(), 0.25);
        simulationStartTimeSpinner.setValueFactory(svf);
        simulationStartTimeSpinner.setEditable(true);
        updateChartPoints();
    }

    private void updateChartPoints() {
        try {
            int replications = Integer.parseInt(replicationsField.getText().trim());
            double skipPct = Double.parseDouble(skipPercentageField.getText().trim());
            double renderPct = Double.parseDouble(renderPercentageField.getText().trim());

            long skipCount = Math.round(replications * skipPct / 100.0);
            long activeCount = replications - skipCount;
            long chartPoints = Math.round(activeCount * renderPct / 100.0);

            chartPointsLabel.setText(String.valueOf(chartPoints));
        } catch (NumberFormatException e) {
            chartPointsLabel.setText("—");
        }
    }

    @FXML
    void onSaveAndGoBack() {
        if (applyValues()) {
            navigateBack();
        }
    }

    @FXML
    void onBack() {
        navigateBack();
    }

    private boolean applyValues() {
        SimulationConfig cfg = config;
        try {
            cfg.setSeedGeneratorSeed(Long.parseLong(seedField.getText().trim()));

            cfg.setDefaultReplications(Integer.parseInt(replicationsField.getText().trim()));
            cfg.setSkipPercentage(Double.parseDouble(skipPercentageField.getText().trim()));
            cfg.setRenderPercentage(Double.parseDouble(renderPercentageField.getText().trim()));

            simulationStartTimeSpinner.commitValue();
            cfg.setSimulationStartTime(simulationStartTimeSpinner.getValue());

            statusLabel.setText("Konfigurácia uložená ✓");
            statusLabel.setStyle("-fx-text-fill: #34d399;");
            return true;
        } catch (NumberFormatException e) {
            statusLabel.setText("Neplatná konfigurácia: ");
            statusLabel.setStyle("-fx-text-fill: #f87171;");
            return false;
        }
    }

    private void navigateBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(returnViewPath));
            Parent root = loader.load();

            Object controller = loader.getController();
            if (controller instanceof SimulationController sc) {
                sc.setConfig(config);
            } else if (controller instanceof SecondTaskController stc) {
                stc.setConfig(config);
            }

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
