package uniza.fri.majba.dis1.ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import uniza.fri.majba.dis1.ui.model.SimulationConfig;

import java.io.IOException;

public final class ConfigController {

    @FXML private Button backButton;
    @FXML private Label statusLabel;

    // Seed
    @FXML private TextField seedField;

    // Task 1
    @FXML private TextField replicationsField;
    @FXML private TextField skipPercentageField;
    @FXML private TextField renderPercentageField;

    // Task 2
    @FXML private TextField monteCarloSamplesField;
    @FXML private TextField earliestDepartureField;
    @FXML private TextField arriveByHourField;
    @FXML private TextField arriveByMinuteField;
    @FXML private TextField requiredOnTimePctField;
    @FXML private TextField initialStepField;
    @FXML private TextField precisionField;

    /** Which view to return to: "/chart_view.fxml" or "/second_task_view.fxml". */
    private String returnViewPath = "/chart_view.fxml";

    public void setReturnViewPath(String path) {
        this.returnViewPath = path;
    }

    @FXML
    void initialize() {
        SimulationConfig cfg = SimulationConfig.getInstance();

        seedField.setText(String.valueOf(cfg.getSeedGeneratorSeed()));

        replicationsField.setText(String.valueOf(cfg.getDefaultReplications()));
        skipPercentageField.setText(String.valueOf(cfg.getSkipPercentage()));
        renderPercentageField.setText(String.valueOf(cfg.getRenderPercentage()));

        monteCarloSamplesField.setText(String.valueOf(cfg.getMonteCarloSamples()));
        earliestDepartureField.setText(String.valueOf(cfg.getEarliestDeparture()));
        arriveByHourField.setText(String.valueOf(cfg.getMustArriveByHour()));
        arriveByMinuteField.setText(String.valueOf(cfg.getMustArriveByMinute()));
        requiredOnTimePctField.setText(String.valueOf(cfg.getRequiredOnTimePercentage() * 100));
        initialStepField.setText(String.valueOf(cfg.getInitialStep()));
        precisionField.setText(String.valueOf(cfg.getPrecision()));
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
        SimulationConfig cfg = SimulationConfig.getInstance();
        try {
            cfg.setSeedGeneratorSeed(Long.parseLong(seedField.getText().trim()));

            cfg.setDefaultReplications(Integer.parseInt(replicationsField.getText().trim()));
            cfg.setSkipPercentage(Double.parseDouble(skipPercentageField.getText().trim()));
            cfg.setRenderPercentage(Double.parseDouble(renderPercentageField.getText().trim()));

            cfg.setMonteCarloSamples(Integer.parseInt(monteCarloSamplesField.getText().trim()));
            cfg.setEarliestDeparture(Double.parseDouble(earliestDepartureField.getText().trim()));
            cfg.setMustArriveByHour(Integer.parseInt(arriveByHourField.getText().trim()));
            cfg.setMustArriveByMinute(Integer.parseInt(arriveByMinuteField.getText().trim()));
            cfg.setRequiredOnTimePercentage(
                    Double.parseDouble(requiredOnTimePctField.getText().trim()) / 100.0);
            cfg.setInitialStep(Double.parseDouble(initialStepField.getText().trim()));
            cfg.setPrecision(Double.parseDouble(precisionField.getText().trim()));

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

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

