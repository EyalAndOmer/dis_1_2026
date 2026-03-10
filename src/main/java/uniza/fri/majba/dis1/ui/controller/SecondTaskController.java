package uniza.fri.majba.dis1.ui.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uniza.fri.majba.dis1.simulation_core.SimulationCore;
import uniza.fri.majba.dis1.traversal_simulation.MonteCarloSimulationCore;
import uniza.fri.majba.dis1.traversal_simulation.SecondTaskReplication;
import uniza.fri.majba.dis1.traversal_simulation.SecondTaskResult;
import uniza.fri.majba.dis1.traversal_simulation.TraversalSimulationConstants;

import java.io.IOException;

public final class SecondTaskController {

    @FXML private ComboBox<String> routeComboBox;
    @FXML private Button startButton;
    @FXML private Button backButton;
    @FXML private Button configButton;
    @FXML private Label statusLabel;
    @FXML private VBox resultsCard;

    @FXML private Label resultRouteName;
    @FXML private Label resultDepartureTime;
    @FXML private Label resultArrivalDeadline;
    @FXML private Label resultAcceptedPct;
    @FXML private Label resultRejectedPct;
    @FXML private Label resultRequiredPct;
    @FXML private Label resultRejectedTime;

    private static final String[] ROUTE_NAMES = {
            "Žilina --> Divinka --> Strečno --> Rajecké Teplice --> Žilina",
            "Žilina --> Divinka --> Rajecké Teplice --> Strečno --> Žilina",
            "Žilina --> Strečno --> Divinka --> Rajecké Teplice --> Žilina",
            "Žilina --> Strečno --> Rajecké Teplice --> Divinka --> Žilina",
            "Žilina --> Rajecké Teplice --> Divinka --> Strečno --> Žilina",
            "Žilina --> Rajecké Teplice --> Strečno --> Divinka --> Žilina"
    };

    private Thread simulationThread;

    @FXML
    void initialize() {
        routeComboBox.setItems(FXCollections.observableArrayList(ROUTE_NAMES));
        routeComboBox.getSelectionModel().select(3);
    }

    @FXML
    void onStartSimulation() {
        if (simulationThread != null && simulationThread.isAlive()) {
            simulationThread.interrupt();
        }

        int selectedIndex = routeComboBox.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0) {
            statusLabel.setText("Vyberte trasu!");
            return;
        }

        // Reset UI
        resultsCard.setVisible(false);
        resultsCard.setManaged(false);
        statusLabel.setText("Simulácia beží…");
        startButton.setDisable(true);

        SecondTaskReplication replication = new SecondTaskReplication();
        replication.setSelectedRouteIndex(selectedIndex);
        replication.setOnResultReady(this::onResultReady);

        // Rebuild generators so they pick up the latest seed
        TraversalSimulationConstants.rebuildGenerators();

        SimulationCore core = new MonteCarloSimulationCore(replication);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                core.simulate(1);
                return null;
            }
        };

        task.setOnSucceeded(event -> {
            startButton.setDisable(false);
            statusLabel.setText("Dokončené ✓");
        });

        task.setOnFailed(event -> {
            startButton.setDisable(false);
            statusLabel.setText("Chyba!");
            Throwable ex = task.getException();
            if (ex != null) {
                ex.printStackTrace();
            }
        });

        simulationThread = new Thread(task);
        simulationThread.setDaemon(true);
        simulationThread.setName("second-task-thread");
        simulationThread.start();
    }

    private void onResultReady(SecondTaskResult result) {
        Platform.runLater(() -> {
            resultRouteName.setText(result.routeName());
            resultDepartureTime.setText(result.latestDepartureTime());
            resultArrivalDeadline.setText(result.mustArriveBy());
            resultAcceptedPct.setText(String.format("%.4f %%", result.acceptedOnTimePercentage() * 100));
            resultRejectedPct.setText(String.format("%.4f %%", result.rejectedOnTimePercentage() * 100));
            resultRequiredPct.setText(String.format("%.0f %%", result.requiredOnTimePercentage() * 100));
            resultRejectedTime.setText(result.rejectedDepartureTime());

            resultsCard.setVisible(true);
            resultsCard.setManaged(true);
        });
    }

    @FXML
    void onBackToFirstTask() {
        if (simulationThread != null && simulationThread.isAlive()) {
            simulationThread.interrupt();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chart_view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) backButton.getScene().getWindow();
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
            controller.setReturnViewPath("/second_task_view.fxml");

            Stage stage = (Stage) configButton.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

